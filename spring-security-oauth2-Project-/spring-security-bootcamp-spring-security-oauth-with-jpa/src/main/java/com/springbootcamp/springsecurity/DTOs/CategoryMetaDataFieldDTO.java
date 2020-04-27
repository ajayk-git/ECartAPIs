package com.springbootcamp.springsecurity.DTOs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;

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
