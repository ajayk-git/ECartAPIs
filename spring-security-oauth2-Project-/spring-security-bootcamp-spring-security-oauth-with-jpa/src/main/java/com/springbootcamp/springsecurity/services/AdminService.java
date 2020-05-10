package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.AuditHistory;
import com.springbootcamp.springsecurity.AuditHistoryRepository;
import com.springbootcamp.springsecurity.AuditHistoryService;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class AdminService {


    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    MetaDataFieldRepository categoryMetaDataFieldRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuditHistoryService auditService;

    @Autowired
    EmailService emailService;

    @Autowired
    AuditHistoryRepository auditRepository;

    @Autowired
    ProductRepository productRepository;


    @Scheduled(cron = "0 0/15 20 * * ?")
    public void scheduleTaskSendEmailAdmin() {
        List<User> usersToActivate = userRepository.findByIsNotActive();
        List<Product> productListToActivate = productRepository.findIsNotActiveProduct();
        emailService.sendScheduleMailToActivateUserAndProduct(usersToActivate, productListToActivate);

    }

    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deactivateAccountById(Long id, Principal principal) {

        log.info("inside deactivateAccountById method");


        if (!userRepository.findById(id).isPresent()) {
            log.warn("AccountDoesNotExistException may occur");
            throw new AccountDoesNotExistException("User with mentioned id is registered.So not able to deactivate.");
        } else {
            User user = userRepository.findById(id).get();
            if (user.isEnabled()) {
                user.setEnabled(false);
                user.setActive(false);
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setText("Oppps,Your account has been deactivated by admin registered with mail id :" + user.getEmail());
                simpleMailMessage.setTo(user.getEmail());
                simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
                simpleMailMessage.setSubject("Alert : Account Deactivated by admin");
                javaMailSender.send(simpleMailMessage);
                userRepository.save(user);

                log.info("Account deactivated by Admin.");
                auditService.deactivateObject("User", user.getId(), principal.getName());

                return new ResponseEntity<String>("User is deactivated", HttpStatus.OK);


            } else return new ResponseEntity<String>("User is already deactivated", HttpStatus.BAD_REQUEST);
        }

    }


    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> activateAccountById(Long id, Principal principal) {

        log.info("inside activateAccountById method");


        if (!userRepository.findById(id).isPresent()) {
            log.warn("AccountDoesNotExistException may occur");
            throw new AccountDoesNotExistException(" User with given id is not registered.");
        }
        User user = userRepository.findById(id).get();
        if (!user.isEnabled()) {
            user.setActive(true);
            user.setEnabled(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNotExpired(true);
            user.setFalseAttemptCount(0);
            userRepository.save(user);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setText("Congratulations,Your account has been activated by admin.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("imcoolajaykumar2010@gmail.com");
            mailMessage.setSubject("Alert : Account Activated by admin.");
            javaMailSender.send(mailMessage);

            log.info("Account deactivated by Admin.");

            auditService.activateObject("User", user.getId(), principal.getName());

            return new ResponseEntity<String>("User's Account associated email id : " + user.getEmail() + "is activated.", HttpStatus.CREATED);
        } else
            return new ResponseEntity<String>("User's Account associated with email id  " + user.getEmail() + "is already Activated", HttpStatus.BAD_REQUEST);

    }


    //=================================to get a customer by id from repository===========================

    @Secured("ROLE_ADMIN")
    public CustomerDto getCustomerById(long id, Principal principal) throws AccountDoesNotExistException {

        log.info("inside getCustomerById method");

        CustomerDto customerDTO = new CustomerDto();
        Customer customer;


        if (!customerRepository.findById(id).isPresent()) {
            log.warn("AccountDoesNotExistException may occur");
            throw new AccountDoesNotExistException("Customer does not exist having Customer Id : " + id);
        }
        customer = customerRepository.findById(id).get();

        customerDTO.setId(customer.getId());
        customerDTO.setContact(customer.getContact());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setLastName(customer.getLastName());


        auditService.readObject("User", customer.getId(), principal.getName());

        return customerDTO;
    }


    //==============================to get all customers from repository==============================

    @Secured("ROLE_ADMIN")
    public List<CustomerDto> getAllCustomers(Principal principal) {
        log.info("inside getAllCustomers method");
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Iterable<Customer> customerIterable = customerRepository.findAll();
        customerIterable.forEach(customer -> customerDtoList.add(new CustomerDto(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getContact(),
                customer.getEmail(), customer.isActive())));
        auditService.readAllObjects("User", principal.getName());
        return customerDtoList;
    }


//====================================get details of   a given seller===========================================

    @Secured("ROLE_ADMIN")
    public SellerDto getSellerByid(long id, Principal principal) {

        log.info("inside getSellerByid method");
        SellerDto sellerDto = new SellerDto();
        Seller seller = sellerRepository.findById(id).get();
        if (seller==null){
            log.warn("ResourceNotFoundException may occur.");
            throw new ResourceNotFoundException("Seller with mentioned SellerId is not exist.");
        }
        sellerDto.setId(seller.getId());
        sellerDto.setFirstName(seller.getFirstName());
        sellerDto.setLastName(seller.getLastName());
        sellerDto.setCompanyName(seller.getCompanyName());
        sellerDto.setCompanyContact(seller.getCompanyContact());
        sellerDto.setGst(seller.getGst());
        sellerDto.setEmail(seller.getEmail());
        sellerDto.setAddressLine(seller.getAddress().getAddressLine());
        sellerDto.setCity(seller.getAddress().getCity());
        sellerDto.setState(seller.getAddress().getState());
        sellerDto.setLable(seller.getAddress().getLable());
        sellerDto.setCountry(seller.getAddress().getCountry());
        sellerDto.setZipcode(seller.getAddress().getZipcode());
        auditService.readObject("Seller", seller.getId(), principal.getName());
        return sellerDto;
    }


//==============================get all seller details================================


    @Secured("ROLE_ADMIN")
    public List<SellerDto> getAllSellers(Principal principal) {

        log.info("inside getAllSellers method");
        List<SellerDto> sellerDtoList = new ArrayList<>();
        Iterable<Seller> sellerIterable = sellerRepository.findAll();
        sellerIterable.forEach(seller -> sellerDtoList.add(new SellerDto(seller.getCompanyContact(), seller.getCompanyName(),
                seller.getLastName(), seller.getGst(), seller.getFirstName(), seller.getEmail(), seller.getId(),
                seller.getAddress().getAddressLine(), seller.getAddress().getCity(),
                seller.getAddress().getCountry(), seller.getAddress().getLable(),
                seller.getAddress().getZipcode(), seller.getAddress().getState(), seller.isActive())));

        auditService.readAllObjects("Seller", principal.getName());

        return sellerDtoList;
    }


}
