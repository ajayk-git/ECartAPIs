package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.ConfirmPasswordMatcher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfirmPasswordMatcher
@ApiModel(description = "Command Object to update user's password.")
public class PasswordUpdateCO {

    @ApiModelProperty(value = "New Password of user.", required = true)
    @NotNull(message = "password cannot be empty")
    //@IsValidPassword
    private String password;

    @ApiModelProperty(value = "Confirm Password of user.", required = true)
    @NotNull(message = "Confirm password cannot be empty")
    //@IsValidPassword
    private String confirmPassword;
}
