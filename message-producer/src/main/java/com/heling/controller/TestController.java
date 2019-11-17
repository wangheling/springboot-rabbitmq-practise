package com.heling.controller;

import com.heling.service.SendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whl
 * @description
 * @date 2019/11/16 15:16
 */
@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @Autowired
    private SendService sendService;

    @GetMapping("simple")
    public void test() {
        sendService.sendSimpleMq();
    }

    @GetMapping("manual")
    public void test(@RequestParam("msg") String msg) {

        sendService.sendManualAckMq(msg);
    }
}
