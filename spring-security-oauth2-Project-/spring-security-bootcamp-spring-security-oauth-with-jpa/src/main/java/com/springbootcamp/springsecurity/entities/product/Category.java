package com.springbootcamp.springsecurity.entities.product;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description = "All details about the category.")
//@Table(name = "CATEGORY")
public class Category {

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastModifiedDate;


    @Id
    @Column(name = "categoryId")
    @ApiModelProperty(notes = "Category Id.")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ApiModelProperty(notes = "Category Name")
    @Column(name = "NAME")
    String name;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    List<Category> subCategory;


    @ManyToOne
    @JoinColumn(name = "parent_Id")
    Category parentCategory;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // to map product with category
            List<Product> productList;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<CategoryMetadataFieldValues> categoryMetadataFieldValuesList;

    public Category() {

    }


    public void addCategory(Category categoryObject) {
        if (categoryObject != null) {
            if (subCategory == null)
                subCategory = new ArrayList<>();
            else
                subCategory.add(categoryObject);
        }

    }

}
