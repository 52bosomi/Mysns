package com.app.mysns.controller;


import com.app.mysns.dto.SocketVO;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class socketController {

    // /receive를 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/agent/request")
    
    // /send로 메시지를 반환합니다.
    @SendTo("/agent/consumer")
    
    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 SocketVO를 1) 인자값, 2) 반환값으로 사용합니다.
    public SocketVO SocketHandler(SocketVO socketVO) {
        // vo에서 getter로 userName을 가져옵니다.
        String username = socketVO.getUsername();
        // vo에서 setter로 content를 가져옵니다.
        String content = socketVO.getContent();

        String taskKey = socketVO.getTaskKey();

        // 누가 누구에게 발송하는지 from - to 가 필요함

        // 생성자로 반환값을 생성, 반환
        return new SocketVO(username, content, taskKey);
    }
}
