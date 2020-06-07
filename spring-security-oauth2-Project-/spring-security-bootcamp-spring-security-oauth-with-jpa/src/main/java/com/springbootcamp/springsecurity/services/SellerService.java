package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.repositories.AuditHistoryRepository;
import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.ConfirmationToken;
import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.entities.users.Address;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.ConfirmationTokenRepository;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;

@Service
@Log4j2
public class SellerService {
    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuditHistoryRepository auditRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    EmailService emailService;

    @Autowired
    AuditLogsMongoDBService auditService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();




    private boolean isEmailExists(String Email) {
        return sellerRepository.findByEmail(Email) != null;
    }


//==============================================get seller Address==================================================================

    @Secured("ROLE_SELLER")
    public AddressDto getAddressSeller(String email,Principal principal) {

        log.info("Inside getAddressSeller method.");
        log.warn("email may give null pointer exception");

       boolean emailFlag=isEmailExists(email);
        if (emailFlag==false){
            throw new AccountDoesNotExistException("User's  does not exist");
        }
        Seller seller=sellerRepository.findByEmail(email);
        AddressDto addressDto=new AddressDto(seller.getAddress().getId(),
                seller.getAddress().getAddressLine(),
                seller.getAddress().getCity(),
                seller.getAddress().getState(),
                seller.getAddress().getZipcode(),
                seller.getAddress().getLable(),
                seller.getAddress().getCountry());

        auditService.readObject("Address",seller.getAddress().getId(),principal.getName());

        return addressDto;

    }



//=============================Confirmation of Sellers registration==================================

    @Secured("ROLE_SELLER")
    public String sellerRegistrationConfirm(String token) {
        log.info("Inside sellerRegistrationConfirm method.");

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
        if (confirmationToken == null) {
            return "Invalid token";
        }

        User user = confirmationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (ConfirmationToken.calculateExpiryDate().getTime()-calendar.getTime().getTime()<0) {
            return "token expired.";
        }
        user.setIsActive(false);
        userRepository.save(user);
        return "Congratulations......Your account have been created and registered as a Seller.Please wait for some time to get activated by admin.";
    }



//=====================================================forgot Password===============================================================

    @Secured("ROLE_SELLER")
    public ResponseEntity<String> forgotPasswordSendTokenToMail(String email) {

        log.info("Inside sellerRegistrationConfirm method.");

        User userFromDatabase = userRepository.findByEmail(email);
        if (userFromDatabase != null) {
            ConfirmationToken confirmationToken = new ConfirmationToken(userFromDatabase);
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
            simpleMailMessage.setTo(userFromDatabase.getEmail().toString());
            simpleMailMessage.setSubject("Reset Your Account Password");
            simpleMailMessage.setText("To complete the password reset process, please click here: "
                    + "localhost:8080/seller/confirmReset?token=" + confirmationToken.getToken());
            javaMailSender.send(simpleMailMessage);
            log.info("Seller forgot password mail sent.");

            return  new ResponseEntity<String>("Link to reset your forgot password has been sent to registered mail id : "+email+" .",HttpStatus.OK);
        } else  return new ResponseEntity<String>("Account does not exist with given mail id",HttpStatus.NOT_FOUND);
    }






//===================================================View profile by seller===================================================

    @Secured("ROLE_SELLER")
    public SellerDto viewSellerProfile(String email,Principal principal) {

        log.info("Inside viewSellerProfile method.");

        Seller seller=sellerRepository.findByEmail(email);

        SellerDto sellerDto=new SellerDto(seller.getCompanyContact(),seller.getCompanyName(),
                seller.getLastName(),seller.getGst(),seller.getFirstName(),seller.getEmail(),seller.getId(),
                seller.getAddress().getAddressLine(),seller.getAddress().getCity(),
                seller.getAddress().getCountry(),seller.getAddress().getLable(),
                seller.getAddress().getZipcode(),seller.getAddress().getState(),seller.getIsActive());

        auditService.readObject("Seller",seller.getId(),principal.getName());

        return sellerDto;
    }

