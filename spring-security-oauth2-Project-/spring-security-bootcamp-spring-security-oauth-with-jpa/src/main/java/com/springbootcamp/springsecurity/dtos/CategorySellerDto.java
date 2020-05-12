package com.springbootcamp.springsecurity.dtos;

import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Category Seller DTO representation")
public class CategorySellerDto {


    @ApiModelProperty(value = "Category Name ")
    String categoryName;

    @ApiModelProperty(value = "Category ID ")
    Long categoryId;

    @ApiModelProperty(value = "Category MetaData Field")
    String metaDataField;

    @ApiModelProperty(value = "Category MetaData Field Name")
    List<String> fieldName;

    @ApiModelProperty(value = "Category MetaData Field Values")
    List<String> fieldValues;

    public CategorySellerDto(Long id, String name, List<String> fieldName, List<String> valueList) {
        this.setCategoryId(id);
        this.setCategoryName(name);
        this.setFieldName(fieldName);
        this.setFieldValues(valueList);
    }
}
