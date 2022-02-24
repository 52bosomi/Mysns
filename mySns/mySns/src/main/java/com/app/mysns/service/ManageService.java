package com.app.mysns.service;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.CommentDto;
import com.app.mysns.dto.QnaDto;
import com.app.mysns.dto.SyncSiteDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageService {

    @Autowired
    private ManageDao dao;

    // 테스트용
    // public ArrayList<ClientDto> ListClient() {
    //     return dao.ListClient();
    // }

    public ClientDto findUser(String username) { return dao.findUser(username); }

    public int summarySyncSite(SyncSiteDto syncSiteDto) { return dao.summarySyncSite(syncSiteDto); }

    public ArrayList<SyncSiteDto> getProfile(Long username) { return dao.getProfile(username); }

    public ArrayList<SyncSiteDto> getSnsType(String url) {return dao.getSnsType(url); }

    public int writeForm(QnaDto writeQna) {return dao.writeForm(writeQna); }

    public List<QnaDto> getQna() {return dao.getQna(); }

    public QnaDto getQnaDetail(long data) { return dao.getQnaDetail(data); }

    public int detailDelete(long idx) { return dao.detailDelete(idx); }

    public int detailUpdate(QnaDto idx) {return dao.detailUpdate(idx); }

    public int writeComment(CommentDto comments) { return dao.writeComment(comments); }
}
