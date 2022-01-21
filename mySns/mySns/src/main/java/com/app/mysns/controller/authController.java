package com.app.mysns.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
// import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    private static final String TEMPLATE_NAME = "signup";
    private static final String SPRING_LOGO_IMAGE = "templates/images/spring.png";
    private static final String PNG_MIME = "image/png";
    private static final String MAIL_SUBJECT = "Registration Confirmation";
  
    // private final MailGunConfig config;
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    // private final Logger logger = LoggerFactory.getLogger(authController.class);

    public authController(JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
      }

    
    @PostMapping("/auth/email/send")
    public ResponseEntity<Object> register(@RequestBody String username)
        throws MessagingException, UnsupportedEncodingException {
        
        String confirmationUrl = "http://localhost:8888/auth/email/check";
        // String mailFrom = config.mailGunAPIFrom();
        // String mailFromName = config.mailGunAPIFromName();

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(username);
        email.setSubject(MAIL_SUBJECT);
        // email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", username);
        ctx.setVariable("springLogo", SPRING_LOGO_IMAGE);
        ctx.setVariable("url", confirmationUrl);

        final String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);

        email.setText(htmlContent, true);

        ClassPathResource clr = new ClassPathResource(SPRING_LOGO_IMAGE);

        email.addInline("springLogo", clr, PNG_MIME);

        mailSender.send(mimeMessage);

        Map<String, String> body = new HashMap<>();
        body.put("message", "User created successfully.");
        
        return new ResponseEntity<>(body, HttpStatus.OK);
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
