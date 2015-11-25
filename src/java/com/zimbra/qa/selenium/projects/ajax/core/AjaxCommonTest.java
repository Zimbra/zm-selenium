/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.core;

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
import com.thoughtworks.selenium.*;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogError.DialogErrorID;

/**
 * The <code>AjaxCommonTest</code> class is the base test case class
 * for normal Ajax client test case classes.
 * <p>
 * The AjaxCommonTest provides two basic functionalities:
 * <ol>
 * <li>{@link AbsTab} {@link #startingPage} - navigate to this
 * page before each test case method</li>
 * <li>{@link ZimbraAccount} {@link #startingAccountPreferences} - ensure this
 * account is authenticated before each test case method</li>
 * </ol>
 * <p>
 * It is important to note that no re-authentication (i.e. logout
 * followed by login) will occur if {@link #startingAccountPreferences} is 
 * already the currently authenticated account.
 * <p>
 * The same rule applies to the {@link #startingPage}, as well.  If
 * the "Contact App" is the specified starting page, and the contact
 * app is already opened, albiet in a "new contact" view, then the
 * "new contact" view will not be closed.
 * <p>
 * Typical usage:<p>
 * <pre>
 * {@code
 * public class TestCaseClass extends AjaxCommonTest {
 * 
 *     public TestCaseClass() {
 *     
 *         // All tests start at the Mail page
 *         super.startingPage = app.zPageMail;
 *         
 *         // Create a new account to log into
 *         ZimbraAccount account = new ZimbraAccount();
 *         super.startingAccount = account;
 *         
 *         // ...
 *         
 *     }
 *     
 *     // ...
 * 
 * }
 * }
 * </pre>
 * 
 * @author Matt Rhoades
 *
 */
public class AjaxCommonTest {
	
	protected static Logger logger = LogManager.getLogger(AjaxCommonTest.class);


	private WebDriverBackedSelenium _webDriverBackedSelenium = null;
	private WebDriver _webDriver = null;
	
	/**
	 * The AdminConsole application object
	 */
	protected AppAjaxClient app = null;

	/**
	 * BeforeMethod variables
	 * startingPage = the starting page before the test method starts
	 * startingAccountSettings = the account's settings (ModifyAccountRequest)
	 * startingAccountPreferences = the account's preferences (ModifyPrefsRequest)
	 * startingAccountZimletPreferences = the account's zimlet preferences (ModifyZimletPrefsRequest)
	 */
	protected AbsTab startingPage = null;
	protected Map<String, String> startingAccountPreferences = null;
	protected Map<String, String> startingUserPreferences = null;		// TODO:
	protected Map<String, String> startingUserZimletPreferences = null;

