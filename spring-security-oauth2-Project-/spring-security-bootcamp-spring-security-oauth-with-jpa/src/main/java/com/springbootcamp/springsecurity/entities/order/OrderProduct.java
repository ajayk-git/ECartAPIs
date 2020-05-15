package com.springbootcamp.springsecurity.entities.order;

import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description = "All details about the Order in transition ")
public class OrderProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Order Id")
    @Column(name = "ORDER_PRODUCT_ID")
     Long id;

    @ApiModelProperty(notes = "Quantity of product.")
    @Column(name = "QUANTITY")
     int quantity;

    @ApiModelProperty(notes = "MetaData of products .")
    @Column(name = "METADATA")
     String metadata;

    @ApiModelProperty(notes = "Price of product.")
     float price;


    @ManyToOne
    @JoinColumn(name="ORDER_ID")
     Order order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_VAR_ID")
     ProductVariation productVariation;

    @OneToOne(mappedBy = "orderProduct")
     OrderStatus orderStatus;







    //    ID.
//            ORDER_ID.
//    QUANTITY.
//            PRICE.
//    PRODUCT_VARIATION_ID
//            PRODUCT_VARIATION_METADATA
}
