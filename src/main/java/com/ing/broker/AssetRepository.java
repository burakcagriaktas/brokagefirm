package com.ing.broker;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetRepository extends CrudRepository<Asset, Long> {
    List<Asset> findAssetsByCustomer_IdAndAndAssetName(Long customerId, String assetName);

    @Query("SELECT a FROM Asset a WHERE (:customerId is null or a.customer.id = :customerId) " +
            "and (:asset is null or a.assetName = :asset) " +
            "and ((:smallerUsableSize is null or :smallerUsableSize <= 0)  or a.usableSize > :smallerUsableSize) " +
            "and ((:biggerUsableSize is null or :biggerUsableSize <= 0) or a.usableSize < :biggerUsableSize)")
    List<Asset> search(@Param("customerId") Long customerId,
                       @Param("asset") String asset,
                       @Param("smallerUsableSize") float smallerUsableSize,
                       @Param("biggerUsableSize") float biggerUsableSize);
}