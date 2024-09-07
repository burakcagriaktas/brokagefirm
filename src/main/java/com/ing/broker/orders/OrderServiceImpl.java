package com.ing.broker.orders;

import com.ing.broker.customers.Customer;
import com.ing.broker.customers.CustomerService;
import com.ing.broker.assets.Asset;
import com.ing.broker.assets.AssetService;
import org.springframework.stereotype.Service;

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
    public Order create(OrderDTO orderDTO) {

        Customer customer;
        if (!customerService.isCustomerExist(orderDTO.getCustomerId())) {
            // TODO throw customer not found rte
            return null;
        }

        List<Asset> assetResults = assetService.findByCustomerIdAndAssetName(
                orderDTO.getCustomerId(),
                orderDTO.getAsset()
        );
        if (assetResults.isEmpty()) {
            // TODO throw customer not found exception
            return null;
        }
        if (!Side.anyMatch(orderDTO.getSide())) {
            // TODO throw unsupported param rte
            return null;
        }
        Asset asset = assetService.findByCustomerIdAndAssetName(orderDTO.getCustomerId(), "TRY").get(0);
        if (!asset.isUsableSizeEnough(calculateTotalSize(orderDTO))) {
            // TODO throw unsupported param rte
            return null;
        }

        // make asset update transactional if order is unsuccessful then asset should not be updated

        Order order = new Order();
        order.setCustomer(customerService.findOne(orderDTO.getCustomerId()));
        order.setAssetName(orderDTO.getAsset());
        order.setOrderSide(Side.find(orderDTO.getSide()));
        order.setSize(orderDTO.getSize());
        order.setPrice(orderDTO.getPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());

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
        Optional<Order> toBeCancelledOrder = orderRepository.findById(id);
        if (toBeCancelledOrder.isEmpty()) {
            // TODO throw not found exception rte
            return false;
        }
        Order order = toBeCancelledOrder.get();
        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            // TODO throw rte exception only pending orders can be deleted
            return false;
        }
        order.setStatus(OrderStatus.CANCELED);
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
    public void match(List<OrderMatchDTO> matchList) {
        List<Order> collect = matchList
                .stream()
                .filter(dto -> orderRepository.findById(dto.getId()).isPresent())
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
        orderRepository.saveAll(collect);

        collect.stream()
                .filter(Order::isMatched)
                .forEach(assetService::updateAssetBasedOnOrder);
    }
}
