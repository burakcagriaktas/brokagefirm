package com.ing.broker;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetSearchDTO {
    private Long customerId;

    private String asset;

    private float biggerUsableSize;

    private float smallerUsableSize;
}