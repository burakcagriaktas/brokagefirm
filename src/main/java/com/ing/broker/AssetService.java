package com.ing.broker;

import java.util.List;
import java.util.UUID;

public interface AssetService {
    Asset findById(Long id);
    // TODO add more filters
    List<Asset> search(Long customerId, String assetName);
    boolean depositMoney(Long customerId, float amount);
    boolean withdrawMoney(Long customerId, float amount, String IBAN);
    boolean isAssetValid(Long assetId);
}
