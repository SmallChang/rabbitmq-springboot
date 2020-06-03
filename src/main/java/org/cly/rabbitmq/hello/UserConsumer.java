package org.cly.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import org.cly.rabbitmq.config.RabbitmqConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;


@Component
//@RabbitListener(queues = RabbitmqConfiguration.QUEUE_USER)
public class UserConsumer implements ChannelAwareMessageListener {

    /**
     * 这些注解没有了，在配置文件里配置
     * @param message
     */
//    @RabbitHandler

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msg = new String(message.getBody());
            System.out.println("UserReceiver>>>>>>>接收到消息:"+msg);
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        false);
                System.out.println("UserReceiver>>>>>>消息已应答");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false,true);
                System.out.println("UserReceiver>>>>>>拒绝消息，要求Mq重新派发");
                throw e;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
