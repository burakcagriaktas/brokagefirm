package com.ing.broker;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class OrderDTO {
    @NotNull(message = "Customer is empty!")
    @Min(value = 1, message = "Customer is empty!")
    private Long customerId;

    @NotBlank(message = "Asset is empty!")
    private String asset;

    @NotBlank(message = "Side is empty!")
    private String side;

    @DecimalMin(value = "0.00001", message = "Size should be greater than 0")
    private float size;

    @DecimalMin(value = "0.00001", message = "Price should be greater than 0")
    private float price;
}