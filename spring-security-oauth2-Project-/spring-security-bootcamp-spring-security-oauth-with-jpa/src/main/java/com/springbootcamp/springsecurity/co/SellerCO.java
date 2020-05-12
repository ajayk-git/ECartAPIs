package com.springbootcamp.springsecurity.co;


import com.springbootcamp.springsecurity.annotations.IsValidGST;
import com.springbootcamp.springsecurity.annotations.IsValidMobileNo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to add a new seller.")
public class SellerCO extends UserCO {

    @NotNull
    @IsValidGST
    @ApiModelProperty(value = "GST number  of seller", required = true)
    String gst;


    @ApiModelProperty(value = "Company Name of seller", required = true)
    @NotNull
    String companyName;

    @NotEmpty
    @IsValidMobileNo
    @ApiModelProperty(value = "Contact number of seller", required = true)
    String companyContact;

    @NotNull
    @ApiModelProperty(value = "City of Seller", required = true)
    String city;

    @ApiModelProperty(value = "Country of Seller", required = true)
    @NotNull
    String state;

    @ApiModelProperty(value = "Address line for address  of Seller", required = true)
    @NotNull
    String country;

    @ApiModelProperty(value = "Address line for address  of Seller", required = true)
    @NotNull
    String addressLine;

    @ApiModelProperty(value = "Type of Address (HOME/OFFICE) of Seller")
    String lable;

    @ApiModelProperty(value = "Address ZipCode  of seller", required = true)
    @NotNull
    @Pattern(regexp = "^[1-9][0-9]{5}$")
    String zipcode;
}
