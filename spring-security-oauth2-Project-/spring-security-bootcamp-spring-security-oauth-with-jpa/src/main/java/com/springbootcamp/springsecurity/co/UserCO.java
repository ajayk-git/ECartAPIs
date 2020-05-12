package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.ConfirmPasswordMatcher;
import com.springbootcamp.springsecurity.annotations.IsValidEmail;
import com.springbootcamp.springsecurity.annotations.IsValidPassword;
import com.springbootcamp.springsecurity.entities.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ConfirmPasswordMatcher
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to add a new User.")
public class UserCO {

    @NotNull(message = "Email must be unique")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", message = "enter a valid email id.")
    String email;

    @NotNull(message = "First Name cannot be Null")
    @ApiModelProperty(value = "First name of  seller", required = true)
    String firstName;

    @NotNull(message = "Last Name cannot be Null")
    @ApiModelProperty(value = "Last name of  seller", required = true)
    String lastName;

    @NotNull
    @NotEmpty(message = "Password Cant be null")
    @IsValidPassword
    @ApiModelProperty(value = "Password of  seller", required = true)
    String password;

    @NotNull(message = "ConfirmPassword Cant be null.")
    @IsValidPassword
    @ApiModelProperty(value = "Confirm Password of  seller", required = true)
    String confirmPassword;

    Address address;


}


//  private List<Address> addressList;
