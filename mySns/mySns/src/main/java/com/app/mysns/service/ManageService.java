package com.app.mysns.service;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.SyncSiteDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
}
