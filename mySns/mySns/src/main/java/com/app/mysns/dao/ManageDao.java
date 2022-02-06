package com.app.mysns.dao;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.SnsTypeDto;
import com.app.mysns.dto.SyncSiteDto;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import groovyjarjarantlr.collections.List;

import java.util.ArrayList;
import java.util.HashMap;

//매퍼 xml 과 연동해주는 인터페이스 제공
// @Mapper
@Repository
public interface ManageDao {
    // CRUD 기준

    // client
    boolean CreateClient(ClientDto client);
    ClientDto FindClient(Long idx);
    ClientDto FindClientByUsername(String username);
    boolean UpdateClient(ClientDto client);
    boolean DeleteClient(Long idx);
    ArrayList<ClientDto> ListClient();

    // sns_type
    boolean CreateSnsType(SnsTypeDto SnsType);
    SnsTypeDto FindSnsType(Long idx);
    SnsTypeDto FindSnsTypeByName(String username);
    boolean UpdateSnsType(SnsTypeDto SnsType);
    boolean DeleteSnsType(Long idx);
    ArrayList<SnsTypeDto> ListSnsType();

    // sync_site
    boolean CreateSyncSite(SyncSiteDto SyncSite);
    SyncSiteDto FindSyncSite(Long idx);
    SyncSiteDto FindSyncSiteByName(String name);
    SyncSiteDto FindSyncSiteByDescription(String description);
    boolean UpdateSyncSite(SyncSiteDto SyncSite);
    boolean DeleteSyncSite(Long idx);
    ArrayList<SyncSiteDto> ListSyncSite();

    // ut
    ClientDto login(String username);
}


// public interface ManageDao {

// }