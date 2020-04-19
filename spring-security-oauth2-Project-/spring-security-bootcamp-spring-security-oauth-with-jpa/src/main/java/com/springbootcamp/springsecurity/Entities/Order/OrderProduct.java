package com.springbootcamp.springsecurity.Entities.Order;

import com.springbootcamp.springsecurity.Entities.Product.ProductVariation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@Table(name = "ORDER_PRODUCT")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_PRODUCT_ID")
    private Long id;
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "METADATA")
    private String metadata;

    private float price;


    @ManyToOne
    @JoinColumn(name="ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_VAR_ID")
    private ProductVariation productVariation;

    @OneToOne(mappedBy = "orderProduct")
    private OrderStatus orderStatus;







    //    ID.
//            ORDER_ID.
//    QUANTITY.
//            PRICE.
//    PRODUCT_VARIATION_ID
//            PRODUCT_VARIATION_METADATA
}
