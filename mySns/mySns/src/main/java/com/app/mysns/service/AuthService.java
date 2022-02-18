package com.app.mysns.service;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.ClientDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    @Autowired
    private ManageDao dao;
    private SecureUtilsService util;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    

    public AuthService(SecureUtilsService util) {
        this.util = util;
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
        return dao.CreateClient(client);
    }

    public ClientDto checkDuplicate(String username) {

        ClientDto client = dao.FindClientByUsername(username);
        if(client == null || client.getIdx() < 1) { return null; }
        // DB에 저장하는 로직 타기, DTO 사용
        return client;
    }

    // 쿠키를 저장해야 해서 HttpServletResponse 같이 파라미터로 전달
    public ClientDto login (ClientDto client) {
        ClientDto rvClientDto = new ClientDto();
        rvClientDto.setIdx(0);
        
        try {

            System.out.println("요청 비밀번호 : " + client.getUsername());
            System.out.println("요청 비밀번호 : " + client.getPassword());
            System.out.println("요청 비밀번호 암호화 : " + util.getSecurePassword(client.getPassword(), client.getUsername()));

            ClientDto loginInfo = dao.login(client.getUsername());

            String username = loginInfo.getUsername();
            String password = loginInfo.getPassword();
            System.out.println("DB 이메일 : " + username);
            System.out.println("DB 비밀번호 : " + password);
            if(username.equals(client.getUsername()) && password.equals(util.getSecurePassword(client.getPassword(), client.getUsername()))) {
                return loginInfo;
            }
            return rvClientDto;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return rvClientDto;
        }
    }


    public ClientDto findByUsername(String username) {
        ClientDto userOptional = dao.findUser(username);
        if(userOptional == null || userOptional.getIdx() < 1) { new ClientDto(); }

        return userOptional;

        // User user = userOptional.orElseThrow(()->new UsernameNotFoundException("user name not found!"));
        // return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),new ArrayList<>());

    }
}
