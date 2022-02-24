package com.app.mysns.controller;

import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.Restful;
import com.app.mysns.dto.SyncSiteDto;
import com.app.mysns.service.JwtService;
import com.app.mysns.service.MailService;
import com.app.mysns.service.ManageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.thymeleaf.TemplateEngine;
import groovyjarjarpicocli.CommandLine.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintStream;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@RequiredArgsConstructor
@Controller
//@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ManageService service;
    private final JwtService jwtService;

    // @Autowired
    // private MailService mailService;
    // private final Logger logger = LoggerFactory.getLogger(profileController.class);

   /* public ProfileController(TemplateEngine htmlTemplateEngine, MailService mailService, JwtService jwtService) {
        // this.mailService = mailService;
        this.jwtService = jwtService;
    }*/

    /*@GetMapping("")
    public ModelAndView index(HttpServletRequest httpServletRequest, Model model){
        ModelAndView mav = new ModelAndView();
        System.out.println("client redirect to profile");
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_uuid");
        if(cookie == null && cookie.getValue() == null) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/auth/login");
            return mv;
        };
        String userInfo = jwtService.getUsernameFromToken(cookie.getValue());
        String username = userInfo.split("\\|")[0];
        System.out.println("사용자메일"+username);
        ClientDto client = service.findUser(username);
        Long id = client.getIdx();
        ArrayList<SyncSiteDto> profile =  service.getProfile(id);
        System.out.println("가져온 값"+profile);
        mav.setViewName("profile");
        //r그 url에 해당하는 sns들만 가지고와야된다.

        *//*for(int i =0; i < profile.size(); i++){


            ArrayList<SyncSiteDto> snsType = service.getSnsType(profile.get(i).getUrl());
           *//**//* for(int j =0; j < snsType.size(); j++){
                String url = snsType.get(i).getUrl();
                Long type_id = snsType.get(i).getSns_type_id();
                //System.out.println(snsType.get(i).getUrl() +"/"+snsType.get(i).getSns_type_id());
                map.put(url+i,type_id);
                System.out.println("맵"+map);
            }*//**//*



        }*//*

                //.getSns_type_id();
        mav.addObject("profile",profile);
        //System.out.println("사용자 idx"+client);


        return mav;
    }*/

    @RequestMapping(value = (""), method = RequestMethod.GET)
    public String index(HttpServletRequest httpServletRequest){
        System.out.println("client redirect to profile");
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_uuid");

        String userInfo = jwtService.getUsernameFromToken(cookie.getValue());
        String username = userInfo.split("\\|")[0];
        System.out.println("사용자메일"+username);
        ClientDto client = service.findUser(username);
        Long id = client.getIdx();
        ArrayList<SyncSiteDto> profile =  service.getProfile(id);

        System.out.println("가공전 데이터: "+profile);
//        ArrayList<HashMap<Object,Object>> list = new ArrayList<>();
//
//        JSONArray jsonArray = new JSONArray();
//        JSONObject jsonData = new JSONObject();
//        JSONObject json = new JSONObject();
//        for(int i =0; i< profile.size(); i++){
//
//            HashMap<Object ,Object> map = new HashMap<>();
//            String urlValue = profile.get(i).getUrl().replace("/","");
//            System.out.println("가공"+urlValue);
//            map.put("url",urlValue);
//            map.put("typeBox",profile.get(i).getTypeBox());
//            System.out.println("맵"+map);
//
//            json.put("url",urlValue);
//            json.put("typeBox",profile.get(i).getTypeBox());
//            System.out.println("제이슨값"+json);
//            //json -> jsonArray -> json
//            //list.add(String.valueOf(map));
//            //jsonData.put(json);
//            jsonArray.add(json);
//            list.add(json);
//         /*  *//* Object key = json.put("url",profile.get(i).getUrl());
//            Object value = json.put("typeBox",profile.get(i).getTypeBox());*//*
//            map.put(key,value);
//            //list.add(String.valueOf(map).replace("\\/",""));
//            list.add(map);
//*/
//        }
//        System.out.println("제이슨값2"+json);
//        System.out.println("어레이"+jsonArray);
//
//        System.out.println("list"+list);

        //jsonData.put(list);

        //SyncSiteDto -> String -> ArrayList - > JsonArray -> JSONObject

        //json.put("data",jsonArray);
        //map에 넣기??????? 그게되려나

        //JSONArray jsonArray = new JSONArray(list);




        return "profile";
    }

    @RequestMapping(value = (""),method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> profileTable(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        System.out.println("client redirect to profile");
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_uuid");

        String userInfo = jwtService.getUsernameFromToken(cookie.getValue());
        String username = userInfo.split("\\|")[0];
        System.out.println("사용자메일"+username);
        ClientDto client = service.findUser(username);
        Long id = client.getIdx();
        ArrayList<SyncSiteDto> profile =  service.getProfile(id);

        System.out.println("아 띠바!: "+profile);

        //ObjectMapper ob = new ObjectMapper();
//        String ooo = ob.writeValueAsString(profile);
       // System.out.println("멀까"+ooo);
//        String result = ooo.replace("[","").replace("]","");
        //System.out.println("진짜멀까"+result);
        //return new ResponseEntity<>(new Restful().Data("data",ooo), HttpStatus.OK);
        JSONObject jsonObject = new JSONObject();


//        JSONObject a = new JSONObject();
//        ArrayList<String> bb = new ArrayList<>();
//        for (SyncSiteDto el : profile) {
//            Gson gson = new Gson();
//            String jsonString = gson.toJson(el);
//            bb.add(jsonString);
////            System.out.println(el);
//        }

//        jsonObject.put("draw", profile.size());
//        jsonObject.put("recordsTotal", profile.size());
//        jsonObject.put("recordsFiltered", 3);

        jsonObject.put("data", profile);







//      return jsonObject;
        return new ResponseEntity<>(jsonObject.toMap(), HttpStatus.OK);
        //동화가 많은걸 도와줬다. 원래 되야하는데 그 방식으로 죽어도 안되는 문제였다 분명 JSON으로 보냈는데..
        //그래서 다른사람이 자기도 안되서 이런식으로 해결했다는 글을 보고 고쳐서 했다.
        //동화가 너무 자책하지말라고했다.
    }
}


//"[{idx : 0}, {idx : 0}]" => [{idx : 0}, {idx : 0}]
//        "{idx : 0}, {idx : 0}" => [{idx : 0}, {idx : 0}]
