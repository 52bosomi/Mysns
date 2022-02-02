
let io = require('socket.io-client')
// let option = { transports: ["websocket"], path: "/ws/agent" }
// let socket = io("http://localhost:8888", option);


const socket = io("ws://localhost:8888", {
  // transports: ["websocket"], // use WebSocket first, if available
  path: "/ws"
});

socket.on("connect_error", (x) => {
  console.log("connect_error", x)
  // revert to classic upgrade
  // socket.io.opts.transports = ["polling", "websocket"];
});

socket.on("connect", (x) => {
  // revert to classic upgrade
  // socket.io.opts.transports = ["polling", "websocket"];
  console.log(x)
});

 
let main = async () => {
  // socket.on("connect", () => {
  //   // const engine = socket.io.engine;
  //   console.log(engine.transport.name); // in most cases, prints "polling"
  
  //   socket.once("upgrade", (x) => {
  //     console.log(x)
  //     // called when the transport is upgraded (i.e. from HTTP long-polling to WebSocket)
  //     // console.log(socket.transport.name); // in most cases, prints "websocket"
  //   });
  
  //   socket.on("packet", ({ type, data }) => {
  //     console.log(type, data)
  //     // called for each packet received
  //   });
  
  //   socket.on("packetCreate", ({ type, data }) => {
  //     console.log(type, data)
  //     // called for each packet sent
  //   });
  
  //   socket.on("drain", (x) => {
  //     console.log(x)
  //     // called when the write buffer is drained
  //   });
  
  //   socket.on("close", (reason) => {
  //     // called when the underlying connection is closed
  //   });
  // });

  let wait = (x) => {
    return new Promise((resolve, reject) => {
      return setTimeout(resolve, x * 1000)
    })
  }

  console.log(socket)
  console.log("socketsocketsocketsocketsocket")
  // while (true) {
  //   // console.log(websocket)
  //   await wait(1)
  //   console.log('loop', new Date())
  // }
}

try {
  main();
} catch (e) {
  console.log(e)
}