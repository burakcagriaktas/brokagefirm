package com.ing.broker.assets;

import com.ing.broker.customers.Customer;
import com.ing.broker.orders.Side;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asset")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "size")
    private float size;

    @Column(name = "usable_size")
    private float usableSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public boolean isUsableSizeEnough(float totalAmount) {
        return this.getUsableSize() > totalAmount;
    }

    public float calculateNewUsableSize(String orderSide, float usableSize, float totalAmount) {
        if (assetName.equals("TRY")) {
            return orderSide.equals(Side.BUY.value) ?
                    this.getUsableSize() - totalAmount :
                    this.getUsableSize() + totalAmount;
        }
        else {
            return orderSide.equals(Side.BUY.value) ?
                    this.getUsableSize() + usableSize :
                    this.getUsableSize() - usableSize;
        }
    }

    public float calculateNewSize(String orderSide, float size, float totalAmount) {
        if (assetName.equals("TRY")) {
            return orderSide.equals(Side.BUY.value) ?
                    this.getSize() - totalAmount :
                    this.getSize() + totalAmount;
        }
        else {
            return orderSide.equals(Side.BUY.value) ?
                    this.getSize() + size :
                    this.getSize() - size;
        }
    }
}
