const objFacebook = require("./facebook");
const objGoogle = require("./google");
// const objHttp = require("http");
const objSocket = require("socket.io");
const { io } = require("socket.io-client");

const snsFlag = {   "facebook" : 0, 
                    "google" : 0, 
                    "instargram" : 0, 
                    "naver" : 0, 
                    "kakao": 0
                };

const resultEnum = { "SUCCESS" : 0, "FAIL" : 1, "PENDING" : 2};

let loginInfo;

/* Execute each scrapping method according to "snsFlag" */
async function MainLoop(loginInfo)
{   
    let datas = [];
    try{
        for (var key in snsFlag)
        {  
            if(snsFlag[key])
            {
                console.log(key + " scraper is runing...");

                const res = await Scraper(key, loginInfo);
                if (res != resultEnum.FAIL)
                {   
                    for (var i=0; i<res.length; i++)
                    {
                        datas.push(res[i]);
                    }
                    // datas.push(res);
                    console.log(key + "scraper finished successfuly");    
                }
                else
                {
                    console.log(key + "scraper fail");
                }
                console.log(key + "scraper is end...");
            }
            
        }
        console.log(datas);
        return datas;
    }
    catch(err)
    {
        console.log(err);
    }
}

/* Scraper running function */
async function Scraper(site, account) 
{   
    let srp_data;
    if (site == "facebook")
    {
        srp_data = await objFacebook.FacebookScraper(account);
        return srp_data;
    }    
    
    else if( site == "google")
    {
        srp_data = await objGoogle.GoogleScraper(account);
        return srp_data;
    }
    // else if( site == "google")
    //     continue;
    // else if( site == "google")
    //     break;
    // else if( site == "google")
    //     break;
    else
        return resultEnum.FAIL;
}


async function Run()
{   
    try
    {   
        /* websocket connection part  */ 
        // const httpServer = objHttp.createServer();
        const io = new objSocket.Server(8888, {
        });
        io.on("connection", socket => {
            console.log(socket.id+ "(id)" + " connect")
            socket.on("loginInfo", async info => {
                await console.log(info);
                loginInfo = info;
                // await io.disconnectSockets();
                // await io.close();
                
                /* Setting the snsFlag object */
                for (var i=0; i<Object.keys(snsFlag).length; i++)
                {   
                    if(loginInfo.type == Object.keys(snsFlag)[i])
                    {
                        snsFlag[Object.keys(snsFlag)[i]] = 1;
                    }
                }
                let res = await MainLoop(loginInfo);
                await io.emit("response", res);
                await io.disconnectSockets();
                await io.close();
                await io.removeAllListeners();
            })
        })

        io.on("close", socket => {
            console.log(socket.id + "(id)" + " close")
        })

        io.on("connect_error", (err) => {
            console.log(err);
        })
    }
    catch(err)
    {
        console.log(err);
    }
}

try{
    Run();
}
catch(err)
{
    console.log(err);
}
