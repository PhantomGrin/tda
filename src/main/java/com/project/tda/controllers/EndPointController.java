package com.project.tda.controllers;

import com.google.gson.Gson;
import com.project.tda.models.ThreadDumps;
import com.project.tda.repositories.ThreadDumpsRepo;
import com.project.tda.security.JwtTokenUtil;
import com.project.tda.services.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class EndPointController {
    AnalyzerService analyzerService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ThreadDumpsRepo threadDumpsRepo;

    EndPointController(AnalyzerService analyzerService){
        this.analyzerService = analyzerService;
    }

    @GetMapping("/")
    public String welcome(){
        return "Welcome";
    }

    @CrossOrigin
    @PostMapping("/analyze")
    public String Analyze(HttpServletRequest request, @RequestParam MultipartFile file){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        String content = "Test";
        try {
            content = new String(file.getBytes(), "UTF-8");
            content = analyzerService.analyze(content, username);
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
            content = analyzerService.analyze(text, "sample");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @CrossOrigin
    @GetMapping("/getanalysis")
    public String getAnalyze(HttpServletRequest request){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        List<ThreadDumps> dumps = threadDumpsRepo.findAllByUsername(username);
        return new Gson().toJson(dumps);
    }

    @CrossOrigin
    @GetMapping("/getresult")
    public String getResult(@RequestParam int id){
        String resultString = "None";
        Optional<ThreadDumps> dump = threadDumpsRepo.findById(id);

        if(dump.isPresent()){
            resultString = dump.get().getResultString();
        }
        return resultString;
    }
}
