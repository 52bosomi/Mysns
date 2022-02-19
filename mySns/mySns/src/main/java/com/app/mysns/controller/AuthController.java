package com.app.mysns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.Restful;
import com.app.mysns.service.SecureUtilsService;
import com.app.mysns.service.MailService;
import com.app.mysns.service.AuthService;
import com.app.mysns.service.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import groovyjarjarpicocli.CommandLine.Model;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static int ttl = 10 * 60 * 1000; // 유효 시간 10분 뒤 만료

    private MailService mailService;
    private AuthService authService;
    private SecureUtilsService secureUtilsService;
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JwtService jwtService;

    public AuthController(TemplateEngine htmlTemplateEngine, MailService mailService, SecureUtilsService secureUtilsService, AuthService authService, RedisTemplate<String, String> redisTemplate, JwtService jwtservice) {
        this.mailService = mailService;
        this.secureUtilsService = secureUtilsService;
        this.authService = authService;
        this.redisTemplate = redisTemplate;
        this.jwtService = jwtservice;
    }

    @PostMapping("/email/signup")
    public ResponseEntity<Object> register(@RequestBody ClientDto client)
            throws MessagingException, UnsupportedEncodingException {

        try {
            // 중복 체크
            if(authService.checkDuplicate(client.getUsername()) != null) {
                return new ResponseEntity<>(new Restful().Error("Exising user"), HttpStatus.BAD_REQUEST);
            }

            // 이메일 발송
            boolean rs = this.mailService.sendEmailSignup(client.getUsername());
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

        ClientDto clientInfo = authService.login(client);
        if(clientInfo.getIdx() < 1) {
            return new ResponseEntity<>(new Restful().Error("Login failed"), HttpStatus.OK);
        }

        // 필요시 나중에 시간 검증용
        // Instant instant = Instant.now();
        // long timeStampSeconds = instant.getEpochSecond();
        // timeStampSeconds += ttl;
        // Timestamp timestamp = new Timestamp(timeStampSeconds);

        String clientIpAddress = (null != request.getHeader("X-FORWARDED-FOR")) ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr();
        clientInfo.setLastAccess(clientIpAddress);
        

        // 접속한 곳 기준으로 쿠키 생성
        // String uuid = "mysns." + secureUtilsService.SHA256(clientIpAddress + "." + timeStampSeconds) + "." + timeStampSeconds;
        // clientInfo.setPassword("");
        // clientInfo
        // ClientDto jwtInfo = new ClientDto();
        // jwtInfo.setUsername(clientInfo.getUsername());
        // jwtInfo.setLastAccess(clientInfo.getLastAccess());
        // jwtInfo.setIdx(clientInfo.getIdx());

        final String uuid = jwtService.generateToken(clientInfo);

        System.out.println("쿠키 토큰 UUID : " + uuid);
        Cookie cookie = new Cookie("mysns_token", uuid);

        System.out.println("쿠키 : " + cookie);

        cookie.setComment("mysns auth code for login");
        // 글로벌
        cookie.setPath("/");
        // 유효시간: 10분
        cookie.setMaxAge(ttl);
        response.addCookie(cookie);
        System.out.println("client redirect to welcome");

        return new ResponseEntity<>(new Restful().Data("login successful"), HttpStatus.OK);
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request,HttpServletResponse response) {
        System.out.println("client redirect to logout");

        // 쿠키 삭제 여부 검증
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie c : cookies) {
                System.out.println(c.getName());

                if(c.getName().startsWith("mysns_token")) {
                    c.setValue(null);
                    c.setSecure(true);
                    c.setHttpOnly(true);
                    c.setMaxAge(-1); // 유효시간을 0으로 설정
                    c.setPath("/");
                }
            }
        }

        // 쿠키 삭제
        Cookie cookie = new Cookie("mysns_token", null);
        cookie.setMaxAge(-1);
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
    public ModelAndView emailCheck(@RequestParam(required = false) String username,
                                   @RequestParam(required = false) String token,
                                   HttpServletResponse response) throws IOException {
        ModelAndView rv = new ModelAndView();
        
        if(token == null || token.length() < 1) { return null; }

        System.out.println("사용자 이메일 : " + username);
        System.out.println("사용자 token : " + token);

        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String result = "";
        try { 
            result = (String)vop.get(token);
            // redisTemplate.delete(token);
            // vop.get(token).delete();
        } catch (Exception e) {}

        System.out.println("메모리 email : " + result);

        //이메일 토큰 사용가능 또는 만료상태로 분기
        if(username.equals(result)) {
            rv.addObject("username", username);
            rv.addObject("isAuth", true);
            rv.setViewName("signup");

            return rv;
        }

        response.setContentType("text/html; charset=euc-kr");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('이메일 유효시간이 만료되었습니다. 다시 시도해주세요.');location.href='/auth/signup'; </script>");
        out.flush();

        return null;
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

        // 회원 가입 성공 후, redis 정보 삭제
        try { 
            redisTemplate.delete(client.getToken());
        } catch (Exception e) {}

        // 회원가입성공 여부 리턴
        if(authService.emailJoin(client)) {
            // 생성된 패스워드는 초기화
            return new ResponseEntity<>(new Restful().Data("Create Sucessful"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new Restful().Error("create user failed"), HttpStatus.BAD_REQUEST);
    }
}
