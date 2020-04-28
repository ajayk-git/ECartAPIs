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
    String parentCategoryName;

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

    public CategoryDTO(String categoryName, Long id, Long parentId, String parentCategoryName1) {
        this.setCategoryId(id);
        this.setCategoryName(categoryName);
        this.setParentId(parentId);
        this.setParentCategoryName(parentCategoryName1);
    }
}
