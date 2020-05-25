package com.springbootcamp.springsecurity.entities;

import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
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
//@Table(name = "CART")
public class Cart {

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastModifiedDate;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer quantity;

    Boolean isWishListItem;

    @OneToOne
    @JoinColumn(name = "CartId")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "Product_variation_id")
    ProductVariation productVariation;

}
