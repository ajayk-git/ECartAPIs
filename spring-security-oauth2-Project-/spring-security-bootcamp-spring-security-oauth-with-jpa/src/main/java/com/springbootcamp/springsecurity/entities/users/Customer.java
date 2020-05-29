package com.springbootcamp.springsecurity.entities.users;


import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.order.Order;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@PrimaryKeyJoinColumn(name = "ID")
@ApiModel(description="All details about Customer.")
public class Customer extends User {


     String contact;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value= FetchMode.SUBSELECT)
     Set<Order> orderSet;

    @OneToOne(mappedBy = "customer")
     Cart cart;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
     List<Address> addressList;

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
