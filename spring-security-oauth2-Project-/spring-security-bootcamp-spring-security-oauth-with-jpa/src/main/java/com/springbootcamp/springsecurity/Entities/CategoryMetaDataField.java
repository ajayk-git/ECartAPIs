package com.springbootcamp.springsecurity.Entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetaDataField {

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
