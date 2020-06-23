package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.entities.users.User;
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

    @InjectMocks
    AdminService adminService;


    @Test
    @WithMockUser
    public void deactivateAccountByIdByAdminSuccessFullTest() {
        User user=new User();
        user.setId(1L);
        user.setEmail("userTest@gmail.com");
        user.setIsActive(true);
        user.setIsEnabled(true);

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setText("testMessage");
        simpleMailMessage.setTo("testMail@gmail.com");


        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(javaMailSender).send(simpleMailMessage);
        Mockito.doNothing().when(auditLogsMongoDBService).deactivateObject(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString());

        ResponseEntity responseEntity=adminService.deactivateAccountById(1L, new Principal() {
            @Override
            public String getName() {
                return "admin@gmail.com";
            }
        });

        assertEquals("User is deactivated",responseEntity.getBody().toString());
    }

    @Test
    @WithMockUser
    public void activateAccountByIdByAdminSuccessFullTest() {
        User user=new User();
        user.setId(1L);
        user.setEmail("userTest@gmail.com");
        user.setIsActive(false);
        user.setIsEnabled(false);

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setText("testMessage");
        simpleMailMessage.setTo("testMail@gmail.com");


        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(javaMailSender).send(simpleMailMessage);
        Mockito.doNothing().when(auditLogsMongoDBService).activateObject(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString());

        ResponseEntity responseEntity=adminService.activateAccountById(1L, new Principal() {
            @Override
            public String getName() {
                return "admin@gmail.com";
            }
        });

        assertEquals("User's Account associated email id : " + user.getEmail() + "is activated.",responseEntity.getBody().toString());

    }

    @Test
    public void getCustomerById() {
    }

    @Test
    public void getSellerByid() {
    }
}