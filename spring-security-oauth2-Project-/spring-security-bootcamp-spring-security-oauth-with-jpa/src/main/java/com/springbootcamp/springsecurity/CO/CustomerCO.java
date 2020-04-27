package com.springbootcamp.springsecurity.CO;

import com.springbootcamp.springsecurity.Annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CustomerCO  extends UserCO{

    @NotNull
    @IsValidMobileNo
    private String contact;



}
