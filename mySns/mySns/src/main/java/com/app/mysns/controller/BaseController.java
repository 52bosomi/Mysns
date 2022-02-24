package com.app.mysns.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.SyncSiteDto;
import com.app.mysns.service.JwtService;
import com.app.mysns.service.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/") // base is start with root(/)
public class BaseController {

    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private ManageService service;
    @Autowired
    private JwtService jwtService;

    @RequestMapping("/")
    public String index(Model model){
        return "redirect:/auth/login";
    }

    @RequestMapping("/login")
    public String login(Model model){
        return "redirect:/auth/login";
    }

    // 기본 보여주는 페이지
    @RequestMapping("/welcome")
    public ModelAndView welcome(HttpServletRequest httpServletRequest){
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_token");
        if(cookie == null && cookie.getValue() == null) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/auth/login");
            return mv;
        };
        String username = jwtService.getUsernameFromToken(cookie.getValue());
        // String[] data = username.split("\\|");

        // if(data.length < 2) {
        //     ModelAndView mv = new ModelAndView();
        //     mv.setViewName("redirect:/auth/login");
        //     return mv;
        // }

        System.out.println("welcome init");
        ClientDto clientdto = service.findUser(username);
        int facebook = service.summarySyncSite(new SyncSiteDto(clientdto.getIdx(), 1));
        int google = service.summarySyncSite(new SyncSiteDto(clientdto.getIdx(), 2));
        int insta = service.summarySyncSite(new SyncSiteDto(clientdto.getIdx(), 3));
        int naver = service.summarySyncSite(new SyncSiteDto(clientdto.getIdx(), 4));

        logger.info("페이스북 : " + facebook + "/" + google + "/" + insta + "/" + naver);

        ModelAndView mav = new ModelAndView();
        mav.addObject("facebook",facebook);
        mav.addObject("google",google);
        mav.addObject("insta",insta);
        mav.addObject("naver",naver);

        mav.setViewName("welcome");

        return mav;
    }

    // 기본 보여주는 페이지
    @RequestMapping("/about")
    public String about(){
        System.out.println("about init");
        return "about_sample";
    }

    // 기본 보여주는 페이지
    @RequestMapping("/logout")
    public String logout(){
        System.out.println("logout init");
        return "redirect:/auth/logout";
    }

    // 기본 보여주는 페이지
    // @RequestMapping("/connect")
    // public String connect(){
    //     System.out.println("connect init");
    //     return "connect";
    // }
}
