/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.core;

import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.testng.*;
import org.testng.annotations.*;
import org.xml.sax.SAXException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.pages.TouchPages;

public class TouchCore {

	protected static Logger logger = LogManager.getLogger(TouchCore.class);
	protected TouchPages app = null;

	private WebDriver webDriver = ClientSessionFactory.session().webDriver();
	WebElement we = null;

	/**
	 * BeforeMethod variables
	 * startingPage = the starting page before the test method starts
	 * startingAccountSettings = the account's settings (ModifyAccountRequest)
	 * startingAccountPreferences = the account's preferences (ModifyPrefsRequest)
	 * startingAccountZimletPreferences = the account's zimlet preferences (ModifyZimletPrefsRequest)
	 */
	protected AbsTab startingPage = null;
	protected Map<String, String> startingAccountPreferences = null;
	protected Map<String, String> startingUserPreferences = null;
	protected Map<String, String> startingUserZimletPreferences = null;

	protected TouchCore() {
		logger.info("New "+ TouchCore.class.getCanonicalName());

		app = TouchPages.getInstance();

		startingPage = app.zPageMain;
		startingAccountPreferences = new HashMap<String, String>();
		startingUserZimletPreferences = new HashMap<String, String>();
	}

	@BeforeSuite( groups = { "always" } )
	public void coreBeforeSuite()
	throws HarnessException, IOException, InterruptedException, SAXException {
		logger.info("coreBeforeSuite: start");
		ZimbraAccount.ResetAccountZCS();

		try
		{
			ConfigProperties.setAppType(ConfigProperties.AppType.TOUCH);

			webDriver = ClientSessionFactory.session().webDriver();
			Capabilities cp =  ((RemoteWebDriver)webDriver).getCapabilities();
			 if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())) {
				webDriver.manage().window().setPosition(new Point(0, 0));
				webDriver.manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
			}

