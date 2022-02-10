const websock = require("socket.io-client")

const socket_cli = websock("ws://localhost:8888")

socket_cli.on("connect", (msg) => {
    console.log("connection")
    socket_cli.emit('loginInfo', {email: "wdt0818@naver.com", password: "jeon5376!!", type: "facebook"})
    socket_cli.on("disconnecting", () => {
        console.log(socket_cli.id);
    })
    // socket_cli.disconnect();
    // if(socket_cli.disconnected)
    // {
    //     console.log("disconnected")
        
    // } 
})


socket_cli.on("response", req => {
    console.log(req)
})