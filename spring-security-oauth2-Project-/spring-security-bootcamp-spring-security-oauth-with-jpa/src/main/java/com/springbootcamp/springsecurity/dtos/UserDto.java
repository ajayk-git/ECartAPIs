package com.springbootcamp.springsecurity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Product Variant DTO representation")
public class UserDto {


    @ApiModelProperty(value = "User's Id")
    Long id;

    @ApiModelProperty(value = "User's Email Id")
    String email;

    @ApiModelProperty(value = "User's Password")
    String password;

    @ApiModelProperty(value = "User's first Name")
    String firstName;

    @ApiModelProperty(value = "User's last Name")
    String lastName;

    @ApiModelProperty(value = "User is active or not")
    boolean isActive;

    public UserDto() {

    }

    public UserDto(@NotEmpty Long id, @NotEmpty String email, @NotEmpty String password, @NotEmpty String firstName, @NotEmpty String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
