package com.springbootcamp.springsecurity.entities.product;


import com.springbootcamp.springsecurity.entities.users.Seller;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@ApiModel(description = "All details about the Product Entity.")
//@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Product Id.")
    Long id;

    @ApiModelProperty(notes = "Product Name.")
    String name;

    @ApiModelProperty(notes = "Product Description.")
    String description;

    @ApiModelProperty(notes = "Product cancellable or not.")
    Boolean isCancelable;

    @ApiModelProperty(notes = "Product returnable or not.")
    Boolean isReturnable;

    @ApiModelProperty(notes = "Brand of product")
    String brand;

    @ApiModelProperty(notes = "Product is active or not.")
    Boolean isActive;

    @ApiModelProperty(notes = "Product is deleted or not.")
    Boolean isDeleted;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)    // to map product to product variant
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ProductVariation> productVariationList;


    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")   //to map product to category
    private Category category;

    @ManyToOne
    @JoinColumn(name = "Seller_id")   //to map product to seller
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> productReviewList;


    public Product() {
    }

}
