package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(value = MockitoJUnitRunner.class)
public class AdminServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    AdminService adminService;


    @Test
    @WithMockUser
    public void deactivateAccountByIdByAdminSuccessFullTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("userTest@gmail.com");
        user.setIsActive(true);
        user.setIsEnabled(true);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText("testMessage");
        simpleMailMessage.setTo("testMail@gmail.com");


        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(javaMailSender).send(simpleMailMessage);
        Mockito.doNothing().when(auditLogsMongoDBService).deactivateObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = adminService.deactivateAccountById(1L, new Principal() {
            @Override
            public String getName() {
                return "admin@gmail.com";
            }
        });

        assertEquals("User is deactivated", responseEntity.getBody().toString());
    }

    @Test
    @WithMockUser
    public void activateAccountByIdByAdminSuccessFullTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("userTest@gmail.com");
        user.setIsActive(false);
        user.setIsEnabled(false);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText("testMessage");
        simpleMailMessage.setTo("testMail@gmail.com");


        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(javaMailSender).send(simpleMailMessage);
        Mockito.doNothing().when(auditLogsMongoDBService).activateObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = adminService.activateAccountById(1L, new Principal() {
            @Override
            public String getName() {
                return "admin@gmail.com";
            }
        });

        assertEquals("User's Account associated email id : " + user.getEmail() + "is activated.", responseEntity.getBody().toString());

    }

    @Test
    @WithMockUser
    public void getCustomerByIdByAdminSuccessFullTest() {
        Customer customer = new Customer();
        customer.setEmail("testCustomer@gmail.com");
        customer.setId(1L);
        customer.setContact("1234567890");
        customer.setFirstName("testCustomerFirstName");
        customer.setLastName("testCustomerLastName");

        Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(customer));
        Mockito.doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        CustomerDto customerDto = adminService.getCustomerById(1L, new Principal() {
            @Override
            public String getName() {
                return "admin@gmail.com";
            }
        });

        assertEquals(customerDto.getEmail(), customer.getEmail());
    }

    @Test
    public void getSellerByid() {
    }
}