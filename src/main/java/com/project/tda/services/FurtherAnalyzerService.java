package com.project.tda.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FurtherAnalyzerService {

    private Map<String,List<String>> deamons = new HashMap<>();
    private Map<String,List<String>> stackLength = new HashMap<>();
    private Map<String,List<String>> threadStatus = new HashMap<>();

    private Map<String,List<String>> identicleStackTrace = new HashMap<>();

    private Map<String, JsonArray> stackTraceMap = new HashMap<String, JsonArray>();

    public Map<String,List<String>> stateviseSummary(String basicAnalysis){
        threadStatus.clear();
        JsonArray basicAnalysisArray = new JsonParser().parse(basicAnalysis).getAsJsonArray();

        for (int i = 0; i < basicAnalysisArray.size(); i++) {
            JsonObject currentObject = basicAnalysisArray.get(i).getAsJsonObject();
            String status = currentObject.get("getStatus").getAsString();
            if (this.threadStatus.containsKey(status)) {
                List allthreads = threadStatus.get(status);
                allthreads.add(currentObject.get("id").getAsString());
                threadStatus.put(status, allthreads);
            } else {
                List<String> allthreads = new ArrayList();
                allthreads.add(currentObject.get("id").getAsString());
                threadStatus.put(status, allthreads);
            }
        }
        return threadStatus;
    }

    public Map<String,List<String>> deamonSummary(String basicAnalysis){
        deamons.clear();
        JsonArray basicAnalysisArray = new JsonParser().parse(basicAnalysis).getAsJsonArray();

        for (int i = 0; i < basicAnalysisArray.size(); i++) {
            JsonObject currentObject = basicAnalysisArray.get(i).getAsJsonObject();
            String daemon = currentObject.get("daemon").getAsString();
            if (this.deamons.containsKey(daemon)) {
                List<String> allthreads = deamons.get(daemon);
                allthreads.add(currentObject.get("id").getAsString());
                deamons.put(daemon, allthreads);
            } else {
                List<String> allthreads = new ArrayList();
                allthreads.add(currentObject.get("id").getAsString());
                deamons.put(daemon, allthreads);
            }
        }
        return deamons;
    }

    public Map<String, List<String>> stackLengthSummary(String basicAnalysis){
        stackLength.clear();
        JsonArray basicAnalysisArray = new JsonParser().parse(basicAnalysis).getAsJsonArray();

        for (int i = 0; i < basicAnalysisArray.size(); i++) {
            JsonObject currentObject = basicAnalysisArray.get(i).getAsJsonObject();
            JsonArray frames = currentObject.get("frames").getAsJsonArray();
            String key = "none";
            if(frames.size()<=10){
                key = "slength_less10";
            }else if(frames.size()<=100){
                key = "slength_to100";
            }else if(frames.size()<=500){
                key = "slength_to500";
            }else {
                key = "slength_more500";
            }

            if (this.stackLength.containsKey(key)) {
                List<String> allthreads = stackLength.get(key);
                allthreads.add(currentObject.get("id").getAsString());
                stackLength.put(key, allthreads);
            } else {
                List<String> allthreads = new ArrayList();
                allthreads.add(currentObject.get("id").getAsString());
                stackLength.put(key, allthreads);
            }
        }
        return stackLength;
    }

    public Map<String, List<String>> identicalStackTraceSummary(String basicAnalysis){
        JsonArray basicAnalysisArray = new JsonParser().parse(basicAnalysis).getAsJsonArray();

        for (int i = 0; i < basicAnalysisArray.size(); i++) {
            JsonObject currentObject = basicAnalysisArray.get(i).getAsJsonObject();
            JsonArray frames = currentObject.get("frames").getAsJsonArray();
            ArrayList identicleStackIDs = new ArrayList();

            for(int j=i+1; j < basicAnalysisArray.size(); j++){
                JsonObject currentObject2 = basicAnalysisArray.get(j).getAsJsonObject();
                JsonArray frames2 = currentObject2.get("frames").getAsJsonArray();

                String frames_str = frames.toString();
                String frames2_str = frames2.toString();

                frames_str = frames_str.replaceAll("\\P{L}", "");
                frames2_str = frames2_str.replaceAll("\\P{L}", "");

                if(frames_str.equals(frames2_str)){
                    if(identicleStackIDs.isEmpty()){
                        identicleStackIDs.add(currentObject.get("id").getAsString());
                    }
                    identicleStackIDs.add(currentObject2.get("id").getAsString());
                    basicAnalysisArray.remove(j);
                    j--;
                }
            }

            String key;
            if(frames.size()==0){
                key = "EMPTY";
            }else{
                key = "ST"+(String.valueOf(i+1));
            }
            stackTraceMap.put(key, frames);

            if(identicleStackIDs.isEmpty()){
                identicleStackIDs.add(currentObject.get("id").getAsString());
                identicleStackTrace.put(key, identicleStackIDs);
            }else{
                identicleStackTrace.put(key, identicleStackIDs);
            }

        }
        return identicleStackTrace;
    }

    public Map<String, JsonArray> getStackTraceMap() {
        return stackTraceMap;
    }
}
