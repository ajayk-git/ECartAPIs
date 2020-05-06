package com.springbootcamp.springsecurity.co;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateBySellerCo {

    String productName;

    String brandName;

    String description;

    @NotNull(message = "Set value true/false")
    boolean isReturnable;

    @NotNull(message = "Set value true/false")
    boolean isCancellable;

}
