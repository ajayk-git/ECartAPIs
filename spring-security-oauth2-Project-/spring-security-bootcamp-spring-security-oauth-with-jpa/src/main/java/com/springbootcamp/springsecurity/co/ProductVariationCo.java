package com.springbootcamp.springsecurity.co;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariationCo {

    @Min(value = 1,message ="Quantity Should be greater than 1." )
    int quantityAvailable;
    @Min(value = 1,message = "Price of product should be grater than 1.")
    float price;
    Map<String,String>metaData;


}
//
//    Product Id
//    Metadata (field-value map)
////    Primary Image
//Quantity Available
//Price
//        List of secondary images