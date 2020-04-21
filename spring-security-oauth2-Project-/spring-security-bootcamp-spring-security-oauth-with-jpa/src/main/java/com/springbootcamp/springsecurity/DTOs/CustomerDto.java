package com.springbootcamp.springsecurity.DTOs;
import com.springbootcamp.springsecurity.Entities.Users.Customer;

import lombok.Getter;
import lombok.Setter;
import com.springbootcamp.springsecurity.dto.UserDto;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@Slf4j
public class CustomerDto extends UserDto {


    private String contact;

    public CustomerDto(Long id, String email, String firstName, String lastName, String contact,boolean isactive) {

        this.setLastName(lastName);
        this.setContact(contact);
        this.setEmail(email);
        this.setFirstName(firstName);
        this.setId(id);
        this.setActive(isactive);
    }

    public CustomerDto(){

    }

    public CustomerDto customerObjectToDTO(Customer customer){

        CustomerDto dto = new CustomerDto();
        dto.setEmail(customer.getEmail());
        dto.setContact(customer.getContact());
        dto.setLastName(customer.getLastName());
        dto.setFirstName(customer.getFirstName());
        dto.setId(customer.getId());
        return dto;
    }

}

//    public CustomerDto getCustomerById(int id) throws AccountDoesNotExist {
//        Optional<Customer> optionalCustomer=customerRepository.findById(id);
//        if(!optionalCustomer.isPresent()) throw new AccountDoesNotExist();
//        else {
//            Customer customer=optionalCustomer.get();
//            CustomerDto customerDto=new CustomerDto();
//            customerDto.setId(customer.getId());
//            customerDto.setEmail(customer.getEmail());
//            customerDto.setFirstName(customer.getFirstName());
//            customerDto.setLastName(customer.getLastName());
//            customerDto.setContact(customer.getContact());
//            return customerDto;
//        }
//
//    }


// (through reference chain: com.springbootcamp.springsecurity.DTOs.CustomerDto[\"allCustomers\"])

//    public List<CustomerDto> getAllCustomers(){
//
//
////        List<CustomerDto> customerDtos=new ArrayList<>();
//////        Iterable<Customer> customerList= customerRepository.findAll();
////        customerList.forEach(customer -> customerDtos.add(customerObjectToDTO(customer)));
////        return customerDtos;
////    }
////

//    public List<CustomerDto> getAllCustomers(){
//        Iterable<Customer> customersList= customerRepository.findAll();
//        List<CustomerDto> customerDtoList = new ArrayList<>();
//        customersList.forEach(customer -> customerDtoList
//                .add(new CustomerDto(customer.getId(),
//                customer.getEmail(),
//                customer.getFirstName(),
//                customer.getLastName(),
//                customer.getContact())));
//        return customerDtoList;
//    }
