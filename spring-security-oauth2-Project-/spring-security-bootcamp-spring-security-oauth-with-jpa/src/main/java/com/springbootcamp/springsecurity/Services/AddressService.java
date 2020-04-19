package com.springbootcamp.springsecurity.Services;

import com.springbootcamp.springsecurity.CO.AddressCO;
import com.springbootcamp.springsecurity.DTOs.AddressDto;
import com.springbootcamp.springsecurity.Entities.Address;
import com.springbootcamp.springsecurity.Entities.Users.Customer;
import com.springbootcamp.springsecurity.Entities.Users.Seller;
import com.springbootcamp.springsecurity.Exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.Exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.Repositories.AddressRepository;
import com.springbootcamp.springsecurity.Repositories.CustomerRepository;
import com.springbootcamp.springsecurity.Repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    MessageSource messageSource;



    public List<AddressDto> getAddressListCustomer(Long id) {
        if(!customerRepository.findById(id).isPresent()){
            throw new AccountDoesNotExistException("User's  does not exist");
        }
        List<AddressDto> addressList=new ArrayList<>();
        Customer customer=customerRepository.findById(id).get();

        customer.getAddressList().forEach(address ->addressList.add(new AddressDto(address.getId(),
                address.getAddressLine(),address.getCity(),address.getState(),
                address.getZipcode(),address.getLable(),address.getCountry())));

        return addressList;
    }


    public AddressDto getAddressSeller(Long id) {
        if (!sellerRepository.findById(id).isPresent()){
            throw new AccountDoesNotExistException("User's  does not exist");
        }
        Seller seller=sellerRepository.findById(id).get();
        AddressDto addressDto=new AddressDto(seller.getAddress().getId(),
                seller.getAddress().getAddressLine(),
                seller.getAddress().getCity(),
                seller.getAddress().getState(),
                seller.getAddress().getZipcode(),
                seller.getAddress().getLable(),
                seller.getAddress().getCountry());
        return addressDto;

    }


    public ResponseEntity<String> deleteAddressById(Long id,String email){
        Customer customer = customerRepository.findByEmail(email);

        List<Address> addressList =  customer.getAddressList();

        for(Address address : addressList)
        {
            if(address.getId() == id)
            {
                customer.deleteAddress(address);
                customerRepository.save(customer);
                return new ResponseEntity<String>("Deleted Address with id " + id, null, HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>("Address not found by mentioned adress id ",HttpStatus.NOT_FOUND);

        }




    public ResponseEntity<String> addCustomerAddress(AddressCO addressCO,String email) {

        if(customerRepository.findByEmail(email)==null){
            return new ResponseEntity<String>("customer does not exist with given id.",HttpStatus.BAD_REQUEST);
        }
        Customer customer=customerRepository.findByEmail(email);
        Address address=new Address();
        List<Address> addressList=new ArrayList<>();
        address.setAddressLine(addressCO.getAddressLine());
        address.setCity(addressCO.getCity());
        address.setState(addressCO.getState());
        address.setCountry(addressCO.getCountry());
        address.setZipcode(addressCO.getZipcode());
        address.setLable(addressCO.getLable());
        address.setUser(customer);
        addressList.add(address);
        customer.addAddress(address);
        customerRepository.save(customer);
        return new ResponseEntity<String>("Address is successfully added to customers address list",HttpStatus.CREATED);

    }

    public ResponseEntity<String> updateCustomerAddress(AddressCO addressCO,Long id,String email) {

        Customer customer=customerRepository.findByEmail(email);
        List<Address> addressList=customer.getAddressList();
        for(Address address:addressList){
            if(address.getId()==id){
                if(addressCO.getAddressLine()!=null)
                    address.setAddressLine(addressCO.getAddressLine());
                if(addressCO.getLable()!=null)
                    address.setLable(addressCO.getLable());
                if(addressCO.getCity()!=null)
                    address.setCity(addressCO.getCity());
                if(addressCO.getState()!=null)
                    address.setState(addressCO.getState());
                if(addressCO.getCountry()!=null)
                    address.setCountry(addressCO.getCountry());
                if(addressCO.getZipcode()!=null)
                    address.setZipcode(addressCO.getZipcode());
                customerRepository.save(customer);
                return new ResponseEntity<>("Customer Address has been updated successfully.",HttpStatus.OK);

            }

        }
        throw new ResourceNotFoundException("No address found with id : " + id + ", associated with your account");
    }

    public ResponseEntity<String> updateSellerAddress(AddressCO addressCO, Long id, String email) {
          Seller seller=sellerRepository.findByEmail(email);
          Address address=seller.getAddress();
        if(address.getId()==id){
            if(addressCO.getAddressLine()!=null)
                address.setAddressLine(addressCO.getAddressLine());
            if(addressCO.getLable()!=null)
                address.setLable(addressCO.getLable());
            if(addressCO.getCity()!=null)
                address.setCity(addressCO.getCity());
            if(addressCO.getState()!=null)
                address.setState(addressCO.getState());
            if(addressCO.getCountry()!=null)
                address.setCountry(addressCO.getCountry());
            if(addressCO.getZipcode()!=null)
                address.setZipcode(addressCO.getZipcode());
            sellerRepository.save(seller);
            return new ResponseEntity<>("Seller Address has been updated successfully.",HttpStatus.OK);

        }
        throw new ResourceNotFoundException("No address found with id : " + id + ", associated with your account");

    }



}
