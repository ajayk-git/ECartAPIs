package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.CustomerCO;
import com.springbootcamp.springsecurity.co.PasswordUpdateCO;
import com.springbootcamp.springsecurity.co.SellerCO;
import com.springbootcamp.springsecurity.ConfirmationToken;
import com.springbootcamp.springsecurity.entities.Address;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.exceptions.AccountAreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.ConfirmationTokenRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class RegistrationService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    UserRepository userRepository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private boolean isEmailExists(String Email){
        return userRepository.findByEmail(Email)!=null;
    }

    public ResponseEntity registerCustomer(CustomerCO customerCO) throws AccountAreadyExistException {
        if (isEmailExists(customerCO.getEmail())) {
            throw new AccountAreadyExistException("The given email id  is already registered.Kindly register with another mail id,or login with registered mail id.");
        }

        Customer customer = new Customer();
        customer.setFirstName(customerCO.getFirstName());
        customer.setLastName(customerCO.getLastName());
        customer.setEmail(customerCO.getEmail());
        customer.setContact(customerCO.getContact());
        customer.setPassword(encoder.encode(customerCO.getPassword()));
        customerRepository.save(customer);

        ConfirmationToken confirmationToken = new ConfirmationToken(customer);
        confirmationTokenRepository.save(confirmationToken);
        emailService.sendEmailToCustomer(customer.getEmail(), confirmationToken.getConfirmationToken());
        return new ResponseEntity("Verification mail is send to registered mail id.", HttpStatus.OK);

    }



    public ResponseEntity registerSeller(SellerCO sellerCO) throws AccountAreadyExistException {
        if (isEmailExists(sellerCO.getEmail())) {
            throw new AccountAreadyExistException("The given email id  is already registered.Kindly register with another mail id,or login with registered mail id.");
        }

        Seller seller = new Seller();
        Address address=new Address();
        seller.setFirstName(sellerCO.getFirstName());
        seller.setLastName(sellerCO.getLastName());
        seller.setEmail(sellerCO.getEmail());
        seller.setCompanyName(sellerCO.getCompanyName());
        seller.setCompanyContact(sellerCO.getCompanyContact());
        seller.setGst(sellerCO.getGst());
        address.setAddressLine(sellerCO.getAddressLine());
        address.setLable(sellerCO.getLable());
        address.setZipcode(sellerCO.getZipcode());
        address.setCountry(sellerCO.getCountry());
        address.setState(sellerCO.getState());
        seller.setAddress(address);
        seller.setPassword(encoder.encode(sellerCO.getPassword()));
        sellerRepository.save(seller);

        ConfirmationToken confirmationToken = new ConfirmationToken(seller);
        confirmationTokenRepository.save(confirmationToken);
        emailService.sendEmailToSeller(seller.getEmail(), confirmationToken.getConfirmationToken());
        return new ResponseEntity("Verification mail is send to registered mail id.", HttpStatus.OK);


    }

    public ResponseEntity registrationConfirmCustomer(String token) {

        ConfirmationToken confirmationToken=confirmationTokenRepository.findByConfirmationToken(token);

        if(confirmationToken==null){
            return new ResponseEntity("Invalid Token is entered by customer",HttpStatus.BAD_REQUEST);
        }

        User user=confirmationToken.getUser();
        Calendar calendar=Calendar.getInstance();

        if(ConfirmationToken.calculateExpiryDate().getTime()-calendar.getTime().getTime()<0){
            return new ResponseEntity("Token Expired",HttpStatus.BAD_REQUEST);

        }
        user.setActive(false);
        userRepository.save(user);
        return new ResponseEntity("Congratulations......Your account have been created and registered as a Customer." +
                "Kindly wait to get activated your account by Admin",HttpStatus.CREATED);

    }


    public ResponseEntity sellerRegistrationConfirm(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
        if (confirmationToken == null) {
            return new ResponseEntity("Invalid Token is entered by customer",HttpStatus.BAD_REQUEST);
        }

        User user = confirmationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (ConfirmationToken.calculateExpiryDate().getTime()-calendar.getTime().getTime()<0) {
            return new ResponseEntity("Token Expired",HttpStatus.BAD_REQUEST);
        }
        user.setActive(false);
        userRepository.save(user);
        return new ResponseEntity("Congratulations......Your account have been created and registered as a seller." +
                "Kindly wait to get activated your account by Admin",HttpStatus.CREATED);    }




    public  ResponseEntity forgotPasswordSendTokenToMail(String email){
        User userFromDatabase=userRepository.findByEmail(email);
        if(userFromDatabase!=null){
            ConfirmationToken confirmationToken=new ConfirmationToken(userFromDatabase);
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage simpleMailMessage=   new SimpleMailMessage();
            simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
            simpleMailMessage.setTo(userFromDatabase.getEmail().toString());
            simpleMailMessage.setSubject("Reset Your Account Password");
            simpleMailMessage.setText("To complete the password reset process, please click here: "
                    + "localhost:8080/register/confirm-reset?token="+confirmationToken.getConfirmationToken());
         //   System.out.println("password reset link has been sent to mail : "+userFromDatabase.getEmail());
            javaMailSender.send(simpleMailMessage);
            return new ResponseEntity("password reset link has been sent to mail : "+userFromDatabase.getEmail(),HttpStatus.OK);
        }
        else return new ResponseEntity("this email does not exist",HttpStatus.NOT_FOUND);

    }


    public ResponseEntity updateForgotPassword(String token, PasswordUpdateCO passwordUpdateCO) {
        if (confirmationTokenRepository.findByConfirmationToken(token)==null)
            throw new ResourceNotFoundException("Invalid/ Token");

        ConfirmationToken confirmationToken=confirmationTokenRepository.findByConfirmationToken(token);

        Customer customer=(Customer) confirmationToken.getUser();
        customer.setPassword(encoder.encode(passwordUpdateCO.getPassword()));
        customerRepository.save(customer);
        emailService.sendMailPasswordUpdate(customer.getEmail());
        return new ResponseEntity("Password Successfully updated.",HttpStatus.OK);


    }




}
