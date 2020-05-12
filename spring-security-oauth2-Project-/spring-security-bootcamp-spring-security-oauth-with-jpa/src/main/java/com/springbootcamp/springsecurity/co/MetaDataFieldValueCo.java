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
@ApiModel(description = "Command Object to add new/update MetaData field values.")
public class MetaDataFieldValueCo {

    @ApiModelProperty(value = "Category id for which adding metadata field and values.", required = true)
    @NotNull(message = "CategoryId can not null.")
    Long categoryId;

    @ApiModelProperty(value = "Field id for which adding metadata values.", required = true)
    @NotNull(message = "MetaData FieldId can not null.")
    Long fieldId;

    @ApiModelProperty(value = "Metadata values for field.", required = true)
    @NotNull(message = "At least one value should added in Metadata field.")
    String fieldValues;
}
