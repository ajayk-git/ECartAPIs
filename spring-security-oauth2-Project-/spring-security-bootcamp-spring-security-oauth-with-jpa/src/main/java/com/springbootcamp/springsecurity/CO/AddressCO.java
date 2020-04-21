package com.springbootcamp.springsecurity.CO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Getter
@Setter
public class AddressCO {


    private String city;

    private String state;

    private String country;

    @NotNull
    private String addressLine;

    private String lable;
    @NotNull
    @Pattern(regexp = "[1-9]{6}")
    private String zipcode;
}
