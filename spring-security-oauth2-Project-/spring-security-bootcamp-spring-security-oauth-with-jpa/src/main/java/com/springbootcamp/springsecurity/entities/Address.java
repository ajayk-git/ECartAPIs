package com.springbootcamp.springsecurity.entities;

import com.springbootcamp.springsecurity.entities.users.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
//@Table(name = "ADDRESS")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private String lable;
    private String zipcode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;


    public Address(String city, String state, String country, String lable, String addressLine, String zipcode) {
        this.setAddressLine(addressLine);
        this.setCity(city);
        this.setState(state);
        this.setCountry(country);
        this.setLable(lable);
        this.setZipcode(zipcode);
    }

    public Address(){

    }
}
