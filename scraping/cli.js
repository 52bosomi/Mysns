const websock = require("socket.io-client")

const socket_cli = websock("ws://localhost:3000")

socket_cli.on("connect", () => {
    console.log("connection")
})
socket_cli.emit('message', 'hello world')

socket_cli.on("response", req => {
    console.log(req)
})