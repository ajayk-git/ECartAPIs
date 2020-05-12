package com.springbootcamp.springsecurity.co;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Min;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to update a Product Variation.")
public class ProductVariationUpdateCo {

    @ApiModelProperty(value = "Quantity of product (minimum = 1) ")
    @Min(value = 1, message = "Minimum Quantity must be greater than equals to 1.")
    Integer quantityAvailable;

    @ApiModelProperty(value = "Price of product (minimum = 1) ")
    @Min(value = 1, message = "Minimum price must be greater than equals to 1 Rs. .")
    Float price;

    @ApiModelProperty(value = "Product variation is active or not. ")
    Boolean isActive;

    @ApiModelProperty(value = "Metadata field and their values ")
    Map<String, String> metaData;
}
