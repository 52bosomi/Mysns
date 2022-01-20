package com.app.mysns.dao;

import com.app.mysns.dto.TypeDto;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;


@Repository
public interface ManageDao {

    ArrayList<TypeDto> dbtest();

    void mailAccept(HashMap<String,Object> map);
}
