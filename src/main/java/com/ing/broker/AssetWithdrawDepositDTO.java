package com.ing.broker;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetWithdrawDepositDTO {

    @NotNull(message = "Customer is empty!")
    @Min(value = 1, message = "Customer is empty!")
    private Long customerId;

    @DecimalMin(value = "0.00001", message = "Amount should be greater than 0")
    private float amount;

    private String IBAN;
}
