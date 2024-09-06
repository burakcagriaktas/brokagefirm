package com.ing.broker;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends CrudRepository<Asset, Long> {
    Optional<Asset> getById(Long id);
    List<Asset> findAssetsByCustomer_Id(Long customerId);
}
