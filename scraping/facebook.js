const peppeteer = require("puppeteer");
const screenShot = 'facebook.png'
const scraper = async() => {
    try {
        // const browser = await peppeteer.launch({headless: false, args:['--window-size=1920,1080','--disable-notifications']});
        
        const browser = await peppeteer.launch({headless: true, args: ['--disable-notifications']});
        const page = await browser.newPage();
        await page.setViewport({
            width:1920,
            height:1080
        })
        const id = "wdt0818@naver.com";
        const pw = "jeon5376!!";
        await page.goto('https://facebook.com');
        
        await page.evaluate((id, pw) => {
            document.querySelector("#email").value = id;
            document.querySelector("#pass").value = pw;
//            document.querySelector("button[type=submit]").click();
        }, id, pw)

        await page.click("button[type=submit]");
        await page.waitForNavigation();
        // await page.keyboard.press("Escape");
        // await page.screenshot({path: screenShot});

        await page.goto('https://www.facebook.com/settings?tab=applications&ref=settings');

        const loginList = await page.$x('.//div[@class="pow20xho"]/div/div/div/div/div/div/span[@class="d2edcug0 hpfvmrgz qv66sw1b c1et5uql lr9zc1uh a8c37x1j fe6kdd0r mau55g9w c8b282yb keod5gw0 nxhoafnm aigsh9s9 d3f4x2em iv3no6db jq4qci2q a3bd9o3v lrazzd5p oo9gr5id hzawbc8m"]');
        const text = await page.evaluate(loginList => loginList.textContent, loginList[1]);
        console.log(text);

        browser.close();
    } catch(err) {
        console.log(err);
    }
}

// const screenShot = 'facebook.png';

// const scraper2 = async() => {
//     try {
//         const
//     }
// }

scraper();