    //===============================================Update Seller profile============================================================

    @Secured("ROLE_SELLER")
    public ResponseEntity<String> upadteSellerProfile(String email, SellerProfileUpdateCO sellerProfileUpdateCO,Principal principal) {

        log.info("Inside upadteSellerProfile method.");

        Seller sellerToUpdate=sellerRepository.findByEmail(email);

        if (sellerProfileUpdateCO.getContact() != null)
            sellerToUpdate.setCompanyContact(sellerProfileUpdateCO.getContact());
        if (sellerProfileUpdateCO.getLastName() != null)
            sellerToUpdate.setLastName(sellerProfileUpdateCO.getLastName());
        if (sellerProfileUpdateCO.getFirstName() != null)
            sellerToUpdate.setFirstName(sellerProfileUpdateCO.getFirstName());

        sellerRepository.save(sellerToUpdate);

        log.info("Seller profile has been updated.");


        auditService.updateObject("Seller",sellerToUpdate.getId(),principal.getName());

        return new ResponseEntity<String>("Profile Updated.",HttpStatus.OK);

    }

    //===============================================Update Seller Password============================================================


    @Secured("ROLE_SELLER")
    public ResponseEntity<String> updateSellerPassword(PasswordUpdateCO passwordUpdateCO, String email,Principal principal) {

        log.info("Inside updateSellerPassword method.");


        User user=userRepository.findByEmail(email);
        user.setPassword(encoder.encode(passwordUpdateCO.getPassword()));
        emailService.sendMailPasswordUpdate(user.getEmail());
        userRepository.save(user);

        log.info("Seller password has been updated.");

        auditService.updateObject("User",user.getId(),principal.getName());

        return new ResponseEntity<>("Password updated and alert message has been send to registered mail id.",HttpStatus.OK);
    }

    //===============================================Update Seller Password  when forgot============================================================

    @Secured("ROLE_SELLER")
    public ResponseEntity<String> updateForgotPassword(String token, PasswordUpdateCO passwordUpdateCO,Principal principal) {

        log.info("Inside updateForgotPassword method.");

        if (confirmationTokenRepository.findByConfirmationToken(token)==null)
            throw new ResourceNotFoundException("Invalid/Expired Token");

        ConfirmationToken confirmationToken=confirmationTokenRepository.findByConfirmationToken(token);

        Seller seller =(Seller) confirmationToken.getUser();
        seller.setPassword(encoder.encode(passwordUpdateCO.getPassword()));
        sellerRepository.save(seller);
        emailService.sendMailPasswordUpdate(seller.getEmail());

        log.info("Seller password has been updated.");

        auditService.updateObject("User",seller.getId(),principal.getName());

        return new ResponseEntity<String>("Password Successfully updated.",HttpStatus.OK);


    }

    //===============================================Update Seller Address ============================================================

    @Secured("ROLE_SELLER")
    public ResponseEntity<String> updateSellerAddress(AddressCO addressCO, Long id, String email,Principal principal) {

        log.info("Inside updateSellerAddress method.");

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

            auditService.updateObject("Address",address.getId(),principal.getName());

            log.info("Seller Address has been updated.");

            return new ResponseEntity<>("Seller Address has been updated successfully.",HttpStatus.OK);

        }
        throw new ResourceNotFoundException("No address found with id : " + id + ", associated with your account");

    }


}














