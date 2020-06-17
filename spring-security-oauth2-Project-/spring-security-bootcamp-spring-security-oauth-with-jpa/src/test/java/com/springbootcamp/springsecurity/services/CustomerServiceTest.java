package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
//import org.junit.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(value = MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @Test
    @WithMockUser
    public void viewCustomerProfile() {

        Customer customer = new Customer();
        customer.setEmail("customer@gmail.com");
        customer.setId(1L);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setContact("1111111111");
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

}