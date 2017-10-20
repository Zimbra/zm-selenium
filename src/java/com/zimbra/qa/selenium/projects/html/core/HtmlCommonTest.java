/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.html.core;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.*;
import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.testng.annotations.*;
import org.xml.sax.SAXException;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.html.ui.AppHtmlClient;

public class HtmlCommonTest {
	
	protected static Logger logger = LogManager.getLogger(HtmlCommonTest.class);
	protected AppHtmlClient app = null;

	private WebDriver webDriver = ClientSessionFactory.session().webDriver();
	WebElement we = null;

	/**
	 * BeforeMethod variables
	 * startingPage = the starting page before the test method starts
	 * startingAccount = the account to log in as
	 */
	protected AbsTab startingPage = null;
	protected Map<String, String> startingAccountPreferences = null;
	protected Map<String, String> startingAccountZimletPreferences = null;

	protected HtmlCommonTest() {
		logger.info("New "+ HtmlCommonTest.class.getCanonicalName());

		app = new AppHtmlClient();

		startingPage = app.zPageMain;
		startingAccountPreferences = new HashMap<String, String>();
		startingAccountZimletPreferences = new HashMap<String, String>();
	}

	/**
	 * Global BeforeSuite
	 * <p>
	 * <ol>
	 * <li>Start the DefaultSelenium client</li>
	 * </ol>
	 * <p>
	 * @throws HarnessException
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	@BeforeSuite( groups = { "always" } )
	public void commonTestBeforeSuite() throws HarnessException {
		logger.info("commonTestBeforeSuite: start");


		ConfigProperties.setAppType(ConfigProperties.AppType.HTML);

		try {
			webDriver = ClientSessionFactory.session().webDriver();
	
			Capabilities cp =  ((RemoteWebDriver)webDriver).getCapabilities();
			if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())) {				
				webDriver.manage().window().setPosition(new Point(0, 0));
				webDriver.manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
			}
			
		} catch (WebDriverException e) {
			logger.error("Unable to mobile app.", e);
			throw new HarnessException(e);
		}

		// Dynamic wait for App to be ready	
		final int maxRetry = 10;
		for ( int retry = 0; retry < maxRetry; retry++ ) {
			
			try
			{
				logger.info("Retry #" + retry);
				webDriver.navigate().to(ConfigProperties.getBaseURL());
				
				// If we made it here, everything is ok
				logger.info("App is ready!");
				break; // for ( int retry = 0; retry ...
				
			} catch (WebDriverException e) {
				if ( retry >= maxRetry ) {
					logger.error("Unable to open browser app.  Is a valid cert installed?", e);
					throw new HarnessException(e);
				} else {
					logger.info("App is still not ready...", e);
					SleepUtil.sleep(10000);
				}
			}
		}


		logger.info("commonTestBeforeSuite: finish");		
	}

	@BeforeClass( groups = { "always" } )
	public void commonTestBeforeClass() throws HarnessException {
		logger.info("commonTestBeforeClass: start");

		logger.info("commonTestBeforeClass: finish");

	}

	@BeforeMethod( groups = { "always" } )
	public void commonTestBeforeMethod() throws HarnessException {
		logger.info("commonTestBeforeMethod: start");


		// If test account preferences are defined, then make sure the test account
		// uses those preferences
		//
		if ( (startingAccountPreferences != null) && (!startingAccountPreferences.isEmpty()) ) {
			logger.debug("commonTestBeforeMethod: startingAccountPreferences are defined");

			StringBuilder settings = new StringBuilder();
			for (Map.Entry<String, String> entry : startingAccountPreferences.entrySet()) {
				settings.append(String.format("<a n='%s'>%s</a>", entry.getKey(), entry.getValue()));
			}
			ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
					+		"<id>"+ ZimbraAccount.AccountZCS().ZimbraId +"</id>"
					+		settings.toString()
					+	"</ModifyAccountRequest>");


			// Set the flag so the account is reset for the next test
			ZimbraAccount.AccountZCS().accountIsDirty = true;
		}

		// If test account zimlet preferences are defined, then make sure the test account
		// uses those zimlet preferences
		//
		if ( (startingAccountZimletPreferences != null) && (!startingAccountZimletPreferences.isEmpty()) ) {
			logger.debug("commonTestBeforeMethod: startingAccountPreferences are defined");
			ZimbraAccount.AccountZCS().modifyUserZimletPreferences(startingAccountZimletPreferences);
		}

		// If AccountZCS is not currently logged in, then login now
		if ( !ZimbraAccount.AccountZCS().equals(app.zGetActiveAccount()) ) {
			logger.debug("commonTestBeforeMethod: AccountZCS is not currently logged in");

			if ( app.zPageMain.zIsActive() )
				app.zPageMain.zLogout();

			app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

			// Confirm
			if ( !ZimbraAccount.AccountZCS().equals(app.zGetActiveAccount())) {
				throw new HarnessException("Unable to authenticate as "+ ZimbraAccount.AccountZCS().EmailAddress);
			}

		}

		// If a startingPage is defined, then make sure we are on that page
		if ( startingPage != null ) {
			logger.debug("commonTestBeforeMethod: startingPage is defined");

			// If the starting page is not active, navigate to it
			if ( !startingPage.zIsActive() ) {
				startingPage.zNavigateTo();
			}

			// Confirm that the page is active
			if ( !startingPage.zIsActive() ) {
				throw new HarnessException("Unable to navigate to "+ startingPage.myPageName());
			}

		}

		logger.info("commonTestBeforeMethod: finish");

	}

	@AfterSuite( groups = { "always" } )
	public void commonTestAfterSuite() throws HarnessException {
		logger.info("commonTestAfterSuite: start");
		webDriver.quit();
		logger.info("commonTestAfterSuite: finish");
	}

	@AfterClass( groups = { "always" } )
	public void commonTestAfterClass() throws HarnessException {
		logger.info("commonTestAfterClass: start");

		// For Ajax and Html, if account is considered dirty (modified),
		// then recreate a new account
		ZimbraAccount currentAccount = app.zGetActiveAccount();
		if (currentAccount != null 
				&& currentAccount.accountIsDirty 
				&& currentAccount == ZimbraAccount.AccountZCS()) {

			ZimbraAccount.ResetAccountZCS();

		}

		logger.info("commonTestAfterClass: finish");
	}

	/**
	 * Global AfterMethod
	 *
	 * @throws HarnessException
	 */
	@AfterMethod( groups = { "always" } )
	public void commonTestAfterMethod() throws HarnessException {
		logger.info("commonTestAfterMethod: start");

		logger.info("commonTestAfterMethod: finish");
	}

}
