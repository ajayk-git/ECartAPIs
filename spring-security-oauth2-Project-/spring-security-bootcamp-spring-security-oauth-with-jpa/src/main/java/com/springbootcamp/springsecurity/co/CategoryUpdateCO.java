package com.springbootcamp.springsecurity.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object To update the category details")
public class CategoryUpdateCO {

    @ApiModelProperty(value = "Category Name", required = true)
    @NotNull(message = "Category Name cannot be null and should be unique.")
    String CategoryName;
}
