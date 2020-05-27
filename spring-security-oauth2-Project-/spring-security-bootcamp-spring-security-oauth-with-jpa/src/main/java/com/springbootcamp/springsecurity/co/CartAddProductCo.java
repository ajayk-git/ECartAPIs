package com.springbootcamp.springsecurity.co;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartAddProductCo {

    @NotNull(message = "Quantity can not be null.")
    @Min(value = 1,message = "Minimum quantity  of product should be 1.")
    Integer quantity;
    Boolean isWishListItem;
    @NotNull(message = "ProductVariationId can not be null.")
    Long productVariationId;
}
