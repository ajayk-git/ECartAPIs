package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.AddressCO;
import com.springbootcamp.springsecurity.co.CustomerProfileUpdateCo;
import com.springbootcamp.springsecurity.co.PasswordUpdateCO;
import com.springbootcamp.springsecurity.ConfirmationToken;
import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.entities.users.Address;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.ConfirmationTokenRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@Log4j2
public class CustomerService {


    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Autowired
    AuditLogsMongoDBService auditService;

    @Autowired
    ConfirmationToken confirmationToken;
    @Autowired
    JavaMailSender javaMailSender;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    private boolean isEmailExists(String Email) {
        return userRepository.findByEmail(Email) != null;
    }


    //=============================update a customer details ================================

    public ResponseEntity<String> updateCustomerProfile(String email, CustomerProfileUpdateCo customerProfileUpdateCo, Principal principal) {

        log.info("inside updateCustomerProfile method");

        Customer customerToUpdate = customerRepository.findByEmail(email);
        if (customerProfileUpdateCo.getContact() != null)
            customerToUpdate.setContact(customerProfileUpdateCo.getContact());
        if (customerProfileUpdateCo.getFirstName() != null)
            customerToUpdate.setFirstName(customerProfileUpdateCo.getFirstName());
        if (customerProfileUpdateCo.getLastName() != null)
            customerToUpdate.setLastName(customerProfileUpdateCo.getLastName());

        customerRepository.save(customerToUpdate);

        auditService.updateObject("User", customerToUpdate.getId(), principal.getName());

        log.info("Customer profile updated successfully.");

        return new ResponseEntity<String>("Profile has been updated.", HttpStatus.OK);


    }

    public ResponseEntity<String> updateCustomerPassword(PasswordUpdateCO passwordUpdateCO, String email, Principal principal) {

        log.info("inside updateCustomerPassword method");

        User user = userRepository.findByEmail(email);
        user.setPassword(encoder.encode(passwordUpdateCO.getPassword()));

        emailService.sendMailPasswordUpdate(user.getEmail());
        userRepository.save(user);

        auditService.updateObject("User", user.getId(), principal.getName());

        return new ResponseEntity<>("Password updated and alert message has been send to registered mail id.", HttpStatus.OK);

    }


    public CustomerDto viewCustomerProfile(String email, Principal principal) {

        log.info("inside viewCustomerProfile method");

        Customer customer = customerRepository.findByEmail(email);
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getEmail(), customer.getFirstName(), customer.getLastName(), customer.getContact(), customer.getIsActive());

        auditService.readObject("Customer", customer.getId(), principal.getName());

        return customerDto;
    }


//===========================================Get addressList  by customer===============================

    public List<AddressDto> getAddressListCustomer(String email, Principal principal) {

        log.info("inside getAddressListCustomer method");


        boolean emailFlag = isEmailExists(email);
        if (emailFlag == false) {
            throw new AccountDoesNotExistException("User's  does not exist");
        }
        List<AddressDto> addressList = new ArrayList<>();
        Customer customer = customerRepository.findByEmail(email);

        customer.getAddressList().forEach(address -> addressList.add(new AddressDto(address.getId(),
                address.getAddressLine(), address.getCity(), address.getState(),
                address.getZipcode(), address.getLable(), address.getCountry())));

        auditService.readObject("Address", customer.getId(), principal.getName());

        return addressList;
    }

    //============================================Delete Address by Customer========================================================

    public ResponseEntity<String> deleteAddressById(Long id, String email, Principal principal) {

        log.info("inside deleteAddressById method");

        Customer customer = customerRepository.findByEmail(email);

        List<Address> addressList = customer.getAddressList();

        for (Address address : addressList) {
            if (address.getId() == id) {

                log.warn("Customer Address will be deleted.");
                customer.deleteAddress(address);
                customerRepository.save(customer);
                auditService.deleteObject("Address", address.getId(), principal.getName());

                log.info("Customer Address has been deleted successfully.");

                return new ResponseEntity<String>("Deleted Address with id " + id, null, HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>("Address not found by mentioned address id ", HttpStatus.NOT_FOUND);

    }

    //============================================Add new Address by Customer========================================================

    public ResponseEntity<String> addCustomerAddress(AddressCO addressCO, String email, Principal principal) {

        log.info("inside addCustomerAddress method");

        if (customerRepository.findByEmail(email) == null) {
            return new ResponseEntity<String>("customer does not exist with given id.", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerRepository.findByEmail(email);
        Address address = new Address();
        List<Address> addressList = new ArrayList<>();
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

        auditService.saveNewObject("Address", address.getId(), principal.getName());

        log.info("Address is successfully added to customers address list");

        return new ResponseEntity<String>("Address is successfully added to customers address list", HttpStatus.CREATED);

    }


    //============================================Update Address by Customer========================================================

    public ResponseEntity<String> updateCustomerAddress(AddressCO addressCO, Long id, String email, Principal principal) {

        log.info("inside updateCustomerAddress method");

        Customer customer = customerRepository.findByEmail(email);
        List<Address> addressList = customer.getAddressList();
        for (Address address : addressList) {
            if (address.getId() == id) {
                if (addressCO.getAddressLine() != null)
                    address.setAddressLine(addressCO.getAddressLine());
                if (addressCO.getLable() != null)
                    address.setLable(addressCO.getLable());
                if (addressCO.getCity() != null)
                    address.setCity(addressCO.getCity());
                if (addressCO.getState() != null)
                    address.setState(addressCO.getState());
                if (addressCO.getCountry() != null)
                    address.setCountry(addressCO.getCountry());
                if (addressCO.getZipcode() != null)
                    address.setZipcode(addressCO.getZipcode());
                customerRepository.save(customer);

                auditService.updateObject("Address", address.getId(), principal.getName());

                log.info("Customer Address has been updated successfully.");

                return new ResponseEntity<>("Customer Address has been updated successfully.", HttpStatus.OK);

            }

        }
        throw new ResourceNotFoundException("No address found with id : " + id + ", associated with your account");
    }


}

