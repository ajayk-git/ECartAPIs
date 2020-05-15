package com.springbootcamp.springsecurity.entities;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import javax.persistence.Embeddable;

@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Addresscopy {

    String city;

    String state;

    String country;

    String addressline;

    String zipcode;

    String label;


    public Addresscopy(Address orderAddress) {
        this.city = orderAddress.getCity();
        this.state = orderAddress.getState();
        this.country = orderAddress.getCountry();
        this.addressline = orderAddress.getAddressLine();
        this.label = orderAddress.getLable();
        this.zipcode = orderAddress.getZipcode();

    }

    public Addresscopy() {

    }
}
