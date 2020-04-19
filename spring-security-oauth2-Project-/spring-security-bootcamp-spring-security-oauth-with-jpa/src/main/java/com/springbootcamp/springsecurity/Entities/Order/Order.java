package com.springbootcamp.springsecurity.Entities.Order;

import com.springbootcamp.springsecurity.Entities.Addresscopy;
import com.springbootcamp.springsecurity.Entities.Users.Customer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.persistence.criteria.From;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="ORDERS")
public class Order {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
    //  @Column(name = "ORDER_ID")
      private Long id;
  //    @Column(name = "AMOUNT_PAID")
      private float amountPaid;
      @Temporal(value = TemporalType.DATE)
    //  @Column(name = "DATE_CREATED")
      private Date dateCreated;

     // @Column(name = "PAYMENT_METHOD")
      private String paymentMethod;

      @Embedded
      Addresscopy addresscopy;

      @ManyToOne   //to map order to user
      @JoinColumn(name = "Customer_ID")
      private Customer customer;


      @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)    //order tp produuct
      @Fetch(FetchMode.SUBSELECT)
      private List<OrderProduct> orderProductList;

      public Order() {
      }

}

//      public boolean setFromStatusAndToStatus(FromStatus fromStatus,ToStatus toStatus){
//
//            if(fromStatus.equals(FromStatus.ORDER_PLACED))
//            {
//                  if(toStatus.equals(ToStatus.CANCELLED) || toStatus.equals(ToStatus.ORDER_CONFIRMED) || toStatus.equals(ToStatus.ORDER_REJECTED))
//                  {
//                        this.fromStatus = fromStatus;
//                        this.toStatus = toStatus;
//                        return true;
//                  }
//
//                  return false;
//            }
//            else if(fromStatus.equals(FromStatus.CANCELLED))
//                  return true;
//
//      }

