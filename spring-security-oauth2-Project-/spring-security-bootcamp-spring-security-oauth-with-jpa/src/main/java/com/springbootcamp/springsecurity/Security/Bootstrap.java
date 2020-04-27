package com.springbootcamp.springsecurity.Security;


import com.springbootcamp.springsecurity.Entities.*;
import com.springbootcamp.springsecurity.Entities.Order.Order;
import com.springbootcamp.springsecurity.Entities.Product.Category;
import com.springbootcamp.springsecurity.Entities.Product.Product;
import com.springbootcamp.springsecurity.Entities.Product.ProductVariation;
import com.springbootcamp.springsecurity.Entities.Product.ProductReview;
import com.springbootcamp.springsecurity.Entities.Users.Customer;
import com.springbootcamp.springsecurity.Entities.Users.Seller;
import com.springbootcamp.springsecurity.Entities.Users.User;
import com.springbootcamp.springsecurity.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Bootstrap {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductVariationRepository productVariationRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;





    public void initialize()  {

        if(userRepository.count()<2){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user1 = new User();
            user1.setFirstName("ADMIN");
            user1.setLastName("ADMIN");
            user1.setEmail("admin@tothenew.com");
            user1.setActive(true);
            user1.setDeleted(false);
            user1.setPassword(passwordEncoder.encode("admin"));
            List<Role> roleList=new ArrayList<>();
            Role role=new Role();
            role.setAuthority("ROLE_ADMIN");
            roleList.add(role);
            user1.setAccountNonLocked(true);
            user1.setEnabled(true);
            user1.setAccountNotExpired(true);
            user1.setCredentialsNonExpired(true);
            user1.setRoleList(roleList);
            userRepository.save(user1);
            System.out.println("Total users saved::"+userRepository.count());

        }

        if(sellerRepository.count()<1){

            Seller seller =new Seller();
            Address address=new Address();
            Role role=new Role();
            List<Role> roleList=new ArrayList<>();

            PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            seller.setFirstName("Aman");
            seller.setLastName("Sharma");
            seller.setDeleted(false);
            seller.setActive(true);
            seller.setCompanyContact("8735279816");
            seller.setEmail("amansharma@gmail.com");
            seller.setPassword(passwordEncoder.encode("Aman@1234"));
            seller.setCompanyName("Aman Communications");
            seller.setGst("18AABCT3518Q1ZV");
            role.setAuthority("ROLE_SELLER");
            roleList.add(role);
            address.setAddressLine("Nirman Vihar");
            address.setCity("East Delhi");
            address.setCountry("India");
            address.setLable("Office");
            address.setState("New Delhi");
            address.setZipcode("110456");
            seller.setAccountNonLocked(true);
            seller.setCredentialsNonExpired(true);
            seller.setAccountNotExpired(true);
            seller.setEnabled(true);
            seller.setActive(true);
            address.setUser(seller);;
            seller.setRoleList(roleList);
            seller.setAddress(address);
            sellerRepository.save(seller);
            System.out.println("Total users saved::"+sellerRepository.count());

        }

        if (customerRepository.count()<1){
            Customer user =new Customer();
            Address address=new Address();
            Role role=new Role();
            List<Address> addressList=new ArrayList<>();
            List<Role> roleList=new ArrayList<>();
            PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            user.setFirstName("Ajay");
            user.setLastName("Kumar");
            user.setDeleted(false);
            user.setActive(true);
            user.setContact("7053872246");
            user.setEmail("ajay.mca17.du@gmail.com");
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNotExpired(true);
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode("Ajay@1234"));
            role.setAuthority("ROLE_USER");

            roleList.add(role);
            address.setAddressLine("Shakti Vihar colony");
            address.setCity("Alwar");
            address.setCountry("India");
            address.setLable("Home");
            address.setState("Rajasthan");
            address.setZipcode("301701");
            address.setUser(user);
            addressList.add(address);
            user.setRoleList(roleList);
            user.setAddressList(addressList);
            customerRepository.save(user);
        }

        if(categoryRepository.count() < 1){
            Category category = new Category();
            category.setName("Mobile Phones");
            categoryRepository.save(category);
        }


        if(productRepository.count() < 1){

            Product product = new Product();
            product.setName("One Plus 7");
            product.setDescription("Smart Phone");

            product.setCategory(categoryRepository.findByName("Mobile Phones"));
            product.setSeller( sellerRepository.findByEmail("amansharama@gmail.com"));
            product.setCancelable(true);
            product.setReturnable(true);
            product.setBrand("OnePlus");
            product.setActive(true);

            List<ProductVariation> productVariations = new ArrayList<>();
            ProductVariation variation1 = new ProductVariation(8,15000,"firstImage","first metadata");
            productVariations.add(variation1);
            variation1.setProduct(product);
            ProductVariation variation2 = new ProductVariation(10,10000,"Second Image","Second Metadata");
            variation2.setProduct(product);
            productVariations.add(variation2);
            product.setProductVariationList(productVariations);



            List<ProductReview> productReviewsList = new ArrayList<>();
            ProductReview review1 = new ProductReview();
            review1.setReview("Nice Camera,Nice Performance");
            review1.setRating(4.8f);
            review1.setProduct(product);
            review1.setCustomer(customerRepository.findByEmail("ajay.mca17.du@gmail.com"));
            productReviewsList.add(review1);

            ProductReview review2 = new ProductReview();
            review2.setReview("Good Battery Backup,PUB-G runs smoothly");
            review2.setRating(4.7f);
            review2.setProduct(product);
            review2.setCustomer(customerRepository.findByEmail("ajay.mca17.du@gmail.com"));
            productReviewsList.add(review2);
            product.setProductReviewList(productReviewsList);
            productRepository.save(product);
        }


        if (cartRepository.count() < 1){
            Cart cart = new Cart();
            cart.setCustomer(customerRepository.findByEmail("ajay.mca17.du@gmail.com"));
            cart.setProductVariation(productVariationRepository.findById(1L).get());
            cart.setQuantity(2);
            cart.setWishListItem(true);
            cartRepository.save(cart);
        }


        if (orderRepository .count() < 1){
            Order order = new Order();
            order.setAmountPaid(10000);

            Date date = new Date();
            order.setDateCreated(date);
            order.setPaymentMethod("Debit card");
            order.setCustomer(customerRepository.findByEmail("ajay.mca17.du@gmail.com"));
            Address address=new Address("Alwar","Rajasthan","India","Home","Near Income Tax Office","301701");
            Addresscopy addresscopy=new Addresscopy(address);
            order.setAddresscopy(addresscopy);
            orderRepository.save(order);
        }

        if(categoryMetaDataFieldRepository.count()<3){
            CategoryMetaDataField categoryMetaDataField=new CategoryMetaDataField();
            CategoryMetaDataField categoryMetaDataField1=new CategoryMetaDataField();
            CategoryMetaDataField categoryMetaDataField2=new CategoryMetaDataField();


            categoryMetaDataField.setFieldName("Size");
            categoryMetaDataField1.setFieldName("Color");
            categoryMetaDataField2.setFieldName("Fabric");
            categoryMetaDataFieldRepository.save(categoryMetaDataField);
            categoryMetaDataFieldRepository.save(categoryMetaDataField1);
            categoryMetaDataFieldRepository.save(categoryMetaDataField2);
        }


    }
}
