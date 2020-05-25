package com.springbootcamp.springsecurity.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailsDto {
    Long customerId;
    Long ProductVariationId;
    Integer quantity;
    Boolean isWishListItem;
}
