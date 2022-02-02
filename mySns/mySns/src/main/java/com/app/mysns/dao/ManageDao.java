package com.app.mysns.dao;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.SnsTypeDto;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import groovyjarjarantlr.collections.List;

// import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;

//매퍼 xml 과 연동해주는 인터페이스 제공

// @Mapper
@Repository
public interface ManageDao {
    // CRUD 한 세트, username 으로 엮었으나 베스트는 아님
    boolean CreateClient(ClientDto user);
    ClientDto FindClient(String user);
    boolean UpdateClient(String user);
    boolean DeleteClient(String user);
    ArrayList<ClientDto> ListClient();


    // @Insert("INSERT INTO user(name, part) VALUES(#{name}, #{part}")
    // @Options(useGeneratedKeys = true, keyProperty = "userIdx")
    // int save(@Param("user") final User user);

    // ArrayList<SnsTypeDto> dbtest();
    // void mailAccept(HashMap<String,Object> map);


    ClientDto login(String username);
}


// public interface ManageDao {

// }