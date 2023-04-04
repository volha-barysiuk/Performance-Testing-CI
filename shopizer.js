const fs = require('fs')
const puppeteer = require('puppeteer')
const lighthouse = require('lighthouse/lighthouse-core/fraggle-rock/api.js')

const waitTillHTMLRendered = async (page, timeout = 30000) => {
  const checkDurationMsecs = 1000;
  const maxChecks = timeout / checkDurationMsecs;
  let lastHTMLSize = 0;
  let checkCounts = 1;
  let countStableSizeIterations = 0;
  const minStableSizeIterations = 2;

  while(checkCounts++ <= maxChecks){
    let html = await page.content();
    let currentHTMLSize = html.length; 

    let bodyHTMLSize = await page.evaluate(() => document.body.innerHTML.length);

    //console.log('last: ', lastHTMLSize, ' <> curr: ', currentHTMLSize, " body html size: ", bodyHTMLSize);

    if(lastHTMLSize != 0 && currentHTMLSize == lastHTMLSize) 
      countStableSizeIterations++;
    else 
      countStableSizeIterations = 0; //reset the counter

    if(countStableSizeIterations >= minStableSizeIterations) {
      console.log("Fully Rendered Page: " + page.url());
      break;
    }

    lastHTMLSize = currentHTMLSize;
    await page.waitForTimeout(checkDurationMsecs);
  }  
};

async function captureReport() {
	const browser = await puppeteer.launch({"headless": false, // false - to open chrome and watch, true - to run it headless (in docker)
											args: ['--allow-no-sandbox-job', '--allow-sandbox-debugging', '--no-sandbox', '--disable-gpu', '--disable-gpu-sandbox', '--display', '--ignore-certificate-errors', '--disable-storage-reset=true']});
											//arguments in '--' for 1)opening Chrome without a window/display; 2) ignore certificate; 3) way of clicking

	const page = await browser.newPage();
	const baseURL = "http://localhost/";
	
	await page.setViewport({"width":1920,"height":1080});
	await page.setDefaultTimeout(10000);
	
	const navigationPromise = page.waitForNavigation({timeout: 30000, waitUntil: ['domcontentloaded']});
	await page.goto(baseURL);
    await navigationPromise;
		
	const flow = await lighthouse.startFlow(page, {
		name: 'Shopizer',
		configContext: {
		  settingsOverrides: {
			throttling: {
			  rttMs: 40,
			  throughputKbps: 10240,
			  cpuSlowdownMultiplier: 1,
			  requestLatencyMs: 0,
			  downloadThroughputKbps: 0,
			  uploadThroughputKbps: 0
			},
			throttlingMethod: "simulate",
			screenEmulation: {
			  mobile: false,
			  width: 1920,
			  height: 1080,
			  deviceScaleFactor: 1,
			  disabled: false,
			},
			formFactor: "desktop",
			onlyCategories: ['performance'],
		  },
		},
	});

  	//================================NAVIGATE================================
    await flow.navigate(baseURL, {
		stepName: 'Open main page'
		});
  	console.log('Main page is opened');
	
	//================================SELECTORS================================
	const tablesTab = "[class*='main-menu'] a[href='/category/tables']";
	const tableImg = " [class*='shop-area'] [class*='product-wrap'] img";
	const addToCartBtn = "//*[contains(@class, 'product-details-content')]//button[text()='Add to cart']";
	const cartItems = "button.icon-cart .count-style";
	const cartIcon = "button.icon-cart";
	const cartViewBtn = ".shopping-cart-content.active a[href='/cart']";
	const checkoutBtn = "//a[text()='Proceed to Checkout']";
	const placeOrderBtn = "//button[text()='Place your order']";

	//================================PAGE_ACTIONS================================
	await page.waitForSelector(tablesTab);
	await flow.startTimespan({ stepName: 'Navigate to Tables' });
		await page.click(tablesTab);
        await waitTillHTMLRendered(page);
		await page.waitForSelector(tableImg);
        await flow.endTimespan();
    console.log('User navigated to Tables shop area');

	await flow.startTimespan({ stepName: 'Click on a table' });
		await page.click(tableImg);
        await waitTillHTMLRendered(page);
		await page.waitForXPath(addToCartBtn);
        await flow.endTimespan();
    console.log('Table Details page is opened');

	await flow.startTimespan({ stepName: 'Add table to Cart' });
		let addToCart = await page.$x(addToCartBtn);
		await addToCart[0].click();
        await waitTillHTMLRendered(page);
		await page.waitForSelector(cartItems);
		console.log('User clicked Add to Cart button');
		const numberOfItemsInCart = await page.$eval(cartItems, el => el.innerText)
		await console.log('Number of items in Cart is: ', numberOfItemsInCart);
    await flow.endTimespan();
	
	if(numberOfItemsInCart == 0) {
    console.log('Failed to add table to Cart');
	} 
	else {
	console.log('Table is added to Cart');
	await flow.startTimespan({ stepName: 'Open Cart and proceed to checkout' });
		//await page.evaluate(()=>document.querySelector('button.icon-cart').click());
		//await page.evaluate(()=>document.querySelector('.shopping-cart-content.active a[href="/cart"]').click());
		await page.click(cartIcon);
		await waitTillHTMLRendered(page);
		await page.click(cartViewBtn);
		await waitTillHTMLRendered(page);
	    await page.waitForXPath(checkoutBtn);
	    await waitTillHTMLRendered(page);
		let checkout = await page.$x(checkoutBtn);
		await checkout[0].click();
        await waitTillHTMLRendered(page);
		await page.waitForXPath(placeOrderBtn);
    await flow.endTimespan();
    console.log('User proceeded to checkout');
	}
	
	//================================REPORTING================================
	const report = await flow.generateReport();
	const reportJson = JSON.stringify(await flow.createFlowResult(), null, 2);
			//for lighthouse v.8 use syntax flow.getFlowResult()).replace(/</g, '\\u003c').replace(/\u2028/g, '\\u2028').replace(/\u2029/g, '\\u2029');

	const reportPath = __dirname + '/user-flow.report.html'; //folder for html report
	const reportPathJson = __dirname + '/user-flow.report.json'; //folder for json report
	fs.writeFileSync(reportPath, report); //write html report
	fs.writeFileSync(reportPathJson, reportJson); //write json report
	console.log("Report >> "+reportPath);
	
    await browser.close();
}
captureReport();