package com.app.mysns.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.KakaoButton;
import net.nurigo.sdk.message.model.KakaoButtonType;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.MultipleMessageSendingRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 모든 발송 API에는 발신, 수신번호 입력 항목에 +82 또는 +8210, 010-0000-0000 같은 형태로 기입할 수 없습니다.
 * 수/발신 가능한 예시) 01000000000, 020000000 등
 */
@RestController
public class SMSController {
    private DefaultMessageService messageService;

    /**
     * 발급받은 API KEY와 API Secret Key를 사용해주세요.
     */
    public SMSController(Environment env) {
        this.messageService = NurigoApp.INSTANCE.initialize(env.getProperty("spring.nurigo.api"), env.getProperty("spring.nurigo.secret"), "https://api.solapi.com");
    }

    /**
     * 메시지 조회 예제
     */
    @GetMapping("/sms/get-message-list")
    public void getMessageList() {
        MessageListResponse response = this.messageService.getMessageList(new MessageListRequest());

        System.out.println(response);
    }

    /**
     * 단일 메시지 발송 예제
     */
    @PostMapping("/sms/send-one")
    public SingleMessageSentResponse sendOne() {
        Message message = new Message();
        message.setFrom("01084956428");
        message.setTo("01097596428");
        message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

    /**
     * MMS 발송 예제
     * 단일 발송, 여러 건 발송 상관없이 이용 가능
     */
    @PostMapping("/sms/send-mms")
    public SingleMessageSentResponse sendMmsByResourcePath() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/sample.jpg");
        File file = resource.getFile();
        String imageId = this.messageService.uploadFile(file, StorageType.MMS, null);

        Message message = new Message();
        message.setFrom("029302266");
        message.setTo("01000000000");
        message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다.");
        message.setImageId(imageId);

        // 여러 건 메시지 발송일 경우 send many 예제와 동일하게 구성하여 발송할 수 있습니다.
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

    /**
     * 여러 메시지 발송 예제
     * 한 번 실행으로 최대 10,000건 까지의 메시지가 발송 가능합니다.
     */
    @PostMapping("/sms/send-many")
    public MultipleMessageSentResponse sendMany() {
        ArrayList<Message> messageList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Message message = new Message();
            message.setFrom("029302266");
            message.setTo("01000000000");
            message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다." + i);

            messageList.add(message);
        }
        MultipleMessageSendingRequest request = new MultipleMessageSendingRequest(messageList);
        // allowDuplicates를 true로 설정하실 경우 중복으로 수신번호를 입력해도 각각 발송됩니다.
        // request.setAllowDuplicates(true);

        MultipleMessageSentResponse response = this.messageService.sendMany(request);
        System.out.println(response);

        return response;
    }

    /**
     * 잔액 조회 예제
     */
    @GetMapping("/kakao/sms/get-balance")
    public Balance getBalance() {
        Balance balance = this.messageService.getBalance();
        System.out.println(balance);

        return balance;
    }



    /**
     * 알림톡 한건 발송 예제
     */
    @PostMapping("/kakao/send-one-ata")
    public SingleMessageSentResponse sendOneAta() {
        KakaoOption kakaoOption = new KakaoOption();
        // disableSms를 true로 설정하실 경우 문자로 대체발송 되지 않습니다.
        // kakaoOption.setDisableSms(true);

        // 등록하신 카카오 비즈니스 채널의 pfId를 입력해주세요.
        kakaoOption.setPfId("");
        // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
        kakaoOption.setTemplateId("");

        // 알림톡 템플릿 내에 #{변수} 형태가 존재할 경우 variables를 설정해주세요.
        /*
        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{변수명1}", "테스트");
        variables.put("#{변수명2}", "치환문구 테스트2");
        kakaoOption.setVariables(variables);
        */

        Message message = new Message();
        message.setFrom("029302266");
        message.setTo("01000000000");
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

    /**
     * 여러 알림톡 발송 예제
     * 한 번 실행으로 최대 10,000건 까지의 메시지가 발송 가능합니다.
     */
    @PostMapping("/kakao/send-many-ata")
    public MultipleMessageSentResponse sendManyAta() {
        ArrayList<Message> messageList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            KakaoOption kakaoOption = new KakaoOption();
            // 등록하신 카카오 비즈니스 채널의 pfId를 입력해주세요.
            kakaoOption.setPfId("");
            // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
            kakaoOption.setTemplateId("");

            // 알림톡 템플릿 내에 #{변수} 형태가 존재할 경우 variables를 설정해주세요.
            /*
            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{변수명1}", "테스트");
            variables.put("#{변수명2}", "치환문구 테스트2");
            kakaoOption.setVariables(variables);
            */

            Message message = new Message();
            message.setFrom("029302266");
            message.setTo("01000000000");

            messageList.add(message);
        }

        MultipleMessageSendingRequest request = new MultipleMessageSendingRequest(messageList);
        // allowDuplicates를 true로 설정하실 경우 중복으로 수신번호를 입력해도 각각 발송됩니다.
        // request.setAllowDuplicates(true);

        MultipleMessageSentResponse response = this.messageService.sendMany(request);
        System.out.println(response);

        return response;
    }

