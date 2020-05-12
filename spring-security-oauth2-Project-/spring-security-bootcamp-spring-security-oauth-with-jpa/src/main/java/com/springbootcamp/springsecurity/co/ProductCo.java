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
@ApiModel(description = "Command Object to add new product.")
public class ProductCo {

    @ApiModelProperty(value = "Product Name", required = true)
    @NotNull(message = "Product Name can not be null.")
    String productName;

    @ApiModelProperty(value = "Brand name", required = true)
    @NotNull(message = "Brand Name can not be null.")
    String brandName;

    @ApiModelProperty(value = "Leaf category Id")
    @NotNull(message = "CategoryId can not be null and should be leaf node.")
    Long categoryId;

    @ApiModelProperty(value = "Description of Product")
    String description;

    @ApiModelProperty(value = "Product is Returnable or Not")
    boolean isReturnable;

    @ApiModelProperty(value = "Product is Cancellable or Not")
    boolean isCancellable;
}
