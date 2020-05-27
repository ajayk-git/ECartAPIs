package com.springbootcamp.springsecurity.entities;

import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer quantity;

    Boolean isWishListItem;

    @ManyToOne
    @JoinColumn(name = " CartId")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "ProductVariationId")
    ProductVariation productVariation;

}
