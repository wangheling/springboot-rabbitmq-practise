package com.heling.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author whl
 * @description
 * @date 2019/11/15 16:48
 */
@Service
@Slf4j
public class SimpleService {

//    @Resource
//    private AmqpTemplate amqpTemplate;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    //@Resource
    @Autowired
    @Qualifier("myRabbitTemplate")
    private RabbitTemplate myRabbiTemplate;

    public void send() {
        String jsonBody = JSONObject.toJSONString("second msg");
        myRabbiTemplate.convertAndSend("DIRECT_EXCHANGE","com.heling", jsonBody);
    }
}
