package com.app.mysns.service;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.TypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ManageService {

    @Autowired
    private ManageDao dao;

    public ArrayList<TypeDto> dbtest() {
        ArrayList<TypeDto> result =  dao.dbtest();
        return result;
    }

    //받은 메일
    public void mailAccept(String username) {
        String test = "1234";
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        HashMap<String,Object> map = new HashMap();
        map.put("username",username);
        map.put("password",test);
        map.put("name",test);
        map.put("use_facebook",false);
        map.put("access_facebook",test);
        map.put("refresh_facebook",test);
        map.put("use_instagram",false);
        map.put("access_instagram",test);
        map.put("use_naver",false);
        map.put("access_naver",test);
        map.put("refresh_naver",test);
        map.put("updated_at",timestamp);
        map.put("created_at",timestamp);


        dao.mailAccept(map);
    }
}
