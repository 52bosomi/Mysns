step = 0
is_clear = false
sns_type = 'naver'
sns_type_list = []
uuid = ''
// 순서는 네이버, 구글, 페이스북, 인스타

$(document).ready(function() {
  // 로드 완료되고 연결 실시
  websocket = new SockJS("/ws", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});
  window['x'] = websocket

  function generateUUID() { // Public Domain/MIT
    var d = new Date().getTime();//Timestamp
    var d2 = ((typeof performance !== 'undefined') && performance.now && (performance.now()*1000)) || 0;//Time in microseconds since page-load or 0 if unsupported
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16;//random number between 0 and 16
        if(d > 0){//Use timestamp until depleted
            r = (d + r)%16 | 0;
            d = Math.floor(d/16);
        } else {//Use microseconds since page-load if supported
            r = (d2 + r)%16 | 0;
            d2 = Math.floor(d2/16);
        }
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
  }
  // uuid = generateUUID()

  function send() {
    let u = document.getElementById("username")
    let p = document.getElementById("password")

    // console.log(u, p)

    let data = {
      username : u.value,
      password : p.value,
      type : sns_type,
      clientUUID : uuid,
      ua : navigator.userAgent
    }
    websocket.send(JSON.stringify(data));
  }

  //채팅창에서 나갔을 때
  function onClose(evt) {
    console.log(evt)
  }

  //채팅창에 들어왔을 때
  function onOpen(evt) {
    evt.type == 'open' ? console.log('connected !!') : ''
  }

  function onMessage(msg) {
    var data = msg.data;

    console.log(data)

    // convert
    data = typeof data == typeof "" ? JSON.parse(data) : data

    if(data.uuid && !uuid) {
      uuid = data.uuid
      return
    }

    
  }

  websocket.onmessage = onMessage;
  websocket.onopen = onOpen;
  websocket.onclose = onClose;

  
  function start(x) { 
    sns_type_list = $('input:checkbox:checked').map(function() { return this.value}).get();
    console.log("sns_type_list", sns_type_list)

    // 버튼 변경
    $('.btn-start').hide()
    $('.btn-step').show()

    loop()
    // setTimeout(() => loop(), 1000) // 화면 렌더링 이슈로 따로 실행
  }

  function loop() {

    if(sns_type_list.length < 1) { return alert("plz, select for sync") }

    $('.start').hide() // 기존거 숨기기

    sns_type = sns_type_list.pop(0);
    console.log("start", sns_type, sns_type_list)


    // 입력 폼 활성화
    $('.step-input').show();
    
    // 기존거 숨기기
    $('.step').hide()

    // 해당하는 이미지만 보여주기
    $(`.step_${sns_type}`).show();

  }

  // 로그인 연동을 위해 계정 정보 전송
  function next() {
    
    // 기존거 숨기기
    $('.step').hide()

    // 계정 정보 보내고 
    send()

    // 입력 폼 비활성화
    $('.step-input').hide()

    // 로딩중 표시
    $(`.loading`).show(); 
    $("#btn_next").hide()

    console.log('결과 대기중')
    
    // step = step
    

    // switch (step) {
    //   case 0:
    //     // naver
    //     $('.step_0').hide()
    //     $('.step_1').show()

    //     $('#username').val('hdh0926@naver.com')
    //     $('#password').val('hdh')
    //     step = 1
    //     break;
    //   case 1:
    //     $('.step_1').hide()
    //     step = 2

    //     $(".loading").show()
    //   default:
    //     break;
    // }

    // console.log('request to email')
    // $.ajax({
    //   async : true,
    //   url:'/auth/login', // 요청 할 주소
    //   type:'POST', // GET, PUT
    //   data: JSON.stringify({ username : username, password : password }),
    //   contentType : 'application/json',
    //   dataType:'json',// xml, json, script, html
    //   success:function(x) {
    //     $('#checker').show();
    //     alert(x.isError ? x.reason : x.data);
    //     location.href = '/welcome'
    //   },// 요청 완료 시
    //   error:function(x) {
    //     console.log('failed', x)
    //     alert(x.responseJSON.isError ? x.responseJSON.reason : x.responseJSON.data);
    //   },// 요청 실패.
    //   complete:function(x) {
    //     console.log(x)
    //   }// 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
    // });
  }


  step = 0
  $('.step-input').hide()
  $('.btn-step').hide()
  $('.btn-start').show()
  $('.step').hide() // 기존거 숨기기
  
  $('.loading').hide()
  $(".step_1").hide()
  $("#btn_next").click((x) => {
    console.log("x", x)
    next()
  })
  
  $("#btn-start").click((x) => start(x))
  $("#btn-step").click((x) => next(x))


  console.log("already loaded")
})
