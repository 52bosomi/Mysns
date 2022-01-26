package com.app.mysns.controller;

import com.app.mysns.dto.SnsTypeDto;
// import com.app.mysns.dto.ClientDto;
import com.app.mysns.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import java.sql.Array;
// import java.sql.Timestamp;
import java.util.ArrayList;
// import java.util.HashMap;


@Controller
@RequestMapping("/") // base is start with root(/)
public class baseController {

    private final Logger logger = LoggerFactory.getLogger(baseController.class);

    @Autowired
    private AuthService service;

    @RequestMapping("/")
    public String snsBoard(Model model){
        // TODO : 로그인시 인증 여부 검증 필요
        // boolean isAuth = false;
        // if(isAuth) {  }
        // return "index";
        return "redirect:/auth/loginPage";
    }


    @RequestMapping("/db")
    public void dbTest(Model model){
        System.out.println("DB 테스트");
        ArrayList<SnsTypeDto> result =  service.dbtest();
        logger.info("db 접속"+result);
    }

    @RequestMapping("/mailLogin")
    public String mailLogin(Model model){
        System.out.println("mailLogin 테스트");
        return "signup";
    }
 /*   @RequestMapping("/mailAccept")
    public void mailAccept(Model model,@RequestParam("username") String username ){
        System.out.println("유저 이름 또는 email : "+username);

        service.mailAccept(username);
    }*/

    @RequestMapping("/test")
    public String test(){
        System.out.println("test ");

        return "signup";
    }

}
