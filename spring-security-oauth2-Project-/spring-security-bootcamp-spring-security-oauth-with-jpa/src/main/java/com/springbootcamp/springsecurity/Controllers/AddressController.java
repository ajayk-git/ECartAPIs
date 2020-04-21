package com.springbootcamp.springsecurity.Controllers;


import com.springbootcamp.springsecurity.CO.AddressCO;
import com.springbootcamp.springsecurity.DTOs.AddressDto;
import com.springbootcamp.springsecurity.Services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping("/customer/{id}")                   // id=customer id
    public List<AddressDto> getAllAddressCustomer(@PathVariable(name = "id") Long id ){
        return addressService.getAddressListCustomer(id);
    }

    @GetMapping("seller/{id}")                   // id=seller id
    public AddressDto getAddressSeller(@PathVariable(name = "id") Long id ){
        return addressService.getAddressSeller(id);
    }

    @DeleteMapping("/delete/{id}")               //id=address id
    public ResponseEntity<String> deleteAddressById(@PathVariable(name = "id") Long id, Principal principal) {
           return addressService.deleteAddressById(id,principal.getName());
    }

    @PostMapping("/addCustomerAddress")
    public ResponseEntity<String> addCustomerNewAddress(@RequestBody AddressCO addressCO,Principal principal){
        return addressService.addCustomerAddress(addressCO,principal.getName());
    }

    @PatchMapping("/updateCustomerAddress/{id}")
    public ResponseEntity<String> updateCustomerAddress(@PathVariable("id") Long id,@RequestBody AddressCO addressCO,Principal principal){
        return addressService.updateCustomerAddress(addressCO,id,principal.getName());
    }

    @PatchMapping("/updateSellerAddress/{id}")
    public ResponseEntity<String> updateSellerAddress(@PathVariable("id") Long id,@RequestBody  AddressCO addressCO,Principal principal){
        return addressService.updateSellerAddress(addressCO,id,principal.getName());
    }

}
