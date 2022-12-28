package com.lexbell.dataprocess.xml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lexbell.dataprocess.utils.GetAllFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author yuntao
 * @date 2022/12/21
 */
public class XmlFileHandle {
    private static final String nmapjson1 = "[\n" +
            "  {\n" +
            "    \"name\": \"up\",\n" +
            "    \"num\": \"single\",\n" +
            "    \"type\": \"text\",\n" +
            "    \"xpath\": \"//runstats/hosts/@up\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"ports\",\n" +
            "    \"num\": \"multi\",\n" +
            "    \"object\": [\n" +
            "      {\n" +
            "        \"name\": \"port\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"@portid\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"protocol\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"@protocol\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"service\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"service/@name\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"state\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"state/@state\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"product\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"service/@product\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"version\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"service/@version\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"cve\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"script/@output\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"type\": \"object\",\n" +
            "    \"xpath\": \"//ports/port\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"osmatchs\",\n" +
            "    \"num\": \"multi\",\n" +
            "    \"type\": \"object\",\n" +
            "    \"xpath\": \"//os/osmatch\",\n" +
            "    \"object\": [\n" +
            "      {\n" +
            "        \"name\": \"name\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"@name\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"accuracy\",\n" +
            "        \"num\": \"single\",\n" +
            "        \"type\": \"text\",\n" +
            "        \"xpath\": \"@accuracy\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"osclass\",\n" +
            "        \"num\": \"multi\",\n" +
            "        \"type\": \"object\",\n" +
            "        \"xpath\": \"osclass\",\n" +
            "        \"object\": [\n" +
            "          {\n" +
            "            \"name\": \"type\",\n" +
            "            \"num\": \"single\",\n" +
            "            \"type\": \"text\",\n" +
            "            \"xpath\": \"@type\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"vendor\",\n" +
            "            \"num\": \"single\",\n" +
            "            \"type\": \"text\",\n" +
            "            \"xpath\": \"@vendor\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"osfamily\",\n" +
            "            \"num\": \"single\",\n" +
            "            \"type\": \"text\",\n" +
            "            \"xpath\": \"@osfamily\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";
    private static final List<NmapParamAnalysis> NMAP_PARAM_ANALYSIS = JSON.parseArray(nmapjson1,
            NmapParamAnalysis.class);


    public void file(String fileFolderPath, String collectionName, MongoTemplate mongoTemplate) throws IOException {
        File[] files = GetAllFile.get(fileFolderPath);
        if (files != null) {
            for (File file : files) {
                try {
                    JSONObject jsonObject = analysis(file, NMAP_PARAM_ANALYSIS);
                    jsonObject.put("ip", file.getName().substring(0, file.getName().indexOf(".xml")));
//                    System.out.println(jsonObject);
                    if (jsonObject != null) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("ports");
                        JSONArray jsonArray2 = jsonObject.getJSONArray("osmatchs");
                        System.out.println(jsonObject.getString("ip"));
                        for (Object p : jsonArray1) {
                            JSONObject port = (JSONObject) p;
                            if (port.get("cve") != null) {
                                String cve = port.getString("cve");
                                if (!cve.contains("MITRE CVE - https://cve.mitre.org:")) {
                                    port.put("cve", null);
                                    continue;
                                }
                                cve = cve.substring(cve.indexOf("MITRE CVE - https://cve.mitre.org:") + "MITRE CVE - https://cve.mitre.org:".length() + 1, cve.indexOf("SecurityFocus - https://www.securityfocus.com/bid/"));
                                String[] ss = cve.split("\n");

                                JSONArray cves = new JSONArray();
                                for (String s : ss) {
                                    String cveNum = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
                                    String des = s.substring(s.indexOf("]") + 1).trim();
                                    JSONObject cveJson = new JSONObject();
                                    cveJson.put("num", cveNum);
                                    cveJson.put("description", des);
                                    cves.add(cveJson);
                                }
                                port.put("cve", cves);
                            }
                        }
                    }
                    Query query = Query.query(Criteria.where("ip").is(file.getName().substring(0, file.getName().indexOf(".xml"))));
//                    System.out.println(mongoTemplate.find(query, JSONObject.class, collectionName).size());
                    if (mongoTemplate.find(query, JSONObject.class, collectionName).size() == 0) {
                        mongoTemplate.insert(jsonObject, collectionName);
                    }else {
                        System.out.println(jsonObject.getString("ip"));
//                        mongoTemplate.save(jsonObject, collectionName);
                    }
//                    return jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static JSONObject analysis(File file, List<NmapParamAnalysis> analysises) {
        SAXReader saxReader = new SAXReader();

        Document document = null;
        try {
            document = saxReader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        Node rootNode = document.selectSingleNode("/");

        try {
            return analysisObject(rootNode, analysises);
        } catch (Exception e) {
            return null;
        }
    }

    private static JSONObject analysisObject(Node parentNode, List<NmapParamAnalysis> paramAnalysis) {
        JSONObject jsonObject = new JSONObject();

        for (NmapParamAnalysis analysis : paramAnalysis) {
            if (NmapJobParam.analysis_num_single.equals(analysis.getNum())) {
                if (NmapJobParam.analysis_type_text.equals(analysis.getType())) {
                    Node node = parentNode.selectSingleNode(analysis.getXpath());
                    if (node != null) {
                        jsonObject.put(analysis.getName(), node.getText());
                    }
                } else if (NmapJobParam.analysis_type_object.equals(analysis.getType())) {
                    Node node = parentNode.selectSingleNode(analysis.getXpath());
                    if (node != null) {
                        jsonObject.put(analysis.getName(), analysisObject(node, analysis.getObject()));
                    }
                }
            } else if (NmapJobParam.analysis_num_multi.equals(analysis.getNum())) {
                jsonObject.put(analysis.getName(), analysisArray(parentNode, analysis));
            }

        }
        return jsonObject;
    }

    private static JSONArray analysisArray(Node parentNode, NmapParamAnalysis analysis) {
        JSONArray jsonArray = new JSONArray();
        if (NmapJobParam.analysis_type_text.equals(analysis.getType())) {
            List<Node> nodes = parentNode.selectNodes(analysis.getXpath());
            for (Node node : nodes) {
                jsonArray.add(node.getText());
            }
        } else if (NmapJobParam.analysis_type_object.equals(analysis.getType())) {
            List<Node> nodes = parentNode.selectNodes(analysis.getXpath());
            for (Node node : nodes) {
                jsonArray.add(analysisObject(node, analysis.getObject()));
            }
        }

        return jsonArray;
    }
}
