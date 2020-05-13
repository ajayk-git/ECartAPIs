package com.springbootcamp.springsecurity.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAdminPanelDto {
    Long id;
    String productName;
    Boolean isActive;
}
