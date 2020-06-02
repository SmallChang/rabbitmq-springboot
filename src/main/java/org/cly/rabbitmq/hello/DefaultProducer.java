package org.cly.rabbitmq.hello;

import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDefaultMessage(){
        String helloMessage = "default hello";
        String userMessage = "default user";
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.QUEUE_HELLO,helloMessage);
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.QUEUE_USER,userMessage);
    }



}
