package com.springbootcamp.springsecurity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Product Seller DTO representation")
public class ProductSellerDto extends JdkSerializationRedisSerializer implements Serializable {

    @ApiModelProperty(value = "Product Id")
    Long id;

    @ApiModelProperty(value = "Product Name")
    String name;

    @ApiModelProperty(value = "Product's Description")
    String description;

    @ApiModelProperty(value = "Product's Brand Name")
    String brand;

    @ApiModelProperty(value = "Product's Company Name")
    String companyName;

    @ApiModelProperty(value = "Product's category Name")
    String CategoryName;

    @ApiModelProperty(value = "Product is active or not")
    boolean isActive;

    @ApiModelProperty(value = "Product is cancellable or not")
    boolean isCancelable;

    @ApiModelProperty(value = "Product is returnable or not")
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
