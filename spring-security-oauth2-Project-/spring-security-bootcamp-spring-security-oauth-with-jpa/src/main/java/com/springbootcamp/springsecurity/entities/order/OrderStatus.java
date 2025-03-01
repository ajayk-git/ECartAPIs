package com.springbootcamp.springsecurity.entities.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "ORDER_STATUS")
@ApiModel(description = "All details about the Order Status in transition ")
public class OrderStatus implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name="order_product_id")
     OrderProduct orderProduct;
//
//    @Enumerated(EnumType.STRING)
//    private FROM_STATUS from_status;
//
//    @Enumerated(EnumType.STRING)
//    private TO_STATUS to_status;

    @ApiModelProperty(notes = "Transition notes like handle with care etc..")
     String transition_notes_comments;

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

//    public FROM_STATUS getFrom_status() {
//        return from_status;
//    }
//
//    public void setFrom_status(FROM_STATUS from_status) {
//        this.from_status = from_status;
//    }
//
//    public TO_STATUS getTo_status() {
//        return to_status;
//    }
//
//    public void setTo_status(TO_STATUS to_status) {
//        this.to_status = to_status;
//    }

    public String getTransition_notes_comments() {
        return transition_notes_comments;
    }

    public void setTransition_notes_comments(String transition_notes_comments) {
        this.transition_notes_comments = transition_notes_comments;
    }

}
