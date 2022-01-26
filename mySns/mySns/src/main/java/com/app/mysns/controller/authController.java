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
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private MailService mailService;
    // private final Logger logger = LoggerFactory.getLogger(authController.class);
    public AuthController(TemplateEngine htmlTemplateEngine, MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/email/signup")
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

    @RequestMapping("/signup")
    public String signup(Model model){
        System.out.println("client redirect to signup");
        return "signup";
    }

    @RequestMapping("/login")
    public String login(Model model){
        System.out.println("client redirect to login");
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(Model model){
        System.out.println("client redirect to logout");
        return "default";
    }

    @RequestMapping("/phone/send")
    public String phoneSend(Model model){
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/phone/check")
    public String phoneCheck(Model model){
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/email/send")
    public String emailSend(Model model){
        System.out.println("client redirect to email");
        return "default";
    }

    @RequestMapping("/email/check")
    public String emailCheck(Model model){
        System.out.println("client redirect to email");
        return "default";
    }
}
