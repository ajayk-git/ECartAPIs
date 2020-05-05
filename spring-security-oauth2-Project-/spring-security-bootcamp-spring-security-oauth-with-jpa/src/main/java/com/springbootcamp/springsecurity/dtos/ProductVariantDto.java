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

    public ProductVariantDto(Long productId, String brand, String productName, Long productVariantId, Map<String, String> metaData, boolean active, int quantityAvailable, float price) {


        this.setBrand(brand);
        this.setPrice(price);
        this.setActive(active);
        this.setMetaData(metaData);
        this.setProductId(productId);
        this.setProductName(productName);
        this.setProductVariantId(productVariantId);
        this.setQuantityAvailable(quantityAvailable);

    }

    public ProductVariantDto() {

    }
}
