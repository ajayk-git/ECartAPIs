package com.springbootcamp.springsecurity.entities.users;

import com.springbootcamp.springsecurity.entities.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Address {

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastModifiedDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     String city;

     String state;

     String country;

     String addressLine;

     String lable;

     String zipcode;

    @ManyToOne
    @JoinColumn(name = "user_id")
      User user;


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
