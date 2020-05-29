package com.springbootcamp.springsecurity.rabbitMqConfigurations;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.springbootcamp.springsecurity.rabbitMqConfigurations.RabbitMqConfig.queueName;


public class RabbitMqMessageReceiver {


    @RabbitListener(queues = {queueName})
    public void receiveMessageFromMessageQueue(String message) {
        System.out.println("Received  MessageQueue message: " + message);
    }

}

