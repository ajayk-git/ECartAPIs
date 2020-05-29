package com.springbootcamp.springsecurity.entities.users;
//table per class is implemented

import com.springbootcamp.springsecurity.entities.product.Product;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@PrimaryKeyJoinColumn(name = "UserID")
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description="All details about Seller.")
public class Seller extends User {




     String gst;

     String companyName;

     String companyContact;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SUBSELECT)
     Set<Product> productSet;

    @OneToOne(cascade = CascadeType.ALL)
     Address address;


}
