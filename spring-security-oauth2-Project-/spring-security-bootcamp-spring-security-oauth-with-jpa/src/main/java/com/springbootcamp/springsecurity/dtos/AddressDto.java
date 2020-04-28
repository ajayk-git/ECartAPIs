package com.springbootcamp.springsecurity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private Long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private String lable;
    private String zipcode;

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
}