////=======================================Activate Sellers account by id============================================================
//
//
//    @Secured("ROLE_ADMIN")
//    public ResponseEntity<String> activateSellerAccountById(Long id) {
//        if(!userRepository.findById(id).isPresent())
//            throw new AccountDoesNotExistException("The customer with given id is not registered.");
//
//        User user=userRepository.findById(id).get();
//        if(!user.isActive()){
//            user.setActive(true);
//            user.setEnabled(true);
//            user.setAccountNonLocked(true);
//            user.setCredentialsNonExpired(true);
//            user.setAccountNotExpired(true);
//            userRepository.save(user);
//            SimpleMailMessage mailMessage=new SimpleMailMessage();
//            mailMessage.setText("Congratulations,Your account has been activated by admin.");
//            mailMessage.setTo(user.getEmail());
//            mailMessage.setFrom("imcoolajaykumar2010@gmail.com");
//            mailMessage.setSubject("Alert : Account Activated by admin.");
//            javaMailSender.send(mailMessage);
//
//            return new ResponseEntity<String>("Seller's Account associated email id : "+user.getEmail()+"is activated.",HttpStatus.CREATED);
//        }
//        else return new ResponseEntity<String>("Seller's Account associated with email id  "+user.getEmail()+"is already Activated",HttpStatus.BAD_REQUEST);
//
//
//    }
////====================================get details of   a given seller===========================================
//
//    public SellerDto getSellerByid(long id) {
//        SellerDto sellerDto = new SellerDto();
//        Seller seller = sellerRepository.findById(id).get();
//        sellerDto.setId(seller.getId());
//        sellerDto.setFirstName(seller.getFirstName());
//        sellerDto.setLastName(seller.getLastName());
//        sellerDto.setCompanyName(seller.getCompanyName());
//        sellerDto.setCompanyContact(seller.getCompanyContact());
//        sellerDto.setGst(seller.getGst());
//        sellerDto.setEmail(seller.getEmail());
//        sellerDto.setAddressLine(seller.getAddress().getAddressLine());
//        sellerDto.setCity(seller.getAddress().getCity());
//        sellerDto.setState(seller.getAddress().getState());
//        sellerDto.setLable(seller.getAddress().getLable());
//        sellerDto.setCountry(seller.getAddress().getCountry());
//        sellerDto.setZipcode(seller.getAddress().getZipcode());
//        return sellerDto;
//    }
//
//
////==============================get all seller details================================
//
//
//    public List<SellerDto> getAllSellers() {
//        List<SellerDto> sellerDtoList = new ArrayList<>();
//        Iterable<Seller> sellerIterable = sellerRepository.findAll();
//        sellerIterable.forEach(seller -> sellerDtoList.add(new SellerDto(seller.getCompanyContact(),seller.getCompanyName(),
//                seller.getLastName(),seller.getGst(),seller.getFirstName(),seller.getEmail(),seller.getId(),
//                seller.getAddress().getAddressLine(),seller.getAddress().getCity(),
//                seller.getAddress().getCountry(),seller.getAddress().getLable(),
//                seller.getAddress().getZipcode(),seller.getAddress().getState(),seller.isActive())));
//        return sellerDtoList;
//    }



//
//    @Secured("ROLE_ADMIN")
//    public ResponseEntity<String> deactivateSellerAccountById(Long id) {
//        if(!userRepository.findById(id).isPresent()){
//            throw  new AccountDoesNotExistException("Seller with mentioned id is registered.So not able to deactivate.");
//        }
//        else {
//            User user=userRepository.findById(id).get();
//            if(user.isEnabled()){
//                user.setEnabled(false);
//                user.setActive(false);
//                SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
//                simpleMailMessage.setText("Oppps,Your account has been deactivated by admin registered with mail id :"+user.getEmail());
//                simpleMailMessage.setTo(user.getEmail());
//                simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
//                simpleMailMessage.setSubject("Alert : Account Deactivated by admin");
//                userRepository.save(user);
//                javaMailSender.send(simpleMailMessage);
//                return new ResponseEntity<String>("User is deactivated", HttpStatus.OK);
//            }
//            else return  new ResponseEntity<String>("User is already deactivated",HttpStatus.BAD_REQUEST);
//        }
//    }






















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
