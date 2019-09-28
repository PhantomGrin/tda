package com.project.tda.controllers;

import com.project.tda.services.AnalyzerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class EndPointController {

    @GetMapping("/")
    public String welcome(){
        return "Welcome";
    }

    @GetMapping("/analyze")
    public String Analyze(@RequestParam MultipartFile file){
        String content = "Test";
        try {
            content = new String(file.getBytes(), "UTF-8");
            AnalyzerService analyzerService = new AnalyzerService();
            content = analyzerService.analyze(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
