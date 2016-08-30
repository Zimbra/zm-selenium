/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.core;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.StafServicePROCESS;
import com.zimbra.qa.selenium.projects.admin.ui.AppAdminConsole;


/**
 * Common definitions for all Admin Console test cases
 * @author Matt Rhoades
 *
 */
public class AdminCommonTest {
	private WebDriverBackedSelenium _webDriverBackedSelenium = null;
	private WebDriver _webDriver = null;
	
	protected static Logger logger = LogManager.getLogger(AdminCommonTest.class);
	
	/**
	 * Helper field.  admin = ZimbraAdminAccount.GlobalAdmin()
	 */
	protected final ZimbraAdminAccount gAdmin = ZimbraAdminAccount.AdminConsoleAdmin();
	

	/**
	 * The AdminConsole application object
	 */
	protected AppAdminConsole app = null;

	
	/**
	 * BeforeMethod variables
	 * startingPage = the starting page before the test method starts
	 * startingAccount = the account to log in as
	 */
	protected AbsTab startingPage = null;
	protected ZimbraAdminAccount startingAccount = null;
	
	protected AdminCommonTest() {
		logger.info("New "+ AdminCommonTest.class.getCanonicalName());
		
		app = new AppAdminConsole();
		
		startingPage = app.zPageMain;
		startingAccount = gAdmin;
		
	}
	
	/**
	 * Global BeforeSuite
	 * 
	 * 1. Make sure the selenium server is available
	 * 
	 * @throws HarnessException
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	@BeforeSuite( groups = { "always" } )
	public void commonTestBeforeSuite() throws HarnessException {
		
		logger.info("commonTestBeforeSuite");
		
		// For coverage ?mode=mjsf&gzip=false
		if (ZimbraSeleniumProperties.getStringProperty(ZimbraSeleniumProperties.getLocalHost() + ".coverage.enabled", ZimbraSeleniumProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			StafServicePROCESS staf = new StafServicePROCESS();
			try {
				staf.execute("zmprov mcf +zimbraHttpThrottleSafeIPs " + InetAddress.getLocalHost().getHostAddress());
				staf.execute("zmmailboxdctl restart");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.ADMIN);

			if(ZimbraSeleniumProperties.isWebDriver()) {
				_webDriver = ClientSessionFactory.session().webDriver();

				Capabilities cp =  ((RemoteWebDriver)_webDriver).getCapabilities();
				if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())){				
					_webDriver.manage().window().setPosition(new Point(0, 0));
					_webDriver.manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
					_webDriver.navigate().to(ZimbraSeleniumProperties.getBaseURL());
				}
				
			}else if (ZimbraSeleniumProperties.isWebDriverBackedSelenium()) {
				_webDriverBackedSelenium = ClientSessionFactory.session().webDriverBackedSelenium();
				_webDriverBackedSelenium.windowMaximize();
				_webDriverBackedSelenium.windowFocus();
				_webDriverBackedSelenium.setTimeout("45000");// Use 45 second timeout for
				_webDriverBackedSelenium.open(ZimbraSeleniumProperties.getBaseURL());
			} else {
				// Use 30 second timeout for opening the browser
				String timeout = ZimbraSeleniumProperties.getStringProperty("selenium.maxpageload.msec", "30000");

				ClientSessionFactory.session().selenium().start();
				ClientSessionFactory.session().selenium().windowMaximize();
				ClientSessionFactory.session().selenium().windowFocus();
				ClientSessionFactory.session().selenium().allowNativeXpath("true");
				ClientSessionFactory.session().selenium().setTimeout("45000");
				ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getBaseURL());
			}
			
		} catch (SeleniumException e) {
			logger.error("Unable to open admin app.  Is a valid cert installed?", e);
			throw e;
		}

		
	}
	
	/**
	 * Global BeforeClass
	 * 
	 * @throws HarnessException
	 */
	@BeforeClass( groups = { "always" } )
	public void commonTestBeforeClass() throws HarnessException {
		logger.info("commonTestBeforeClass");

	}

	/**
	 * Global BeforeMethod
	 * 
	 * 1. For all tests, make sure the CommonTest.startingPage is active
	 * 2. For all tests, make sure the logged in user is 
	 * 
	 * @throws HarnessException
	 */
	@SuppressWarnings("deprecation")
	@BeforeMethod( groups = { "always" } )
	public void commonTestBeforeMethod() throws HarnessException {
		logger.info("commonTestBeforeMethod: start");
		
		// If a startinAccount is defined, then make sure we are authenticated as that user
		if ( startingAccount != null ) {
			logger.debug("commonTestBeforeMethod: startingAccount is defined");
			
			if ( !startingAccount.equals(app.zGetActiveAccount())) {
				
				if ( app.zPageMain.zIsActive() )
					app.zPageMain.logout();
				app.zPageLogin.login(startingAccount);
				
			}
			
			// For coverage ?mode=mjsf&gzip=false
			if (ZimbraSeleniumProperties.getStringProperty(ZimbraSeleniumProperties.getLocalHost() + ".coverage.enabled", ZimbraSeleniumProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
				for (int i=0; i<=10; i++) {
					if (ClientSessionFactory.session().selenium().isElementPresent("css=div[id='ztih__AppAdmin__Home_textCell']") == false) {
						app.zPageLogin.sRefresh();
						SleepUtil.sleepVeryVeryLong();
						SleepUtil.sleepVeryVeryLong();
						if (ClientSessionFactory.session().selenium().isElementPresent("css=div[id='ztih__AppAdmin__Home_textCell']") == true) {
							break;
						}
					}
				}
			}
			
			// Confirm
			if ( !startingAccount.equals(app.zGetActiveAccount())) {
				throw new HarnessException("Unable to authenticate as "+ startingAccount.EmailAddress);
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

	/**
	 * Global AfterSuite
	 * 
	 * @throws HarnessException
	 */
	@SuppressWarnings("deprecation")
	@AfterSuite( groups = { "always" } )
	public void commonTestAfterSuite() throws HarnessException {	
		logger.info("commonTestAfterSuite: start");

		if(ZimbraSeleniumProperties.isWebDriver()) {
			_webDriver.switchTo().defaultContent();
			_webDriver.quit();
		} else if (ZimbraSeleniumProperties.isWebDriverBackedSelenium()) {
			_webDriverBackedSelenium.stop();
		} else {
			ClientSessionFactory.session().selenium().stop();
		}

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
	
	public void zUpload (String filePath) throws HarnessException {

		SleepUtil.sleepLong();

		StringSelection ss = new StringSelection(filePath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			SleepUtil.sleepSmall();
			robot.keyPress(KeyEvent.VK_END);
			robot.keyRelease(KeyEvent.VK_END);
			SleepUtil.sleepMedium();

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		//app.zPageMail.zKeyboardTypeStringUpload(filePath);

		SleepUtil.sleepVeryVeryLong(); //real uploading of the file takes longer time
	}
	
	public void zDownload () throws HarnessException {

		SleepUtil.sleepLong();

	
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		//app.zPageMail.zKeyboardTypeStringUpload(filePath);

		SleepUtil.sleepVeryLong(); //real uploading of the file takes longer time
	}
}
