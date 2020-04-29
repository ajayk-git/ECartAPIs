package com.springbootcamp.springsecurity.co;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class CategoryUpdateCO {

    @NotNull(message = "Category Name cannot be null and should be unique.")
    private String CategoryName;
}
