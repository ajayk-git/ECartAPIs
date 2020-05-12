package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.IsValidMobileNo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object To update the User's details")
public class CustomerCO extends UserCO {

    @NotNull
    @Pattern(regexp = "^[789]\\d{9}$", message = "Should be 10 digit number starting with 7/8/9.")
    @ApiModelProperty(value = "Contact number of User", required = true)
     String contact;


}
