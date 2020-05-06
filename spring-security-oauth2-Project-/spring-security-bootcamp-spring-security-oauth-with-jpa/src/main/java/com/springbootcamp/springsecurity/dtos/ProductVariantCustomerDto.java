package com.springbootcamp.springsecurity.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantCustomerDto {

    Float price;

    Integer quantityAvailable;

    Map<String,String> metaData;
}
