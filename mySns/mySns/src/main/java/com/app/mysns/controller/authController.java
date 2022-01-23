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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import groovyjarjarpicocli.CommandLine.Model;


// @RestController
@Controller
public class authController {

    @Autowired
    private MailService mailService;
    // private final Logger logger = LoggerFactory.getLogger(authController.class);

    public authController(TemplateEngine htmlTemplateEngine, MailService mailService) {
        this.mailService = mailService;
    }

    // @RestController    
    @PostMapping("/auth/email/signup")
    public ResponseEntity<Object> register(@RequestBody ClientDto client)
        throws MessagingException, UnsupportedEncodingException {

        try {
            boolean rs = this.mailService.SendEmailSignup(client.getUsername());
            return rs ? new ResponseEntity<>(new Restful().Data("Sucessful send email"), HttpStatus.OK) : new ResponseEntity<>(new Restful().Data("Sending failed"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // TODO : 로깅 남겨야 함
            return new ResponseEntity<>(new Restful().Error("failed send email"), HttpStatus.BAD_REQUEST);
        }
    }

    // @Autowired
    // private ManageService service;

    // 인증 관련 처리 컨트롤러

    @RequestMapping("/auth/signup")
    public String signup(Model model){
        System.out.println("client redirect to signup");
        return "signup";
    }

    @RequestMapping("/auth/login")
    public String login(Model model){
        System.out.println("client redirect to login");
        return "login";
    }

    @RequestMapping("/auth/logout")
    public String logout(Model model){
        System.out.println("client redirect to logout");
        return "default";
    }

    @RequestMapping("/auth/phone/send")
    public String phoneSend(Model model){
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/auth/phone/check")
    public String phoneCheck(Model model){
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/auth/email/send")
    public String emailSend(Model model){
        System.out.println("client redirect to email");
        return "default";
    }

    @RequestMapping("/auth/email/check")
    public String emailCheck(Model model){
        System.out.println("client redirect to email");
        return "default";
    }
}
