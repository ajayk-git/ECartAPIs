package com.springbootcamp.springsecurity.dtos;

import com.springbootcamp.springsecurity.entities.product.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCustomerDto {

    Long id;

    String name;

    String description;

    String brand;

    String categoryName;

    Boolean isActive;

    Boolean isCancelable;

    Boolean isReturnable;

    List<ProductVariantCustomerDto> productVariationList;

    CategoryDTO category;

   public ProductCustomerDto(){

    }


}
