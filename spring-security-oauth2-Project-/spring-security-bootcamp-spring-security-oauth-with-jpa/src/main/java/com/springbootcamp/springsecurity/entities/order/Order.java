package com.springbootcamp.springsecurity.entities.order;

import com.springbootcamp.springsecurity.entities.Addresscopy;
import com.springbootcamp.springsecurity.entities.users.Customer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
@ApiModel(description = "All details about the Order ")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Order Id")
    //  @Column(name = "ORDER_ID")
    private Long id;
    //    @Column(name = "AMOUNT_PAID")
    @ApiModelProperty(notes = "Amount to be paid.")
    private float amountPaid;

    @Temporal(value = TemporalType.DATE)
    @ApiModelProperty(notes = "Date of order placed.")
    //  @Column(name = "DATE_CREATED")
    private Date dateCreated;

    @ApiModelProperty(notes = "Payment method user to place thr order.")
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

