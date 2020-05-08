package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CustomerProfileUpdateCo {
    private String firstName;
    private String lastName;

   // @IsValidMobileNo
    @NotNull
    @Pattern(regexp = "^[789]\\d{9}$")
    private String contact;
}
