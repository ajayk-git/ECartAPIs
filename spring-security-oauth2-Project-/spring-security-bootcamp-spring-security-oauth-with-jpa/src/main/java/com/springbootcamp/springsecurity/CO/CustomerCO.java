package com.springbootcamp.springsecurity.CO;

import com.springbootcamp.springsecurity.Annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CustomerCO  extends UserCO{

    @NotEmpty
    @IsValidMobileNo
    private String contact;



}
