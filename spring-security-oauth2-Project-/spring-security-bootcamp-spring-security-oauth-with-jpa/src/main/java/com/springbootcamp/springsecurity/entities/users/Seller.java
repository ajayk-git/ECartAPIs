package com.springbootcamp.springsecurity.entities.users;
//table per class is implemented

import com.springbootcamp.springsecurity.entities.Address;
import com.springbootcamp.springsecurity.entities.product.Product;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;
@Getter
@Setter
@Entity
//@Table(name = "SELLER")
@PrimaryKeyJoinColumn(name = "UserID")
public class Seller extends User {


    private String gst;

    private String companyName;

    private String companyContact;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SUBSELECT)
    private Set<Product> productSet;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;


}
