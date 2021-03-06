const objFacebook = require("./facebook");
const objGoogle = require("./google");
const SockJS = require('sockjs-client');

const resultEnum = {    "SUCCESS" : 0, 
                        "FAIL" : 1, 
                        "PENDING" : 2, 
                        "SCRP_FAIL" : 3, 
                        "USERNAME_ERR": 4,
                        "PASSWORD_ERR": 5,
                    };

// info
let UUID = "01ec8ce7-97b0-1726-b4ea-5c8c36567748" // 나중에 환경 변수로 읽어 올 예정

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


/* Execute each scrapping method according to "snsFlag" */
async function MainLoop(loginInfo)
{   
    try{
        let scrapingResult;
        console.log(loginInfo.type + " scraper is runing...");

        const res = await Scraper(loginInfo.type, loginInfo);
        console.log(loginInfo.type + "scraper is end...");
        return res;
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
        // var socketWeb = await new SockJS("http://localhost:8888/ws", null, { transports: ["websocket", "xhr-streaming", "xhr-polling"]}); // for dev

        socketWeb.onopen = async (req) => {
            // console.log(req);
            console.log('connected!!! starting upgrade');
            socketWeb.send(JSON.stringify({ from : 'agent', cmd : 'new', agentUUID : UUID }));
        };

        socketWeb.onmessage = async (msg) => {
            let response;
            let scrapingData = {}
            try {
                webMsg = JSON.parse(msg.data);

                console.log('working with data', webMsg)

                // upgrade
                if(webMsg.cmd == 'upgrade' && webMsg.from == 'server') {
                    console.log("upgrade done!!!, stand-by")
                    return 
                }

                // task work
                if(webMsg.cmd == 'scraping' && webMsg.from == 'client') {
                    scrapingData = await MainLoop(webMsg);
                    if(scrapingData == resultEnum.SCRP_FAIL)
                    {
                        response = Object.assign({}, webMsg, { result : "Scraping fail", from : 'agent', cmd : 'result', agentUUID : UUID, isError: true });
                    } else if (scrapingData == resultEnum.USERNAME_ERR) {
                        response = Object.assign({}, webMsg, { result : "Username wrong", from : 'agent', cmd : 'result', agentUUID : UUID, isError: true });
                    } else if (scrapingData == resultEnum.PASSWORD_ERR) {
                        response = Object.assign({}, webMsg, { result : "Password wrong", from : 'agent', cmd : 'result', agentUUID : UUID, isError: true });
                    } else if (scrapingData == resultEnum.FAIL) {
                        response = Object.assign({}, webMsg, { result : "sns type error", from : 'agent', cmd : 'result', agentUUID : UUID, isError: true });
                    } else {
                        response = Object.assign({}, webMsg, { result : scrapingData, from : 'agent', cmd : 'result', agentUUID : UUID })
                    }
                    
                    socketWeb.send(JSON.stringify(response))

                    console.log('done', response)

                    setTimeout(() => {
                        socketWeb.close();
                        process.exit()
                    }, 1000)
                }

            } catch (e) {
                // TODO : 예외 처리 해야 함, 에러일 경우 전송하는 메세지에 isError : true 세팅 필요
                response = Object.assign({}, webMsg, { result : e, from : 'agent', cmd : 'result', agentUUID : UUID, isError: true });
                socketWeb.send(JSON.stringify(response));
                console.log("e", e)
            }
        }

        socketWeb.onclose = (msg) => {
            console.log(msg);
        }
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
