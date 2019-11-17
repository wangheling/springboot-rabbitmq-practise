package com.heling.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author whl
 * @description
 * @date 2019/11/15 18:28
 */
@Configuration
@PropertySource("classpath:mq.properties")
@Slf4j
public class RabbitConfig {

    @Value("${com.heling.directexchange}")
    private String directExchange;

    @Bean("directExchange")
    public DirectExchange directExchange() {
        return new DirectExchange(directExchange);
    }

    @Bean("myQueue")
    public Queue myQueue() {
        return new Queue("my_queue");
    }

    @Bean("manualAckQueue")
    public Queue manualAckQueue() {
        return new Queue("manual_ack_queue");
    }


    @Bean
    public Binding firstBinding(@Qualifier("myQueue") Queue queue, @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("com.heling.simple");
    }

    @Bean
    public Binding secondBinding(@Qualifier("manualAckQueue") Queue queue, @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("com.heling.manual");
    }

    /**
     * 自定义ListenerContainerFactory
     *
     * 设置应答为手动应答，需要在Consumer上@RabbitListener指定
     * containerFactory = "rabbitListenerContainerFactoryManualAck"
     */
    @Bean("rabbitListenerContainerFactoryManualAck")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryManualAck(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAutoStartup(true);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    /**
     * 自定义ListenerContainerFactory
     *
     * 设置应答为无应答，需要在Consumer上@RabbitListener指定
     * containerFactory = "rabbitListenerContainerFactoryNoneAck"
     */
    @Bean("rabbitListenerContainerFactoryNoneAck")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryNoneAck(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAutoStartup(true);
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        return factory;
    }
}
