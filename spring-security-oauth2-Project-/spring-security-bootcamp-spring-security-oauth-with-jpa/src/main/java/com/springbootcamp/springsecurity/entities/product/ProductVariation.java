package com.springbootcamp.springsecurity.entities.product;

import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.order.OrderProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "PRODUCT_VARIATION")
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     String primaryImage_Name;

     Integer quantityAvailable;

     Float price;

      Boolean isActive;

    @ElementCollection(targetClass=String.class)
     Map<String,String> metaData;

    @ManyToOne
    //@JoinColumn(name = "product_id")
     Product product;

    @OneToMany(mappedBy = "productVariation",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SUBSELECT)
     List<OrderProduct> orderProductList;

    @OneToMany(mappedBy = "productVariation",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value=FetchMode.SUBSELECT)
     List<Cart> cartList;

     public ProductVariation(){

    }

    public ProductVariation(String primaryImage_Name, int quantityAvailable, float price, boolean isActive, Map<String, String> metadata) {
        this.primaryImage_Name = primaryImage_Name;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
        this.isActive = isActive;
        this.setMetaData(metadata);
    }
}
//    @OneToMany(mappedBy ="productVariationSet",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    private Set<Cart> cartSet;



    //    ID
//            PRODUCT_ID
//    QUANTITY_AVAILABLE
//            PRICE
//"METADATA (Type: JSON - available in mysql to store a JSON as it is.)
//        (Note: will contain all the information regarding variations in JSON format)
//            (All variations of same category will have a fixed similar JSON structure)"
//    PRIMARY_IMAGE_NAME


