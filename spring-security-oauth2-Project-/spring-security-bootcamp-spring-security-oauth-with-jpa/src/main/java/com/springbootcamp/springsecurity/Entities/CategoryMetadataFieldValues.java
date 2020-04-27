package com.springbootcamp.springsecurity.Entities;

import com.springbootcamp.springsecurity.Entities.Product.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CategoryMetadataFieldValues {

    @EmbeddedId
    CategoryMetadataCompositeKey compositeKey=new CategoryMetadataCompositeKey();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("categoryId")
     Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("categoryMetadataFieldId")
    CategoryMetaDataField categoryMetaDataField;


    String fieldValues;

}
