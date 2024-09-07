package com.ing.broker.orders;

import java.util.List;

public interface OrderService {
    Order create(OrderDTO orderDTO);
    boolean delete(Long id);
    List<Order> search(OrderSearchDTO orderSearchDTO);
    float calculateTotalSize(OrderDTO orderDTO);

    List<Order> getAllPendingOrders();

    void match(List<OrderMatchDTO> matchList);
}
