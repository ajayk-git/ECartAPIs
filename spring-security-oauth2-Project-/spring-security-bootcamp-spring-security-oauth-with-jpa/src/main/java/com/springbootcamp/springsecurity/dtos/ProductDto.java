package com.springbootcamp.springsecurity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Product DTO representation")
public class ProductDto {

    @ApiModelProperty(value = "Product Id")
    Long id;

    @ApiModelProperty(value = "Product Name")
    String name;

    @ApiModelProperty(value = "Product description")
    String description;

    @ApiModelProperty(value = "Product is cancellable or not")
    boolean isCancelable;

    @ApiModelProperty(value = "Product is returnable or not")
    boolean isReturnable;

    @ApiModelProperty(value = "Product's Brand")
    String brand;

    @ApiModelProperty(value = "Product's Company Name")
    String companyName;

    public ProductDto(String brand, String description, Long id, String name, String companyName, boolean cancelable, boolean returnable) {

        this.setBrand(brand);
        this.setDescription(description);
        this.setId(id);
        this.setName(name);
        this.setReturnable(returnable);
        this.setCancelable(cancelable);
        this.setCompanyName(companyName);
    }




    public boolean isCancelable() {
        return isCancelable;
    }

    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }

    public boolean isReturnable() {
        return isReturnable;
    }

    public void setReturnable(boolean returnable) {
        isReturnable = returnable;
    }
}
