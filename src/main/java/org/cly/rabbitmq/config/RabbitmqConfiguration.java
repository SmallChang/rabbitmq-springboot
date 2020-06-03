package org.cly.rabbitmq.config;

import org.cly.rabbitmq.hello.UserConsumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserConsumer userConsumer;

    public final static String EXCHANGE_FANOUT = "sb.fanout.exchange";
    public final static String EXCHANGE_QUEUE = "sb.fanout.queue";

    public final static String EXCHANGE_TOPIC = "sb.topic.exchange";
    public final static String QUEUE_TOPIC_EMAIL = "sb.info.email";
    public final static String QUEUE_TOPIC_USER = "sb.info.user";
    public final static String RK_EMAIL = "sb.info.email";
    public final static String RK_USER = "sb.info.user";

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
        rabbitTemplate.setMandatory(true);
        //发送方确认
        rabbitTemplate.setConfirmCallback(confirmCallback());
        //失败回调
        rabbitTemplate.setReturnCallback(returnCallback());
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



    //============以下是验证Topic Exchange=======
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE_TOPIC);
    }

    @Bean
    public Queue queueEmailMessage() {
        return new Queue(QUEUE_TOPIC_EMAIL);
    }

    @Bean
    public Queue queueUserMessages() {
        return new Queue(QUEUE_TOPIC_USER);
    }

    @Bean
    public Binding topicEmailBindQueue(){
        return BindingBuilder.bind(queueEmailMessage()).to(topicExchange()).with("sb.*.email");
    }

    @Bean
    public Binding topicUserBindQueue(){
        return BindingBuilder.bind(queueUserMessages()).to(topicExchange()).with("sb.*.user");
    }
    //===============以上是验证topic Exchange==========




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


    //===============消费者手动应答==========
    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container
                = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(userQueue());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(userConsumer);
        return container;
    }



    //=============生产者 发送方确认回调==========
    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback(){
        return new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if(ack){
                    System.out.println("发送者确认发送给mq成功");
                }else {
                    //处理失败的消息
                    System.out.println("发送者发送给mq失败,考虑重发:"+cause);
                }

            }
        };
    }

    //=============生产者 失败通知回调==========
    @Bean
    public RabbitTemplate.ReturnCallback returnCallback(){
        return new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message,
                                        int replyCode,
                                        String replyText,
                                        String exchange,
                                        String routingKey) {
                System.out.println("无法路由的消息，需要考虑另外处理。");
                System.out.println("Returned replyText："+replyText);
                System.out.println("Returned exchange："+exchange);
                System.out.println("Returned routingKey："+routingKey);
                String msgJson  = new String(message.getBody());
                System.out.println("Returned Message："+msgJson);
            }
        };

    }







}
