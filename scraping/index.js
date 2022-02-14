const objFacebook = require("./facebook");
const objGoogle = require("./google");
const SockJS = require('sockjs-client');

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

const snsFlag = {   "facebook" : 0, 
                "google" : 0, 
                "instargram" : 0, 
                "naver" : 0, 
                "kakao": 0
            };

let webMsg = {
    type: '',
    bubbles: false,
    cancelable: false,
    timeStamp: 0,
    data: ''
};

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
        var socketWeb = await new SockJS("http://mysns.info/ws", null, { transports: ["websocket", "xhr-streaming", "xhr-polling"]});

        socketWeb.onopen = async (req) => {
            console.log(req);

            socketWeb.onmessage = async (msg) => {
                webMsg = msg;
                // if(Object.keys(webMsg.data).length = Object.keys(webData).length)
                // {
                //     webData = Object.assign(webMsg.data);

                //     /* Setting the snsFlag object */
                //     for (var i=0; i<Object.keys(snsFlag).length; i++)
                //     {   
                //         if(webData.type == Object.keys(snsFlag)[i])
                //         {
                //             snsFlag[Object.keys(snsFlag)[i]] = 1;
                //         }
                //     }
                    
                //     let scrapingData = await MainLoop(webData);
                //     // console.log(scrapingData);
                //     await ResponseProcess(scrapingData);
                //     console.log(webData);
                // }
                // else
                // {
                //     await ErrorProcesss("data length not match");
                //     console.log(webData);
                // }
                
                /* temp code */
                if((Object.keys(sample_data).length) == (Object.keys(webData).length))
                {   
                    webData = Object.assign(sample_data);

                    /* Setting the snsFlag object */
                    for (var i=0; i<Object.keys(snsFlag).length; i++)
                    {   
                        if(webData.type == Object.keys(snsFlag)[i])
                        {
                            snsFlag[Object.keys(snsFlag)[i]] = 1;
                        }
                    }
                    
                    let scrapingData = await MainLoop(webData);
                    // console.log(scrapingData);
                    await ResponseProcess(scrapingData);
                    console.log(webData);
                    
                    /* socket send */
                    await socketWeb.send(webData);
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

try{
    Run();
}
catch(err)
{
    console.log(err);
}
