package com.lexbell.dataprocess.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

/**
 * @author yuntao
 * @date 2022/12/21
 */
public class GetAllFile {
    public static File[] get(String fileFolderPath) {
        File file = new File(fileFolderPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                assert files != null;
                if (files.length == 0) {
                    System.out.println("文件夹中不存在文件");
                    return null;
                } else {
                    return files;
                }
            } else {
                System.out.println("请输入文件夹的路径！");
                return null;
            }
        } else {
            System.out.println("此路径不存在！");
            return null;
        }
    }
}
