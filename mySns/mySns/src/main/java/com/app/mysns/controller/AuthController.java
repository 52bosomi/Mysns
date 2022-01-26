package com.app.mysns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.Restful;
import com.app.mysns.service.MailService;

import com.app.mysns.service.AuthService;
import com.app.mysns.service.UserSha256;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import groovyjarjarpicocli.CommandLine.Model;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthService authService;


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


    // 인증 관련 처리 컨트롤러

    @RequestMapping("/signup")
    public String signup(Model model){
        System.out.println("client redirect to signup");
        return "signup";
    }

    @RequestMapping(value = "/loginPage")
    public String loginPage(Model model,HttpServletRequest request){
        //쿠키삭제여부보기
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                System.out.println(cookie.getName());

            }
        }
        System.out.println("client redirect to login");
        return "login";
    }

    //웰컴으로 가는 controller 여기서 쿠키가 없으면 다른데로 보내준다.
    @RequestMapping("/dashboard")
    public String dashboard(HttpServletRequest request){
        System.out.println("대시보드 컨트롤러 진입");
        Cookie[] cookies= request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                System.out.println("쿠키이름"+name);
                if (name.equals("userName")) {
                    //쿠키가있으면 welcome 페이지로
                    return "welcome";
                }
            }
        }
        //쿠키가 없으면 로그인 페이지로
        return "redirect:/auth/loginPage";
    }

    @RequestMapping("/login")
    public String login(Model model, HttpServletResponse response,
                        @RequestParam("username") String username,
                        @RequestParam("pw") String pw) throws IOException {
        System.out.println("client redirect to login");
        System.out.println("로그인 시도: "+username+"/"+pw);
        boolean same = authService.login(username,pw,response);
        System.out.println("로그인 성공 : " + same);
        if(same){
            return "redirect:/auth/dashboard";
        }
        return "redirect:/auth/loginPage";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request,HttpServletResponse response){
        System.out.println("client redirect to logout");
        //쿠키삭제
        Cookie[] cookies = request.getCookies(); // 모든 쿠키의 정보를 cookies에 저장
        if (cookies != null) { // 쿠키가 한개라도 있으면 실행
            for (int i = 0; i < cookies.length; i++) {
                cookies[i].setValue(null);
                cookies[i].setMaxAge(0); // 유효시간을 0으로 설정
                cookies[i].setPath("/");
                response.addCookie(cookies[i]); // 응답에 추가하여 만료시키기.
            }
        }
        return "redirect:/auth/loginPage";
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

    @RequestMapping(value = "/email/check" , method = RequestMethod.GET)
    public ModelAndView emailCheck(@RequestParam("email") String email){
        ModelAndView mav = new ModelAndView();
        System.out.println("사용자 이메일"+email);
        System.out.println("client redirect to email");
        mav.addObject("email",email);
        mav.setViewName("signupForm");
        return mav;
    }

    @RequestMapping(value = "/email/join" , method = RequestMethod.POST)
    public String emailJoin(Model model,
                            @RequestParam("userEmail")String userEmail,
                            @RequestParam("username")String username,
                            @RequestParam("pw")String pw){
        logger.info("회원가입 정보 : "+ userEmail+"/"+username+"/"+pw);
        System.out.println("client redirect to login");

        int success = authService.emailJoin(userEmail,username,pw);
        if(success > 0){//회원가입성공
            System.out.println("회원가입 성공");
            return "login";
        }//회원가입실패
        return "signup";
    }
}
