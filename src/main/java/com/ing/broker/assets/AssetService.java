package com.ing.broker.assets;

import com.ing.broker.orders.Order;

import java.util.List;

public interface AssetService {
    Asset findById(Long id);
    List<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);
    List<Asset> search(Long customerId, String assetName, float smallerUsableSize, float biggerUsableSize);
    boolean depositMoney(Long customerId, float amount);
    boolean withdrawMoney(Long customerId, float amount, String IBAN);
    boolean isAssetValid(Long assetId);
    void updateAssetBasedOnOrder(Order order);
}
