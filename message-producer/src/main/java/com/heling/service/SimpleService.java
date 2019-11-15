package com.heling.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author whl
 * @description
 * @date 2019/11/15 16:48
 */
@Service
@Slf4j
public class SimpleService {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        rabbitTemplate.convertAndSend("MY_FIRST_QUEUE", "first msg");
    }
}
