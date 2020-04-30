package com.springbootcamp.springsecurity.dtos;

import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySellerDto {
    String categoryName;
    Long categoryId;
    String metaDataField;
    List<CategoryMetadataFieldValues> fieldValues;

    public CategorySellerDto(Long id, String name, List<CategoryMetadataFieldValues> categoryMetadataFieldValuesList) {
        this.setCategoryId(id);
        this.setCategoryName(name);
        this.setFieldValues(categoryMetadataFieldValuesList);
    }
}
