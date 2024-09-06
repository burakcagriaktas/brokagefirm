package com.ing.broker;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "assetName")
    private String assetName;

    @Column(name = "order_side")
    private Side orderSide;

    @Column(name = "size")
    private int size;

    @Column(name = "price")
    private double price;

    private OrderStatus status;

    @Column(name = "creation_time")
    private Date createDate;
}
