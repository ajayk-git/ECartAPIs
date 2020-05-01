package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.CategoryMetadataCompositeKey;
import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MetaDataFieldValuesRepository extends CrudRepository< CategoryMetadataFieldValues,CategoryMetadataCompositeKey> {

    Optional<CategoryMetadataFieldValues> findById(CategoryMetadataCompositeKey compositeKey);


    @Query(value = "select * from CategoryMetadataFieldValues where categoryId=:id",nativeQuery = true)
    List<CategoryMetadataFieldValues> findByCategoryId(@Param("id") Long id);

}
