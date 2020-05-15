package com.springbootcamp.springsecurity.entities;

import com.springbootcamp.springsecurity.entities.product.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CategoryMetadataFieldValues {

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastModifiedDate;

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
