package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.ConfirmPasswordMatcher;
import com.springbootcamp.springsecurity.annotations.IsValidEmail;
import com.springbootcamp.springsecurity.annotations.IsValidPassword;
import com.springbootcamp.springsecurity.entities.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ConfirmPasswordMatcher
public class UserCO {

    @NotNull(message = "Email must be unique")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$",message = "enter a valid email id.")
    private String email;

     @NotNull(message = "First Name cannot be Null")
    private String firstName;

    @NotNull(message = "Last Name cannot be Null")
    private String lastName;

    @NotNull
    @NotEmpty(message ="confirmPassword Cant be null" )
    @IsValidPassword
    private String password;

    @NotNull(message = "confirmPassword Cant be null.")
    @IsValidPassword
    private String confirmPassword;

    private Address address;


}









//  private List<Address> addressList;
