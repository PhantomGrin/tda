package com.project.tda.services;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyzerService {
    BasicAnalyzerService basicAnalyzerService;
    FurtherAnalyzerService furtherAnalyzerService;

    public AnalyzerService(){
        basicAnalyzerService = new BasicAnalyzerService();
        furtherAnalyzerService = new FurtherAnalyzerService();
    }

    public String analyze(String dump){
        String result;
        Map<String, Object> message = new HashMap<>();
        ArrayList<SingleThreadAnalyzerService> basicAnalysis = basicAnalyzerService.generateAnalysis(dump);
        Map<String, SynchronizerAnalysisService> syncMap = basicAnalyzerService.getSync();

        HashMap<String, SingleThreadAnalyzerService> threadAnalysisMap = new HashMap<>();
        for (SingleThreadAnalyzerService singleThread : basicAnalysis) {
            threadAnalysisMap.put(singleThread.id, singleThread);
        }

        result = new Gson().toJson(basicAnalysis);

        message.put("thread_array", basicAnalysis);
        message.put("sync_map", syncMap);
        message.put("thread_map", threadAnalysisMap);
        message.put("state_vise", furtherAnalyzerService.stateviseSummary(result));
        message.put("deamons", furtherAnalyzerService.deamonSummary(result));
        message.put("stack_length", furtherAnalyzerService.stackLengthSummary(result));
        message.put("identicle_stack_report", furtherAnalyzerService.identicalStackTraceSummary(result));
        message.put("identicle_stack_map", furtherAnalyzerService.getStackTraceMap());

        result =  new Gson().toJson(message);
        basicAnalysis.clear();
        return result;
    }
}
