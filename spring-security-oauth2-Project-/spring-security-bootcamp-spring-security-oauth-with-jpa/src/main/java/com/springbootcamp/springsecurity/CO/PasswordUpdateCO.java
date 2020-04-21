package com.springbootcamp.springsecurity.CO;

import com.springbootcamp.springsecurity.Annotations.ConfirmPasswordMatcher;
import com.springbootcamp.springsecurity.Annotations.IsValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ConfirmPasswordMatcher
public class PasswordUpdateCO {
    @NotEmpty(message = "password cannot be empty")
    @IsValidPassword
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty")
    @IsValidPassword
    private String confirmPassword;
}
