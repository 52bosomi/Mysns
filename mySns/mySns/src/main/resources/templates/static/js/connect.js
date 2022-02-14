step = 0
is_step_done = false
sns_type = 'naver'
sns_type_list = []
sns_type_list_origin = []
uuid = ''
// 순서는 네이버, 구글, 페이스북, 인스타

$(document).ready(function() {
  // 로드 완료되고 연결 실시
  websocket = new SockJS("/ws", null, { transports : ["websocket", "xhr-streaming", "xhr-polling"]});
  // window['x'] = websocket

  function send() {
    let u = document.getElementById("username")
    let p = document.getElementById("password")
    if(!u.value || !p.value) {
      alert('계정 정보를 입력하세요')
      return false
    }

    // console.log(u, p)

    let data = {
      username : u.value,
      password : p.value,
      type : sns_type,
      clientUUID : uuid,
      ua : navigator.userAgent
    }
    websocket.send(JSON.stringify(data));
    return true
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
    let data = msg.data;

    // convert
    data = typeof data == typeof "" ? JSON.parse(data) : data


    console.log('data', 'from', data.result.from, 'agentUUID', data.result.agentUUID)
    if(!data.result.from.startsWith('agent'))
    {
      console.log('is not correct signal from agent')
      return
    }

    if(!data.result.agentUUID)
    {
      console.log('is not correct agent uuid')
      return
    }

    console.log(data)

    // 연동 완료 표시 및 다음 스탭 시작
    // 로딩중 표시
    $(`.loading`).hide();
    $(`.done`).show()
    $('#checker').hide()
    $('#btn-step').attr('disabled', false)
    is_step_done = true
    // $("#btn_next").hide()
    
  }

  websocket.onmessage = onMessage;
  websocket.onopen = onOpen;
  websocket.onclose = onClose;

  
  function start(x) { 
    sns_type_list = $('input:checkbox:checked').map(function() { return this.value}).get();
    console.log("sns_type_list", sns_type_list)
    sns_type_list_origin = sns_type_list.map(x => x) // 클론 방식 때문에 그럼

    // 버튼 변경
    $('#btn-start').attr('disabled', true)
    $('.btn-start').hide()
    $('.btn-step').show()

    loop()
    // setTimeout(() => loop(), 1000) // 화면 렌더링 이슈로 따로 실행
  }

  function loop() {

    if(sns_type_list.length < 1) { 
      if(sns_type_list_origin.length < 1) {
        return alert("연동 대상을 선택하세요!!") 
      }

      // 연동 완료!!!!!!!!!
      alert('연동이 완료 되었습니다, 상세 연동을 확인하세요')
      location.href = "/profile"

    }

    $('.start').hide() // 기존거 숨기기

    sns_type = sns_type_list.pop(0);
    console.log("start", sns_type, sns_type_list)


    // 입력 폼 활성화
    $('.step-input').show();
    
    // 기존거 숨기기
    $('.step').hide()

    // 해당하는 이미지만 보여주기
    $(`.step_${sns_type.toLowerCase()}`).show();

  }

  // 로그인 연동을 위해 계정 정보 전송
  function next() {

    console.log("next!!", is_step_done)

    if(is_step_done) {
      $(`.done`).hide()
      $('#checker').hide()

      // reset
      $(`#password`).val('')
      is_step_done = false
      return loop()
    }
    
    
    // 계정 정보 보내고 
    if(!send()) { return }

    // 기존거 숨기기
    $('.step').hide()

    // 입력 폼 비활성화
    $('.step-input').hide()

    // 로딩중 표시
    $(`.loading`).show(); 
    $("#btn_next").hide()
    $('#btn-step').attr('disabled', true)

    console.log('결과 대기중')
    
  }


  step = 0
  $('#checker').hide()
  $('.step-input').hide()
  $('.btn-step').hide()
  $('.btn-start').show()
  $('.step').hide() // 기존거 숨기기
  
  $('.loading').hide()
  $(`.done`).hide(); 
  $(".step_1").hide()
  $("#btn_next").click((x) => {
    console.log("x", x)
    next()
  })
  $("#username").enterKey(() => $("#password").focus())
  $("#password").enterKey(() => $(".btn-submit").click())
  
  $("#btn-start").click((x) => start(x))
  $("#btn-step").click((x) => next(x))


  console.log("already loaded")
})
