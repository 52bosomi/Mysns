package com.app.mysns.controller;

// import com.app.mysns.dto.ClientDto;
// import com.app.mysns.service.ManageService;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
// import java.util.ArrayList;

@Controller
@RequestMapping("/") // base is start with root(/)
public class BaseController {

    // private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    // @Autowired
    // private ManageService service;

    // public side

    @RequestMapping("/")
    public String index(Model model){
        return "redirect:/auth/login";
    }

    @RequestMapping("/login")
    public String login(Model model){
        return "redirect:/auth/login";
    }

    // private side
    @RequestMapping("/welcome")
    public String welcome(){
        System.out.println("welcome init");
        return "welcome";
    }

    @RequestMapping("/about")
    public String about(){
        System.out.println("about init");
        return "about";
    }

    @RequestMapping("/logout")
    public String logout(){
        System.out.println("logout init");
        return "redirect:/auth/logout";
    }

    @RequestMapping("/connect")
    public String connect(){
        System.out.println("connect init");
        return "connect";
    }
}
