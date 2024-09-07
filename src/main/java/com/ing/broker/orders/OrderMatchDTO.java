package com.ing.broker.orders;

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
public class OrderMatchDTO {
    @NotNull(message = "Order is empty!")
    @Min(value = 1, message = "Order is empty!")
    private Long id;

    private String status;
}
