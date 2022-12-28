package com.lexbell.dataprocess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lexbell.dataprocess.website.WebsiteFileHandle;
import com.lexbell.dataprocess.xml.XmlFileHandle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;

@SpringBootTest
class DataProcessApplicationTests {

    @Resource
    MongoTemplate mongoTemplate;

    // 将扫描好的网站数据文件直接入库
    @Test
    void contextLoads() {
        WebsiteFileHandle websiteFileHandle = new WebsiteFileHandle(mongoTemplate);
        try {
            websiteFileHandle.file("D:\\dataCollect\\out", "website");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test() {
        XmlFileHandle xmlFileHandle = new XmlFileHandle();
        try {
            xmlFileHandle.file("D:\\Desktop\\ipscan", "ipscan", mongoTemplate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
