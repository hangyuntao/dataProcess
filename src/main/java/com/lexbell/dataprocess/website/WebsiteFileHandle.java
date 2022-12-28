package com.lexbell.dataprocess.website;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * @author yuntao
 * @date 2022/12/21
 */
public class WebsiteFileHandle {
    @Resource
    MongoTemplate mongoTemplate;

    public WebsiteFileHandle(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public void file(String fileFolderPath, String collectionName) throws IOException {
        File file = new File(fileFolderPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                assert files != null;
                if (files.length > 0) {
                    for (File f : files) {
                        BufferedReader reader = new BufferedReader(new FileReader(f));
                        String line;
                        StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        JSONObject json = JSON.parseObject(sb.toString());

                        json.put("domainCount", JSON.parseObject(json.getString("domainCount").replaceAll("\\.", "点")));
                        URL url = new URL(json.getString("url"));
                        json.put("hostname", url.getHost());
                        Query query = Query.query(Criteria.where("hostname").is(url.getHost()));
                        if (mongoTemplate.find(query, JSONObject.class, collectionName).size() == 0) {
                            mongoTemplate.insert(json, collectionName);
                        } else {
                            System.out.println(url.getHost());
                        }
//                        mongoTemplate.insert(json, collectionName);
                    }
                } else {
                    System.out.println("文件夹中不存在文件");
                }
            } else {
                System.out.println("请输入文件夹的路径！");
            }
        } else {
            System.out.println("此路径不存在！");
        }
    }
}
