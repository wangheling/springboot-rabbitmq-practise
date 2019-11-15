package com.heling;

import com.heling.service.SimpleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageProducerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private SimpleService simpleService;
    @Test
    public void test01() {
        simpleService.send();
    }

}
