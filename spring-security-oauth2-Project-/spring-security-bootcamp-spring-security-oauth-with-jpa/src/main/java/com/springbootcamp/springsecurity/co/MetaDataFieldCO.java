package com.springbootcamp.springsecurity.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to add new MetaData field.")
public class MetaDataFieldCO {

    @ApiModelProperty(value = "MetaData field name.", required = true)
    @NotNull(message = "MetaData field can be null.")
    String fieldName;

}
