package com.app.mysns.controller;

import com.app.mysns.dto.SnSTypeDto;
// import com.app.mysns.dto.ClientDto;
import com.app.mysns.service.ManageService;
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
public class baseController {

    private final Logger logger = LoggerFactory.getLogger(baseController.class);

    @Autowired
    private ManageService service;

    @RequestMapping("/")
    public String snsBoard(Model model){
        // 로그인시 인증 여부 검증
        boolean isAuth = false;
        if(isAuth) {
            return "login";
        }
        return "login";
    }

    @RequestMapping("/db")
    public void dbTest(Model model){
        // System.out.println("DB 테스트");
        // ArrayList<SnSTypeDto> result =  service.dbtest();
        // logger.info("db 접속"+result);

    }

    @RequestMapping("/mailLogin")
    public String mailLogin(Model model){
        System.out.println("mailLogin 테스트");
        return "signup";
    }
    @RequestMapping("/mailAccept")
    public void mailAccept(Model model,@RequestParam("username") String username ){
        // System.out.println("유저 이름 또는 email : "+username);

        // service.mailAccept(username);
    }


}
