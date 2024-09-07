package com.ing.broker;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AssetRepository extends CrudRepository<Asset, Long> {
    List<Asset> findAssetsByCustomer_Id(Long customerId);

    List<Asset> findAssetsByCustomer_IdAndAndAssetName(Long customerId, String assetName);
}
