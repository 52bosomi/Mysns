package com.app.mysns.service;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.SnsTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ManageService {

    @Autowired
    private ManageDao dao;

    public ArrayList<SnsTypeDto> dbtest() {
        ArrayList<SnsTypeDto> result =  dao.dbtest();
        return result;
    }

    // //받은 메일
    public void mailAccept(String username) {
        String test = "1234";
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("username",username);
        map.put("password",test);
        map.put("name",test);
        map.put("phone",test);
        map.put("updated_at",timestamp);
        map.put("created_at",timestamp);


        dao.mailAccept(map);
    }
}
