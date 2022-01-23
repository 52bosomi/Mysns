package com.app.mysns.controller;

import com.app.mysns.dto.SnsTypeDto;
// import com.app.mysns.dto.ClientDto;
import com.app.mysns.service.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

// import java.sql.Array;
// import java.sql.Timestamp;
import java.util.ArrayList;
// import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/") // base is start with root(/)
public class baseController {

    private final Logger logger = LoggerFactory.getLogger(baseController.class);

    @Autowired
    private ManageService service;

    @RequestMapping("/")
    public String snsBoard(Model model){
        // TODO : 로그인시 인증 여부 검증 필요
        // boolean isAuth = false;
        // if(isAuth) {  }
        // return "index";
        return "redirect:/auth/login";
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
    @RequestMapping("/mailAccept")
    public void mailAccept(Model model,@RequestParam("username") String username ){
        System.out.println("유저 이름 또는 email : "+username);

        service.mailAccept(username);
    }


}
