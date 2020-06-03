package org.cly.rabbitmq.topic;

import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(){
        String userMessage = "topic user message";
        String emailMessage = "topic email message";
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_TOPIC,RabbitmqConfiguration.RK_EMAIL,emailMessage);
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_TOPIC,RabbitmqConfiguration.RK_USER,userMessage);

        String msg3 = "I am error mesaages";
        System.out.println("TopicSender send the 3rd : " + msg3);
        this.rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_TOPIC, "errorkey", msg3);
    }

}
