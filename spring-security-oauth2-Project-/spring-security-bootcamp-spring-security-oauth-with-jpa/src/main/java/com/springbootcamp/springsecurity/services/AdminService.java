package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.CategoryCO;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.entities.CategoryMetaDataField;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.MetaDatafieldAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Iterator;
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
    @Autowired
    CategoryRepository categoryRepository;


    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deactivateAccountById(Long id) {
        if (!userRepository.findById(id).isPresent()) {
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
                return new ResponseEntity<String>("User is deactivated", HttpStatus.OK);
            } else return new ResponseEntity<String>("User is already deactivated", HttpStatus.BAD_REQUEST);
        }
    }


    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> activateAccountById(Long id) {
        if (!userRepository.findById(id).isPresent())
            throw new AccountDoesNotExistException(" User with given id is not registered.");

        User user = userRepository.findById(id).get();
        if (!user.isEnabled()) {
            user.setActive(true);
            user.setEnabled(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNotExpired(true);
            userRepository.save(user);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setText("Congratulations,Your account has been activated by admin.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("imcoolajaykumar2010@gmail.com");
            mailMessage.setSubject("Alert : Account Activated by admin.");
            javaMailSender.send(mailMessage);

            return new ResponseEntity<String>("User's Account associated email id : " + user.getEmail() + "is activated.", HttpStatus.CREATED);
        } else
            return new ResponseEntity<String>("User's Account associated with email id  " + user.getEmail() + "is already Activated", HttpStatus.BAD_REQUEST);

    }


    //=================================to get a customer by id from repository===========================

    @Secured("ROLE_ADMIN")
    public CustomerDto getCustomerById(long id) throws AccountDoesNotExistException {
        CustomerDto customerDTO = new CustomerDto();
        Customer customer;


        if (!customerRepository.findById(id).isPresent())
            throw new AccountDoesNotExistException("Customer does not exist having Customer Id : " + id);

        customer = customerRepository.findById(id).get();

        customerDTO.setId(customer.getId());
        customerDTO.setContact(customer.getContact());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setLastName(customer.getLastName());
        return customerDTO;
    }


    //==============================to get all customers from repository==============================

    @Secured("ROLE_ADMIN")
    public List<CustomerDto> getAllCustomers() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Iterable<Customer> customerIterable = customerRepository.findAll();
        customerIterable.forEach(customer -> customerDtoList.add(new CustomerDto(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getContact(),
                customer.getEmail(), customer.isActive())));
        return customerDtoList;
    }


//====================================get details of   a given seller===========================================

    @Secured("ROLE_ADMIN")
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


    @Secured("ROLE_ADMIN")
    public List<SellerDto> getAllSellers() {
        List<SellerDto> sellerDtoList = new ArrayList<>();
        Iterable<Seller> sellerIterable = sellerRepository.findAll();
        sellerIterable.forEach(seller -> sellerDtoList.add(new SellerDto(seller.getCompanyContact(), seller.getCompanyName(),
                seller.getLastName(), seller.getGst(), seller.getFirstName(), seller.getEmail(), seller.getId(),
                seller.getAddress().getAddressLine(), seller.getAddress().getCity(),
                seller.getAddress().getCountry(), seller.getAddress().getLable(),
                seller.getAddress().getZipcode(), seller.getAddress().getState(), seller.isActive())));
        return sellerDtoList;
    }


    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addMetaDataField(String fieldName) {
        if (!(categoryMetaDataFieldRepository.findByFieldName(fieldName) == null))
            throw new MetaDatafieldAlreadyExistException("MetaData field " + fieldName + " already exist.");

        CategoryMetaDataField metaDataField = new CategoryMetaDataField(fieldName);
        categoryMetaDataFieldRepository.save(metaDataField);
        return new ResponseEntity<String>("MetaData field " + fieldName + " is added.", HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    public List<CategoryMetaDataFieldDTO> getAllMetaDataFieldList() {
        if (categoryMetaDataFieldRepository.findAll() == null)
            throw new ResourceNotFoundException("No Category MetaData fields exist in database.");
        else {
            Iterable<CategoryMetaDataField> categoryMetaDataFieldIterable = categoryMetaDataFieldRepository.findAll();
            List<CategoryMetaDataFieldDTO> categoryMetaDataFieldDTOList = new ArrayList<>();
            categoryMetaDataFieldIterable.forEach(categoryMetaDataField -> categoryMetaDataFieldDTOList.
                    add(new CategoryMetaDataFieldDTO(categoryMetaDataField.getId(), categoryMetaDataField.getFieldName())));

            return categoryMetaDataFieldDTOList;
        }
    }

    @Secured("ROLE_ADMIN")
    public ResponseEntity addNewCategory(CategoryCO categoryCO) {
        if (categoryCO.getParentId() == null) {
            if(categoryRepository.findByName(categoryCO.getCategoryName())==null) {

                Category category = new Category();
                category.setName(categoryCO.getCategoryName());

                category.setId(categoryCO.getParentId());
                categoryRepository.save(category);
                return new ResponseEntity("New category " + categoryCO.getCategoryName() + " is Added as a Root category because it has null Id. ", HttpStatus.CREATED);
            }
            else
                throw new ResourceAlreadyExistException("Category with " + categoryCO.getCategoryName()+ " already exist.");

        }

        if (categoryCO.getParentId() != null) {
            if (!categoryRepository.findById(categoryCO.getParentId()).isPresent()) {
                throw new ResourceNotFoundException("Category not exist with given Category Parent Id.");
            }
        }
        String categoryName = categoryCO.getCategoryName();

        if (categoryRepository.findByName(categoryName) != null) {
            throw new ResourceAlreadyExistException("Category with " + categoryName + " already exist.");
        }
        Category subCategory = new Category();
        Category parentCategory = categoryRepository.findById(categoryCO.getParentId()).get();


        subCategory.setName(categoryCO.getCategoryName());
        subCategory.setParentCategory(parentCategory);
        parentCategory.addCategory(subCategory);
        categoryRepository.save(subCategory);

        return new ResponseEntity("New category " + categoryName + " is added having Parent id is : " + categoryCO.getParentId() + ".", HttpStatus.CREATED);


    }


    @Secured("ROLE_ADMIN")
    public List<CategoryDTO> getAllCategories() {

        List<Category> categoryList = categoryRepository.findAll();

        if (categoryList == null)
            throw new ResourceNotFoundException("Category List is not available.");

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        Iterator<Category> iterator = categoryList.iterator();

        while (iterator.hasNext()) {

            Category currentCategory = iterator.next();

            CategoryDTO categoryDTO = new CategoryDTO();

            categoryDTO.setCategoryName(currentCategory.getName());

           categoryDTO.setCategoryId(currentCategory.getId());

            if (currentCategory.getParentCategory() ==null) {
                categoryDTO.setParentId(null);
            }
            else
                categoryDTO.setParentId(currentCategory.getParentCategory().getId());

            categoryDTOList.add(categoryDTO);
        }

        return categoryDTOList;
    }
}
