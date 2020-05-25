package com.springbootcamp.springsecurity.co;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartAddProductCo {
    @Min(value = 1,message = "Minimum quantity  of product should be 1.")
    Integer quantity;
    Long productVariationId;
}
