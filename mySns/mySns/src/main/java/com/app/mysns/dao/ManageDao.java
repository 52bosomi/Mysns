package com.app.mysns.dao;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.SnsTypeDto;
import org.springframework.stereotype.Repository;

// import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Repository
public interface ManageDao {
    
    boolean AddClient(ClientDto user);

    ArrayList<SnsTypeDto> dbtest();
    void mailAccept(HashMap<String,Object> map);


    ClientDto login(String username);
}
