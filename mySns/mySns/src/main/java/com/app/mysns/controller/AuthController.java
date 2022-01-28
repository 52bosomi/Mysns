package com.app.mysns.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.Restful;
import com.app.mysns.service.SecureUtilsService;
import com.app.mysns.service.MailService;
import com.app.mysns.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private static int ttl = 10 * 60 * 1000; // 유효 시간 10분 뒤 만료

    @Autowired
    private MailService mailService;
    private AuthService authService;
    private SecureUtilsService secureUtilsService;

    public AuthController(TemplateEngine htmlTemplateEngine, MailService mailService, SecureUtilsService secureUtilsService, AuthService authService) {
        this.mailService = mailService;
        this.secureUtilsService = secureUtilsService;
        this.authService = authService;
    }

    @PostMapping("/email/signup")
    public ResponseEntity<Object> register(@RequestBody ClientDto client)
            throws MessagingException, UnsupportedEncodingException {

        try {
            // 중복 체크
            if(authService.checkDuplicate(client.getUsername()) != null) {
                return new ResponseEntity<>(new Restful().Error("Exising user"), HttpStatus.BAD_REQUEST);
            }

            // 
            boolean rs = this.mailService.SendEmailSignup(client.getUsername());
            return rs ? new ResponseEntity<>(new Restful().Data("Sucessful send email"), HttpStatus.OK) : new ResponseEntity<>(new Restful().Data("Sending failed"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // TODO : 로깅 남겨야 함
            return new ResponseEntity<>(new Restful().Error("failed send email"), HttpStatus.BAD_REQUEST);
        }
    }

    // 인증 관련 처리 컨트롤러

    @RequestMapping("/signup")
    public ModelAndView signup(Model model) {
        System.out.println("client redirect to signup");

        ModelAndView rv = new ModelAndView("signup");
        rv.addObject("isAuth", false);
        return rv;
    }

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/login" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody ClientDto client, HttpServletRequest request, HttpServletResponse response) {

        // 로그인일경우 새로운 쿠키 생성
        // 쿠키로 로그인 여부는 보안상 좋지 않음, 지향하지 않는 추세

        boolean isLoginSuccess = authService.login(client);
        if(!isLoginSuccess) {
            return new ResponseEntity<>(new Restful().Error("Login failed"), HttpStatus.OK);
        }
        
        // 필요시 나중에 시간 검증용
        Instant instant = Instant.now();
        long timeStampSeconds = instant.getEpochSecond();
        timeStampSeconds += ttl;
        // Timestamp timestamp = new Timestamp(timeStampSeconds);

        String clientIpAddress = (null != request.getHeader("X-FORWARDED-FOR")) ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr();

        // 접속한 곳 기준으로 쿠키 생성
        String uuid = "mysns." + secureUtilsService.SHA256(clientIpAddress + "." + timeStampSeconds) + "." + timeStampSeconds;

        System.out.println("쿠키 토큰 UUID : " + uuid);
        Cookie cookie = new Cookie("mysns_uuid", uuid);
        
        System.out.println("쿠키 : " + cookie);

        cookie.setComment("mysns auth code for login");
        // 글로벌
        cookie.setPath("/"); 
        // 유효시간: 30분
        cookie.setMaxAge(ttl);
        response.addCookie(cookie);
        System.out.println("client redirect to welcome");

        return new ResponseEntity<>(new Restful().Data("login successful"), HttpStatus.OK);
    }

    @RequestMapping("/")
    public String login(Model model, HttpServletResponse response,
                        @RequestParam("username") String username,
                        @RequestParam("pw") String pw) throws IOException {
        System.out.println("client redirect to login");

        System.out.println("로그인 시도: "+username+"/"+pw);
        // boolean same = authService.login(username, response);
        // System.out.println("로그인 성공 : " + same);
        // if(same) {
        //     return "redirect:/auth/dashboard";
        // }
        return "redirect:/auth/login";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request,HttpServletResponse response) {
        System.out.println("client redirect to logout");

        // 쿠키 삭제 여부 검증
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie c : cookies) {
                System.out.println(c.getName());

                if(c.getName().startsWith("mysns.")) {
                    c.setValue(null);
                    c.setSecure(true);
                    c.setHttpOnly(true);
                    c.setMaxAge(0); // 유효시간을 0으로 설정
                    c.setPath("/");
                }
            }
        }
        
        // 쿠키 삭제
        Cookie cookie = new Cookie("mysns_uuid", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //add cookie to response
        response.addCookie(cookie);

        return "redirect:/auth/login";
    }

    @RequestMapping("/phone/send")
    public String phoneSend(Model model) {
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/phone/check")
    public String phoneCheck(Model model) {
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/email/send")
    public String emailSend(Model model) {
        System.out.println("client redirect to email");
        return "default";
    }

    @RequestMapping(value = "/email/check" , method = RequestMethod.GET)
    public ModelAndView emailCheck(@RequestParam(required = false) String username) {
        ModelAndView rv = new ModelAndView();

        System.out.println("사용자 이메일 : "+username);
        System.out.println("client redirect to email");

        rv.addObject("username", username);
        rv.addObject("isAuth", true);
        rv.setViewName("signup");
        return rv;
    }

    @RequestMapping(value = "/email/join" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity emailJoin(@RequestBody ClientDto client) {
        // 여기서 패스워드는 1차 암호화 되어서 전송됨
        logger.info("회원가입 정보");
        logger.info("username : " + client.getUsername());
        logger.info("password : " + client.getPassword());
        logger.info("name : " + client.getName());
        logger.info("phone : " + client.getPhone());
        System.out.println("client redirect to login");

        // 중복 체크
        if(authService.checkDuplicate(client.getUsername()) != null) {
            return new ResponseEntity<>(new Restful().Error("Exising user"), HttpStatus.BAD_REQUEST);
        }
        
        // 회원가입성공 여부 리턴
        if(authService.emailJoin(client)) {
            // 생성된 패스워드는 초기화
            return new ResponseEntity<>(new Restful().Data("Create Sucessful"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Restful().Error("create user failed"), HttpStatus.BAD_REQUEST);
    }
}
