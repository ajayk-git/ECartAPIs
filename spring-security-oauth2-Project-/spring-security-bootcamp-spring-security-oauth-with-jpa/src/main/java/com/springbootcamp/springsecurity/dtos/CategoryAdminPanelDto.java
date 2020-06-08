package com.springbootcamp.springsecurity.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CategoryAdminPanelDto {
    Long id;
    String name;

    public CategoryAdminPanelDto() {
        
    }

}
