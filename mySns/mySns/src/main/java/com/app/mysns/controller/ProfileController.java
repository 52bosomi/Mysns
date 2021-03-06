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
        System.out.println("???????????????"+username);
        ClientDto client = service.findUser(username);
        Long id = client.getIdx();
        ArrayList<SyncSiteDto> profile =  service.getProfile(id);
        System.out.println("????????? ???"+profile);
        mav.setViewName("profile");
        //r??? url??? ???????????? sns?????? ?????????????????????.

        *//*for(int i =0; i < profile.size(); i++){


            ArrayList<SyncSiteDto> snsType = service.getSnsType(profile.get(i).getUrl());
           *//**//* for(int j =0; j < snsType.size(); j++){
                String url = snsType.get(i).getUrl();
                Long type_id = snsType.get(i).getSns_type_id();
                //System.out.println(snsType.get(i).getUrl() +"/"+snsType.get(i).getSns_type_id());
                map.put(url+i,type_id);
                System.out.println("???"+map);
            }*//**//*



        }*//*

                //.getSns_type_id();
        mav.addObject("profile",profile);
        //System.out.println("????????? idx"+client);


        return mav;
    }*/

    @RequestMapping(value = (""), method = RequestMethod.GET)
    public String index(HttpServletRequest httpServletRequest){
        System.out.println("client redirect to profile");
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_uuid");

        String userInfo = jwtService.getUsernameFromToken(cookie.getValue());
        String username = userInfo.split("\\|")[0];
        System.out.println("???????????????"+username);
        ClientDto client = service.findUser(username);
        Long id = client.getIdx();
        ArrayList<SyncSiteDto> profile =  service.getProfile(id);

        System.out.println("????????? ?????????: "+profile);
//        ArrayList<HashMap<Object,Object>> list = new ArrayList<>();
//
//        JSONArray jsonArray = new JSONArray();
//        JSONObject jsonData = new JSONObject();
//        JSONObject json = new JSONObject();
//        for(int i =0; i< profile.size(); i++){
//
//            HashMap<Object ,Object> map = new HashMap<>();
//            String urlValue = profile.get(i).getUrl().replace("/","");
//            System.out.println("??????"+urlValue);
//            map.put("url",urlValue);
//            map.put("typeBox",profile.get(i).getTypeBox());
//            System.out.println("???"+map);
//
//            json.put("url",urlValue);
//            json.put("typeBox",profile.get(i).getTypeBox());
//            System.out.println("????????????"+json);
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
//        System.out.println("????????????2"+json);
//        System.out.println("?????????"+jsonArray);
//
//        System.out.println("list"+list);

        //jsonData.put(list);

        //SyncSiteDto -> String -> ArrayList - > JsonArray -> JSONObject

        //json.put("data",jsonArray);
        //map??? ????????????? ???????????????

        //JSONArray jsonArray = new JSONArray(list);




        return "profile";
    }

    @RequestMapping(value = (""),method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> profileTable(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        System.out.println("client redirect to profile");
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_uuid");

        String userInfo = jwtService.getUsernameFromToken(cookie.getValue());
        String username = userInfo.split("\\|")[0];
        System.out.println("???????????????"+username);
        ClientDto client = service.findUser(username);
        Long id = client.getIdx();
        ArrayList<SyncSiteDto> profile =  service.getProfile(id);

        System.out.println("??? ??????!: "+profile);

        //ObjectMapper ob = new ObjectMapper();
//        String ooo = ob.writeValueAsString(profile);
       // System.out.println("??????"+ooo);
//        String result = ooo.replace("[","").replace("]","");
        //System.out.println("????????????"+result);
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
        //????????? ????????? ????????????. ?????? ??????????????? ??? ???????????? ????????? ????????? ???????????? ?????? JSON?????? ????????????..
        //????????? ??????????????? ????????? ????????? ??????????????? ??????????????? ?????? ?????? ????????? ??????.
        //????????? ?????? ???????????????????????????.
    }
}


//"[{idx : 0}, {idx : 0}]" => [{idx : 0}, {idx : 0}]
//        "{idx : 0}, {idx : 0}" => [{idx : 0}, {idx : 0}]
