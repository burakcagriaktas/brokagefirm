package com.ing.broker.orders;

import com.ing.broker.assets.AssetService;
import com.ing.broker.customers.Customer;
import com.ing.broker.customers.CustomerService;
import com.ing.broker.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final AssetService assetService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerService customerService,
                            AssetService assetService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.assetService = assetService;
    }

    @Override
    @Transactional
    public Order create(OrderDTO orderDTO) {
        Customer customer = customerService.findOne(orderDTO.getCustomerId());

        if (!Side.anyMatch(orderDTO.getSide())) {
            throw new NotFoundException(String.format("Order side : %s does not exist", orderDTO.getSide()));
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setAssetName(orderDTO.getAsset());
        order.setOrderSide(Side.find(orderDTO.getSide()));
        order.setSize(orderDTO.getSize());
        order.setPrice(orderDTO.getPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());

        assetService.updateTRYAssetBasedOnOrder(order);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> search(OrderSearchDTO orderSearchDTO) {
        return orderRepository.search(
                orderSearchDTO.getCustomerId(),
                orderSearchDTO.getAsset(),
                Side.find(orderSearchDTO.getSide()),
                orderSearchDTO.getFrom(),
                orderSearchDTO.getTo());
    }

    @Override
    public boolean delete(Long id) {
        Order order = getOrder(id);
        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new RuntimeException(String.format(
                    "Orders: %s that are not in %s status cannot be cancelled",
                    order.getOrderInfo(),
                    order.getStatus()));
        }
        order.setStatus(OrderStatus.CANCELED);

        assetService.updateAssetBasedOnOrder(order);
        orderRepository.save(order);
        return true;
    }

    @Override
    public float calculateTotalSize(OrderDTO orderDTO) {
        return orderDTO.getSize() * orderDTO.getPrice();
    }

    @Override
    public List<Order> getAllPendingOrders() {
        return this.orderRepository.findAll()
                .stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> match(List<OrderMatchDTO> matchList) {
        List<Order> collect = matchList
                .stream()
                .filter(dto -> {
                    Optional<Order> orderOpt = orderRepository.findById(dto.getId());
                    return orderOpt.isPresent() && orderOpt.get().isPending();
                })
                .toList()
                .stream()
                .map(dto -> {
                    Optional<Order> orderOpt = orderRepository.findById(dto.getId());
                    if (orderOpt.isPresent()) {
                        Order order = orderOpt.get();
                        order.setStatus(OrderStatus.find(dto.getStatus()));
                        return order;
                    }
                    return null;
                })
                .toList();
        List<Order> matchedOrders = orderRepository.saveAll(collect);

        collect.stream()
                .filter(Order::isMatched)
                .forEach(assetService::updateAssetBasedOnOrder);
        return matchedOrders;
    }

    private Order getOrder(Long id) {
        Optional<Order> toBeCancelledOrder = orderRepository.findById(id);
        if (toBeCancelledOrder.isEmpty()) {
            throw new NotFoundException("Order: not found");
        }
        return toBeCancelledOrder.get();
    }
}
