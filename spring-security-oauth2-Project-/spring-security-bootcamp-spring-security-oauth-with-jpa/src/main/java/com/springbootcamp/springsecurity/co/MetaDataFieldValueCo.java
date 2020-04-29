package com.springbootcamp.springsecurity.co;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetaDataFieldValueCo {

    @NotNull(message = "CategoryId can not null.")
    Long categoryId;

    @NotNull(message = "MetaData FieldId can not null.")
    Long fieldId;

    @NotNull(message = "At least one value should added in Metadata field.")
    String fieldValues;
}
