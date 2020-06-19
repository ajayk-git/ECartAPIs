package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.entities.users.Address;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class SellerServiceTest {

    @InjectMocks
    SellerService sellerService;

    @Mock
    SellerRepository sellerRepository;

    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;


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
    public void getAddressSellerSuccessFullTest() {
        Address address = new Address();
        address.setId(1L);
        address.setAddressLine("testAddressLine");
        address.setCity("testCity");
        address.setState("testState");
        address.setCountry("testCountry");
        address.setZipcode("123456");
        address.setLable("testLabel");

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setEmail("seller@gmail.com");
        seller.setAddress(address);


        Mockito.when(sellerRepository.findByEmail(Mockito.anyString())).thenReturn(seller);
        Mockito.doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        AddressDto addressDto = sellerService.getAddressSeller(seller.getEmail(), new Principal() {
            @Override
            public String getName() {
                return seller.getEmail();
            }
        });

        assertEquals(addressDto.getAddressLine(), seller.getAddress().getAddressLine());
    }
}