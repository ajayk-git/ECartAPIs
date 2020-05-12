package com.springbootcamp.springsecurity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Category  DTO representation")
public class CategoryDTO {


    @ApiModelProperty(value = "Category Name ")
    String categoryName;

    @ApiModelProperty(value = "Category Id ")
    Long categoryId;

    @ApiModelProperty(value = "Parent Category ID ")
    Long parentId;

    @ApiModelProperty(value = "Parent Category Name ")
    String parentCategoryName;

    public CategoryDTO(String categoryName, Long categoryId, Long parentId) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.parentId = parentId;
    }

    public CategoryDTO(String name, Long id) {
        this.setCategoryId(id);
        this.setCategoryName(name);
        this.setParentCategoryName("Root Category");
    }

    public CategoryDTO() {

    }

    public CategoryDTO(String categoryName, Long id, Long parentId, String parentCategoryName1) {
        this.setCategoryId(id);
        this.setCategoryName(categoryName);
        this.setParentId(parentId);
        this.setParentCategoryName(parentCategoryName1);
    }

    public CategoryDTO(String name, Long id, String name1) {
        this.setParentCategoryName(name);
        this.setCategoryId(id);
        this.setParentCategoryName(name1);
    }
}
