package org.cly.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String addresses;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    public final static String EXCHANGE_FANOUT = "sb.fanout.exchange";
    public final static String EXCHANGE_QUEUE = "sb.fanout.queue";
    public final static String QUEUE_HELLO = "sb.hello";
    public final static String QUEUE_USER = "sb.user";



    /**
     * 连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        //todo 消息发送确认
        connectionFactory.setPublisherConfirms(publisherConfirms);

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate newRabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //失败通知
//        rabbitTemplate.setMandatory(true);
        //发送方确认
//        rabbitTemplate.setConfirmCallback();
        //失败回调
//        rabbitTemplate.setReturnCallback();
        return rabbitTemplate;
    }

    //===============使用了RabbitMQ系统缺省的交换器==========
    //绑定键即为队列名称
    @Bean
    public Queue helloQueue() {
        return new Queue(QUEUE_HELLO);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(QUEUE_USER);
    }



    //===============以下是验证Fanout Exchange==========
    //交换器
    @Bean
    public FanoutExchange fanoutExchange01(){
        return new FanoutExchange(EXCHANGE_FANOUT);
    }

    //队列
    @Bean
    public Queue fanoutQueue(){
        return new Queue(EXCHANGE_QUEUE);
    }

    //绑定
    @Bean
    public Binding fanoutBindQueue(){
       return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange01());
    }
    //===============以上是验证Fanout Exchange==========











}
