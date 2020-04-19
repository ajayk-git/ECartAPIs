package com.springbootcamp.springsecurity.CO;

import com.springbootcamp.springsecurity.Annotations.ConfirmPasswordMatcher;
import com.springbootcamp.springsecurity.Annotations.IsValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfirmPasswordMatcher
public class PasswordUpdateCO {
    @IsValidPassword
    private String password;
    @IsValidPassword
    private String confirmPassword;
}
