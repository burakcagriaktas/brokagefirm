package com.ing.broker;

import java.util.List;
import java.util.UUID;

public class AssetServiceImpl implements AssetService{

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset findById(Long id) {
        // TODO throw not found runtime ex
        return assetRepository.getById(id).orElse(null);
    }

    @Override
    public List<Asset> search(Long customerId) {
        return assetRepository.findAssetsByCustomer_Id(customerId);
    }
}
