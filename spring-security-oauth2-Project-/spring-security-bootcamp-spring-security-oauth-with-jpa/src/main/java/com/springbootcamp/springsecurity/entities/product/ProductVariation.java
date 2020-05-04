package com.springbootcamp.springsecurity.entities.product;

import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.order.OrderProduct;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
//@Table(name = "PRODUCT_VARIATION")
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String primaryImage_Name;

    private int quantityAvailable;

    private float price;

    private  boolean isActive;

    @ElementCollection(targetClass=String.class)
    private Map<String,String> metaData;

    @ManyToOne
    //@JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariation",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SUBSELECT)
    private List<OrderProduct> orderProductList;

    @OneToMany(mappedBy = "productVariation",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value=FetchMode.SUBSELECT)

    private List<Cart> cartList;

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


