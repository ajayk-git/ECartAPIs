package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.CategoryMetadataCompositeKey;
import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MetaDataFieldValuesRepository extends CrudRepository< CategoryMetadataFieldValues,CategoryMetadataCompositeKey> {

    Optional<CategoryMetadataFieldValues> findById(CategoryMetadataCompositeKey compositeKey);


    List<CategoryMetadataFieldValues> findByCategoryId(Long id);
}
