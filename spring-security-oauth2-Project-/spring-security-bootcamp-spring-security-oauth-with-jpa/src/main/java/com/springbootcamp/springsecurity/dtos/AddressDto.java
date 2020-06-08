package com.springbootcamp.springsecurity.dtos;

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
@ApiModel(description = "Address  DTO representation")
public class AddressDto {

    @ApiModelProperty(value = "Address ID")
    Long id;

    @ApiModelProperty(value = "City of User")
    String city;

    @ApiModelProperty(value = "State of User")
    String state;

    @ApiModelProperty(value = "Country of User")
    String country;

    @ApiModelProperty(value = "AddressLine for address  of User")
    String addressLine;

    @ApiModelProperty(value = "Type of Address (HOME/OFFICE)")
    String lable;

    @ApiModelProperty(value = "Area ZipCode of User")
    String zipcode;

    public AddressDto(Long id, String addressLine, String city, String state, String zipcode, String lable, String country) {
        this.setAddressLine(addressLine);
        this.setCity(city);
        this.setState(state);
        this.setCountry(country);
        this.setCountry(country);
        this.setZipcode(zipcode);
        this.setId(id);
        this.setLable(lable);
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ApiModel(description = "Product Variant DTO representation")
    public static class UserDto {


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
}
