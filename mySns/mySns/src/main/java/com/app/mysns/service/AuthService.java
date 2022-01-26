package com.app.mysns.service;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.SnsTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private ManageDao dao;

    public ArrayList<SnsTypeDto> dbtest() {
        ArrayList<SnsTypeDto> result =  dao.dbtest();
        return result;
    }

    public int emailJoin(String userEmail, String username, String pw) {
        //암호화시키기 ,SHA256 암호화 사용
        System.out.println("비밀번호"+pw);
        String encryPassword = UserSha256.encrypt(pw);
        System.out.println("비밀번호 암호화: "+encryPassword);

        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        //값 받아서 다시 DB에 저장하는 로직 타기, DTO 사용
        ClientDto user = new ClientDto();
        user.setUsername(userEmail);
        user.setName(username);
        user.setPassword(encryPassword);
        user.setCreated_at(timestamp);
        user.setUpdated_at(timestamp);//추후 업데이트시? 새로 넣을 값
        user.setPhone("010-1234-5678");//
        int success = dao.emailJoin(user);
        return success;
    }


    public boolean login(String username, String pw, HttpServletResponse response) {
        //비밀번호 암호화
        String encryPassword = UserSha256.encrypt(pw);
        System.out.println("들어온 비밀번호 암호화: "+encryPassword);

        ClientDto loginInfo = dao.login(username);

        String id = loginInfo.getUsername();
        String pass =loginInfo.getPassword();
        System.out.println("DB 값 :"+ id+"/"+pass +"로그인 값"+username+"/"+encryPassword);

        if(username.equals(id) && encryPassword.equals(pass)) {
            Cookie cookie = new Cookie("userName",username);
            System.out.println("쿠키생성"+cookie);
            cookie.setComment("유저 아이디");
            cookie.setPath("/");
            cookie.setMaxAge(60*30);//유효시간: 30분
            response.addCookie(cookie);
            return true;
        }

        return false;
    }
}
