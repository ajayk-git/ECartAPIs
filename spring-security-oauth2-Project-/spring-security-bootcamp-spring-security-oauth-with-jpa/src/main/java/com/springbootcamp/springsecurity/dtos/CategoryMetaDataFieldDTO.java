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
@ApiModel(description = "Category MetaData Field DTO representation")
public class CategoryMetaDataFieldDTO {

    @ApiModelProperty(value = "Category MetaData Field Id ")
    Long id;

    @ApiModelProperty(value = "Category MetaData Field Name")
    String fieldName;

    public CategoryMetaDataFieldDTO(Long id, String fieldName) {
        this.setId(id);
        this.setFieldName(fieldName);
    }
}
