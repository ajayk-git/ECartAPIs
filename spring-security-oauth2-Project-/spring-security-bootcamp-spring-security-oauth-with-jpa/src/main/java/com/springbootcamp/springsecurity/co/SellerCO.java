package com.springbootcamp.springsecurity.co;


import com.springbootcamp.springsecurity.annotations.IsValidGST;
import com.springbootcamp.springsecurity.annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class  SellerCO  extends UserCO{
    @NotNull
    @IsValidGST
    private String gst;
    @NotNull
    private String companyName;
    @NotEmpty
    @IsValidMobileNo
    private String companyContact;

    @NotNull
    private String city;
    @NotNull
    private String state;

    @NotNull
    private String country;
    @NotNull
    private String addressLine;

    private String lable;
    @NotNull
    @Pattern(regexp = "^[1-9][0-9]{5}$")
    private String zipcode;
}
