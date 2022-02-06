const objFacebook = require("./facebook");
const objGoogle = require("./google");

const snsFlag = {   "facebook" : 0, 
                    "google" : 0, 
                    "instargram" : 0, 
                    "naver" : 0, 
                    "kakao": 0
                };

const resultEnum = { "SUCCESS" : 0, "FAIL" : 1, "PENDING" : 2};

/* Execute each scrapping method according to "snsFlag" */
async function MainLoop()
{   
    let datas = [];
    try{
        for (var key in snsFlag)
        {  
            if(snsFlag[key])
            {
                console.log(key + " scraper is runing...");

                const res = await Scraper(key);
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
    }
    catch(err)
    {
        console.log(err);
    }
}

/* Scraper running function */
async function Scraper(site) 
{   
    let srp_data;
    if (site == "facebook")
    {
        srp_data = await objFacebook.FacebookScraper();
        return srp_data;
    }    
    
    else if( site == "google")
    {
        srp_data = await objGoogle.GoogleScraper();
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

/* Setting the snsFlag object */
// var FlagList = [1, 1, 1, 1, 1];
var FlagList = [1, 0, 0, 0, 0];
for (var i=0; i<FlagList.length; i++)
    snsFlag[Object.keys(snsFlag)[i]] = FlagList[i];

try{
    MainLoop();
}
catch (err){
    console.log(err);
}