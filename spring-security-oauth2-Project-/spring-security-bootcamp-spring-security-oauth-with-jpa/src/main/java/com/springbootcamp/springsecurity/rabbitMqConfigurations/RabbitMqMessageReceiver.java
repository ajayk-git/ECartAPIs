package com.springbootcamp.springsecurity.rabbitMqConfigurations;

import com.springbootcamp.springsecurity.entities.order.Order;
import com.springbootcamp.springsecurity.services.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import static com.springbootcamp.springsecurity.rabbitMqConfigurations.RabbitMqConfig.queueName;

@Log4j2

public class RabbitMqMessageReceiver {

    @Autowired
    EmailService emailService;


    @RabbitListener(queues = {queueName})
    public void receiveMessageFromOrderQueue(Long orderId) {
        emailService.sendOrderPlacedNotificationToCustomer(orderId);
        emailService.sendOrderPlacedNotificationToSeller(orderId);
    }

}

