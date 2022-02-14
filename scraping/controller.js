const websock = require("socket.io-client")
var SockJS = require('sockjs-client');

const sample_data = {
    username: "wdt0818@naver.com",
    password: "jeon5376!!",
    type: "facebook",
    cmd: 'scraping',
    description: '',
    name: '',
    agentUUID: '',
    clientUUID: '01ec8ce7-97b0-1726-b4ea-5c8c36567748',
    from: 'client',
    ua: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36"
  };

let webMsg = {
    type: '',
    bubbles: false,
    cancelable: false,
    timeStamp: 0,
    data: ''
}
let webData = {
    username: "",
    password: "",
    type: "",
    cmd: "",
    description: "",
    name: "",
    agentUUID: "",
    clientUUID: "",
    from: "",
    ua: ""
  };

async function WebComm()
{
    try
    {
        // var socketWeb = await new SockJS("http://mysns.info/ws", null, { transports: ["websocket", "xhr-streaming", "xhr-polling"]});
        var socketWeb = await new SockJS("http://localhost:8888/ws", null, { transports: ["websocket", "xhr-streaming", "xhr-polling"]});

        // socketWeb.onmessage = onMessage;
        socketWeb.onopen = async (req) => {
            console.log(req);

            socketWeb.onmessage = async (msg) => {
                webMsg = msg;
                // if(Object.keys(webMsg.data).length = Object.keys(webData).length)
                // {
                //     webData = Object.assign(webMsg.data);
                //     scrapingData = await DockerComm();
                //     await ResponseProcess(scrapingData);
                //     console.log(webData);
                // }
                // else
                // {
                //     await ErrorProcesss("data length not match");
                //     console.log(webData);
                // }
    
                if((Object.keys(sample_data).length) == (Object.keys(webData).length))
                {
                    webData = Object.assign(sample_data);
                    let scrapingData = await DockerComm(webData);
                    // await ResponseProcess(scrapingData);
                    await console.log(webData);
                }
                else
                {
                    await ErrorProcesss("data length not match");
                    await console.log(webData);
                }
                
                
                // console.log(webMsg);
                await socketWeb.close();
            }
    
            socketWeb.onclose = (msg) => {
                console.log(msg);
            }
        };

        
        
    }
    catch(err)
    {
        console.log(err);
    }
}

async function DockerComm(_loginInfo)
{
    try{
        const socketDocker = await websock("ws://localhost:8888", {
            transports:  ["polling", "websocket"]
            })

        await socketDocker.on("connect", () => {
            const engine = socketDocker.io.engine;

            /* login info send */
            socketDocker.emit('loginInfo', _loginInfo);

            /* response process */
            socketDocker.on("response", async (req) =>{
                // console.log(req);
                return req;
            })

            // engine.on("close", (reason) => {
            //     console.log("socketDocker closed\n(" + reason + ")");
            // })

            /* docker socket disconnection msg */
            socketDocker.on("disconnect", (reason) =>{
                if(reason == "io server disconnect")
                    console.log("socketDocker disconnected\n(" + reason + ")");    
                else if(reason == "io client disconnect")
                    console.log("socketDocker disconnected\n(" + reason + ")");
                else if(reason == "ping timeout")
                    console.log("socketDocker disconnected\n(" + reason + ")");
                else if(reason == "transport close")
                    console.log("socketDocker disconnected\n(" + reason + ")");
                else if(reason == "transport error")
                    console.log("socketDocker disconnected\n(" + reason + ")");
            })
        });

    }
    catch(err)
    {
        console.log(err);
    }
}

function ResponseProcess(data)
{
    webData = Object.assign(sample_data);
    webData.cmd = "result";
    webData.from = "agent";
    webData.agentUUID = GetUUID();
    webData.result = data;
}

function ErrorProcesss(msg)
{   
    var errMsg = {error: msg};

    webData = Object.assign(sample_data);
    webData.cmd = "result";
    webData.from = "agent";
    webData.agentUUID = GetUUID();
    webData.result = [errMsg];
}

function GetUUID()
{
    function _s4() {
        return ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
      }
    return _s4() + _s4() + '-' + _s4() + '-' + _s4() + '-' + _s4() + '-' + _s4() + _s4() + _s4();
}

try
{
    WebComm();
}
catch(err)
{
    console.log(err);
}