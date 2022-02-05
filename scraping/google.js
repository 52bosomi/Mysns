const puppeteer = require('puppeteer')
const config = require('./config')

if(process.env.NODE_ENV !== 'production') {
  // for dev account!!! careful leack!!!!
  process.env.USERNAME_GOOGLE = 'mysns.devops@gmail.com'
  process.env.PASSWORD_GOOGLE = 'mysns_password!0M'
}

const GoogleScraper = async () => {
  let browser = await puppeteer.launch(config)
  try {
    const context = await browser.createIncognitoBrowserContext()
    const page = await context.newPage()
    /* Set the window agent */
    await page.setUserAgent(
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36"            
    );
    const navigationPromise = page.waitForNavigation()

    await page.goto('https://accounts.google.com/')
    await navigationPromise
  
    await page.waitForSelector('input[type="email"]')
    await page.click('input[type="email"]')
    await navigationPromise
  
    await page.type('input[type="email"]', process.env.USERNAME_GOOGLE)
    await page.waitForSelector('#identifierNext')
    await page.click('#identifierNext')
    await navigationPromise
  
    // 타이밍 조정용, 최대 3번 max
    let reqtry = 0
    while (reqtry < 3){
      try {
        await page.waitFor(1000)
        await page.waitForSelector('input[type="password"]')
        break
      } catch (e) {
        console.log('retry!!!!', e)
      } finally {
        reqtry += 1
      }
    }
  
    await page.type('input[type="password"]', process.env.PASSWORD_GOOGLE)
    await page.waitForSelector('#passwordNext')
    // async wait all resolve
    await Promise.all([
      page.click('#passwordNext'),
      // page.click('button[type=submit]'),
      page.waitForNavigation({waitUntil: 'networkidle2'})
    ])
  
    await navigationPromise
    await page.goto('https://myaccount.google.com/security') // goto my page security tab
  
    // for fake, like a human
    await page.keyboard.press('ArrowUp')
    await page.waitFor(800)
    await page.keyboard.press('ArrowDown')
    await page.evaluate(async () => {
      await new Promise((resolve, reject) => {
        var totalHeight = 0;
        var distance = 100;
        var timer = setInterval(() => {
          // var scrollHeight = document.body.scrollHeight;
          var scrollHeight = window.outerHeight
          window.scrollBy(0, distance)
          totalHeight += distance;
          console.log(totalHeight, scrollHeight)
          if(totalHeight >= scrollHeight * 0.9){
            clearInterval(timer)
            resolve()
          }
        }, 150)
      })
    })
  
    
    await page.waitFor(1000)
    await navigationPromise

    await Promise.all([
      page.goto('https://myaccount.google.com/permissions?continue=https%3A%2F%2Fmyaccount.google.com%2Fsecurity%3Fpli%3D1'),
      // page.click('button[type=submit]'),
      page.waitForNavigation({waitUntil: 'networkidle2'})
    ])
    await navigationPromise

    // start data scraping
    const body = await page.content()
    let match = body.matchAll(/AF_initDataCallback.*data:(.*), sideChannel/gm)
    let data = match.next().value

    // 좀 복잡함, 최대한 잘 처리 되도록!!!
    let rt_data = []
    if(data.length < 2) {
      console.log('shit!!!! not found from data content')
    } else {
      let _struct = JSON.parse(data[1])
      let _last = _struct[_struct.length - 1]

      if(_last.length > 1) {
        for (let l = 0; l < _last[0].length; l++) {
          const target = _last[0][l];
          rt_data.push({
            title : target[1],
            type : 'google',
            url : target[14].join(','),
            join_at :  target[3]
          })
          // console.log(target[1]) // title
          // console.log(target[3]) // join_at
          // console.log(target[13]) // sso url
          // console.log(target[14]) // domain url
        }
      }
    }

    console.log(rt_data)
  } catch (e) {
    console.log(e)
    browser ? await browser.close() : ''
  }
  console.log('End!!!')
}

exports.GoogleScraper = GoogleScraper;
// description!!
// 보안 이슈로 특정 계정은 로그인 안될 수 있음