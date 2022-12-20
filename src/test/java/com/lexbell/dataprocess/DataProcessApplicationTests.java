package com.lexbell.dataprocess;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

@SpringBootTest
class DataProcessApplicationTests {

    @Resource
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        System.out.println(mongoTemplate.findAll(JSONObject.class, "test"));
    }

}
