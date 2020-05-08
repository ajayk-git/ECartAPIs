package com.springbootcamp.springsecurity.co;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetaDataFieldCO {

    @NotNull(message = "MetaData field can be null.")
    String fieldName;

}
