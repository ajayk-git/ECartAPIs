package com.springbootcamp.springsecurity.Entities.Users;


import com.springbootcamp.springsecurity.Entities.Address;
import com.springbootcamp.springsecurity.Entities.Cart;
import com.springbootcamp.springsecurity.Entities.Order.Order;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "ID")
public class Customer extends User {

    private String contact;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SUBSELECT)
    private Set<Order> orderSet;

    @OneToOne(mappedBy = "customer")
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Address> addressList;

    public Customer(){

    }

    public void deleteAddress(Address... addresses)
    {
        if(addresses != null)
        {
            if(addressList == null)
                return;

            for(Address address : addresses)
            {
                if(addressList.contains(address))
                {
                    addressList.remove(address);
                    address.setUser(null);
                }
            }
        }
    }
    public void addAddress(Address... addresses)
    {
        if(addresses != null)
        {
            if(addressList == null)
                addressList = new ArrayList<>();

            for(Address address : addresses)
            {
                if(!addressList.contains(address))
                {
                    addressList.add(address);
                    address.setUser(this);
                }
            }

        }
    }
}
