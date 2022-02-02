package com.app.mysns.controller;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.service.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping("/connect") // base is start with root(/)

public class ConnectController {

    private final Logger logger = LoggerFactory.getLogger(ConnectController.class);

    @Autowired
    private ManageService service;

    @RequestMapping("/")
    public String index(Model model){
        // 연동 페이지 메인으로 현재 연동된 사이트 정보를 리턴
        
        // 모델 받아서 넘기기
        model.addAttribute("isFacebook", false);
        model.addAttribute("isGoogle", false);
        model.addAttribute("isNaver", true);

        return "connect";
    }
}
