package com.ing.broker.orders;

import com.ing.broker.customers.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @Enumerated(EnumType.STRING)
    private Side orderSide;

    @Column(name = "size")
    private float size;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "creation_time")
    private LocalDateTime createDate;

    public boolean isPending() {
        return this.status.equals(OrderStatus.PENDING);
    }

    public boolean isMatched() {
        return this.status.equals(OrderStatus.MATCHED);
    }

    public float totalAmount() {
        return (float) (this.getSize() * this.getPrice());
    }

    public String getOrderInfo() {
        return String.format("Asset: %s with amount: %s * %s for customer: %s on date: %s",
                this.assetName,
                this.size,
                this.price,
                this.customer.getFullName(),
                this.createDate);

    }
}
