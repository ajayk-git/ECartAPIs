package com.springbootcamp.springsecurity.entities;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import javax.persistence.Embeddable;
import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class CategoryMetadataCompositeKey  implements Serializable{

     Long categoryId;
     Long categoryMetadataFieldId;

    public CategoryMetadataCompositeKey(){

    }
    public CategoryMetadataCompositeKey(Long categoryId,Long categoryMetadataFieldId){
        this.categoryId=categoryId;
        this.categoryMetadataFieldId=categoryMetadataFieldId;
    }

}