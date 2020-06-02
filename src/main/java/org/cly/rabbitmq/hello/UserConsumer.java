package org.cly.rabbitmq.hello;

import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RabbitListener(queues = RabbitmqConfiguration.QUEUE_USER)
public class UserConsumer {

    @RabbitHandler
    public void getMessage(String message){
        System.out.println("default user consumer receive-->"+message);
    }
}
