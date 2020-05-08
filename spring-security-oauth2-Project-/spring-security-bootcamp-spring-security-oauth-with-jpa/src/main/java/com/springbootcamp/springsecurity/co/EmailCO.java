package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.IsValidEmail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCO {
    @IsValidEmail
    private String email;
}
