package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.CategoryMetaDataField;
import org.springframework.data.repository.CrudRepository;

public interface CategoryMetaDataFieldRepository extends CrudRepository<CategoryMetaDataField,Long> {

    String findByFieldName(String fieldName);
}
