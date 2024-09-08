package com.ing.broker.assets;

import com.ing.broker.customers.CustomerService;
import com.ing.broker.exceptions.NotFoundException;
import com.ing.broker.exceptions.NotSufficientFundsException;
import com.ing.broker.orders.Order;
import com.ing.broker.orders.Side;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return assetRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Asset: %s is not found!", id))
                );
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
    public void depositMoney(Long customerId, float amount) {
        Long cId = customerService.findOne(customerId).getId();
        Asset asset = this.assetRepository
                .findAssetsByCustomer_IdAndAndAssetName(cId, "TRY")
                .get(0);
        float newBalance = asset.getUsableSize() + amount;
        asset.setUsableSize(newBalance);
        assetRepository.save(asset);
    }

    @Override
    public void withdrawMoney(Long customerId, float amount, String IBAN) {
        Long cId = customerService.findOne(customerId).getId();
        Asset asset = this.assetRepository
                .findAssetsByCustomer_IdAndAndAssetName(cId, "TRY")
                .get(0);
        if (asset.isUsableSizeEnough(amount)) {
            throw new NotSufficientFundsException(String.format("Insufficient funds for customer: %s",
                    asset.getCustomer().getFullName()
            ));
        }
        float remainingBalance = asset.getUsableSize() - amount;
        asset.setUsableSize(remainingBalance);
        assetRepository.save(asset);
    }

    @Override
    public boolean isAssetExist(Long customerId, String assetName) {
        List<Asset> assetResults = assetRepository.findAssetsByCustomer_IdAndAndAssetName(
                customerId, assetName);
        return !assetResults.isEmpty();
    }

    @Override
    @Transactional
    public void updateTRYAssetBasedOnOrder(Order order) {
        Long customerId = order.getCustomer().getId();
        float totalAmount = order.totalAmount();
        String orderSide = order.getOrderSide().value;

        Asset tryAsset = findByCustomerIdAndAssetName(customerId, "TRY").get(0);

        if (orderSide.equals(Side.BUY.value)) {
            if (!tryAsset.isUsableSizeEnough(totalAmount)) {
                throw new NotSufficientFundsException(String.format(
                        "Not sufficient funds on asset 'TRY' for customer: %s",
                        order.getCustomer().getFullName()));
            }
            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalAmount);
            tryAsset.setSize(tryAsset.getSize() - totalAmount);
        }
        assetRepository.save(tryAsset);
    }

    @Override
    @Transactional
    public void updateAssetBasedOnOrder(Order order) {
        validateAssetBasedOnOrder(order);
        Long customerId = order.getCustomer().getId();
        String orderSide = order.getOrderSide().value;

        Asset updateAsset = findByCustomerIdAndAssetName(customerId, order.getAssetName()).get(0);

        if (orderSide.equals(Side.BUY.value)) {
            updateAsset.setUsableSize(updateAsset.getUsableSize() + order.getSize());
            updateAsset.setSize(updateAsset.getSize() + order.getSize());
        } else {
            updateAsset.setUsableSize(updateAsset.getUsableSize() - order.getSize());
            updateAsset.setSize(updateAsset.getSize() - order.getSize());
        }
    }

    private void validateAssetBasedOnOrder(Order order) {
        boolean assetExist = isAssetExist(order.getCustomer().getId(), order.getAssetName());
        if (!assetExist) {
            throw new NotFoundException(String.format(
                    "No asset is associated to the given order: %s ",
                    order.getOrderInfo())
            );
        }
    }
}
