package com.project.tda.services;

import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {
    DumpAnalyzerService dumpAnalyzerService;

    public AnalyzerService(){
        dumpAnalyzerService = new DumpAnalyzerService();
    }

    public String analyze(String dump){
        String result = "Test";
        result = dumpAnalyzerService.generateAnalysis(dump);
        return result;
    }
}
