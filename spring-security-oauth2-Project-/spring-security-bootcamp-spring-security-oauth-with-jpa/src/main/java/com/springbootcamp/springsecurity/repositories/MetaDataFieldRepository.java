package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.CategoryMetaDataField;
import com.springbootcamp.springsecurity.entities.CategoryMetadataCompositeKey;
import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MetaDataFieldRepository extends CrudRepository<CategoryMetaDataField,Long> {

    String findByFieldName(String fieldName);

}