    /**
     * 친구톡 한건 발송 예제, send many 호환
     * 친구톡 내 버튼은 최대 5개까지만 생성 가능합니다.
     */
    @PostMapping("/kakao/send-cta")
    public SingleMessageSentResponse sendOneCta() {
        KakaoOption kakaoOption = new KakaoOption();
        // disableSms를 true로 설정하실 경우 문자로 대체발송 되지 않습니다.
        // kakaoOption.setDisableSms(true);

        // 등록하신 카카오 비즈니스 채널의 pfId를 입력해주세요.
        kakaoOption.setPfId("");
        kakaoOption.setVariables(null);

        // 친구톡에 버튼을 넣으실 경우에만 추가해주세요.
        ArrayList<KakaoButton> kakaoButtons = new ArrayList<>();
        // 웹링크 버튼
        KakaoButton kakaoWebLinkButton = new KakaoButton(
                "테스트 버튼1", KakaoButtonType.WL,
                "https://example.com", "https://example.com",
                null, null
        );

        // 앱링크 버튼
        KakaoButton kakaoAppLinkButton = new KakaoButton(
                "테스트 버튼2", KakaoButtonType.AL,
                null, null,
                "exampleapp://test", "exampleapp://test"
        );

        // 봇 키워드 버튼, 버튼을 클릭하면 버튼 이름으로 수신자가 발신자에게 채팅을 보냅니다.
        KakaoButton kakaoBotKeywordButton = new KakaoButton(
                "테스트 버튼3", KakaoButtonType.BK, null, null, null, null
        );

        // 메시지 전달 버튼, 버튼을 클릭하면 버튼 이름과 친구톡 메시지 내용을 포함하여 수신자가 발신자에게 채팅을 보냅니다.
        KakaoButton kakaoMessageDeliveringButton = new KakaoButton(
                "테스트 버튼4", KakaoButtonType.MD, null, null, null, null
        );

        /*
         * 상담톡 전환 버튼, 상담톡 서비스를 이용하고 있을 경우 상담톡으로 전환. 상담톡 서비스 미이용시 해당 버튼 추가될 경우 발송 오류 처리됨.
         * @see <a href="https://business.kakao.com/info/bizmessage/">상담톡 딜러사 확인</a>
         */
        /*KakaoButton kakaoBotCustomerButton = new KakaoButton(
                "테스트 버튼6", KakaoButtonType.BC, null, null, null, null
        );*/

        // 봇전환 버튼, 해당 비즈니스 채널에 카카오 챗봇이 없는 경우 동작안함.
        // KakaoButton kakaoBotTransferButton = new KakaoButton("테스트 버튼7", KakaoButtonType.BT, null, null, null, null);

        kakaoButtons.add(kakaoWebLinkButton);
        kakaoButtons.add(kakaoAppLinkButton);
        kakaoButtons.add(kakaoBotKeywordButton);
        kakaoButtons.add(kakaoMessageDeliveringButton);

        kakaoOption.setButtons(kakaoButtons);

        Message message = new Message();
        message.setFrom("029302266");
        message.setTo("01000000000");
        message.setText("친구톡 테스트 메시지");
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

    /**
     * 친구톡 이미지 단건 발송, send many 호환
     * 친구톡 내 버튼은 최대 5개까지만 생성 가능합니다.
     */
    @PostMapping("/kakao/send-cti")
    public SingleMessageSentResponse sendOneCti() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/cti.jpg");
        File file = resource.getFile();
        // 이미지 크기는 가로 500px 세로 250px 이상이어야 합니다, 링크도 필수로 기입해주세요.
        String imageId = this.messageService.uploadFile(file, StorageType.KAKAO, "https://example.com");

        KakaoOption kakaoOption = new KakaoOption();
        // disableSms를 true로 설정하실 경우 문자로 대체발송 되지 않습니다.
        // kakaoOption.setDisableSms(true);

        // 등록하신 카카오 비즈니스 채널의 pfId를 입력해주세요.
        kakaoOption.setPfId("");
        kakaoOption.setImageId(imageId);
        kakaoOption.setVariables(null);

        Message message = new Message();
        message.setFrom("029302266");
        message.setTo("01000000000");
        message.setText("테스트");
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }
}
