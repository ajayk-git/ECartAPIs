package com.springbootcamp.springsecurity.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to update a product.")
public class ProductUpdateBySellerCo {

    @ApiModelProperty(value = "Product Name", required = true)
    String productName;

    @ApiModelProperty(value = "Product's Brand Name ")
    String brandName;

    @ApiModelProperty(value = "Product's Description ")
    String description;

    @ApiModelProperty(value = "Product is returnable or not", required = true)
    @NotNull(message = "Set value true/false")
    Boolean isReturnable;

    @ApiModelProperty(value = "Product is cancellable or not", required = true)
    @NotNull(message = "Set value true/false")
    Boolean isCancellable;

}
