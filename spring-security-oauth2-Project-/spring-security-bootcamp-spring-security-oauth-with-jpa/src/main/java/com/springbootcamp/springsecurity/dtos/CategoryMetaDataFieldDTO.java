package com.springbootcamp.springsecurity.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetaDataFieldDTO {
    Long id;
    String fieldName;

    public CategoryMetaDataFieldDTO(Long id, String fieldName) {
        this.setId(id);
        this.setFieldName(fieldName);
    }
}
