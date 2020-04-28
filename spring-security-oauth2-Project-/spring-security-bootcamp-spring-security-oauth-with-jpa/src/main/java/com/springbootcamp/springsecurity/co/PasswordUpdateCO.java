package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.ConfirmPasswordMatcher;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfirmPasswordMatcher
public class PasswordUpdateCO {
    @NotNull(message = "password cannot be empty")
    //@IsValidPassword
    private String password;

    @NotNull(message = "Confirm password cannot be empty")
    //@IsValidPassword
    private String confirmPassword;
}
