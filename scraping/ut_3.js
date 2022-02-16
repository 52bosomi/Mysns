

var SockJS = require('sockjs-client');
const { v4: uuidv4 } = require('uuid');
// uuidv4(); // ⇨ '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed'

let main = async () => {
  var websocket = new SockJS("http://localhost:8888/ws", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});
  // var websocket = new SockJS("http://mysns.info/ws", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});
  var task = {}
  var agent_uuid = uuidv4() // 원래는 db 등록된 값
  var task_uuid =  uuidv4()

  function result(){
    task.agentUUID = agent_uuid
    task.result = [
      { title : "사람인앱", type : 'facebook', url : 'url', join_at : '' },
      { title : "잡코리아", type : 'facebook', url : 'url', join_at : '' }
    ] // 스크랩핑 데이터
    let x = {
      username: 'wdt0818@naver.com',
      password: 'jeon5376!!',
      cmd: 'result',
      description: '',
      name: '',
      agentUUID: '',
      clientUUID: '01ec8ed5-7179-1d1f-a18a-8244191771ee',
      from: 'agent',
      type: 'facebook',
      ua: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36',
      result: [
        { title: '사람인 앱', type: 'facebook', url: ' ', join_at: ' ' },
        { title: '잡코리아', type: 'facebook', url: ' ', join_at: ' ' }
      ]
    }
    let data = JSON.stringify(x)
    websocket.send(data);
    console.log("result sent!!")
    setTimeout(() => {
      process.exit()
    }, 1000);
  }
  
  function onClose(evt) {
      console.log(evt)
      console.log("bye")
      websocket.send("bye");
  }
  
  //채팅창에 들어왔을 때
  function onOpen(evt) {
    // 1. 에이전트는 등록시 아래 구분 전송
    websocket.send(JSON.stringify({ from : 'agent', cmd : 'new', agentUUID : agent_uuid }));
    setTimeout(() => {
      result()
    }, 3000)
  }

  function onMessage(msg) {
    let x = JSON.parse(msg.data)
    if(x.cmd == 'scraping' && x.from == 'client') {
      console.log("get job sent!!")
      task = Object.assign({}, JSON.parse(msg.data))
      setTimeout(function() { result() }, 5*1000)
    }
    
    console.log(x)
  }

  websocket.onmessage = onMessage;
  websocket.onopen = onOpen;
  websocket.onclose = onClose;
  websocket.onerror = (x) => {
    console.log("x", x)
  };

}

main();