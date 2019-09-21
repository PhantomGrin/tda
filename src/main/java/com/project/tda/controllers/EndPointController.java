package com.project.tda.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndPointController {

    @GetMapping("/")
    public String welcome(){
        return "Welcome";
    }
}
