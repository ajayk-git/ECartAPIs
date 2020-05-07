package com.springbootcamp.springsecurity.entities.order;

import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@Table(name = "ORDER_PRODUCT")

@ApiModel(description = "All details about the Order in transition ")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Order Id")
    @Column(name = "ORDER_PRODUCT_ID")
    private Long id;

    @ApiModelProperty(notes = "Quantity of product.")
    @Column(name = "QUANTITY")
    private int quantity;

    @ApiModelProperty(notes = "MetaData of products .")
    @Column(name = "METADATA")
    private String metadata;

    @ApiModelProperty(notes = "Price of product.")
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
