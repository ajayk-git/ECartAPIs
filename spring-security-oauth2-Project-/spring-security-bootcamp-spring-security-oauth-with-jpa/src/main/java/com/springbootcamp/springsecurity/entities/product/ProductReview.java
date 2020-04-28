package com.springbootcamp.springsecurity.entities.product;

import com.springbootcamp.springsecurity.entities.users.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
//@Table(name = "PRODUCT_REVIEW")
public class ProductReview{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "REVIEW")
    private String review;

    //@Column(name = "RATING(1-5)")
    private float rating;

    @ManyToOne
    @JoinColumn(name = "Customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

}
