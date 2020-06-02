package org.cly.rabbitmq.fanout;

import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitmqConfiguration.EXCHANGE_QUEUE)
public class FanoutConsumer {

    @RabbitHandler
    public void getMessage(String message){
        System.out.println("fanout consumer receive--->"+message);
    }
}
