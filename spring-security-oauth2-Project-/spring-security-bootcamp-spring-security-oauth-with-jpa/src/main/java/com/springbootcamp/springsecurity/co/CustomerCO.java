package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CustomerCO  extends UserCO{

    @NotNull
    @Pattern(regexp = "^[789]\\d{9}$",message = "Should be 10 digit number starting with 7/8/9.")
    private String contact;



}
