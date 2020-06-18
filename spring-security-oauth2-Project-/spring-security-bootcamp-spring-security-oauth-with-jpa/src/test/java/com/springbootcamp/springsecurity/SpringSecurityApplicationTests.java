package com.springbootcamp.springsecurity;
//
//import com.springbootcamp.springsecurity.co.CustomerCO;
//import com.springbootcamp.springsecurity.entities.ConfirmationToken;
//import com.springbootcamp.springsecurity.entities.users.Customer;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.when;
//
//import com.springbootcamp.springsecurity.entities.users.Customer;
//import com.springbootcamp.springsecurity.entities.users.User;
//import com.springbootcamp.springsecurity.repositories.*;
//import com.springbootcamp.springsecurity.services.AuditLogsMongoDBService;
//import com.springbootcamp.springsecurity.services.CustomerService;
//import com.springbootcamp.springsecurity.services.EmailService;
//import com.springbootcamp.springsecurity.services.RegistrationService;
//import org.junit.Test;

import com.springbootcamp.springsecurity.co.AddressCO;
import com.springbootcamp.springsecurity.co.CustomerCO;
import com.springbootcamp.springsecurity.dtos.*;
import com.springbootcamp.springsecurity.entities.ConfirmationToken;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Address;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.*;
import com.springbootcamp.springsecurity.services.*;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SpringSecurityApplicationTests {

    @Mock
    SellerRepository sellerRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductVariationRepository productVariationRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    EmailService emailService;

    @Mock
    ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomerService customerService;

    @InjectMocks
    RegistrationService registrationService;

    @InjectMocks
    ProductService productService;

    @InjectMocks
    SellerService sellerService;

    @Test
    public void contextLoads() {
    }


    @Test
    public void isEmailExistIsTrue() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(new User());
        Boolean actualValue = registrationService.isEmailExists("ajay@gmail.com");
        assertTrue(actualValue);
    }


    @Test
    public void isEmailExistIsFalse() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);
        Boolean actualValue = registrationService.isEmailExists("ajay@gmail.com");
        assertFalse(actualValue);
    }


    @Test
    public void addCustomer() {

        CustomerCO user = new CustomerCO();
        user.setFirstName("Aryan");
        user.setLastName("ajay");
        user.setContact("9845632341");
        user.setEmail("aaryanajay@gmail.com");
        user.setPassword("anshul@1234");
        user.setConfirmPassword("anshul@1234");

        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(new Customer());
        Mockito.doNothing().when(auditLogsMongoDBService).registerUser(Mockito.anyString(), anyLong(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(emailService).sendEmailToCustomer(Mockito.anyString(), Mockito.anyString());
        Mockito.when(confirmationTokenRepository.save(Mockito.any())).thenReturn(null);
        ResponseEntity responseEntity = registrationService.registerCustomer(user);
        assertEquals("Verification mail is send to registered mail id.", responseEntity.getBody().toString());

    }


    @Test
    @WithMockUser
    public void viewProductByAdmin() {
        Category category = new Category();
        category.setId(1L);
        Product product = new Product();
        product.setCategory(category);
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");

        Mockito.when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), anyLong(), Mockito.anyString());

        ProductAdminDto productAdminDto = productService.viewProductByAdmin(1L, new Principal() {
            @Override
            public String getName() {
                return "Ajay";
            }
        });
        assertEquals("6S", productAdminDto.getName());

    }

    @WithMockUser
    @Test
    public void viewProductBySeller() {
        Category category = new Category();
        category.setId(1L);
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setEmail("seller@gmail.com");
        Product product = new Product();
        product.setCategory(category);
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");
        product.setSeller(seller);
        product.setIsDeleted(false);
        product.setIsActive(true);
        product.setIsCancelable(false);
        product.setIsReturnable(false);

        Mockito.when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), anyLong(), Mockito.anyString());

        ProductSellerDto productSellerDto = productService.viewProductBySeller(2L, new Principal() {
            @Override
            public String getName() {
                return "seller@gmail.com";
            }
        });
        assertEquals("6S", productSellerDto.getName());

    }


    @WithMockUser
    @Test
    public void getProductByCustomer() {

        Customer customer = new Customer();
        customer.setEmail("customer@gmail.com");
        Product product = new Product();
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");
        product.setIsDeleted(false);
        product.setIsActive(true);
        product.setIsCancelable(false);
        product.setIsReturnable(false);

        Mockito.when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), anyLong(), Mockito.anyString());

        ProductCustomerDto productCustomerDto = productService.getProductByCustomer(2L, new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertEquals("6S", productCustomerDto.getName());

    }

    @WithMockUser
    @Test
    public void getProductVariantBySeller() {

        ProductVariation productVariation = new ProductVariation();
        Product product = new Product();
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");
        product.setIsDeleted(false);
        product.setIsActive(true);
        product.setIsCancelable(false);
        product.setIsReturnable(false);
        productVariation.setIsActive(true);
        productVariation.setId(2L);
        productVariation.setProduct(product);
        productVariation.setPrice(234F);
        productVariation.setQuantityAvailable(10);

        Mockito.when(productVariationRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(productVariation));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), anyLong(), Mockito.anyString());

        ProductVariantDto productVariantDto = productService.getProductVariantBySeller(2l, new Principal() {
            @Override
            public String getName() {
                return "Seller@gmail.com";
            }
        });
        assertEquals(2L, productVariantDto.getProductVariantId());
    }

    @Test
    @WithMockUser
    public void viewCustomerProfile() {

        Customer customer = new Customer();
        customer.setEmail("customer@gmail.com");
        customer.setId(1L);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setContact("1234567890");
        customer.setIsActive(false);

        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        CustomerDto customerDto = customerService.viewCustomerProfile("customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });

        assertEquals("customer@gmail.com", customerDto.getEmail());

    }


    @WithMockUser
    @Test
    public void viewSellerProfile() {

        Address address = new Address();
        address.setId(1L);
        address.setAddressLine("testAddressLine");
        address.setCity("testCity");
        address.setState("testState");
        address.setCountry("testCountry");
        address.setZipcode("123456");
        address.setLable("testLable");

        Seller seller = new Seller();
        seller.setEmail("seller@gmail.com");
        seller.setId(1L);
        seller.setCompanyName("testCompanyName");
        seller.setFirstName("testSellerFirstName");
        seller.setLastName("testSellerLastName");
        seller.setCompanyContact("1234567890");
        seller.setIsActive(true);
        seller.setGst("testGST");
        seller.setAddress(address);

        Mockito.when(sellerRepository.findByEmail(Mockito.anyString())).thenReturn(seller);
        Mockito.doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        SellerDto sellerDto = sellerService.viewSellerProfile("seller@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "seller@gmail.com";
            }
        });

        assertEquals("seller@gmail.com", sellerDto.getEmail());

    }


    @Test
    @WithMockUser
    public void getAddressListCustomer() {

        Address address = new Address();
        address.setId(1L);
        address.setAddressLine("testAddressLine");
        address.setCity("testCity");
        address.setState("testState");
        address.setCountry("testCountry");
        address.setZipcode("123456");
        address.setLable("testLable");

        Address address1 = new Address();
        address1.setId(2L);
        address1.setAddressLine("testAddressLine");
        address1.setCity("testCity");
        address1.setState("testState");
        address1.setCountry("testCountry");
        address1.setZipcode("123456");
        address1.setLable("testLable");

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        addressList.add(address1);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");
        customer.setAddressList(addressList);
        User user = new User();
        user.setId(1L);
        user.setEmail("xyz");


        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        List<AddressDto> addressesResult = customerService.getAddressListCustomer("customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });

        assertEquals(2, addressesResult.size());

    }


    @Test
    @WithMockUser
    public void getAddressListCustomerFailTest() {

        Address address = new Address();
        address.setId(1L);
        address.setAddressLine("testAddressLine");
        address.setCity("testCity");
        address.setState("testState");
        address.setCountry("testCountry");
        address.setZipcode("123456");
        address.setLable("testLable");

        Address address1 = new Address();
        address1.setId(2L);
        address1.setAddressLine("testAddressLine");
        address1.setCity("testCity");
        address1.setState("testState");
        address1.setCountry("testCountry");
        address1.setZipcode("123456");
        address1.setLable("testLable");

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        addressList.add(address1);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");
        customer.setAddressList(addressList);
        User user = new User();
        user.setId(1L);
        user.setEmail("xyz");


        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        List<AddressDto> addressesResult = customerService.getAddressListCustomer("customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertNotEquals(1, addressesResult.size());
    }


    @Test
    @WithMockUser
    public void addCustomerAddressSuccessFullTest() {
        AddressCO addressCo = new AddressCO();
        addressCo.setAddressLine("testAddressLine");
        addressCo.setCity("testCity");
        addressCo.setState("testState");
        addressCo.setCountry("testCountry");
        addressCo.setZipcode("123456");
        addressCo.setLable("testLable");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");

        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.doNothing().when(auditLogsMongoDBService).saveNewObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = customerService.addCustomerAddress(addressCo, "customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });

        assertEquals("Address is successfully added to customers address list", responseEntity.getBody().toString());

    }


    @Test
    @WithMockUser
    public void addCustomerAddressFailTest() {
        AddressCO addressCo = new AddressCO();
        addressCo.setAddressLine("testAddressLine");
        addressCo.setCity("testCity");
        addressCo.setState("testState");
        addressCo.setCountry("testCountry");
        addressCo.setZipcode("123456");
        addressCo.setLable("testLabel");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customerTest@gmail.com");

        Mockito.when(customerRepository.findByEmail("customerTest@gmail.com")).thenReturn(customer);
        Mockito.doNothing().when(auditLogsMongoDBService).saveNewObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = customerService.addCustomerAddress(addressCo, "customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });

        assertNotEquals("Address is successfully added to customers address list", responseEntity.getBody().toString());

    }


    @Test
    @WithMockUser
    public void deleteAddressByIdSuccessFullTest() {


        Address address = new Address();
        address.setId(1L);
        address.setAddressLine("testAddressLine");
        address.setCity("testCity");
        address.setState("testState");
        address.setCountry("testCountry");
        address.setZipcode("123456");
        address.setLable("testLable");

        Address address1 = new Address();
        address1.setId(2L);
        address1.setAddressLine("testAddressLine");
        address1.setCity("testCity");
        address1.setState("testState");
        address1.setCountry("testCountry");
        address1.setZipcode("123456");
        address1.setLable("testLable");

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        addressList.add(address1);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");
        customer.setAddressList(addressList);

        Mockito.when(customerRepository.findByEmail("customer@gmail.com")).thenReturn(customer);
        Mockito.doNothing().when(auditLogsMongoDBService).deleteObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());


        ResponseEntity responseEntity = customerService.deleteAddressById(2L, "customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertEquals("Deleted Address with id " + address1.getId(), responseEntity.getBody().toString());
    }


    @Test
    @WithMockUser
    public void deleteAddressByIdFailTest() {

        Address address = new Address();
        address.setId(1L);
        address.setAddressLine("testAddressLine");
        address.setCity("testCity");
        address.setState("testState");
        address.setCountry("testCountry");
        address.setZipcode("123456");
        address.setLable("testLable");

        Address address1 = new Address();
        address1.setId(2L);
        address1.setAddressLine("testAddressLine");
        address1.setCity("testCity");
        address1.setState("testState");
        address1.setCountry("testCountry");
        address1.setZipcode("123456");
        address1.setLable("testLable");

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        addressList.add(address1);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");
        customer.setAddressList(addressList);

        Mockito.when(customerRepository.findByEmail("customer@gmail.com")).thenReturn(customer);
        Mockito.doNothing().when(auditLogsMongoDBService).deleteObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());


        ResponseEntity responseEntity = customerService.deleteAddressById(3L, "customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertNotEquals("Deleted Address with id " + address1.getId(), responseEntity.getBody().toString());
    }
//	@Test
//	void addSeller(){
//
//		Seller seller =new Seller();
//		Address address=new Address();
//		Role role=new Role();
//		List<Role> roleList=new ArrayList<>();
//		PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
//		seller.setFirstName("Sandeep");
//		seller.setLastName("Chaoudhary");
//		seller.setIsDeleted(false);
//		seller.setCompanyContact("987654321");
//		seller.setEmail("sandeepjaat@gmail.com");
//		seller.setPassword(passwordEncoder.encode("sandeep@1234"));
//		seller.setCompanyName("Bala Ji Granites");
//		seller.setGst("23634687");
//		role.setAuthority("ROLE_SELLER");
//		roleList.add(role);
//		address.setAddressLine("Ricco Industrial area");
//		address.setCity("Alwar");
//		address.setCountry("India");
//		address.setLable("Office");
//		address.setState("Rajasthan");
//		address.setZipcode("301701");
//		address.setUser(seller);
//		seller.setRoleList(roleList);
//		seller.setAddress(address);
//		sellerRepository.save(seller);
//	}
//
    //
////	@Test
////	void test(){
////		//addressRepository.deleteById(2L);
////		//System.out.println("=========================="+addressRepository.findById(2L).get().getCity());
////		userRepository.findById(3L);
////		userRepository.deleteById(3L);
////		Customer customer=userRepository.findById()
////	}
//
//	@Test
//	void test(){
//		Category category = new Category();
//		Category parent = categoryRepository.findById(1L).get();
//		category.setParentCategory(parent);
//		category.setName("Shoes");
//		categoryRepository.save(category);
//
//	}
////	@Tes
////	void addProduct(){
////
////		Product product=new Product();
////		ProductVariation productVariation=new ProductVariation();
////		ProductVariation productVariation1=new ProductVariation();
////		Category category=new Category();
////		List<ProductVariation> productVariationList =new ArrayList<>();
////		List<List<ProductVariation>> productList=new ArrayList<>();
////		PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
////		Seller seller =new Seller();
////		Address address=new Address();
////		Role role=new Role();
////		List<Role> roleList=new ArrayList<>();
////		seller.setFirstName("Flip");
////		seller.setLastName("cart");
////		seller.setDeleted(false);
////		seller.setActive(true);
////		seller.setCompanyContact("1233357789");
////		seller.setEmail("flipkart@gmail.com");
////		seller.setPassword(passwordEncoder.encode("flipkart@1234"));
////		seller.setCompanyName("flipkart");
////		seller.setGst("1233455");
////		role.setAuthority("ROLE_SELLER");
////		roleList.add(role);
////		address.setAddressLine("RICCO INDUSTRIAL AREA");
////		address.setCity("OKHLA");
////		address.setCountry("India");
////		address.setLable("Office");
////		address.setState("UP");
////		address.setZipcode("207230");
////		address.setUser(seller);
////		seller.setAddress(address);
////		seller.setRoleList(roleList);
////		product.setCancelable(false);
////		product.setBrand("Flipkart Basics");
////		product.setActive(true);
////		product.setName("Flipkart basics Data cable");
////		product.setDescription("Data Cable");
////		product.setSeller(seller);
////		productVariation.setPrice(200f);
////		productVariation.setQuantityAvailable(50);
////		productVariation.setPrimaryImage_Name("Datacable image");
////		productVariation.setMetadata("3 feet datacable");
////		productVariation1.setPrice(250f);
////		productVariation1.setQuantityAvailable(100);
////		productVariation1.setPrimaryImage_Name("Datacable image");
////		productVariation1.setMetadata("6 feet datacable");
////		productVariationList.add(productVariation);
////		productVariationList.add(productVariation1);
////		product.setProductVariationList(productVariationList);
////		category.setName("Mobile Accessories");
////		product.setCategory(category);
////		sellerRepository.save(seller);
////		productVariation.setProduct(product);
////		productRepository.save(product);
////
////	}
////
////	@Test
////	void addProdcutReview(){
////		ProductReview productReview =new ProductReview();
////		productReview.setRating(5);
////		productReview.setReview("Amazing product");
////		Product product=new Product();
////		Customer customer=new Customer();
////		product=productRepository.findById(3).get();
////		productReview.setProduct(product);
////		customer= (Customer) userRepository.findById(2).get();
////		productReview.setCustomer(customer);
////		productReviewRepository.save(productReview);
////
////	}
//
//	@Test
//			public void adddummy() {
//        PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
//		List<Role> roleList = new ArrayList<>();
//		Role role = new Role();
//		Role role2 = new Role();
//		role.setAuthority("ROLE_SELLER");
//		role2.setAuthority("ROLE_ADMIN");
//		roleList.add(role);
//		roleList.add(role2);
//		Seller seller = new Seller();
//		seller.setFirstName("dummy2");
//		seller.setEmail("123@abcd");
//		seller.setPassword(passwordEncoder.encode("pass"));
//		seller.setRoleList(roleList);
//		sellerRepository.save(seller);
//	}
//
//
////	@Test
////	void addCart(){
////		Cart cart=new Cart();
////		ProductVariation productVariation=new ProductVariation();
////		Customer customer =new Customer();
////		productVariation=productVariationRepository.findById(2).get();
////		cart.setProductVariation(productVariation);
////		customer=(Customer) userRepository.findById(2).get();
////		cart.setQuantity(1);
////		cart.setCustomer(customer);
////		cart.setWishListItem(true);
////		cartRepository.save(cart);
////	}


}
