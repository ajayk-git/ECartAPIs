package com.springbootcamp.springsecurity.co;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
public class ProductVariationUpdateCo {

    @Min(value = 1,message = "Minimum Quantity must be greater than equals to 1.")
     Integer quantityAvailable;

    @Min(value = 1,message = "Minimum price must be greater than equals to 1 Rs. .")
    Float price;

    Boolean isActive;

     Map<String,String> metaData;
}
