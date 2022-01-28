package com.app.mysns.controller;

import com.app.mysns.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import groovyjarjarpicocli.CommandLine.Model;


// @RestController
@Controller
@RequestMapping("/profile")
public class ProfileController {

    // @Autowired
    // private MailService mailService;
    // private final Logger logger = LoggerFactory.getLogger(profileController.class);

    public ProfileController(TemplateEngine htmlTemplateEngine, MailService mailService) {
        // this.mailService = mailService;
    }

    @GetMapping("")
    public String index(Model model){
        System.out.println("client redirect to profile");
        return "profile";
    }
}
