package com.app.mysns.controller;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.service.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;

@Controller
@RequestMapping("/") // base is start with root(/)
public class BaseController {

    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private ManageService service;

    @RequestMapping("/")
    public String index(Model model){
        // TODO : 로그인시 인증 여부 검증 필요
        // boolean isAuth = false;
        // if(isAuth) {  }
        // return "index";
        return "redirect:/auth/login";
    }

    @RequestMapping("/login")
    public String login(Model model){
        // TODO : 로그인시 인증 여부 검증 필요
        // boolean isAuth = false;
        // if(isAuth) {  }
        // return "index";
        return "redirect:/auth/login";
    }


    @RequestMapping("/db")
    public void dbTest(Model model){
        System.out.println("DB 테스트");
        ArrayList<ClientDto> result =  service.ListClient();
        logger.info("db 접속"+result);
    }

    @RequestMapping("/mailLogin")
    public String mailLogin(Model model){
        System.out.println("mailLogin 테스트");
        return "signup";
    }


    // 기본 보여주는 페이지
    @RequestMapping("/welcome")
    public String welcome(){
        System.out.println("welcome init");
        return "welcome";
    }

    // 기본 보여주는 페이지
    @RequestMapping("/about")
    public String about(){
        System.out.println("about init");
        return "about";
    }

    // 기본 보여주는 페이지
    @RequestMapping("/logout")
    public String logout(){
        System.out.println("logout init");
        return "redirect:/auth/logout";
    }

    // 기본 보여주는 페이지
    @RequestMapping("/connect")
    public String connect(){
        System.out.println("connect init");
        return "connect";
    }
}
