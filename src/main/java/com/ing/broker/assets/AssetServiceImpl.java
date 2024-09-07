package com.ing.broker.assets;

import com.ing.broker.customers.CustomerService;
import com.ing.broker.orders.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final CustomerService customerService;

    public AssetServiceImpl(AssetRepository assetRepository, CustomerService customerService) {
        this.assetRepository = assetRepository;
        this.customerService = customerService;
    }

    @Override
    public Asset findById(Long id) {
        // TODO throw not found runtime ex
        return assetRepository.findById(id).orElse(null);
    }

    @Override
    public List<Asset> search(Long customerId, String assetName, float smallerUsableSize, float biggerUsableSize) {
        return assetRepository.search(customerId, assetName, smallerUsableSize, biggerUsableSize);
    }

    @Override
    public List<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName) {
        return assetRepository.findAssetsByCustomer_IdAndAndAssetName(customerId, assetName);
    }

    @Override
    public boolean depositMoney(Long customerId, float amount) {
        if (!customerService.isCustomerExist(customerId)) {
            // TODO throw customer not found rte
            return false;
        }
        Asset asset = this.assetRepository
                .findAssetsByCustomer_IdAndAndAssetName(customerId, "TRY")
                .get(0);
        float newBalance = asset.getUsableSize() + amount;
        asset.setUsableSize(newBalance);
        assetRepository.save(asset);
        // TODO Log deposit operation with
        return true;
    }

    @Override
    public boolean withdrawMoney(Long customerId, float amount, String IBAN) {
        if (!customerService.isCustomerExist(customerId)) {
            // TODO throw customer not found rte
            return false;
        }
        Asset asset = this.assetRepository
                .findAssetsByCustomer_IdAndAndAssetName(customerId, "TRY")
                .get(0);
        if (asset.isUsableSizeEnough(amount)) {
            // TODO throw insufficient resources rte
            return false;
        }
        float remainingBalance = asset.getUsableSize() - amount;
        asset.setUsableSize(remainingBalance);
        assetRepository.save(asset);
        // TODO Log withdraw operation with iban transfer
        return true;
    }

    @Override
    public boolean isAssetValid(Long assetId) {
        return this.findById(assetId) != null;
    }

    @Override
    public void updateAssetBasedOnOrder(Order order) {
        Long customerId = order.getCustomer().getId();
        float totalAmount = order.totalAmount();
        String orderSide = order.getOrderSide().value;

        Asset tryAsset = findByCustomerIdAndAssetName(customerId, "TRY").get(0);
        tryAsset.setUsableSize(tryAsset.calculateNewUsableSize(orderSide, order.getSize(), totalAmount));
        tryAsset.setSize(tryAsset.calculateNewSize(orderSide, order.getSize(), totalAmount));
        assetRepository.save(tryAsset);

        Asset updateAsset = findByCustomerIdAndAssetName(customerId, order.getAssetName()).get(0);
        updateAsset.setUsableSize(updateAsset.calculateNewUsableSize(orderSide, order.getSize(), totalAmount));
        updateAsset.setSize(updateAsset.calculateNewSize(orderSide, order.getSize(), totalAmount));
        assetRepository.save(updateAsset);
    }
}
