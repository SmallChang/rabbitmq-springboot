package org.cly.rabbitmq.topic;

import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitmqConfiguration.QUEUE_TOPIC_EMAIL)
public class TopicEmailMessageConsumer {

    @RabbitHandler
    public void getMessage(String message){
        System.out.println("topic get email message--->"+message);
    }
}
