package com.ing.broker;

import java.util.List;

public interface OrderService {
    Order create(OrderDTO orderDTO);
    List<Order> search(OrderSearchDTO orderSearchDTO);
    boolean delete(Long id);

    float calculateTotalSize(OrderDTO orderDTO);
}
