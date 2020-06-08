package com.springbootcamp.springsecurity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Product Variant DTO representation")
public class SellerDto extends AddressDto.UserDto {

    @ApiModelProperty(value = "Seller's GST number")
    String gst;

    @ApiModelProperty(value = "Seller's Company Name")
    String companyName;

    @ApiModelProperty(value = "Seller's Contact Number")
    String companyContact;

    @ApiModelProperty(value = "Address line of Address")
    String addressLine;

    @ApiModelProperty(value = "Seller's city")
    String city;

    @ApiModelProperty(value = "Seller's State")
    String state;

    @ApiModelProperty(value = "Seller's Country")
    String country;

    @ApiModelProperty(value = "Address lable")
    String lable;

    @ApiModelProperty(value = "Area ZipCode of Seller")
    String zipcode;


    public SellerDto(String companyContact, String companyName, String lastName, String gst, String firstName, String email, long id) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setCompanyName(companyName);
        this.setGst(gst);
        this.setEmail(email);
        this.setLastName(lastName);
        this.setCompanyContact(companyContact);
    }

    public SellerDto() {

    }

    public SellerDto(String companyContact, String companyName, String lastName, String gst, String firstName, String email, Long id, String addressLine, String city, String country, String lable, String zipcode, String state, boolean isactive) {

        this.setCompanyName(companyName);
        this.setCompanyContact(companyContact);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setGst(gst);
        this.setId(id);
        this.setAddressLine(addressLine);
        this.setCity(city);
        this.setState(state);
        this.setLable(lable);
        this.setCountry(country);
        this.setZipcode(zipcode);
        this.setActive(isactive);
    }
}
