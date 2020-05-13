package com.springbootcamp.springsecurity.dtos;

import com.springbootcamp.springsecurity.entities.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCountAdminPanelDto {
    Product product;
    Long quantityAvailable;

}
