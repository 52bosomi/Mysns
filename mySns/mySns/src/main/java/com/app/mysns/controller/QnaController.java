package com.app.mysns.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.app.mysns.dto.*;
import com.app.mysns.service.JwtService;
import com.app.mysns.service.MailService;

import com.app.mysns.service.ManageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;
import org.thymeleaf.TemplateEngine;
import groovyjarjarpicocli.CommandLine.Model;


@RequiredArgsConstructor
@Controller
@RequestMapping("/qna")
public class QnaController {

    // @Autowired
    // private MailService mailService;
    // private final Logger logger = LoggerFactory.getLogger(qnaController.class);

  /*  public QnaController(TemplateEngine htmlTemplateEngine, MailService mailService) {
        // this.mailService = mailService;
    }*/
  private final JwtService jwtService;
  private final ManageService service;

    @GetMapping("")
    public String index(Model model){
        System.out.println("client redirect to qnaGet");
        return "qna";
    }

    @RequestMapping("/form")
    public ModelAndView form(HttpServletRequest httpServletRequest){
        System.out.println("client redirect to qnaform");
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_uuid");

        String userInfo = jwtService.getUsernameFromToken(cookie.getValue());
        String username = userInfo.split("\\|")[0];
        ModelAndView mav = new ModelAndView();
        mav.addObject("username",username);
        mav.setViewName("qnaForm");

        return mav;
    }

    @RequestMapping(value = "/writeForm" ,method = RequestMethod.POST)
    public ResponseEntity<Object> writeForm(@RequestBody QnaDto board){
        System.out.println("client press the save button");
        System.out.println("qna ??????"+board);
        //db??? ???????????? ??????

        /*?????? ????????? dto set*/
        QnaDto writeQna = new QnaDto();
        writeQna.setUsername(board.getUsername());
        writeQna.setTitle(board.getTitle());
        writeQna.setContent(board.getContent());
        LocalDateTime currentDataTime = LocalDateTime.now();
        String now = currentDataTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writeQna.setCreated_at(now);
        //disclosure??? true ??? ??????, false??? ?????????, ????????? ?????? true??? ?????? ??????
        writeQna.setDisclosure(true);
        if(writeQna.isDisclosure()){
            writeQna.setPassword("");
        }
        int saveSuccess = service.writeForm(writeQna);
        if(saveSuccess==1){
            return new ResponseEntity<>(new Restful().Data("??????"), HttpStatus.OK);
        }


        return new ResponseEntity<>(new Restful().Data("??????"), HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "qnalist",method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> gnaTable() throws JsonProcessingException {
        System.out.println("client redirect to QNA");
        System.out.println("QNA");
        List<QnaDto> qna = service.getQna();
        String userId ="";
        for(int i =0; i < qna.size(); i++){
            qna.get(i).setUserId(qna.get(i).getUsername());
            String user = qna.get(i).getUsername().substring(0,7).replaceAll("(?<=.{4}).","*");//??? 7????????? ????????? ???4???????????????
            qna.get(i).setUsername(user);

        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", qna);

        return new ResponseEntity<>(jsonObject.toMap(), HttpStatus.OK);
    }

   /* @RequestMapping(value = "qnaDetail",method = RequestMethod.POST)
    public void qnaDetail() {
        System.out.println("client redirect to QNA DETAIL");




        //return new ResponseEntity<>(jsonObject.toMap(), HttpStatus.OK);
    }*/

    @RequestMapping(value = "detail",method = RequestMethod.POST)
    public @ResponseBody ModelAndView detailIndex(HttpServletRequest httpServletRequest,@RequestParam long data,@RequestParam String userId){
        System.out.println("??? ??? ????????????"+data+userId);
        ModelAndView mav = new ModelAndView();
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "mysns_uuid");

        String userInfo = jwtService.getUsernameFromToken(cookie.getValue());
        String username = userInfo.split("\\|")[0];

        QnaDto detail = service.getQnaDetail(data);
        System.out.println("??? ?????? ?????????:"+detail);
        String user ="";

            detail.setUserId(detail.getUsername());
            user = detail.getUsername().substring(0,7).replaceAll("(?<=.{4}).","*");//??? 7????????? ????????? ???4???????????????
            detail.setUsername(user);
        System.out.println("??????"+username+" ??????:"+detail.getUserId());
        if(username.equals(detail.getUserId())){//TODO: ?????? ?????????
            mav.addObject("isAuth", true);
            System.out.println("?????? ?????????");
        }else{
            mav.addObject("isAuth", false);
        }

        mav.setViewName("qnaDetail");
        mav.addObject("detail",detail);
        return mav;

    }

    @RequestMapping(value = "detailDelete",method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> detailDelete(@RequestBody QnaDto idx)  {
        System.out.println("Here is delete Controller"+idx);

        int deleteSuccess = service.detailDelete(idx.getIdx());
        System.out.println("??????"+deleteSuccess);
        if(deleteSuccess==1){
            return new ResponseEntity<>(new Restful().Data("Delete Successful"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Restful().Data("Delete Fail"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "detailUpdateForm",method = RequestMethod.GET)
    public @ResponseBody ModelAndView detailUpdateForm(@RequestParam long idx)  {
        //????????? ?????? @ResponseBody??? ????????? ?????????????????????????? ?????? ?????????????????????
        System.out.println("Here is Bring post Info Controller"+idx);
        ModelAndView mav = new ModelAndView();
        QnaDto detailUpdateForm = service.getQnaDetail(idx);
        System.out.println("???????????????"+detailUpdateForm);
        mav.addObject("updateInfo",detailUpdateForm);
        mav.setViewName("qnaUpdateForm");

        return mav;
    }

    @RequestMapping(value = "detailUpdate",method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> detailUpdate(@RequestBody QnaDto data)  {
        System.out.println("Here is update Controller"+data);

        int UpdateSuccess = service.detailUpdate(data);
        System.out.println("??????"+UpdateSuccess);
        if(UpdateSuccess==1){
            return new ResponseEntity<>(new Restful().Data("Update Successful"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Restful().Data("Update Fail"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "writeComment",method = RequestMethod.POST)
    public String writeComment(@RequestParam long idx, @RequestParam String comment,RedirectAttributes redirect){
        System.out.println("?????? ??????: "+idx+comment);

        CommentDto comments = new CommentDto();

        comments.setIdx(idx);
        comments.setComment(comment);
        comments.setWriter("?????????");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        sdf.format(timestamp);
        comments.setDate(timestamp);
        int commentSuccess = service.writeComment(comments);
        System.out.println("?????? ???????"+commentSuccess);
        redirect.addFlashAttribute(idx);
        String redirect_url = "redirect:/qna/detail?idx="+idx;
        return redirect_url;
    }


}
