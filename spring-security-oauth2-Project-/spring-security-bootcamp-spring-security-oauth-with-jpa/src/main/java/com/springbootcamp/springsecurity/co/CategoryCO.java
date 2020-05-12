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
@ApiModel(description = "Command Object to add a new Category")
public class CategoryCO {

    @ApiModelProperty(value = "Product's Category Name", required = true)
    @NotNull(message = "Category name can not be null.")
    String categoryName;

    @ApiModelProperty(value = "Product's parent category Name", required = true)
    Long parentId;

}
