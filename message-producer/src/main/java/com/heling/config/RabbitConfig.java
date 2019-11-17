package com.heling.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author whl
 * @description rabbitMq配置类
 * @date 2019/11/15 17:54
 */
@Configuration
@Slf4j
public class RabbitConfig {

    /**
     * 可以设置admin的属性
     * RabbitAdmin主要是用来进行交换机，队列，以及绑定的声明和删除
     * @param connectionFactory
     * @return RabbitAdmin
     */
//    @Bean
//    public RabbitAdmin amqpAdmin(ConnectionFactory connectionFactory) {
//        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
//        //autoStartup必须设置true，否则Spring容器不会加载RabbitAdmin类
//        // admin.setAutoStartup(true);//默认值
//        return admin;
//    }

    /**
     * 可以自定义自己的消息模板
     *
     * @return RabbitTemplate
     * @Desc：封装了创建连接、创建消息信道、收发消息、消息格式转换（ConvertAndSend→Message）、关闭信道、关闭连接等等操作
     */
    @Bean("myRabbitTemplate")
    public RabbitTemplate myRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //所有的消息发送都会转换成JSON格式发到交换机
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        /**
         * 1.当mandatory标志位设置为true时，
         * 如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，那么broker会调用basic.return方法将消息返还给生产者;
         * 2.当mandatory设置为false时，
         * 出现上述情况broker会直接将消息丢弃;
         * 通俗的讲，mandatory标志告诉broker代理服务器至少将消息route到一个队列中，否则就将消息return给发送者
         */
        rabbitTemplate.setMandatory(true);

        // publisher-confirms: true #开启消息确认机制
        //异步确认模式，可以一边发送一边确认
        //confirmCallBack是生产者和交换机之间的确认，如果交换机不存在等原因，则ack=false。
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    log.error("消息确认失败,cause:" + cause);
                    throw new RuntimeException("发送异常：" + cause);
                }
            }
        });

        //returnCallBack和confirmCallBack都是broker给生产者确认信息是否发送到broker
        //publisher-returns: true #支持消息发送失败返回队列
        //returnCallback 是交换机和队列间的确认，如果路由键不存在或者队列不存在，返回错误信息。
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("发送消息失败，returnCallBack======>>\n" +
                                "message:{},\n" +
                                "replyCode:{},\n" +
                                "replyText:{},\n" +
                                "exchange:{},\n" +
                                "routingKey:{}",
                        JSONObject.toJSON(message), replyCode, replyText, exchange, routingKey);
            }
        });

        return rabbitTemplate;
    }

}
