package com.springbootcamp.springsecurity.co;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCo {
    @NotNull(message = "Product Name can not be null.")
    String productName;

    @NotNull(message = "Brand Name can not be null.")
    String brandName;

    @NotNull(message = "CategoryId can not be null and should be leaf node.")
    Long categoryId;

    String description;
    boolean isReturnable;
    boolean isCancellable;
}
