package com.springbootcamp.springsecurity.Controllers;


import com.springbootcamp.springsecurity.CO.CustomerCO;
import com.springbootcamp.springsecurity.CO.CustomerProfileUpdateCo;
import com.springbootcamp.springsecurity.CO.EmailCO;
import com.springbootcamp.springsecurity.CO.PasswordUpdateCO;
import com.springbootcamp.springsecurity.DTOs.CustomerDto;
import com.springbootcamp.springsecurity.Entities.Address;
import com.springbootcamp.springsecurity.Entities.Users.Customer;
import com.springbootcamp.springsecurity.Exceptions.AccountAreadyExistException;
import com.springbootcamp.springsecurity.Exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.Repositories.CustomerRepository;
import com.springbootcamp.springsecurity.Repositories.UserRepository;
import com.springbootcamp.springsecurity.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/customer")
@RestController
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerService customerService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


//=============================================get a customer by id===========================================================================
    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Integer id) throws AccountDoesNotExistException {
        return customerService.getCustomerById(id);
    }


//=============================================get all customers===========================================================================


    @GetMapping("/")
    public List<CustomerDto> getAllCustomer(){
        return  customerService.getAllCustomers();
    }




    @PatchMapping("updateCustomerPassword")
    public ResponseEntity<String> updateCustomerPassword(@Valid @RequestBody PasswordUpdateCO passwordUpdateCO,Principal principal){
        return customerService.updateCustomerPassword(passwordUpdateCO,principal.getName());
    }


//================================================== Customer registration====================================================================


    @PostMapping("/registration")
    public String registerCustomer(@Valid @RequestBody CustomerCO customerCO) throws AccountAreadyExistException {
       // CustomerDto customerDto1=new CustomerDto();
        Customer customer=customerService.registerCustomer(customerCO);
        CustomerDto customerDto=new CustomerDto(customer.getId(),customer.getFirstName(),customer.getLastName(),customer.getEmail(),customer.getContact());
        //return customerDto1;
        return "successfully registered, Kindly go to registered mail inbox to get verified your account.";
    }

//=====================================================Confirmation of Customer registration================================================


    @PatchMapping("/registrationConfirm")
    public String registrationConfirm(@RequestParam("token") String token){
        return  customerService.registrationConfirmCustomer(token);
    }


//============================================================forgot password=========================================================================
    @PostMapping("/forgotPassword")
    public  void  forgotPassword(@RequestBody EmailCO emailCO){
        customerService.forgotPasswordSendTokenToMail(emailCO.getEmail());
    }


//==================================================reset your forgot passsword=====================================================================
    @PatchMapping("/confirmReset")
    public ResponseEntity<String> updateForgotPassword(@RequestParam("token") String token,@Valid @RequestBody PasswordUpdateCO passwordUpdateCO ){
       return customerService.updateForgotPassword(token,passwordUpdateCO);
    }


//=================================================Activation  of Customer Account=========================================================

    @PatchMapping("/activateAccount/{id}")
    public ResponseEntity<String> activateCustomerAccountById(@PathVariable("id") Long id)   {
        return  customerService.activateCustomerAccountById(id);
    }



//==============================================Deactivation  of Customer Account============================================================

    @PatchMapping("/deactivateAccount/{id}")
    public ResponseEntity<String> deactivateCustomerAccountById(@PathVariable("id")Long id) {
        return  customerService.deactivateCustomerAccountById(id);
    }

    //=================================================View Profile  of Customer Account==================================

    @GetMapping("/viewProfile")
    public CustomerDto viewCustomerProfile(Principal principal){
        return  customerService.viewCustomerProfile(principal.getName());
    }

    //=================================================Update Profile  of Customer Account==================================

    @PatchMapping("updateProfile")
    public ResponseEntity<String> updateCustomerProfile(Principal principal, @RequestBody CustomerProfileUpdateCo customerProfileUpdateCo){
        return customerService.updateCustomerProfile(principal.getName(),customerProfileUpdateCo);
    }

}






















//
//    @DeleteMapping("/{id}")
//    public String deleteCustomer(@PathVariable Long id) {
//        return customerService.deleteCustomer(id);
//    }