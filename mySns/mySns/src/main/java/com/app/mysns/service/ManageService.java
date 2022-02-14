package com.app.mysns.service;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ManageService {

    @Autowired
    private ManageDao dao;

    // 테스트용
    public ArrayList<ClientDto> ListClient() {
        return dao.ListClient();
    }

    public ClientDto findUser(String username) {return dao.findUser(username);}

    public int countFacebook(long user, long sns_type_id) {
        sns_type_id =1;
        return dao.countFacebook(user,sns_type_id);}

    public int countGoogle(long user, long sns_type_id) {
        sns_type_id =2;
        return dao.countGoogle(user,sns_type_id);}

    public int countInsta(long user, long sns_type_id) {
        sns_type_id =3;
        return dao.countInsta(user, sns_type_id);}

    public int countNaver(long user, long sns_type_id) {
        sns_type_id =4;
        return dao.countNaver(user,sns_type_id);}


    // // 사용자 생성
    // public void mailAccept(String username) {
    //     String test = "1234";
    //     Long datetime = System.currentTimeMillis();
    //     Timestamp timestamp = new Timestamp(datetime);
    //     HashMap<String,Object> map = new HashMap<String, Object>();
    //     map.put("username",username);
    //     map.put("password",test);
    //     map.put("name",test);
    //     map.put("phone",test);
    //     map.put("updated_at",timestamp);
    //     map.put("created_at",timestamp);


    //     dao.CreateClient(map);
    // }
}
