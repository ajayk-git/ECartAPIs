package com.springbootcamp.springsecurity.CO;


import com.springbootcamp.springsecurity.Annotations.IsValidGST;
import com.springbootcamp.springsecurity.Annotations.IsValidMobileNo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class  SellerCO  extends UserCO{
    @NotEmpty
    @IsValidGST
    private String gst;
    @NotEmpty
    private String companyName;
    @NotEmpty
    @IsValidMobileNo
    private String companyContact;

    private String city;

    private String state;

    private String country;
    @NotNull
    private String addressLine;

    private String lable;
    @NotNull
    @Pattern(regexp = "[1-9]{6}")
    private String zipcode;
}
