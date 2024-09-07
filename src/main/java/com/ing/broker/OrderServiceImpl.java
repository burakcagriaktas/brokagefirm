package com.ing.broker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

        List<Asset> assetResults = assetService.search(orderDTO.getCustomerId(), orderDTO.getAsset());
        if (assetResults.isEmpty()) {
            // TODO throw customer not found exception
            return null;
        }
        if (!Side.anyMatch(orderDTO.getSide())) {
            // TODO throw unsupported param rte
            return null;
        }
        Asset asset = assetService.search(orderDTO.getCustomerId(), "TRY").get(0);
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

}
