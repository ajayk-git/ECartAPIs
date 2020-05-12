package com.springbootcamp.springsecurity.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to update seller profile.")
public class SellerProfileUpdateCO {

    @ApiModelProperty(value = "First name of  seller")
    String firstName;

    @ApiModelProperty(value = "Last name of  seller")
    String lastName;

    @ApiModelProperty(value = "Contact number of  seller")
    String contact;


}
