

var SockJS = require('sockjs-client');
const { v4: uuidv4 } = require('uuid');
// uuidv4(); // ⇨ '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed'

let main = async () => {
  var websocket = new SockJS("http://localhost:8888/ws", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});
  var task = {}
  var agent_uuid = uuidv4() // 원래는 db 등록된 값
  var task_uuid =  uuidv4()

  function result(){
    task.agentUUID = agent_uuid
    task.result = [{ aa : "aa" }] // 스크랩핑 데이터
    let data = JSON.stringify(Object.assign({}, task, { from : 'agent', cmd : 'result' }))
    websocket.send(data);
    console.log("result sent!!")
  }
  
  function onClose(evt) {
      console.log(evt)
      console.log("bye")
      websocket.send("bye");
  }
  
  //채팅창에 들어왔을 때
  function onOpen(evt) {
    // 1. 에이전트는 등록시 아래 구분 전송
    websocket.send(JSON.stringify({ from : 'agent', cmd : 'new', uuid : 'uuid' }));
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