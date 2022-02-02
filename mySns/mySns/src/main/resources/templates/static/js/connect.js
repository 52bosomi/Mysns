this.step = 0
this.is_clear = false
this.sns_type = 'naver'
this.sns_type_list = []
// 순서는 네이버, 구글, 페이스북, 인스타

$(document).ready(function() {
  
  function start(x) { 
    this.sns_type_list = $('input:checkbox:checked').map(function() { return this.value}).get();
    console.log("this.sns_type_list", this.sns_type_list)
    loop()
    // setTimeout(() => loop(), 1000) // 화면 렌더링 이슈로 따로 실행
  }

  function loop() {

    if(this.sns_type_list.length < 1) { return alert("plz, select for sync") }

    $('.start').hide(); // 기존거 숨기기

    this.sns_type = this.sns_type_list.pop(0);
    console.log("start", this.sns_type, this.sns_type_list)
      
      $('.step-input').show();
      // 해당 화면 보여주기
      
      
      $('.step').hide(); // 기존거 숨기기
      $(`.step_${this.sns_type}`).show(); // 해당하는 것만 사용

      // 웹소켓으로 전송

  }


  function next() {
    
    console.log(this.step, "type")
    // this.step = step
    

    switch (this.step) {
      case 0:
        // naver
        $('.step_0').hide()
        $('.step_1').show()

        $('#username').val('hdh0926@naver.com')
        $('#password').val('hdh')
        this.step = 1
        break;
      case 1:
        $('.step_1').hide()
        this.step = 2

        $(".loading").show()
      default:
        break;
    }

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


  this.step = 0
  $('.step-input').hide()
  $('.btn-step').hide()
  $('.btn-start').show()
  $('.step').hide(); // 기존거 숨기기
  
  $('.loading').hide()
  $(".step_1").hide()
  $("#btn_next").click((x) => {
    console.log("x", x)
    next()
  })
  
  $("#btn-start").click((x) => start(x))


  console.log("already loaded")
})

var websocket = new SockJS("/ws", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});

websocket.onmessage = onMessage;
websocket.onopen = onOpen;
websocket.onclose = onClose;

function send(){

    let msg = document.getElementById("msg");
    let u = document.getElementById("username")
    let p = document.getElementById("password")
    let t = document.getElementById("type")

    console.log(u, p)

    let data = {
      username : u.value,
      password : p.value,
      type = t.value
    }

  //   console.log(username + ":" + msg.value);
    websocket.send(JSON.stringify(data));
  //   msg.value = '';

    var str = "<div class='col-6'>";
  str += "<div class='alert alert-warning'>";
  str += "<b>" + "sessionId" + " : " + JSON.stringify(data) + "</b>";
  str += "</div></div><br/>";
  $("#msgArea").append(str);
}

//채팅창에서 나갔을 때
function onClose(evt) {
    var str = username + ": 님이 방을 나가셨습니다.";
    websocket.send(str);
}

//채팅창에 들어왔을 때
function onOpen(evt) {
    var str = username + ": 님이 입장하셨습니다.";
    websocket.send(str);
}

function onMessage(msg) {
    var data = msg.data;
    var sessionId = null;
    //데이터를 보낸 사람
    var message = null;
    var arr = data.split(":");

    for(var i=0; i<arr.length; i++){
        console.log('arr[' + i + ']: ' + arr[i]);
    }

    var cur_session = username;

    //현재 세션에 로그인 한 사람
    console.log("cur_session : " + cur_session);
    sessionId = arr[0];
    message = arr[1];

    console.log("sessionID : " + sessionId);
    console.log("cur_session : " + cur_session);

    //로그인 한 클라이언트와 타 클라이언트를 분류하기 위함
    if(sessionId == cur_session){
        var str = "<div class='col-6'>";
        str += "<div class='alert alert-secondary'>";
        str += "<b>" + sessionId + " : " + message + "</b>";
        str += "</div></div>";
        $("#msgArea").append(str);
    }
    else{
        var str = "<div class='col-6'>";
        str += "<div class='alert alert-warning'>";
        str += "<b>" + sessionId + " : " + message + "</b>";
        str += "</div></div>";
        $("#msgArea").append(str);
    }
}