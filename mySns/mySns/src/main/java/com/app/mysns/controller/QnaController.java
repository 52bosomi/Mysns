package com.app.mysns.controller;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.Restful;
import com.app.mysns.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.TemplateEngine;
import groovyjarjarpicocli.CommandLine.Model;


// @RestController
@Controller
@RequestMapping("/qna")
public class QnaController {

    // @Autowired
    // private MailService mailService;
    // private final Logger logger = LoggerFactory.getLogger(qnaController.class);

    public QnaController(TemplateEngine htmlTemplateEngine, MailService mailService) {
        // this.mailService = mailService;
    }

    @GetMapping("")
    public String index(Model model){
        System.out.println("client redirect to signup");
        return "qna";
    }
}
