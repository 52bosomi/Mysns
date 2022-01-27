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

@Service
public class AuthService {

    @Autowired
    private ManageDao dao;
    private SecureUtilsService util;

    public AuthService(SecureUtilsService util) {
        this.util = util;
    }

    public ArrayList<SnsTypeDto> dbtest() {
        ArrayList<SnsTypeDto> result =  dao.dbtest();
        return result;
    }

    public boolean emailJoin(ClientDto client) {
        //암호화시키기 ,SHA256 암호화 사용
        System.out.println("1차 비밀번호 : " + client.getPassword());
        // String password = UserSha256.encrypt(client.getPassword());
        String password = util.getSecurePassword(client.getPassword(), client.getUsername());
        System.out.println("2차 비밀번호 : " + password);
        // 암호화 패스워드 저장
        client.setPassword(password);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        client.setUpdated_at(timestamp);
        client.setCreated_at(timestamp);
        
        client.setCreated_at(timestamp); // 추후 업데이트시 새로 넣을 값
        client.setUpdated_at(timestamp);

        // DB에 저장하는 로직 타기, DTO 사용
        return dao.AddClient(client);
    }

    // 쿠키를 저장해야 해서 HttpServletResponse 같이 파라미터로 전달
    public boolean login (ClientDto client, HttpServletResponse response) {


        // System.out.println("1차 로그인 비밀번호 : " + client.getPassword());
        // String password = UserSha256.encrypt(client.getPassword());
        // String password = util.getSecurePassword(client.getPassword(), client.getUsername());
        // System.out.println("2차 로그인 비밀번호 : " + password);

        System.out.println("요청 비밀번호 : " + client.getUsername());
        System.out.println("요청 비밀번호 : " + client.getPassword());

        ClientDto loginInfo = dao.login(client.getUsername());

        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        System.out.println("DB 이메일 : " + username);
        System.out.println("DB 비밀번호 : " + password);

        if(username.equals(client.getUsername()) && password.equals(util.getSecurePassword(client.getPassword(), client.getUsername()))) {
            // 필요시 나중에 시간 검증용
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String uuid = util.SHA256(username + "." + timestamp.toString()) + "." + timestamp.toString();

            System.out.println("쿠키 토큰 UUID : " + uuid);
            Cookie cookie = new Cookie("mysns_uuid", uuid);
            
            System.out.println("쿠키 : " + cookie);

            cookie.setComment("mysns auth code for login");
            // 글로벌
            cookie.setPath("/"); 
            // 유효시간: 30분
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);
            return true;
        }

        return false;
    }
}
