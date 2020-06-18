package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.AddressCO;
import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.entities.users.Address;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
//import org.junit.Test;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(value = MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    UserRepository userRepository;

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

    @Test
    @WithMockUser
    public void getAddressListCustomerSuccessFullTest() {

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
        Mockito.doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());


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
        Mockito.doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());


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
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
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
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
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
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
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
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
        Mockito.doNothing().when(auditLogsMongoDBService).deleteObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());


        ResponseEntity responseEntity = customerService.deleteAddressById(3L, "customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertNotEquals("Deleted Address with id " + address1.getId(), responseEntity.getBody().toString());
    }


    @Test
    @WithMockUser
    public void updateCustomerAddressSuccessFullTest() {

        AddressCO addressCo = new AddressCO();
        addressCo.setAddressLine("testUpdateAddressLine");
        addressCo.setCity("testUpdateCity");
        addressCo.setState("testUpdateState");
        addressCo.setCountry("testUpdateCountry");
        addressCo.setZipcode("123456");
        addressCo.setLable("testUpdateLabel");

        Address address = new Address();
        address.setId(2L);
        address.setAddressLine("testAddressLine");
        address.setCity("testCity");
        address.setState("testState");
        address.setCountry("testCountry");
        address.setZipcode("123456");
        address.setLable("testLable");

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");
        customer.setAddressList(addressList);

        Mockito.when(customerRepository.findByEmail("customer@gmail.com")).thenReturn(customer);
        Mockito.doNothing().when(auditLogsMongoDBService).updateObject(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString());

        ResponseEntity responseEntity=customerService.updateCustomerAddress(addressCo,2L,"customer@gmail.com", new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertEquals(addressCo.getAddressLine(),address.getAddressLine());
    }

}
