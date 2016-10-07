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
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.staf.StafServicePROCESS;
import com.zimbra.qa.selenium.projects.admin.ui.AppAdminConsole;

public class AdminCommonTest {

	private WebDriver webDriver = ClientSessionFactory.session().webDriver();
	protected static Logger logger = LogManager.getLogger(AdminCommonTest.class);
	protected final ZimbraAdminAccount gAdmin = ZimbraAdminAccount.AdminConsoleAdmin();
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

	@BeforeSuite( groups = { "always" } )
	public void commonTestBeforeSuite() throws HarnessException {

		logger.info("commonTestBeforeSuite");

		// For coverage ?mode=mjsf&gzip=false
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled", ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
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
			ConfigProperties.setAppType(ConfigProperties.AppType.ADMIN);
			webDriver = ClientSessionFactory.session().webDriver();

			Capabilities cp =  ((RemoteWebDriver)webDriver).getCapabilities();
			if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())) {
				webDriver.manage().window().setPosition(new Point(0, 0));
				webDriver.manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
				webDriver.navigate().to(ConfigProperties.getBaseURL());
			}

		} catch (WebDriverException e) {
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
		logger.info("commonTestBeforeClass: start");		
		logger.info("commonTestBeforeClass: finish");
	}

	@BeforeMethod( groups = { "always" } )
	public void commonTestBeforeMethod() throws HarnessException {
		logger.info("commonTestBeforeMethod: start");

		//Close all the dialogs left opened by the previous test 
		app.zPageMain.zHandleDialogs();

		// If a startinAccount is defined, then make sure we are authenticated as that user
		if ( startingAccount != null ) {	
			logger.debug("commonTestBeforeMethod: startingAccount is defined");

			if ( !startingAccount.equals(app.zGetActiveAccount())) {

				if ( app.zPageMain.zIsActive() )
					app.zPageMain.logout();
				app.zPageLogin.login(startingAccount);

			}

			// For coverage ?mode=mjsf&gzip=false
			if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled", ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
				for (int i=0; i<=10; i++) {
					if (ClientSessionFactory.session().webDriver().findElement(By.cssSelector("css=div[id='ztih__AppAdmin__Home_textCell']")) != null) {
						app.zPageLogin.sRefresh();
						SleepUtil.sleepVeryVeryLong();
						SleepUtil.sleepVeryVeryLong();
						if (ClientSessionFactory.session().webDriver().findElement(By.cssSelector("css=div[id='ztih__AppAdmin__Home_textCell']")) != null) {
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

	@AfterSuite( groups = { "always" } )
	public void commonTestAfterSuite() throws HarnessException {
		logger.info("commonTestAfterSuite: start");

		webDriver.switchTo().defaultContent();
		webDriver.quit();
		logger.info("commonTestAfterSuite: finish");

	}

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
