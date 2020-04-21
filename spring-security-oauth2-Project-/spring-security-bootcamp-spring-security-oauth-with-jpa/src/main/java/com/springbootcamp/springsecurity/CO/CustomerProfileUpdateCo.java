package com.springbootcamp.springsecurity.CO;

import com.springbootcamp.springsecurity.Annotations.IsValidMobileNo;
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
