package com.ing.broker;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset findById(Long id) {
        // TODO throw not found runtime ex
        return assetRepository.findById(id).orElse(null);
    }

    @Override
    public List<Asset> search(Long customerId, String assetName) {
        return assetRepository.findAssetsByCustomer_IdAndAndAssetName(customerId, assetName);
    }

    @Override
    public boolean isAssetValid(Long assetId) {
        return this.findById(assetId) != null;
    }
}
