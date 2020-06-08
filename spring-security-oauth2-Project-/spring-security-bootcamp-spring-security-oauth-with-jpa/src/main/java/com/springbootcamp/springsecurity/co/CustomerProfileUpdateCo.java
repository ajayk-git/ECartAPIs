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
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object To update the User's profile")
public class CustomerProfileUpdateCo {

    @ApiModelProperty(value = "First Name of USer")
    String firstName;

    @ApiModelProperty(value = "Last Name of USer")
    String lastName;

    @NotNull
    @Pattern(regexp = "^[789]\\d{9}$")
    @ApiModelProperty(value = "Contact number of USer", required = true)
    String contact;
}
