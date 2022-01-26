package com.app.mysns.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class AgentController {
    
    @GetMapping("/ws")
    public String chatGET(){

        System.out.println("@AgentController, chat GET()");

        return "ws";
    }
}