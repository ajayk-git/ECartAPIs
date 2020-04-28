package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CustomerCO  extends UserCO{

    @NotNull
   // @IsValidMobileNo
    private String contact;



}
