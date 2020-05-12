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
@ApiModel(description = "Product Variant Customer DTO representation")
public class ProductVariantCustomerDto {

    @ApiModelProperty(value = "Product's price")
    Float price;

    @ApiModelProperty(value = "Product's quantity")
    Integer quantityAvailable;

    @ApiModelProperty(value = "Product's metadata")
    Map<String, String> metaData;

    public ProductVariantCustomerDto() {

    }
}
