package com.springbootcamp.springsecurity.entities;

import com.springbootcamp.springsecurity.entities.product.Category;
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
    @JoinColumn(name = "categoryId")
    @MapsId("categoryId")
     Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fieldId")
    @MapsId("categoryMetadataFieldId")
    CategoryMetaDataField categoryMetaDataField;

    String fieldValues;

}
