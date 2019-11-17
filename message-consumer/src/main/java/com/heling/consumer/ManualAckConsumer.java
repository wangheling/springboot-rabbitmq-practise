package com.heling.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author whl
 * @description 手动确认消息
 * @date 2019/11/17 15:22
 */
@Component
@RabbitListener(queues = "manual_ack_queue")
@Slf4j
public class ManualAckConsumer {

    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws IOException {
        log.info("receive message:" + msg);
        String s = JSONObject.parseObject(msg, String.class);
        switch (s) {
            case "确认":
                // 手工应答
                // 如果不应答，队列中的消息会一直存在，重新连接的时候会重复消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                break;
            case "否认":
                //如果只有这一个消费者，requeue 为true 的时候会造成消息重复消费
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
                break;
            case "拒收":
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                break;
        }
    }
}
