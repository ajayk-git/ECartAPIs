package com.springbootcamp.springsecurity.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ApiModel(description = "Command Object to add  User's Address details.")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCO {

    @ApiModelProperty(value = "City of User")
    String city;

    @ApiModelProperty(value = "State of User")
    String state;

    @ApiModelProperty(value = "Country of User")
    String country;

    @ApiModelProperty(value = "Address line for address  of User")
    @NotNull
    String addressLine;

    @ApiModelProperty(value = "Type of Address (HOME/OFFICE) of User")
    String lable;

    @NotNull
    @ApiModelProperty(value = "ZipCode  of User")
    @Pattern(regexp = "[1-9]{6}")
    String zipcode;
}
