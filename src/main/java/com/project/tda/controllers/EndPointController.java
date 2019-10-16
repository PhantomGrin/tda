package com.project.tda.controllers;

import com.project.tda.services.AnalyzerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class EndPointController {
    AnalyzerService analyzerService;
    EndPointController(){
        analyzerService = new AnalyzerService();
    }

    @GetMapping("/")
    public String welcome(){
        return "Welcome";
    }

    @CrossOrigin
    @PostMapping("/analyze")
    public String Analyze(@RequestParam MultipartFile file){
        String content = "Test";
        try {
            content = new String(file.getBytes(), "UTF-8");
            content = analyzerService.analyze(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    @CrossOrigin
    @PostMapping("/trysample")
    public String Analyze(@RequestParam String text){
        String content = "Test";
        try {
            content = analyzerService.analyze(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
