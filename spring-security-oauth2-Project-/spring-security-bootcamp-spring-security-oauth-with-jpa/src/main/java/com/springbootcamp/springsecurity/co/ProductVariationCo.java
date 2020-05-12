package com.springbootcamp.springsecurity.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to add a new Product Variation.")
public class ProductVariationCo {

    @ApiModelProperty(value = "Quantity of product (minimum = 1) ")
    @Min(value = 1, message = "Quantity Should be greater than 1.")
    int quantityAvailable;

    @ApiModelProperty(value = "Price of product (minimum = 1) ")
    @Min(value = 1, message = "Price of product should be grater than 1.")
    float price;
    Map<String, String> metaData;


}
//
//    Product Id
//    Metadata (field-value map)
////    Primary Image
//Quantity Available
//Price
//        List of secondary images