package com.project.tda.services;

import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {
    BasicAnalyzerService basicAnalyzerService;
    FurtherAnalyzerService furtherAnalyzerService;

    public AnalyzerService(){
        basicAnalyzerService = new BasicAnalyzerService();
        furtherAnalyzerService = new FurtherAnalyzerService();
    }

    public String analyze(String dump){
        String result = "Test";
        result = basicAnalyzerService.generateAnalysis(dump);
        furtherAnalyzerService.stateviseSummary(result);
        furtherAnalyzerService.deamonSummary(result);
        furtherAnalyzerService.stackLengthSummary(result);
        furtherAnalyzerService.identicalStackTrace(result);
        return result;
    }
}
