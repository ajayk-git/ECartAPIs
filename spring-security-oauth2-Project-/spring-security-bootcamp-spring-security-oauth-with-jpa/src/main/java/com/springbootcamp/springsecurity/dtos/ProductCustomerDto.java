package com.springbootcamp.springsecurity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Product Customer DTO representation")
public class ProductCustomerDto  implements Serializable {

    @ApiModelProperty(value = "Product Id")
    Long id;

    @ApiModelProperty(value = "Product Name")
    String name;

    @ApiModelProperty(value = "Product Description")
    String description;

    @ApiModelProperty(value = "Product Brand")
    String brand;

    @ApiModelProperty(value = "Product's category Name.")
    String categoryName;

    @ApiModelProperty(value = "Product is active/Inactive ")
    Boolean isActive;

    @ApiModelProperty(value = "Product is cancellable or not")
    Boolean isCancelable;

    @ApiModelProperty(value = "Product is returnable or not")
    Boolean isReturnable;

    @ApiModelProperty(value = "Product's productVariations List ")
    List<ProductVariantCustomerDto> productVariationList;

    @ApiModelProperty(value = "Product's Category Details")
    CategoryDTO category;

    public ProductCustomerDto() {

    }


}
