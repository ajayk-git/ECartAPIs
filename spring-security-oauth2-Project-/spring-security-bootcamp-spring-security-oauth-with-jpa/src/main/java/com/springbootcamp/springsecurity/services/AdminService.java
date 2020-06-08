
package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    AuditLogsMongoDBService auditRepository;

    @Autowired
    ProductRepository productRepository;


    //Scheduled task will be executed  at 11:00 PM everyday when  application server is up.
    @Scheduled(cron = "0 0 23 * * ?")
    public void scheduleTaskSendEmailAdmin() {
        //   List<User> usersToActivate = userRepository.findByIsNotActive();
        List<Product> productListToActivate = productRepository.findIsNotActiveProduct();
        List productIdList = new ArrayList();
        productListToActivate.forEach(product -> productIdList.add(product.getId()));

        emailService.sendScheduleMailToActivateProduct(productIdList);

    }

    //===============================================To activate a user ======================================================================

    public ResponseEntity<String> deactivateAccountById(Long id, Principal principal) {

        log.info("inside deactivateAccountById method");

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                if (user.getIsActive()) {
                    user.setIsEnabled(false);
                    user.setIsActive(false);
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
        throw new AccountDoesNotExistException("User with mentioned id is registered.So not able to deactivate.");

    }

    //===============================================To deactivate a user ======================================================================

    public ResponseEntity<String> activateAccountById(Long id, Principal principal) {

        log.info("inside activateAccountById method");

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (!user.isEnabled()) {
                if (!user.getIsActive()) {

                    user.setIsActive(true);
                    user.setIsEnabled(true);
                    user.setIsAccountNonLocked(true);
                    user.setIsCredentialsNonExpired(true);
                    user.setIsAccountNotExpired(true);
                    user.setFalseAttemptCount(0);
                    userRepository.save(user);

                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setText("Congratulations,Your account has been activated by admin.");
                    mailMessage.setTo(user.getEmail());
                    mailMessage.setFrom("imcoolajaykumar2010@gmail.com");
                    mailMessage.setSubject("Alert : Account Activated by admin.");
                    javaMailSender.send(mailMessage);
                    log.info("Account activated by Admin.");
                    auditService.activateObject("User", user.getId(), principal.getName());
                    return new ResponseEntity<String>("User's Account associated email id : " + user.getEmail() + "is activated.", HttpStatus.CREATED);
                }
                return new ResponseEntity<String>("User's Account associated with email id  " + user.getEmail() + "is already Activated", HttpStatus.BAD_REQUEST);
            }
        }
        log.warn("AccountDoesNotExistException may occur");
        throw new AccountDoesNotExistException(" User with given id is not registered.");

    }


    //===============================================To get customer details by Admin==============================================

    public CustomerDto getCustomerById(long id, Principal principal) throws AccountDoesNotExistException {

        log.info("inside getCustomerById method");

        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {

            CustomerDto customerDTO = new CustomerDto();
            Customer customer = optionalCustomer.get();
            customerDTO.setId(customer.getId());
            customerDTO.setContact(customer.getContact());
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setLastName(customer.getLastName());
            auditService.readObject("User", customer.getId(), principal.getName());
            return customerDTO;
        } else
            throw new AccountDoesNotExistException("Customer does not exist having Customer Id : " + id);
    }


    //==============================to get all customers from repository==============================

    public List<CustomerDto> getAllCustomers(Principal principal) {
        log.info("inside getAllCustomers method");
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Iterable<Customer> customerIterable = customerRepository.findAll();
        customerIterable.forEach(customer -> customerDtoList.add(new CustomerDto(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getContact(),
                customer.getEmail(), customer.getIsActive())));
        auditService.readAllObjects("User", principal.getName());
        return customerDtoList;
    }


//====================================get details of   a given seller===========================================

    public SellerDto getSellerByid(long sellerId, Principal principal) {

        log.info("inside getSellerById method");
        log.warn("ResourceNotFoundException may occur.");

        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
        if (optionalSeller.isPresent()) {

            SellerDto sellerDto = new SellerDto();
            Seller seller = optionalSeller.get();

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
        } else
            throw new ResourceNotFoundException("Seller with mentioned SellerId is not exist.");
    }


//==============================get all seller details================================

    public List<SellerDto> getAllSellers(Principal principal) {

        log.info("inside getAllSellers method");
        List<SellerDto> sellerDtoList = new ArrayList<>();
        Iterable<Seller> sellerIterable = sellerRepository.findAll();
        sellerIterable.forEach(seller -> sellerDtoList.add(new SellerDto(seller.getCompanyContact(), seller.getCompanyName(),
                seller.getLastName(), seller.getGst(), seller.getFirstName(), seller.getEmail(), seller.getId(),
                seller.getAddress().getAddressLine(), seller.getAddress().getCity(),
                seller.getAddress().getCountry(), seller.getAddress().getLable(),
                seller.getAddress().getZipcode(), seller.getAddress().getState(), seller.getIsActive())));

        auditService.readAllObjects("Seller", principal.getName());

        return sellerDtoList;
    }


}
