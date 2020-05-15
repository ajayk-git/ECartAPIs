package com.springbootcamp.springsecurity.entities.product;

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

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class ProductReview{

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastModifiedDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    //@Column(name = "REVIEW")
     String review;

    //@Column(name = "RATING(1-5)")
     float rating;

    @ManyToOne
    @JoinColumn(name = "Customer_id")
     Customer customer;

    @ManyToOne
    @JoinColumn(name="product_id")
     Product product;

}
