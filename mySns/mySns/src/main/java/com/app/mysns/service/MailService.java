package com.app.mysns.service;

import com.app.mysns.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class MailService {


    private static final String PHODO_IMAGE = "templates/static/images/phodo.jpg";
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;
    private SecureUtilsService secureUtilsService;
    RedisTemplate<String, String> redisTemplate;

    // private final Logger logger = LoggerFactory.getLogger(authController.class);

    public MailService(JavaMailSender mailSender, TemplateEngine htmlTemplateEngine, RedisTemplate<String, String> redisTemplate, SecureUtilsService secureUtilsService) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
        this.redisTemplate = redisTemplate;
        this.secureUtilsService = secureUtilsService;
    }

    //인증 메일 발송
    public Boolean sendEmailSignup(String username) {

        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper email;

            email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            email.setTo(username);
            email.setSubject("Welcome !!, plz follow sign up URL");
            // email.setFrom(new InternetAddress(from, "MySNS"));

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("email", username);
            ctx.setVariable("phodo", PHODO_IMAGE);

            // change url for production
            String domain =  System.getenv("DOMAIN");
            if(domain == null) { domain = "localhost:8888"; }

            ValueOperations<String, String> vop = redisTemplate.opsForValue();

            String uuid = secureUtilsService.generateType1UUID().toString();

            vop.set(uuid, username); // 인증시 검증
            redisTemplate.expire(uuid, 5, TimeUnit.MINUTES);

            String url = MessageFormat.format("http://{0}/auth/email/check?username={1}&token={2}", domain, username, uuid);

            ctx.setVariable("url", url);
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

