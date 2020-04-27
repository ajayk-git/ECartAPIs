package com.springbootcamp.springsecurity.Entities;

import com.springbootcamp.springsecurity.Entities.Product.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CategoryMetadataCompositeKey  implements Serializable{

    private Long categoryId;
    private Long categoryMetadataFieldId;

    public CategoryMetadataCompositeKey(){

    }
    public CategoryMetadataCompositeKey(Long categoryId,Long categoryMetadataFieldId){
        this.categoryId=categoryId;
        this.categoryMetadataFieldId=categoryMetadataFieldId;
    }

}