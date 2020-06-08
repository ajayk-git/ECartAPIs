package com.springbootcamp.springsecurity.dtos;

import com.springbootcamp.springsecurity.entities.users.Customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Customer DTO representation")
public class CustomerDto extends AddressDto.UserDto {


    @ApiModelProperty(value = "Contact number of User.")
    String contact;

    public CustomerDto(Long id, String email, String firstName, String lastName, String contact, boolean isactive) {

        this.setLastName(lastName);
        this.setContact(contact);
        this.setEmail(email);
        this.setFirstName(firstName);
        this.setId(id);
        this.setActive(isactive);
    }

    public CustomerDto() {

    }

    public CustomerDto customerObjectToDTO(Customer customer) {

        CustomerDto dto = new CustomerDto();
        dto.setEmail(customer.getEmail());
        dto.setContact(customer.getContact());
        dto.setLastName(customer.getLastName());
        dto.setFirstName(customer.getFirstName());
        dto.setId(customer.getId());
        return dto;
    }

}
