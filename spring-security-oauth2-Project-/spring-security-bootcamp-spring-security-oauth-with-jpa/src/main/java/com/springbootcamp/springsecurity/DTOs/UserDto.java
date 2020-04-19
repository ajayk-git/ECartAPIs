package com.springbootcamp.springsecurity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Getter
@Setter

public class UserDto {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String email;
    @NotEmpty
    @JsonIgnore  //Using Json ignore here will create internal server error at registration time
  private String password;
   @NotEmpty
    private String firstName;
   @NotEmpty
    private String lastName;

   private  boolean isActive;

    public UserDto(){

    }

    public UserDto(@NotEmpty Long id, @NotEmpty String email, @NotEmpty String password, @NotEmpty String firstName, @NotEmpty String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
