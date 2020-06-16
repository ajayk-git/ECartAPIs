package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.CustomerCO;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.ConfirmationTokenRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
//import org.junit.jupiter.api.Test;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import com.springbootcamp.springsecurity.services.AuditLogsMongoDBService;
import com.springbootcamp.springsecurity.services.EmailService;
import com.springbootcamp.springsecurity.services.RegistrationService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class RegistrationServiceTest {

    @Mock
    CustomerRepository customerRepository;
    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;
    @Mock
    EmailService emailService;
    @Mock
    ConfirmationTokenRepository confirmationTokenRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    RegistrationService registrationService;

    @Test
    public void registerCustomer() {
        CustomerCO user =new CustomerCO();
        user.setFirstName("Aryan");
        user.setLastName("ajay");
        user.setContact("9845632341");
        user.setEmail("aaryanajay@gmail.com");
        user.setPassword("anshul@1234");
        user.setConfirmPassword("anshul@1234");

        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(new Customer());
       Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(auditLogsMongoDBService).registerUser(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString(),Mockito.any());
        Mockito.doNothing().when(emailService).sendEmailToCustomer(Mockito.anyString(),Mockito.anyString());
        when(confirmationTokenRepository.save(Mockito.any())).thenReturn(null);
        ResponseEntity responseEntity=registrationService.registerCustomer(user);
        assertEquals("Verification mail is send to registered mail id.",responseEntity.getBody().toString());
    }

}