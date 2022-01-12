package com.app.mysns.controller;

import com.app.mysns.service.manageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManageController {

    private final Logger LOGGER = LoggerFactory.getLogger(ManageController.class);

    @Autowired
    manageService service;

    @RequestMapping("/")
    public String snsBoard(Model model){
        System.out.println("접근");
        LOGGER.info("LOGGER");
        return "welcome";
    }

}
