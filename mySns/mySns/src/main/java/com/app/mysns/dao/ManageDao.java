package com.app.mysns.dao;

import com.app.mysns.dto.SnSTypeDto;
import org.springframework.stereotype.Repository;

// import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;


@Repository
public interface ManageDao {

    ArrayList<SnSTypeDto> dbtest();
    void mailAccept(HashMap<String,Object> map);
}
