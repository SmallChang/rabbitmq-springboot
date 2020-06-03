package org.cly.rabbitmq.topic;

import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitmqConfiguration.QUEUE_TOPIC_USER)
public class TopicUserMessageConsumer {

    @RabbitHandler
    public void getMessage(String message){
        System.out.println("topic get user message--->"+message);
    }
}
