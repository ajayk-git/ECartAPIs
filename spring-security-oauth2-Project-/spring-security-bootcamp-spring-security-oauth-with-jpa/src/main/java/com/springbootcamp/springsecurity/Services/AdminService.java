package com.springbootcamp.springsecurity.Services;

import com.springbootcamp.springsecurity.CO.MetaDataFieldCO;
import com.springbootcamp.springsecurity.DTOs.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.DTOs.CustomerDto;
import com.springbootcamp.springsecurity.DTOs.SellerDto;
import com.springbootcamp.springsecurity.Entities.CategoryMetaDataField;
import com.springbootcamp.springsecurity.Entities.Users.Customer;
import com.springbootcamp.springsecurity.Entities.Users.Seller;
import com.springbootcamp.springsecurity.Entities.Users.User;
import com.springbootcamp.springsecurity.Exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.Exceptions.MetaDatafieldAlreadyExistException;
import com.springbootcamp.springsecurity.Exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.Repositories.CategoryMetaDataFieldRepository;
import com.springbootcamp.springsecurity.Repositories.CustomerRepository;
import com.springbootcamp.springsecurity.Repositories.SellerRepository;
import com.springbootcamp.springsecurity.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
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
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;



    public ResponseEntity<String> deactivateAccountById(Long id) {
        if(!userRepository.findById(id).isPresent()){
            throw  new AccountDoesNotExistException("User with mentioned id is registered.So not able to deactivate.");
        }
        else {
            User user=userRepository.findById(id).get();
            if(user.isEnabled()){
                user.setEnabled(false);
                user.setActive(false);
                SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
                simpleMailMessage.setText("Oppps,Your account has been deactivated by admin registered with mail id :"+user.getEmail());
                simpleMailMessage.setTo(user.getEmail());
                simpleMailMessage.setFrom("imcoolajaykumar2010@gmail.com");
                simpleMailMessage.setSubject("Alert : Account Deactivated by admin");
                javaMailSender.send(simpleMailMessage);
                userRepository.save(user);
                return new ResponseEntity<String>("User is deactivated", HttpStatus.OK);
            }
            else return  new ResponseEntity<String>("User is already deactivated",HttpStatus.BAD_REQUEST);
        }
    }



    public ResponseEntity<String> activateAccountById(Long id) {
        if(!userRepository.findById(id).isPresent())
            throw new AccountDoesNotExistException(" User with given id is not registered.");

        User user=userRepository.findById(id).get();
        if(!user.isEnabled()){
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

            return new ResponseEntity<String>("User's Account associated email id : "+user.getEmail()+"is activated.",HttpStatus.CREATED);
        }
        else return new ResponseEntity<String>("User's Account associated with email id  "+user.getEmail()+"is already Activated",HttpStatus.BAD_REQUEST);

    }


    //=================================to get a customer by id from repository===========================

    public CustomerDto getCustomerById(long id) throws AccountDoesNotExistException  {
        CustomerDto customerDTO = new CustomerDto();
        Customer customer;


        if(!customerRepository.findById(id).isPresent())
            throw  new AccountDoesNotExistException("Customer does not exist having Customer Id : "+id);

        customer = customerRepository.findById(id).get();

        customerDTO.setId(customer.getId());
        customerDTO.setContact(customer.getContact());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setLastName(customer.getLastName());
        return customerDTO;
    }


    //==============================to get all customers from repository==============================

    public List<CustomerDto> getAllCustomers() {
        List<CustomerDto> customerDtoList=new ArrayList<>();
        Iterable<Customer> customerIterable=customerRepository.findAll();
        customerIterable.forEach(customer ->customerDtoList.add(new CustomerDto(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getContact(),
                customer.getEmail(),customer.isActive())) );
        return customerDtoList;
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
                seller.getAddress().getZipcode(),seller.getAddress().getState(),seller.isActive())));
        return sellerDtoList;
    }


    public ResponseEntity<String> addMetaDataField(String fieldName) {
        if(!(categoryMetaDataFieldRepository.findByFieldName(fieldName)==null))
            throw  new MetaDatafieldAlreadyExistException("MetaData field "+fieldName+" already exist.");

        CategoryMetaDataField metaDataField=new CategoryMetaDataField(fieldName);
        categoryMetaDataFieldRepository.save(metaDataField);
        return new ResponseEntity<String>("MetaData field "+fieldName+" is added.",HttpStatus.CREATED);
    }

    public List<CategoryMetaDataFieldDTO> getAllMetaDataFieldList() {
        if(categoryMetaDataFieldRepository.findAll()==null)
            throw new ResourceNotFoundException("No Category MetaData fields exist in database.");
        else{
            Iterable<CategoryMetaDataField> categoryMetaDataFieldIterable=categoryMetaDataFieldRepository.findAll();
            List<CategoryMetaDataFieldDTO> categoryMetaDataFieldDTOList =new ArrayList<>();
            categoryMetaDataFieldIterable.forEach(categoryMetaDataField -> categoryMetaDataFieldDTOList.
                    add(new CategoryMetaDataFieldDTO(categoryMetaDataField.getId(),categoryMetaDataField.getFieldName())));

            return categoryMetaDataFieldDTOList;
        }
    }
}
