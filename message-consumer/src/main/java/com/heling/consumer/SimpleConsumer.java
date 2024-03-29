package com.heling.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author whl
 * @description
 * @date 2019/11/15 16:56
 */
@Component
@RabbitListener(queues = "my_queue",containerFactory = "rabbitListenerContainerFactoryNoneAck")
@Slf4j
public class SimpleConsumer {

    @RabbitHandler
    public void process(String msg) {
        log.info("receive message:" + msg);
    }
}
