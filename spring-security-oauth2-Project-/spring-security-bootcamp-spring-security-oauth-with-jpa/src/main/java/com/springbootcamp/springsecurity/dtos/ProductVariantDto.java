package com.springbootcamp.springsecurity.dtos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Product Variant DTO representation")
public class ProductVariantDto {

    @ApiModelProperty(value = "Product's Id")
    Long productId;

    @ApiModelProperty(value = "Product variant's Id")
    Long productVariantId;

    @ApiModelProperty(value = "Product Brand")
    String brand;

    @ApiModelProperty(value = "Product Name")
    String productName;

    @ApiModelProperty(value = "Product's price ")
    float price;

    @ApiModelProperty(value = "Product's quantity ")
    int quantityAvailable;

    @ApiModelProperty(value = "Product is active or not")
    boolean  isActive;

    @ApiModelProperty(value = "Product's Metadata Filed and values")
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
