package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.CategoryMetaDataField;
import org.springframework.data.repository.CrudRepository;

public interface MetaDataFieldRepository extends CrudRepository<CategoryMetaDataField,Long> {

    String findByFieldName(String fieldName);

}
