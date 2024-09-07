package com.ing.broker.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE (:customerId is null or o.customer.id = :customerId) " +
            "and (:asset is null or o.assetName = :asset) " +
            "and (:side is null or o.orderSide = :side) " +
            "and (:from is null or o.createDate > :from)" +
            "and (:to is null or o.createDate < :to)")
    List<Order> search(@Param("customerId") Long customerId,
                       @Param("asset") String asset,
                       @Param("side") Side side,
                       @Param("from") LocalDateTime from,
                       @Param("to") LocalDateTime to);
}
