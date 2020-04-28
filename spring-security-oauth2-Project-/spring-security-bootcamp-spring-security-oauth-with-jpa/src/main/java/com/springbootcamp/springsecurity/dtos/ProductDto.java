package com.springbootcamp.springsecurity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private boolean isCancelable;

    private boolean isReturnable;

    private String brand;

    private String companyName;

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
