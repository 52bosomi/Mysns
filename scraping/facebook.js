const peppeteer = require("puppeteer");

/* Return value */
const resultEnum = { "SUCCESS" : 0, "FAIL" : 1, "PENDING" : 2};

const FacebookScraper = async (loginInfo) => {
    try {        
        /* CLI MODE */
        const browser = await peppeteer.launch({headless: true, args: ['--disable-notifications', '--no-sandbox', '--disable-setuid-sandbox' ]});
        const page = await browser.newPage();
        /* Set the window agent */
        await page.setUserAgent(loginInfo.ua);
        await page.setViewport({
            width:1920,
            height:1080
        });

        await page.goto('https://facebook.com');
        
        /* Input the email and password */
        await page.evaluate((id, pw) => {
            document.querySelector("#email").value = id;
            document.querySelector("#pass").value = pw;
        }, loginInfo.username, loginInfo.password)
        // await page.evaluate((id, pw) => {
        //     document.querySelector("#email").value = id;
        //     document.querySelector("#pass").value = pw;
        // }, process.env.EMAIL_FACEBOOK ? loginInfo.username : process.env.EMAIL_FACEBOOK, process.env.PASSWORD_FACEBOOK ? process.env.PASSWORD_FACEBOOK : loginInfo.password )
        
        /* Click the login button */
        await page.click("button[type=submit]");
        await page.waitForNavigation();

        /* Go to the "APP & WEBSITE" page */
        await page.goto('https://www.facebook.com/settings?tab=applications&ref=settings', {waitUntil: "networkidle2"});

        const xpath = './/div[@class="pow20xho"]/div/div/div/div/div/div/span[@class="d2edcug0 hpfvmrgz qv66sw1b c1et5uql oi732d6d ik7dh3pa ht8s03o8 a8c37x1j fe6kdd0r mau55g9w c8b282yb keod5gw0 nxhoafnm aigsh9s9 d9wwppkn iv3no6db jq4qci2q a3bd9o3v lrazzd5p oo9gr5id hzawbc8m"]';

        /* Get the account info */
        const loginList = await page.$x(xpath);

        /* Parsing the text and make a format */
        let data = {};
        let datas = [];

        if(loginList.length == 0)
        {
            console.log("Can't find data");
            browser.close();
            return datas;
        }
        else{
            for(let i=0; i<loginList.length; i++)
            {   
                data.name =  await page.evaluate((data) => {
                    return data.textContent;
                }, loginList[i]);

                datas.push({
                    title : data.name,
                    type : 'facebook',
                    url : ' ',
                    join_at : ' '
                });
            }
            browser.close();
            return datas;
        }
        
    } catch(err) {
        console.log(err);
    }

    finally{
    }
}

/* Declare the function to export*/
exports.FacebookScraper = FacebookScraper;


// FacebookScraper();
