package com.springbootcamp.springsecurity.entities;

import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@Table(name = "CART")
public class Cart {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    private int quantity;

    private boolean isWishListItem;

    @OneToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "Product_variation_id")
    private ProductVariation productVariation;







//    IS_WISHLIST_ITEM
//            PRODUCT_VARIATION_ID

}
//    @OneToOne(cascade = CascadeType.ALL)//cascade = CascadeType.ALL
//   @JoinColumn(name = "USER_ID")
//    private User user;
