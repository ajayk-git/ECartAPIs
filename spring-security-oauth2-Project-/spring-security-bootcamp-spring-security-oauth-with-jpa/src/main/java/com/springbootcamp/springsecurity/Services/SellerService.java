package com.springbootcamp.springsecurity.Services;

import com.springbootcamp.springsecurity.CO.CustomerProfileUpdateCo;
import com.springbootcamp.springsecurity.CO.PasswordUpdateCO;
import com.springbootcamp.springsecurity.CO.SellerCO;
import com.springbootcamp.springsecurity.CO.SellerProfileUpdateCO;
import com.springbootcamp.springsecurity.ConfirmationToken;
import com.springbootcamp.springsecurity.DTOs.SellerDto;
import com.springbootcamp.springsecurity.Entities.Address;
import com.springbootcamp.springsecurity.Entities.Users.Customer;
import com.springbootcamp.springsecurity.Entities.Users.Seller;
import com.springbootcamp.springsecurity.Entities.Users.User;
import com.springbootcamp.springsecurity.Exceptions.AccountAreadyExistException;
import com.springbootcamp.springsecurity.Exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.Exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.Repositories.ConfirmationTokenRepository;
import com.springbootcamp.springsecurity.Repositories.SellerRepository;
import com.springbootcamp.springsecurity.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SellerService {
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    EmailService emailService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();




    private boolean isEmailExists(String Email) {
        return sellerRepository.findByEmail(Email) != null;
    }


//====================================get details of   a given seller===========================================

    public SellerDto getSellerByid(long id) {
        SellerDto sellerDto = new SellerDto();
        Seller seller = sellerRepository.findById(id).get();
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
        return sellerDto;
    }


//==============================get all seller details================================


    public List<SellerDto> getAllSellers() {
        List<SellerDto> sellerDtoList = new ArrayList<>();
        Iterable<Seller> sellerIterable = sellerRepository.findAll();
        sellerIterable.forEach(seller -> sellerDtoList.add(new SellerDto(seller.getCompanyContact(),seller.getCompanyName(),
                seller.getLastName(),seller.getGst(),seller.getFirstName(),seller.getEmail(),seller.getId(),
                seller.getAddress().getAddressLine(),seller.getAddress().getCity(),
                seller.getAddress().getCountry(),seller.getAddress().getLable(),
                seller.getAddress().getZipcode(),seller.getAddress().getState())));
        return sellerDtoList;
    }

    //================================================Sellers registration==========================================================
    public Seller sellerRegistration(SellerCO sellerCO) throws AccountAreadyExistException {
        if (isEmailExists(sellerCO.getEmail()))
            throw new AccountAreadyExistException("Seller Account with " + sellerCO.getEmail() + "already Exist");

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

        emailService.sendEmail(seller.getEmail(), confirmationToken.getConfirmationToken());

        return seller;
    }


//=============================Confirmation of Sellers registration==================================

    public String sellerRegistrationConfirm(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
        if (confirmationToken == null) {
            return "Invalid token";
        }

        User user = confirmationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (ConfirmationToken.calculateExpiryDate().getTime()-calendar.getTime().getTime()<0) {
            return "token expired.";
        }
        user.setActive(false);
        userRepository.save(user);
        return "Congratulations......Your account have been created and registered as a Seller.Please wait for some time to get activated by admin.";
    }



//=======================================Activate Sellers account by id============================================================


    public ResponseEntity<String> activateSellerAccountById(Long id) {
        if(!userRepository.findById(id).isPresent())
            throw new AccountDoesNotExistException("The customer with given id is not registered.");

        User user=userRepository.findById(id).get();
        if(!user.isActive()){
            user.setActive(true);
            user.setEnabled(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNotExpired(true);
            userRepository.save(user);
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setText("Congratulations,Your account has been activated by admin.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("imcoolajaykumar2010@gmail.com");
            mailMessage.setSubject("Alert : Account Activated by admin.");
            javaMailSender.send(mailMessage);

            return new ResponseEntity<String>("Seller's Account associated email id : "+user.getEmail()+"is activated.",HttpStatus.CREATED);
        }
        else return new ResponseEntity<String>("Seller's Account associated with email id  "+user.getEmail()+"is already Activated",HttpStatus.BAD_REQUEST);


    }
//=====================================================forgot Password===============================================================

    public ResponseEntity<String> forgotPasswordSendTokenToMail(String email) {
        User userFromDatabase = userRepository.findByEmail(email);
        if (userFromDatabase != null) {
            ConfirmationToken confirmationToken = new ConfirmationToken(userFromDatabase);
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
            simpleMailMessage.setTo(userFromDatabase.getEmail().toString());
            simpleMailMessage.setSubject("Reset Your Account Password");
            simpleMailMessage.setText("To complete the password reset process, please click here: "
                    + "localhost:8080/seller/confirmReset?token=" + confirmationToken.getConfirmationToken());
            javaMailSender.send(simpleMailMessage);
            return  new ResponseEntity<String>("Link to reset your forgot password has been sent to registered mail id :"+email+".",HttpStatus.OK);
        } else  return new ResponseEntity<String>("Account does not exist with given mail id",HttpStatus.NOT_FOUND);
    }







    public ResponseEntity<String> deactivateSellerAccountById(Long id) {
        if(!userRepository.findById(id).isPresent()){
            throw  new AccountDoesNotExistException("Seller with mentioned id is registered.So not able to deactivate.");
        }
        else {
            User user=userRepository.findById(id).get();
            if(user.isEnabled()){
                SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
                simpleMailMessage.setText("Oppps,Your account has been deactivated by admin registered with mail id :"+user.getEmail());
                simpleMailMessage.setTo(user.getEmail());
                simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
                simpleMailMessage.setSubject("Alert : Account Deactivated by admin");
                javaMailSender.send(simpleMailMessage);
                return new ResponseEntity<String>("User is deactivated", HttpStatus.OK);
            }
            else return  new ResponseEntity<String>("User is already deactivated",HttpStatus.BAD_REQUEST);
        }
    }

    public SellerDto viewSellerProfile(String email) {

        Seller seller=sellerRepository.findByEmail(email);

        SellerDto sellerDto=new SellerDto(seller.getCompanyContact(),seller.getCompanyName(),
                seller.getLastName(),seller.getGst(),seller.getFirstName(),seller.getEmail(),seller.getId(),
                seller.getAddress().getAddressLine(),seller.getAddress().getCity(),
                seller.getAddress().getCountry(),seller.getAddress().getLable(),
                seller.getAddress().getZipcode(),seller.getAddress().getState());

        return sellerDto;
    }

    public ResponseEntity<String> upadteSellerProfile(String email, SellerProfileUpdateCO sellerProfileUpdateCO) {

        Seller sellerToUpdate=sellerRepository.findByEmail(email);

        if (sellerProfileUpdateCO.getContact() != null)
            sellerToUpdate.setCompanyContact(sellerProfileUpdateCO.getContact());
        if (sellerProfileUpdateCO.getLastName() != null)
            sellerToUpdate.setLastName(sellerProfileUpdateCO.getLastName());
        if (sellerProfileUpdateCO.getFirstName() != null)
            sellerToUpdate.setFirstName(sellerProfileUpdateCO.getFirstName());

        sellerRepository.save(sellerToUpdate);

        return new ResponseEntity<String>("Profile Updated.",HttpStatus.OK);

    }


    public ResponseEntity<String> updateSellerPassword(PasswordUpdateCO passwordUpdateCO, String email) {

        User user=userRepository.findByEmail(email);
        user.setPassword(encoder.encode(passwordUpdateCO.getPassword()));
        emailService.sendMailPasswordUpdate(user.getEmail());
        userRepository.save(user);
        return new ResponseEntity<>("Password updated and alert message has been send to registered mail id.",HttpStatus.OK);
    }

    public ResponseEntity<String> updateForgotPassword(String token, PasswordUpdateCO passwordUpdateCO) {


        if (confirmationTokenRepository.findByConfirmationToken(token)==null)
            throw new ResourceNotFoundException("Invalid/Expired Token");

        ConfirmationToken confirmationToken=confirmationTokenRepository.findByConfirmationToken(token);

        Seller seller =(Seller) confirmationToken.getUser();
        seller.setPassword(encoder.encode(passwordUpdateCO.getPassword()));
        sellerRepository.save(seller);
        emailService.sendMailPasswordUpdate(seller.getEmail());
        return new ResponseEntity<String>("Password Succesfully updated.",HttpStatus.OK);


    }
}


































//
////====================================================Delete/Deactivate Sellers Account======================================
//
//    public String deleteCustomer(long id) throws AccountDoesNotExistException {
//        Seller seller = sellerRepository.findById(id).get();
//        if (seller.isActive()) {
//            seller.setActive(false);
//            seller.setEnabled(false);
//            return "Seller is Deactivated (Deleted) by ADMIN";
//        } else
//            return "Seller " + id + " does not exist";
//    }
