package com.springbootcamp.springsecurity.entities.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
//@Table(name = "ORDER_STATUS")
public class OrderStatus implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name="order_product_id")
    private OrderProduct orderProduct;
//
//    @Enumerated(EnumType.STRING)
//    private FROM_STATUS from_status;
//
//    @Enumerated(EnumType.STRING)
//    private TO_STATUS to_status;

    private String transition_notes_comments;

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
