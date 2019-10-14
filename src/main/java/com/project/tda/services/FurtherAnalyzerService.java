package com.project.tda.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FurtherAnalyzerService {

    Map<String,List<String>> deamons = new HashMap<>();
    Map<String,List<String>> stackLength = new HashMap<>();
    Map<String,List<String>> threadStatus = new HashMap<>();

    Map<String,ArrayList<Object>> identicleStackTrace = new HashMap<>();
    Map<String,ArrayList<Object>> unidenticleStackTrace = new HashMap<>();
    Map<String,String> stackTraceMap = new HashMap<String, String>();

    public String stateviseSummary(String basicAnalysis){
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
        System.out.println(threadStatus);
        String results = "";
        return results;
    }

    public String deamonSummary(String basicAnalysis){
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
        System.out.println(deamons);
        String results = "";
        return results;
    }

    public String stackLengthSummary(String basicAnalysis){
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

        System.out.println(stackLength);

        String results = "";
        return results;
    }

    //TODO: Check how to find identicle stack trace
    public String identicalStackTrace(String basicAnalysis){
        JsonArray basicAnalysisArray = new JsonParser().parse(basicAnalysis).getAsJsonArray();

        for (int i = 0; i < basicAnalysisArray.size(); i++) {
            JsonObject currentObject = basicAnalysisArray.get(i).getAsJsonObject();
            JsonArray frames = currentObject.get("frames").getAsJsonArray();
            ArrayList identicleStackIDs = new ArrayList();

            for(int j=i+1; j < basicAnalysisArray.size(); j++){
                JsonObject currentObject2 = basicAnalysisArray.get(j).getAsJsonObject();
                JsonArray frames2 = currentObject2.get("frames").getAsJsonArray();

                if(frames.toString().equals(frames2.toString())){
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
            stackTraceMap.put(key, frames.toString());

            if(identicleStackIDs.isEmpty()){
                identicleStackIDs.add(currentObject.get("id").getAsString());
                unidenticleStackTrace.put(key, identicleStackIDs);
            }else{
                identicleStackTrace.put(key, identicleStackIDs);
            }

        }
        System.out.println("#######################");
        System.out.println(identicleStackTrace);
        System.out.println(unidenticleStackTrace);
        String results = "";
        return results;
    }
    public String culpritReport(String basicAnalysis){
        JsonObject basicAnalysisObject = new JsonParser().parse(basicAnalysis).getAsJsonObject();
        String results = "";
        return results;
    }


}
