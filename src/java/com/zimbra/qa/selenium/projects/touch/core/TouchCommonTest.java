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
import com.zimbra.qa.selenium.projects.touch.ui.AppTouchClient;

public class TouchCommonTest {

	protected static Logger logger = LogManager.getLogger(TouchCommonTest.class);
	protected AppTouchClient app = null;
	
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

	protected TouchCommonTest() {
		logger.info("New "+ TouchCommonTest.class.getCanonicalName());

		app = new AppTouchClient();

		startingPage = app.zPageMain;
		startingAccountPreferences = new HashMap<String, String>();
		startingUserZimletPreferences = new HashMap<String, String>();
	}

	@BeforeSuite( groups = { "always" } )
	public void commonTestBeforeSuite()
	throws HarnessException, IOException, InterruptedException, SAXException {
		logger.info("commonTestBeforeSuite: start");
		ZimbraAccount.ResetAccountZTC();

		try
		{
			ConfigProperties.setAppType(ConfigProperties.AppType.TOUCH);

			webDriver = ClientSessionFactory.session().webDriver();
			Capabilities cp =  ((RemoteWebDriver)webDriver).getCapabilities();
			 if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())){
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

		logger.info("commonTestBeforeSuite: finish");
	}

	/**
	 * Global BeforeClass
	 *
	 * @throws HarnessException
	 */
	@BeforeClass( groups = { "always" } )
	public void commonTestBeforeClass() throws HarnessException {
		logger.info("commonTestBeforeClass: start");

		logger.info("commonTestBeforeClass: finish");

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
	public void commonTestBeforeMethod(Method method, ITestContext testContext) throws HarnessException {
		logger.info("commonTestBeforeMethod: start");


		// Get the test description
		// By default, the test description is set to method's name
		// if it is set, then change it to the specified one
		for (ITestNGMethod ngMethod : testContext.getAllTestMethods()) {
			String methodClass = ngMethod.getRealClass().getSimpleName();
			if (methodClass.equals(method.getDeclaringClass().getSimpleName())
					&& ngMethod.getMethodName().equals(method.getName())) {
				synchronized (TouchCommonTest.class) {
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
			logger.debug("commonTestBeforeMethod: startingAccountPreferences are defined");

			// If the current test accounts preferences match, then the account can be used
			if ( !ZimbraAccount.AccountZTC().compareAccountPreferences(startingAccountPreferences) ) {

				logger.debug("commonTestBeforeMethod: startingAccountPreferences do not match active account");

				// Reset the account
				ZimbraAccount.ResetAccountZTC();

				// Create a new account
				// Set the preferences accordingly
				ZimbraAccount.AccountZTC().modifyAccountPreferences(startingAccountPreferences);
				ZimbraAccount.AccountZTC().modifyUserZimletPreferences(startingUserZimletPreferences);

			}

		}

		// If AccountZTC is not currently logged in, then login now
		if ( !ZimbraAccount.AccountZTC().equals(app.zGetActiveAccount()) ) {
			logger.debug("commonTestBeforeMethod: AccountZTC is not currently logged in");

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

		// For Touch, if account is considered dirty (modified),
		// then recreate a new account, but for desktop, the zimlet
		// preferences has to be reset to default, all core zimlets are enabled
		ZimbraAccount currentAccount = app.zGetActiveAccount();
		if (currentAccount != null && currentAccount.accountIsDirty &&
				currentAccount == ZimbraAccount.AccountZTC()) {
			// Reset the account
			ZimbraAccount.ResetAccountZTC();

		}

		logger.info("commonTestAfterClass: finish");
	}

	/**
	 * Global AfterMethod
	 *
	 * @throws HarnessException
	 */
	@AfterMethod( groups = { "always" } )
	public void commonTestAfterMethod(Method method, ITestResult testResult)
	throws HarnessException {
		logger.info("commonTestAfterMethod: start");


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

		logger.info("commonTestAfterMethod: finish");
	}


    /**
     * Performance test after method
     */
    @AfterMethod(groups={"performance"})
    public void performanceTestAfterMethod() {

       // Resetting the account to flush after each performance test method,
       // so that the next test is running with new account
       ZimbraAccount.ResetAccountZTC();

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
}
