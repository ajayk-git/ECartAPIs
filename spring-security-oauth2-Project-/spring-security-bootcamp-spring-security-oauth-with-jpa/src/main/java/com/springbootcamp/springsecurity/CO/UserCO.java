package com.springbootcamp.springsecurity.CO;

import com.springbootcamp.springsecurity.Annotations.ConfirmPasswordMatcher;
import com.springbootcamp.springsecurity.Annotations.IsValidEmail;
import com.springbootcamp.springsecurity.Annotations.IsValidPassword;
import com.springbootcamp.springsecurity.Entities.Address;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfirmPasswordMatcher
public class UserCO {

    @NotNull(message = "Email must be unique")
    @IsValidEmail
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
    @NotEmpty
    @IsValidPassword
    private String confirmPassword;



    private Address address;


}









//  private List<Address> addressList;
