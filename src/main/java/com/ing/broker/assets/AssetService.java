package com.ing.broker.assets;

import com.ing.broker.orders.Order;

import java.util.List;

public interface AssetService {
    Asset findById(Long id);
    List<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);
    List<Asset> search(Long customerId, String assetName, float smallerUsableSize, float biggerUsableSize);
    void depositMoney(Long customerId, float amount);
    void withdrawMoney(Long customerId, float amount, String IBAN);
    boolean isAssetExist(Long customerId, String assetName);
    void updateAssetBasedOnOrder(Order order);
    void updateTRYAssetBasedOnOrder(Order order);
}
