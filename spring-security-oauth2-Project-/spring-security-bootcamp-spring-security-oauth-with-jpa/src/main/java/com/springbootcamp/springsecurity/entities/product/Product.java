package com.springbootcamp.springsecurity.entities.product;


import com.springbootcamp.springsecurity.entities.users.Seller;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "PRODUCT")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;


   String name;

   String description;

   Boolean isCancelable;

   Boolean isReturnable;

   String brand;

   Boolean isActive;

    Boolean isDeleted;


  @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)    // to map product to product variant
  @Fetch(value=FetchMode.SUBSELECT)
  private List<ProductVariation> productVariationList;


  @ManyToOne
  @JoinColumn(name = "CATEGORY_ID")   //to map product to category
  private Category category;

  @ManyToOne
  @JoinColumn(name = "Seller_id")   //to map product to seller
  private Seller seller;

  @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
  private List<ProductReview> productReviewList;



  public Product() {
  }

}
