package com.app.mysns.service;

import com.app.mysns.dto.ClientDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import javax.mail.internet.MimeMessage;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;


@Service
public class MailService {


    private static final String PHODO_IMAGE = "templates/static/images/phodo.jpg";

    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    // private final Logger logger = LoggerFactory.getLogger(authController.class);

    public MailService(JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    //인증 메일 발송
    public Boolean SendEmailSignup(String username) {

        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper email;

            email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            email.setTo(username);
            email.setSubject("Welcome !!, plz follow sign up URL");
            // email.setFrom(new InternetAddress("no-reply@mysns.info", "MySNS"));

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("email", username);
            ctx.setVariable("phodo", PHODO_IMAGE);

            // change url for production
            ctx.setVariable("url", "http://localhost:8888/auth/email/check?email="+username);
            email.setText(this.htmlTemplateEngine.process("email/signup.html", ctx), true);

            // 마스코트 이미지 넣기
            ClassPathResource clr = new ClassPathResource(PHODO_IMAGE);
            email.addInline("phodo", clr, "image/jpg");

            // 슝슝 전송~
            mailSender.send(mimeMessage);

            return true;
        } catch (Exception e) {
            // TODO : 로깅 남겨야 함
            return false;
        }
    }


}

