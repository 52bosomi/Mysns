const peppeteer = require("puppeteer");
const dotenv = require('dotenv');
dotenv.config();
const screenShot = 'facebook.png'
const scraper = async() => {
    try {
        // const browser = await peppeteer.launch({headless: false, args:['--window-size=1920,1080','--disable-notifications']});
        
        const browser = await peppeteer.launch({headless: true, args: ['--disable-notifications']});
        const page = await browser.newPage();
        await page.setViewport({
            width:1920,
            height:1080
        });

        await page.goto('https://facebook.com');
        
        await page.evaluate((id, pw) => {
            document.querySelector("#email").value = id;
            document.querySelector("#pass").value = pw;
        }, process.env.EMAIL, process.env.PASSWORD)

        await page.click("button[type=submit]");
        await page.waitForNavigation();

        await page.goto('https://www.facebook.com/settings?tab=applications&ref=settings');
        await page.waitFor(500);

        const xpath = './/div[@class="' + process.env.MAIN_CLASS + '"]/div/div/div/div/div/div/span[@class="' + process.env.SPAN_CLASS + '"]';
        
        // const loginList = await page.$x(process.env.XPATH);
        const loginList = await page.$x(xpath);
        // const loginList = await page.$('.//pow20xho');

        let data = {};
        let datas = [];

        if(loginList.length == 0)
        {
            console.log("Can't find data");
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
            console.log(datas);
        }
        browser.close();
    } catch(err) {
        console.log(err);
    }

    finally{
    }
}


scraper();
