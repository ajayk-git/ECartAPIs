package com.springbootcamp.springsecurity.entities;

//import javax.persistence.Embeddable;
//import javax.persistence.SecondaryTable;
//import java.io.Serializable;
//@Getter
//@Setter
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Embeddable
//public class    CategoryMetadataCompositeKey implements Serializable {
//
//    Long categoryId;
//    Long categoryMetaDataFieldId;
//}

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