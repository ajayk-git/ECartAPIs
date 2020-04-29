package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.CategoryMetadataCompositeKey;
import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import org.springframework.data.repository.CrudRepository;

public interface MetaDataFieldValuesRepository extends CrudRepository< CategoryMetadataFieldValues,CategoryMetadataCompositeKey> {


}
