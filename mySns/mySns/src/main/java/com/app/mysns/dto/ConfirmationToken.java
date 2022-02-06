package com.app.mysns.dto;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Data
@Configuration
public class ConfirmationToken {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 10L;//10분

    private String id;
    private LocalDateTime expirationDate;
    private boolean expired;
    private  String userid;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    /*
     * 이메일 인증 토큰 생성
     * @param userId
     * @return
     * */
    public static ConfirmationToken createEmailConfirmationToken(String userId){
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE);//10분후 만료
        confirmationToken.userid = userId;
        confirmationToken.expired = false;
        return confirmationToken;
    }

    /*
     * 토큰 사용으로 인한 만료
     * */
    public void userToken(){
        expired = true;
    }
}
