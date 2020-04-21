package com.springbootcamp.springsecurity.DTOs;

import com.springbootcamp.springsecurity.Entities.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerDto extends com.springbootcamp.springsecurity.dto.UserDto {
        //@NotEmpty
        private String gst;
        //@NotEmpty
        private String companyName;
       // @NotEmpty
        private String companyContact;

        private String addressLine;
        private String city;
        private String country;
        private String lable;
        private String zipcode;
        private String state;


    public SellerDto(String companyContact, String companyName,String lastName, String gst, String firstName, String email, long id) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setCompanyName(companyName);
        this.setGst(gst);
        this.setEmail(email);
        this.setLastName(lastName);
        this.setCompanyContact(companyContact);
    }
    public SellerDto(){

    }

    public SellerDto(String companyContact, String companyName, String lastName, String gst, String firstName, String email, Long id, String addressLine, String city, String country, String lable, String zipcode, String state,boolean isactive) {

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
