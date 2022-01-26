

var SockJS = require('sockjs-client');
// var sockjs = new SockJS(url, _reserved, options);

let main = async () => {
  var websocket = new SockJS("http://localhost:8888/ws/agent", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});
// var client = sjsc.create("http://localhost:8888/ws/agent");

console.log(websocket)
username = "WTF"
// sockjs.on('connection', function () {
//   // connection is established
// });
// sockjs.on('data', function (msg) {
//   // received some data
// });
// sockjs.on('error', function (e) { // something went wrong });
//   sockjs.write("Have some text you mighty SockJS server!"); })

  function send(){
    websocket.send("WTF");
  }
  
  //채팅창에서 나갔을 때
  function onClose(evt) {
      // var str = username + ": 님이 방을 나가셨습니다.";
      console.log(evt)
      websocket.send("bye");
  }
  
  //채팅창에 들어왔을 때
  function onOpen(evt) {
    console.log(evt)
      websocket.send('hi');
  }

  function onMessage(msg) {
      var data = msg.data;
      var sessionId = null;
      var message = null;
      console.log(message, data, sessionId)
  }

  websocket.onmessage = onMessage;
  websocket.onopen = onOpen;
  websocket.onclose = onClose;

  let wait = (x) => {
    return new Promise((resolve, reject) => {
      return setTimeout(resolve, x * 1000)
    })
  }
  while (true) {
    await wait(1)
    console.log('loop', new Date())
  }
}

main();