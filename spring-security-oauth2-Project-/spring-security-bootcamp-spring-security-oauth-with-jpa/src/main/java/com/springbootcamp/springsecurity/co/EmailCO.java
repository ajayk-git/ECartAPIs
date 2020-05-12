package com.springbootcamp.springsecurity.co;

import com.springbootcamp.springsecurity.annotations.IsValidEmail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Command Object to resend the activation link")
public class EmailCO {

    @IsValidEmail
    @ApiModelProperty(value = "Email Id of User")
    String email;
}
