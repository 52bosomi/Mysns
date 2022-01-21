package com.app.mysns.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
// import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.Restful;

// import com.app.mysns.config.MailGunConfig;
// import com.app.mysns.dto.TypeDto;
// import com.app.mysns.service.ManageService;

// import org.apache.ibatis.mapping.Environment;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import groovyjarjarpicocli.CommandLine.Model;


// @RestController
@Controller
public class authController {

    private static final String SPRING_LOGO_IMAGE = "templates/static/images/phodo.jpg";
    private static final String MAIL_SUBJECT = "Registration Confirmation";
  
    // private final MailGunConfig config;
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;
    

    // private final Logger logger = LoggerFactory.getLogger(authController.class);

    public authController(JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    // @RestController    
    @PostMapping("/auth/email/signup")
    public ResponseEntity<Object> register(@RequestBody ClientDto client)
        throws MessagingException, UnsupportedEncodingException {
        
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper email;
            
            email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            email.setTo(client.getUsername());
            email.setSubject("Welcome !!, plz follower sign up URL");
            // email.setFrom(new InternetAddress("no-reply@mysns.info", "MySNS"));

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("email", client.getUsername());
            ctx.setVariable("phodo", SPRING_LOGO_IMAGE);

            // change url for production
            ctx.setVariable("url", "http://localhost:8888/auth/email/check");

            email.setText(this.htmlTemplateEngine.process("email/signup.html", ctx), true);
            
            // 마스코트 이미지 넣기
            ClassPathResource clr = new ClassPathResource(SPRING_LOGO_IMAGE);
            email.addInline("springLogo", clr, "image/jpg");
            
            // 슝슝 전송~
            mailSender.send(mimeMessage);
            
            return new ResponseEntity<>(new Restful().Data("Sucessful send email"), HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(new Restful().Error("failed send email"), HttpStatus.BAD_REQUEST);
        }
    }

    // @Autowired
    // private ManageService service;

    // 인증 관련 처리 컨트롤러

    @RequestMapping("/auth/signup")
    public String signup(Model model){
        System.out.println("client redirect to signup");
        return "signup";
    }

    @RequestMapping("/auth/login")
    public String login(Model model){
        System.out.println("client redirect to login");
        return "login";
    }

    @RequestMapping("/auth/logout")
    public String logout(Model model){
        System.out.println("client redirect to logout");
        return "default";
    }

    @RequestMapping("/auth/phone/send")
    public String phoneSend(Model model){
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/auth/phone/check")
    public String phoneCheck(Model model){
        System.out.println("client redirect to phone");
        return "default";
    }

    @RequestMapping("/auth/email/send")
    public String emailSend(Model model){
        System.out.println("client redirect to email");
        return "default";
    }

    @RequestMapping("/auth/email/check")
    public String emailCheck(Model model){
        System.out.println("client redirect to email");
        return "default";
    }
}
