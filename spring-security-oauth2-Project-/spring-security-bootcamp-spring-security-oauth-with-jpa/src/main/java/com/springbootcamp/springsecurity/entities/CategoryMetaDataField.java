package com.springbootcamp.springsecurity.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class CategoryMetaDataField {

    @CreatedDate
    Date createdDate;

    @LastModifiedDate
    Date lastModifiedDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fieldName;

    @OneToMany(mappedBy = "categoryMetaDataField",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<CategoryMetadataFieldValues> categoryMetadataFieldValuesList;

    public CategoryMetaDataField(String fieldName) {
        this.setFieldName(fieldName);
    }

    public CategoryMetaDataField() {

    }
}
