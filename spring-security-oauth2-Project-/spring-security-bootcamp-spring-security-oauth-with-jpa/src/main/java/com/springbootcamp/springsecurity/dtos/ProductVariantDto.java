package com.springbootcamp.springsecurity.dtos;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantDto {
    Long productId;
    Long productVariantId;
    String brand;
    String productName;
    float price;
    int quantityAvailable;
    boolean  isActive;
    Map<String,String> metaData;

}
