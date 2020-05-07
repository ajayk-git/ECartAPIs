package com.springbootcamp.springsecurity.entities.product;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ApiModel(description = "All details about the category.")
//@Table(name = "CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    @ApiModelProperty(notes = "Category Id.")
    private  Long id;

    @ApiModelProperty(notes = "Category Name")
    @Column(name = "NAME")
    private String  name;

    @OneToMany(mappedBy = "parentCategory",cascade = CascadeType.ALL)
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



    public void addCategory(Category categoryObject)
    {
        if(categoryObject != null)
        {
            if(subCategory == null)
                subCategory = new ArrayList<>();
            else
                subCategory.add(categoryObject);
            }

        }

}
