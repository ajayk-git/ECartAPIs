package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerProfileUpdateCo {
    private String firstName;
    private String lastName;
    @IsValidMobileNo
    private String contact;
}
