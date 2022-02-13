const websock = require("socket.io-client")
const 

const sample_data = {   username: "wdt0818@naver.com",
                        password: "jeon5376!!",
                        type: "facebook",
                        uuid: "",
                        ua: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36"
                    };

async function DockerComm()
{
    try{
        const socketDocker = await websock("ws://localhost:8888", {
            transports:  ["polling", "websocket"]
            })

        await socketDocker.on("connect", () => {
            const engine = socketDocker.io.engine;

            /* login info send */
            socketDocker.emit('loginInfo', sample_data);

            /* response proccess */
            socketDocker.on("response", async (req) =>{
                console.log(req);
                // send thd result to web
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



try
{
    DockerComm();
}
catch(err)
{
    console.log(err);
}