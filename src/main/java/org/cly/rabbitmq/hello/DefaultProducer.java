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
        //该队列是普通消费者消费（自动确认）
//        rabbitTemplate.convertAndSend(RabbitmqConfiguration.QUEUE_HELLO,helloMessage);
        //该队列是消费者手动确认
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.QUEUE_USER,userMessage);
    }
}
