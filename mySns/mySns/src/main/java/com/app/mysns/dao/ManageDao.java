package com.app.mysns.dao;

import com.app.mysns.dto.*;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
    SnsTypeDto FindSnsTypeByName(String name);
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

    ClientDto findUser(String username);

    // sns count
    int countFacebook(long user_id, long sns_type_id);

    int countGoogle(long user_id, long sns_type_id);

    int countInsta(long user_id, long sns_type_id);

    int countNaver(long user_id, long sns_type_id);

    int summarySyncSite(SyncSiteDto syncSiteDto);

    ArrayList<SyncSiteDto> getProfile(Long username);

    ArrayList<SyncSiteDto> getSnsType(String url);

    int writeForm(QnaDto writeQna);

    List<QnaDto> getQna();

    QnaDto getQnaDetail(long data);

    int detailDelete(long idx);

    int detailUpdate(QnaDto idx);

    int writeComment(CommentDto comments);
}


