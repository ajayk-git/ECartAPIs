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

import com.springbootcamp.springsecurity.co.CustomerCO;
import com.springbootcamp.springsecurity.entities.ConfirmationToken;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.*;
import com.springbootcamp.springsecurity.services.AuditLogsMongoDBService;
import com.springbootcamp.springsecurity.services.CustomerService;
import com.springbootcamp.springsecurity.services.EmailService;
import com.springbootcamp.springsecurity.services.RegistrationService;
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
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SpringSecurityApplicationTests {


    @Autowired
    AddressRepository addressRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductVariationRepository productVariationRepository;
    @Autowired
    ProductReviewRepository productReviewRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    EmailService emailService;

    @Mock
    ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;

    @Autowired
    CustomerService customerService;

    @InjectMocks
    RegistrationService registrationService;

    @Mock
    ConfirmationToken confirmationToken;
    @Mock
    UserRepository userRepository;

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
    void addCustomer() {

        CustomerCO user = new CustomerCO();
        user.setFirstName("Aryan");
        user.setLastName("ajay");
        user.setContact("9845632341");
        user.setEmail("aaryanajay@gmail.com");
        user.setPassword("anshul@1234");
        user.setConfirmPassword("anshul@1234");

        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(new Customer());
        Mockito.doNothing().when(auditLogsMongoDBService).registerUser(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(emailService).sendEmailToCustomer(Mockito.anyString(), Mockito.anyString());
        Mockito.when(confirmationTokenRepository.save(Mockito.any())).thenReturn(null);
        ResponseEntity responseEntity = registrationService.registerCustomer(user);
        assertEquals("Verification mail is send to registered mail id.", responseEntity.getBody().toString());

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