	protected AjaxCommonTest() {
		logger.info("New "+ AjaxCommonTest.class.getCanonicalName());

		app = new AppAjaxClient();

		startingPage = app.zPageMain;
		startingAccountPreferences = new HashMap<String, String>();
		startingUserZimletPreferences = new HashMap<String, String>();
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
	@SuppressWarnings("deprecation")
	@BeforeSuite( groups = { "always" } )
	public void commonTestBeforeSuite()
	throws HarnessException, IOException, InterruptedException, SAXException {
		logger.info("commonTestBeforeSuite: start");

		// Make sure there is a new default account
		ZimbraAccount.ResetAccountZWC();

		try
		{
			
			ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.AJAX);
			DefaultSelenium _selenium = null;
			
			if (ZimbraSeleniumProperties.isWebDriver()) {
				_webDriver = ClientSessionFactory.session().webDriver();
				
				Capabilities cp =  ((RemoteWebDriver)_webDriver).getCapabilities();
				 if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())){				
					_webDriver.manage().window().setPosition(new Point(0, 0));
					_webDriver.manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
				}								
			} else if (ZimbraSeleniumProperties.isWebDriverBackedSelenium()) {
				_webDriverBackedSelenium = ClientSessionFactory.session()
						.webDriverBackedSelenium();
				_webDriverBackedSelenium.windowMaximize();
				_webDriverBackedSelenium.windowFocus();
				_webDriverBackedSelenium.setTimeout("80000");
			} else {
				_selenium = ClientSessionFactory.session().selenium();
				_selenium.start();
				_selenium.windowMaximize();
				_selenium.windowFocus();
				_selenium.allowNativeXpath("true");
				_selenium.setTimeout("80000");
			}
			// Dynamic wait for App to be ready
			int maxRetry = 10;
			int retry = 0;
			boolean appIsReady = false;
			while (retry < maxRetry && !appIsReady) {       
				try
				{
					logger.info("Retry #" + retry);
					retry ++;
					
					if (ZimbraSeleniumProperties.isWebDriver()) {
						//_webDriver.get(ZimbraSeleniumProperties.getBaseURL());
						_webDriver.navigate().to(ZimbraSeleniumProperties.getBaseURL());
						/*
						if(ZimbraSeleniumProperties.getCalculatedBrowser().contains("iexplore")){	
							if(app.zPageMain.sIsElementPresent("id=overridelink")){
								_webDriver.navigate().to("javascript:document.getElementById('overridelink').click()");						
							}
						}
						*/
					} 
					else if (ZimbraSeleniumProperties.isWebDriverBackedSelenium()) 
						_webDriverBackedSelenium.open(ZimbraSeleniumProperties.getBaseURL());
					else
						_selenium.open(ZimbraSeleniumProperties.getBaseURL());

					appIsReady = true;
				} catch (SeleniumException e) {
					if (retry == maxRetry) {
						logger.error("Unable to open ajax app." +
								"  Is a valid cert installed?", e);
						throw e;
					} else {
						logger.info("App is still not ready...", e);
						SleepUtil.sleep(6000);
						continue;
					}
				}
			}
			logger.info("App is ready!");

		} catch (SeleniumException e) {
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
				synchronized (AjaxCommonTest.class) {
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
			logger.info("commonTestBeforeMethod: startingAccountPreferences are defined");

			// If the current test accounts preferences match, then the account can be used
			if ( !ZimbraAccount.AccountZWC().compareAccountPreferences(startingAccountPreferences) ) {
				
				logger.info("commonTestBeforeMethod: startingAccountPreferences do not match active account");

				// Reset the account
				ZimbraAccount.ResetAccountZWC();
				
				// Create a new account
				// Set the preferences accordingly
				ZimbraAccount.AccountZWC().modifyAccountPreferences(startingAccountPreferences);
				ZimbraAccount.AccountZWC().modifyUserZimletPreferences(startingUserZimletPreferences);
			}
			
		}

		// If test account zimlet preferences are defined, then make sure the test account
		// uses those zimlet preferences
		//
		if ( (startingUserZimletPreferences != null) && (!startingUserZimletPreferences.isEmpty()) ) {
			logger.info("commonTestBeforeMethod: startingAccountZimletPreferences are defined");
			
			// If the current test accounts preferences match, then the account can be used
			if ( !ZimbraAccount.AccountZWC().compareUserZimletPreferences(startingUserZimletPreferences) ) {
				
				logger.info("commonTestBeforeMethod: startingAccountZimletPreferences do not match active account");

				// Reset the account
				ZimbraAccount.ResetAccountZWC();
				
				// Create a new account
				// Set the preferences accordingly
				ZimbraAccount.AccountZWC().modifyAccountPreferences(startingAccountPreferences);
				ZimbraAccount.AccountZWC().modifyUserZimletPreferences(startingUserZimletPreferences);
			}

			ZimbraAccount.AccountZWC().modifyUserZimletPreferences(startingUserZimletPreferences);
		}

		
		
		// If AccountZWC is not currently logged in, then login now
		if ( !ZimbraAccount.AccountZWC().equals(app.zGetActiveAccount()) ) {
			logger.info("commonTestBeforeMethod: AccountZWC is not currently logged in");

			if ( app.zPageMain.zIsActive() )
				try{
					app.zPageMain.zLogout();

				}catch(Exception ex){
					if ( !app.zPageLogin.zIsActive()) {
						logger.error("Login page is not active ", ex);

						app.zPageLogin.sOpen(ZimbraSeleniumProperties.getLogoutURL());            
						app.zPageLogin.sOpen(ZimbraSeleniumProperties.getBaseURL());
					}
				}							
		}

		// If a startingPage is defined, then make sure we are on that page
		if ( startingPage != null ) {
			logger.info("commonTestBeforeMethod: startingPage is defined");

			// If the starting page is not active, navigate to it
			if ( !startingPage.zIsActive() ) {
				SleepUtil.sleepSmall();
				startingPage.zNavigateTo();
				SleepUtil.sleepSmall();
			}

			// Confirm that the page is active
			if ( !startingPage.zIsActive() ) {
				throw new HarnessException("Unable to navigate to "+ startingPage.myPageName());
			}
			
			logger.info("commonTestBeforeMethod: startingPage navigation done");

		}

		// Check for error dialogs
		boolean check = "true".equals( ZimbraSeleniumProperties.getStringProperty("dialog.error.beforetest.check", "true") );
		boolean dismiss = "true".equals( ZimbraSeleniumProperties.getStringProperty("dialog.error.beforetest.dismiss", "false") );
		if ( check ) {

			AbsDialog dialog = app.zPageMain.zGetErrorDialog(DialogErrorID.Zimbra);
			if ( (dialog != null) && (dialog.zIsActive()) ) {

				// Error dialog is visible.
				if ( dismiss ) {

					// Dismiss the dialog and carry on
					dialog.zClickButton(Button.B_OK);

				} else {

					// Throw an exception (all future tests will likely be skipped)
					throw new HarnessException("Error Dialog is visible");

				}
			}
		}

		// Make sure any extra compose tabs are closed
		app.zPageMain.zCloseComposeTabs();

		logger.info("commonTestBeforeMethod: finish");

	}

	/**
	 * Global AfterSuite
	 * <p>
	 * <ol>
	 * <li>Stop the DefaultSelenium client</li>
	 * </ol>
	 * 
	 * @throws HarnessException
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	@AfterSuite( groups = { "always" } )
	public void commonTestAfterSuite() throws HarnessException, IOException, InterruptedException {	
		logger.info("commonTestAfterSuite: start");

		if (ZimbraSeleniumProperties.isWebDriver()) {
			_webDriver.quit();
		} else if (ZimbraSeleniumProperties.isWebDriverBackedSelenium()) {
			_webDriverBackedSelenium.stop();
		} else {
			ClientSessionFactory.session().selenium().stop();
		}
		
		CommandLine.CmdExec("taskkill /f /t /im plugin-container.exe");

		logger.info("commonTestAfterSuite: finish");

	}

	/**
	 * Global AfterClass
	 * 
	 * @throws HarnessException
	 */
	@AfterClass( groups = { "always" } )
	public void commonTestAfterClass() throws HarnessException {
		logger.info("commonTestAfterClass: start");

		// For Ajax, if account is considered dirty (modified),
		// then recreate a new account, but for desktop, the zimlet
		// preferences has to be reset to default, all core zimlets are enabled
		ZimbraAccount currentAccount = app.zGetActiveAccount();
		if (currentAccount != null && currentAccount.accountIsDirty &&
				currentAccount == ZimbraAccount.AccountZWC()) {
			// Reset the account
			ZimbraAccount.ResetAccountZWC();

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
            app.zPageLogin.sOpen(ZimbraSeleniumProperties.getLogoutURL());            
            app.zPageLogin.sOpen(ZimbraSeleniumProperties.getBaseURL());
		}

		// If neither the main page or login page are active, then
		// The app may be in a confused state.
		//
		// Clear the cookies and reload
		//
		if ( (!app.zPageMain.zIsActive()) && (!app.zPageLogin.zIsActive()) ) {
            logger.error("Neither login page nor main page were active.  Reload app.", new Exception());
            // app.zPageLogin.sDeleteAllVisibleCookies();
            app.zPageLogin.sOpen(ZimbraSeleniumProperties.getLogoutURL());            
            app.zPageLogin.sOpen(ZimbraSeleniumProperties.getBaseURL());
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
       ZimbraAccount.ResetAccountZWC();

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

	
	@SuppressWarnings("deprecation")
	public void zKillBrowserAndRelogin () {
		
		logger.error("Reset account");
		ZimbraAccount.ResetAccountZWC();
		
		try {
			String SeleniumBrowser;
			SeleniumBrowser = ZimbraSeleniumProperties.getStringProperty(ZimbraSeleniumProperties.getLocalHost() + ".browser",	ZimbraSeleniumProperties.getStringProperty("browser"));
			
			if (SeleniumBrowser.contains("iexplore")) {
			    CommandLine.CmdExec("taskkill /f /t /im iexplore.exe");
			} else if (SeleniumBrowser.contains("firefox")) {
				CommandLine.CmdExec("taskkill /f /t /im firefox.exe");
			} else if (SeleniumBrowser.contains("safariproxy")) {
			    CommandLine.CmdExec("taskkill /f /t /im safari.exe");
			} else if (SeleniumBrowser.contains("chrome")) {
				CommandLine.CmdExec("taskkill /f /t /im chrome.exe");
			}
			
		} catch (IOException e) {
			logger.error("Unable to kill browsers", e);
		} catch (InterruptedException e) {
			logger.error("Unable to kill browsers", e);
		}
		
		try
		{
			if (ZimbraSeleniumProperties.getAppType() == AppType.AJAX) {
				ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.AJAX);
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.ADMIN) {
				ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.ADMIN);
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.TOUCH) {
				ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.TOUCH);
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.HTML) {
				ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.HTML);
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.MOBILE) {
				ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.MOBILE);
			}
			
			if(ZimbraSeleniumProperties.isWebDriver()) {
				
				_webDriver = ClientSessionFactory.session().webDriver();

				Capabilities cp =  ((RemoteWebDriver)_webDriver).getCapabilities();
				if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())){				
					_webDriver.manage().window().setPosition(new Point(0, 0));
					_webDriver.manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
					_webDriver.navigate().to(ZimbraSeleniumProperties.getBaseURL());
				}
				
			} else {

				ClientSessionFactory.session().selenium().start();
				ClientSessionFactory.session().selenium().windowMaximize();
				ClientSessionFactory.session().selenium().windowFocus();
				ClientSessionFactory.session().selenium().allowNativeXpath("true");
				ClientSessionFactory.session().selenium().setTimeout("60000");
				ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getBaseURL());
			}
			
		} catch (SeleniumException e) {
			logger.error("Unable to open app.", e);
			throw e;
		}
		
		try {
			
			if (ZimbraSeleniumProperties.getAppType() == AppType.AJAX) {
				if (ZimbraAccount.AccountZWC() != null) {
					((AppAjaxClient)app).zPageLogin.zLogin(ZimbraAccount.AccountZWC());
				} else {
					((AppAjaxClient)app).zPageLogin.zLogin(ZimbraAccount.Account10());
				}
			}
			
		} catch (HarnessException e) {
			logger.error("Unable to navigate to app.", e);
		}
		
	}
}
