package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.AddressCO;
import com.springbootcamp.springsecurity.co.CustomerProfileUpdateCo;
import com.springbootcamp.springsecurity.co.PasswordUpdateCO;
import com.springbootcamp.springsecurity.ConfirmationToken;
import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.entities.Address;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.ConfirmationTokenRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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
    ConfirmationToken confirmationToken;
    @Autowired
    JavaMailSender javaMailSender;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    private boolean isEmailExists(String Email) {
        return userRepository.findByEmail(Email) != null;
    }


    //=============================update a customer details ================================

    @Secured("ROLE_USER")
    public ResponseEntity<String> updateCustomerProfile(String email, CustomerProfileUpdateCo customerProfileUpdateCo) {
        Customer customerToUpdate = customerRepository.findByEmail(email);
        if (customerProfileUpdateCo.getContact() != null)
            customerToUpdate.setContact(customerProfileUpdateCo.getContact());
        if (customerProfileUpdateCo.getFirstName() != null)
            customerToUpdate.setFirstName(customerProfileUpdateCo.getFirstName());
        if (customerProfileUpdateCo.getLastName() != null)
            customerToUpdate.setLastName(customerProfileUpdateCo.getLastName());

        customerRepository.save(customerToUpdate);
        return new ResponseEntity<String>("Profile has been updated.", HttpStatus.OK);


    }

    @Secured("ROLE_USER")
    public ResponseEntity<String> updateCustomerPassword(PasswordUpdateCO passwordUpdateCO, String email) {
        User user = userRepository.findByEmail(email);
        user.setPassword(encoder.encode(passwordUpdateCO.getPassword()));
        System.out.println("============================================");
        System.out.println(user.getEmail());
        emailService.sendMailPasswordUpdate(user.getEmail());
        userRepository.save(user);
        return new ResponseEntity<>("Password updated and alert message has been send to registered mail id.", HttpStatus.OK);

    }


    @Secured("ROLE_USER")
    public CustomerDto viewCustomerProfile(String email) {
        Customer customer = customerRepository.findByEmail(email);
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getEmail(), customer.getFirstName(), customer.getLastName(), customer.getContact(), customer.isActive());
        return customerDto;
    }




    @Secured("ROLE_USER")
    public List<AddressDto> getAddressListCustomer(String email) {
        boolean emailFlag=isEmailExists(email);
        if(emailFlag==false){
            throw new AccountDoesNotExistException("User's  does not exist");
        }
        List<AddressDto> addressList=new ArrayList<>();
        Customer customer=customerRepository.findByEmail(email);

        customer.getAddressList().forEach(address ->addressList.add(new AddressDto(address.getId(),
                address.getAddressLine(),address.getCity(),address.getState(),
                address.getZipcode(),address.getLable(),address.getCountry())));

        return addressList;
    }

    @Secured("ROLE_USER")
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
        return new ResponseEntity<String>("Address not found by mentioned address id ",HttpStatus.NOT_FOUND);

    }

    @Secured("ROLE_USER")
    public ResponseEntity<String> addCustomerAddress(AddressCO addressCO, String email) {

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


    @Secured("ROLE_USER")
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


}



































//
//    public ResponseEntity<String> updateForgotPassword(String token, PasswordUpdateCO passwordUpdateCO) {
//        if (confirmationTokenRepository.findByConfirmationToken(token)==null)
//            throw new ResourceNotFoundException("Invalid/Expired Token");
//
//        ConfirmationToken confirmationToken=confirmationTokenRepository.findByConfirmationToken(token);
//
//        Customer customer=(Customer) confirmationToken.getUser();
//        customer.setPassword(encoder.encode(passwordUpdateCO.getPassword()));
//        customerRepository.save(customer);
//        emailService.sendMailPasswordUpdate(customer.getEmail());
//        return new ResponseEntity<String>("Password Successfully updated.",HttpStatus.OK);
//
//
//    }

//
//    public String deleteCustomer(Long id) throws AccountDoesNotExistException
//    {
//        Customer customer=customerRepository.findById((long) id).get();
//        if(customer.isActive()){
//
//            customer.setActive(false);
//            return "User  Switched to Inactive(Deleted) mode ";
//        }
//        else return "User not found";
//    }

