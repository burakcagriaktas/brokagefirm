package com.ing.broker.assets;

import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "asset")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping(
            value = "/deposit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deposit(@Valid @RequestBody AssetWithdrawDepositDTO requestDTO) {
        assetService.depositMoney(requestDTO.getCustomerId(), requestDTO.getAmount());
        return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
    }

    @PostMapping(
            value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> withdraw(@Valid @RequestBody AssetWithdrawDepositDTO requestDTO) {
        assetService.withdrawMoney(
                requestDTO.getCustomerId(),
                requestDTO.getAmount(),
                requestDTO.getIBAN());
        return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
    }

    @PostMapping(
            value = "/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Asset>> search(@RequestBody AssetSearchDTO assetSearchDTO) {
        List<Asset> assets = assetService.search(
                assetSearchDTO.getCustomerId(),
                assetSearchDTO.getAsset(),
                assetSearchDTO.getSmallerUsableSize(),
                assetSearchDTO.getBiggerUsableSize()
        );
        return ResponseEntity.ok(assets);
    }
}