			// Dynamic wait for App to be ready
			int maxRetry = 10;
			int retry = 0;
			boolean appIsReady = false;
			while (retry < maxRetry && !appIsReady) {
				try {
					logger.info("Retry #" + retry);
					retry ++;
					webDriver.navigate().to(ConfigProperties.getBaseURL());
					appIsReady = true;

				} catch (WebDriverException e) {
					if (retry == maxRetry) {
						logger.error("Unable to open admin app." +
								"  Is a valid cert installed?", e);
						throw e;
					} else {
						logger.info("App is still not ready...", e);
						SleepUtil.sleep(10000);
						continue;
					}
				}
			}
			logger.info("App is ready!");

		} catch (WebDriverException e) {
			throw new HarnessException("Unable to open app", e);
		} catch (Exception e) {
			logger.warn(e);
		}

		logger.info("coreBeforeSuite: finish");
	}

	/**
	 * Global BeforeClass
	 *
	 * @throws HarnessException
	 */
	@BeforeClass( groups = { "always" } )
	public void coreBeforeClass() throws HarnessException {
		logger.info("coreBeforeClass: start");

		logger.info("coreBeforeClass: finish");

	}



	/**
	 * Global BeforeMethod
	 * <p>
	 * <ol>
	 * <li>For all tests, make sure {@link #startingPage} is active</li>
	 * <li>For all tests, make sure {@link #startingAccountPreferences} is logged in</li>
	 * <li>For all tests, make any compose tabs are closed</li>
	 * </ol>
	 * <p>
	 * @throws HarnessException
	 */
	@BeforeMethod( groups = { "always" } )
	public void coreBeforeMethod(Method method, ITestContext testContext) throws HarnessException {
		logger.info("coreBeforeMethod: start");


		// Get the test description
		// By default, the test description is set to method's name
		// if it is set, then change it to the specified one
		for (ITestNGMethod ngMethod : testContext.getAllTestMethods()) {
			String methodClass = ngMethod.getRealClass().getSimpleName();
			if (methodClass.equals(method.getDeclaringClass().getSimpleName())
					&& ngMethod.getMethodName().equals(method.getName())) {
				synchronized (TouchCore.class) {
					logger.info("---------BeforeMethod-----------------------");
					logger.info("Test       : " + methodClass
							+ "." + ngMethod.getMethodName());
					logger.info("Description: " + ngMethod.getDescription());
					logger.info("----------------------------------------");
				}
				break;
			}
		}


		// If test account preferences are defined, then make sure the test account
		// uses those preferences
		//
		if ( (startingAccountPreferences != null) && (!startingAccountPreferences.isEmpty()) ) {
			logger.debug("coreBeforeMethod: startingAccountPreferences are defined");

			// If the current test accounts preferences match, then the account can be used
			if ( !ZimbraAccount.AccountZCS().compareAccountPreferences(startingAccountPreferences) ) {

				logger.debug("coreBeforeMethod: startingAccountPreferences do not match active account");

				// Reset the account
				ZimbraAccount.ResetAccountZCS();

				// Create a new account
				// Set the preferences accordingly
				ZimbraAccount.AccountZCS().modifyAccountPreferences(startingAccountPreferences);
				ZimbraAccount.AccountZCS().modifyUserZimletPreferences(startingUserZimletPreferences);

			}

		}

		// If AccountZCS is not currently logged in, then login now
		if ( !ZimbraAccount.AccountZCS().equals(app.zGetActiveAccount()) ) {
			logger.debug("coreBeforeMethod: AccountZCS is not currently logged in");

			if ( app.zPageMain.zIsActive() )
				try {
					app.zPageMain.zLogout();

				} catch(Exception ex) {
					if ( !app.zPageLogin.zIsActive()) {
						logger.error("Login page is not active ", ex);

						/* Commenting below code because touch client always asks for navigate away */
						//app.zPageLogin.sOpen(ConfigProperties.getLogoutURL());
						//app.zPageLogin.sOpen(ConfigProperties.getBaseURL());
					}
				}
		}

		// If a startingPage is defined, then make sure we are on that page
		if ( startingPage != null ) {
			logger.debug("coreBeforeMethod: startingPage is defined");

			// If the starting page is not active, navigate to it
			if ( !startingPage.zIsActive() ) {
				startingPage.zNavigateTo();
			}

			// Confirm that the page is active
			if ( !startingPage.zIsActive() ) {
				throw new HarnessException("Unable to navigate to "+ startingPage.myPageName());
			}

		}

		logger.info("coreBeforeMethod: finish");

	}

	@AfterSuite( groups = { "always" } )
	public void coreAfterSuite() throws HarnessException {
		logger.info("coreAfterSuite: start");
		webDriver.quit();
		logger.info("coreAfterSuite: finish");
	}

	@AfterClass( groups = { "always" } )
	public void coreAfterClass() throws HarnessException {
		logger.info("coreAfterClass: start");

		// For Touch, if account is considered dirty (modified),
		// then recreate a new account, but for the zimlet
		// preferences has to be reset to default, all core zimlets are enabled
		ZimbraAccount currentAccount = app.zGetActiveAccount();
		if (currentAccount != null && currentAccount.accountIsDirty &&
				currentAccount == ZimbraAccount.AccountZCS()) {
			// Reset the account
			ZimbraAccount.ResetAccountZCS();

		}

		logger.info("coreAfterClass: finish");
	}

	/**
	 * Global AfterMethod
	 *
	 * @throws HarnessException
	 */
	@AfterMethod( groups = { "always" } )
	public void coreAfterMethod(Method method, ITestResult testResult)
	throws HarnessException {
		logger.info("coreAfterMethod: start");


		// If the active URL does not match the base URL, then
		// the test case may have manually navigated somewhere.
		//
		// Clear the cookies and reload
		//
		if ( ZimbraURI.needsReload() ) {
            logger.error("The URL does not match the base URL.  Reload app.");
            // app.zPageLogin.sDeleteAllVisibleCookies();

            /* Commenting below code because touch client always asks for navigate away */
            //app.zPageLogin.sOpen(ConfigProperties.getLogoutURL());
            //app.zPageLogin.sOpen(ConfigProperties.getBaseURL());
		}

		// If neither the main page or login page are active, then
		// The app may be in a confused state.
		//
		// Clear the cookies and reload
		//
		if ( (!app.zPageMain.zIsActive()) && (!app.zPageLogin.zIsActive()) ) {
            logger.error("Neither login page nor main page were active.  Reload app.", new Exception());
            // app.zPageLogin.sDeleteAllVisibleCookies();

            /* Commenting below code because touch client always asks for navigate away */
            //app.zPageLogin.sOpen(ConfigProperties.getLogoutURL());
            //app.zPageLogin.sOpen(ConfigProperties.getBaseURL());
        }

		logger.info("coreAfterMethod: finish");
	}


    /**
     * Performance test after method
     */
    @AfterMethod(groups = { "performance" })
    public void performanceTestAfterMethod() {

       // Resetting the account to flush after each performance test method,
       // so that the next test is running with new account
       ZimbraAccount.ResetAccountZCS();

    }

	/**
	 * A TestNG data provider for all supported character sets
	 * @return
	 * @throws HarnessException
	 */
	@DataProvider(name = "DataProviderSupportedCharsets")
	public Object[][] DataProviderSupportedCharsets() throws HarnessException {
		return (ZimbraCharsets.getInstance().getSampleTable());
	}


	public void ModifyAccountPreferences(String string) throws HarnessException {
		StringBuilder settings = new StringBuilder();
		for (Map.Entry<String, String> entry : startingAccountPreferences.entrySet()) {
			settings.append(String.format("<a n='%s'>%s</a>", entry.getKey(), entry.getValue()));
		}
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
				+		"<id>"+ string +"</id>"
				+		settings.toString()
				+	"</ModifyAccountRequest>");
	}

	public boolean zVerifyToastMessage(String toastMessage) throws HarnessException {
		SleepUtil.sleepMedium();
		Boolean elementPresent = false;
		if (webDriver.findElement(By.cssSelector("css=div[class='zcs-toast-message-text']:contains('" + toastMessage + "')")) != null) {
			elementPresent = true;
		} else {
			elementPresent = false;
		}
		return elementPresent;
	}

	// Inject message using REST upload & SOAP AddMsgRequest
	public void injectMessage (ZimbraAccount account, String filePath) throws HarnessException {
		String attachmentId = account.uploadFileUsingRestUtil(filePath);

		// Add message in selected account
		try {
			app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
					"<m l='Inbox' f='u' aid='"+ attachmentId + "'></m>" +
				"</AddMsgRequest>");
		} catch (HarnessException e) {
			throw new HarnessException("Unable to inject message: " + e);
		}
	}
}