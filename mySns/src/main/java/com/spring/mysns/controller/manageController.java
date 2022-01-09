package com.spring.mysns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class manageController {
    @RequestMapping("/")
    public String snsBoarrd(Model model){
        System.out.println("접근");
        return "index";
    }
}
