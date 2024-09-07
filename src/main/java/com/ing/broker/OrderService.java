package com.ing.broker;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order create(OrderDTO orderDTO);
    boolean delete(Long id);
    List<Order> search(OrderSearchDTO orderSearchDTO);
    float calculateTotalSize(OrderDTO orderDTO);
}
