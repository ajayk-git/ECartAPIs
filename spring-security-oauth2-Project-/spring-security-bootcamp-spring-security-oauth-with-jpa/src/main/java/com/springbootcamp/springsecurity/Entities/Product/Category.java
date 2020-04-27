package com.springbootcamp.springsecurity.Entities.Product;


import com.springbootcamp.springsecurity.Entities.CategoryMetadataFieldValues;
import com.springbootcamp.springsecurity.Entities.Product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.PrivateKey;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
//@Table(name = "CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private  Long id;

    @Column(name = "NAME")
    private String  name;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategory;

    @ManyToOne
    @JoinColumn(name = "parent_Id")
    private  Category parentCategory;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.EAGER) // to map product with category
    private List<Product> productList;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValuesList;

    public  Category(){

    }


    //    CATEGORY
//            ID
//    NAME
//            PARENT_ID
}
