package com.springbootcamp.springsecurity.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSellerDto {
     Long id;

     String name;

     String description;

     String brand;

     String companyName;

     String CategoryName;

     boolean isActive;

     boolean isCancelable;

     boolean isReturnable;


     public ProductSellerDto(Long id, String name, String brand, String description, String companyName, String categoryName, boolean active, boolean cancelable, boolean returnable) {

          this.setId(id);
          this.setName(name);
          this.setBrand(brand);
          this.setDescription(description);
          this.setCompanyName(companyName);
          this.setCategoryName(categoryName);
          this.setActive(active);
          this.setCancelable(cancelable);
          this.setReturnable(returnable);
     }

     public ProductSellerDto() {

     }
}
