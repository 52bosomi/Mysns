const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({headless: false});
  const page = await browser.newPage();
  await page.goto('https://www.instagram.com/accounts/login/');
  await page.wait('input[name="username"]');
  await page.focus('input[name="username"]');
  await page.keyboard.type('mysns.devops@gmail.com');
  await page.focus('input[name="password"]');
  await page.keyboard.type('wpqkfehlfkrh!!');
  await page.click('button[type="submit"]');
  await new Promise(r => setTimeout(r, 5000));

  await browser.close();
})();