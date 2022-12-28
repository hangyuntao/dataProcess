package com.lexbell.dataprocess.xml;

import java.util.List;

public class NmapJobParam {
    
    public static final String analysis_num_single = "single";
    public static final String analysis_num_multi = "multi";
    
    public static final String analysis_type_text = "text";
    public static final String analysis_type_object = "object";
    
    private List<String> args;
    
    private List<NmapParamAnalysis> analysis;
    
    private int interval;
    
    public List<String> getArgs() {
        return args;
    }
    
    public void setArgs(List<String> args) {
        this.args = args;
    }
    
    public List<NmapParamAnalysis> getAnalysis() {
        return analysis;
    }
    
    public void setAnalysis(List<NmapParamAnalysis> analysis) {
        this.analysis = analysis;
    }
    
    public int getInterval() {
        return interval;
    }
    
    public void setInterval(int interval) {
        this.interval = interval;
    }
    
}
