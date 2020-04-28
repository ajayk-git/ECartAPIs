package com.springbootcamp.springsecurity.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {
    String categoryName;
    Long categoryId;
    Long parentId;

    public CategoryDTO(String categoryName, Long categoryId, Long parentId) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.parentId = parentId;
    }

    public CategoryDTO(String name, Long id) {
        this.setCategoryId(id);
        this.setCategoryName(name);
    }

    public CategoryDTO() {

    }
}
