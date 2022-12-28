package com.lexbell.dataprocess.xml;

import java.util.List;

public class NmapParamAnalysis {
    
    private String name;
    
    private String num;
    
    private String type;
    
    private String xpath;
    
    private List<NmapParamAnalysis> object;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getNum() {
        return num;
    }
    
    public void setNum(String num) {
        this.num = num;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getXpath() {
        return xpath;
    }
    
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
    
    public List<NmapParamAnalysis> getObject() {
        return object;
    }
    
    public void setObject(List<NmapParamAnalysis> object) {
        this.object = object;
    }
    
}
