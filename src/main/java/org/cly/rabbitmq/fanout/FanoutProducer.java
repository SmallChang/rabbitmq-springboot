package org.cly.rabbitmq.fanout;

import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FanoutProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(){
        String message = "hello";
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_FANOUT,"23",message);
    }
}