//
//    //==============================to login a customer by email id================================
//
//    public void getLoginCustomer(CustomerCO customerCO) throws AccountDoesNotExistException{
//        User userFromDatabase =userRepository.findByEmail(customerCO.getEmail());
//        if(userFromDatabase!=null){
//
//            if(encoder.matches(userFromDatabase.getPassword(),customerCO.getPassword())){
//                System.out.println("Login Successful");
//            }
//            else
//                System.out.println("invalid password");
//        }
//        else System.out.println("User with"+customerCO.getEmail()+"does not exist .");
//    }


   // public ResponseEntity<String> deactivateCustomerAccountById(Long id) {
//        if(!userRepository.findById(id).isPresent()){
//            throw  new AccountDoesNotExistException("Customer with mentioned id is registered.So not able to deactivate.");
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
//                javaMailSender.send(simpleMailMessage);
//                userRepository.save(user);
//                return new ResponseEntity<String>("User is deactivated",HttpStatus.OK);
//            }
//            else return  new ResponseEntity<String>("User is already deactivated",HttpStatus.BAD_REQUEST);
//        }
//    }




//
//
//    public ResponseEntity<String> activateCustomerAccountById(Long id) {
//        if(!userRepository.findById(id).isPresent())
//            throw new AccountDoesNotExistException("The customer with given id is not registered.");
//
//        User user=userRepository.findById(id).get();
//        if(!user.isEnabled()){
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
//            return new ResponseEntity<String>("Customer's Account associated email id : "+user.getEmail()+"is activated.",HttpStatus.CREATED);
//        }
//        else return new ResponseEntity<String>("Customer's Account associated with email id  "+user.getEmail()+"is already Activated",HttpStatus.BAD_REQUEST);
//
//
//    }


//==============================to reset the password, when a customer forgot his/her password================================
//
//
//    public  void forgotPasswordSendTokenToMail(String email){
//        User userFromDatabase=userRepository.findByEmail(email);
//        if(userFromDatabase!=null){
//            ConfirmationToken confirmationToken=new ConfirmationToken(userFromDatabase);
//            confirmationTokenRepository.save(confirmationToken);
//
//            SimpleMailMessage simpleMailMessage=   new SimpleMailMessage();
//            simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
//            simpleMailMessage.setTo(userFromDatabase.getEmail().toString());
//            simpleMailMessage.setSubject("Reset Your Account Password");
//            simpleMailMessage.setText("To complete the password reset process, please click here: "
//                    + "http://localhost:8080/customer/confirmReset?token="+confirmationToken.getConfirmationToken());
//            System.out.println("password reset link has been sent to mail : "+userFromDatabase.getEmail());
//            javaMailSender.send(simpleMailMessage);
//        }
//        else
//            System.out.println("this email does not exist");
//     }




//    //=================================to get a customer by id from repository===========================
//
//    public CustomerDto getCustomerById(long id) throws AccountDoesNotExistException  {
//           CustomerDto customerDTO = new CustomerDto();
//           Customer customer;
//
//
//        if(!customerRepository.findById(id).isPresent())
//            throw  new AccountDoesNotExistException("Customer does not exist having Customer Id : "+id);
//
//        customer = customerRepository.findById(id).get();
//
//        customerDTO.setId(customer.getId());
//           customerDTO.setContact(customer.getContact());
//           customerDTO.setFirstName(customer.getFirstName());
//           customerDTO.setEmail(customer.getEmail());
//           customerDTO.setLastName(customer.getLastName());
//           return customerDTO;
//    }
//
//
//    //==============================to get all customers from repository==============================
//
//    public List<CustomerDto> getAllCustomers() {
//           List<CustomerDto> customerDtoList=new ArrayList<>();
//           Iterable<Customer> customerIterable=customerRepository.findAll();
//           customerIterable.forEach(customer ->customerDtoList.add(new CustomerDto(customer.getId(),
//                  customer.getFirstName(),
//                  customer.getLastName(),
//                  customer.getContact(),
//                  customer.getEmail(),customer.isActive())) );
//
//           return customerDtoList;
//    }





//
//    public String registrationConfirmCustomer(String token) {
//
//        ConfirmationToken confirmationToken=confirmationTokenRepository.findByConfirmationToken(token);
//
//        if(confirmationToken==null){
//            return "Invalid Token is entered by customer";
//        }
//
//        User user=confirmationToken.getUser();
//
//        Calendar calendar=Calendar.getInstance();
//
//        if(ConfirmationToken.calculateExpiryDate().getTime()-calendar.getTime().getTime()<0){
//            return "Token Expired";
//
//        }
//
//        userRepository.save(user);
//        return "Congratulations......Your account have been created and registered as a Customer." +
//                "Kindly wait to get activated your account by Admin";
//
//
//    }


//