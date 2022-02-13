const peppeteer = require("puppeteer");
const dotenv = require('dotenv');
dotenv.config();

/* Return value */
const resultEnum = { "SUCCESS" : 0, "FAIL" : 1, "PENDING" : 2};

/* Email and password setting */
if(process.env.NODE_ENV !== 'production') {
    // for dev account!!! careful leack!!!!
    process.env.EMAIL_FACEBOOK = 'wdt0818@naver.com'
    process.env.PASSWORD_FACEBOOK = 'jeon5376!!'
    process.env.XPATH_FACEBOOK = './/div[@class="pow20xho"]/div/div/div/div/div/div/span[@class="d2edcug0 hpfvmrgz qv66sw1b c1et5uql lr9zc1uh a8c37x1j fe6kdd0r mau55g9w c8b282yb keod5gw0 nxhoafnm aigsh9s9 d3f4x2em iv3no6db jq4qci2q a3bd9o3v lrazzd5p oo9gr5id hzawbc8m"]'
}

const FacebookScraper = async (loginInfo) => {
    try {   
        /* UI MODE */
        // const browser = await peppeteer.launch({headless: false, args:['--window-size=1920,1080','--disable-notifications']});
        
        /* CLI MODE */
        const browser = await peppeteer.launch({headless: true, args: ['--disable-notifications', '--no-sandbox', '--disable-setuid-sandbox' ]});
        // await browser.userAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36');
        const page = await browser.newPage();
        /* Set the window agent */
        await page.setUserAgent(
            loginInfo.ua
            // "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36"            
        );
        await page.setViewport({
            width:1920,
            height:1080
        });

        await page.goto('https://facebook.com');
        
        /* Input the email and password */
        // await page.evaluate((id, pw) => {
        //     document.querySelector("#email").value = id;
        //     document.querySelector("#pass").value = pw;
        // }, process.env.EMAIL_FACEBOOK, process.env.PASSWORD_FACEBOOK)
        await page.evaluate((id, pw) => {
            document.querySelector("#email").value = id;
            document.querySelector("#pass").value = pw;
        }, loginInfo.username, loginInfo.password)
        
        /* Click the login button */
        await page.click("button[type=submit]");
        await page.waitForNavigation();

        /* Go to the "APP & WEBSITE" page */
        await page.goto('https://www.facebook.com/settings?tab=applications&ref=settings', {waitUntil: "networkidle2"});

        // const xpath = './/div[@class="' + process.env.MAIN_CLASS + '"]/div/div/div/div/div/div/span[@class="' + process.env.SPAN_CLASS + '"]';
        
        /* Get the account info */
        const loginList = await page.$x(process.env.XPATH_FACEBOOK);
        // const loginList = await page.$x(xpath);
        // const loginList = await page.$('.//pow20xho');

        /* Parsing the text and make a format */
        let data = {};
        let datas = [];

        if(loginList.length == 0)
        {
            console.log("Can't find data");
            browser.close();
            return resultEnum.FAIL;
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
