
const http_serv = require("http").createServer()


const io = require("socket.io")(http_serv)
io.on('connection', socket => {
    socket.on('message', msg => {
        console.log(msg)
        io.emit('response', "HIHI")
    })
})

http_serv.listen(3000, () => {
    console.log("포트 3000 open")
})
