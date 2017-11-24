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
package com.zimbra.qa.selenium.projects.mobile.core;

import java.awt.Toolkit;
import java.lang.reflect.Method;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.openqa.selenium.WebDriverException;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.mobile.pages.MobilePages;

public class MobileCore {
	
	protected static Logger logger = LogManager.getLogger(MobileCore.class);
	protected MobilePages app = null;
	
	private WebDriver webDriver = ClientSessionFactory.session().webDriver();
	WebElement we = null;

	/**
	 * BeforeMethod variables
	 * startingPage = the starting page before the test method starts
	 * startingAccount = the account to log in as
	 */
	protected AbsTab startingPage = null;
	protected ZimbraAccount startingAccount = null;

	protected MobileCore() {
		logger.info("New "+ MobileCore.class.getCanonicalName());
		
		app = new MobilePages();
		
		startingPage = app.zPageMain;
		startingAccount = ZimbraAccount.AccountZCS();
		
		app.zPageLogin.DefaultLoginAccount = startingAccount;
		
	}
	
	/**
	 * Global BeforeSuite
	 * 
	 * 1. Make sure the selenium server is available
	 * 
	 * @throws HarnessException
	 */
	@BeforeSuite( groups = { "always" } )
	public void coreBeforeSuite() throws HarnessException {
		logger.info("coreBeforeSuite: start");



      	// Make sure there is a new default account
		ZimbraAccount.ResetAccountZCS();
				
		// Set the app type
		ConfigProperties.setAppType(ConfigProperties.AppType.MOBILE);

		try {

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
				
				try
				{
					logger.info("Retry #" + retry);
					retry ++;
					webDriver.navigate().to(ConfigProperties.getBaseURL());
					
					appIsReady = true;
				} catch (WebDriverException e) {
					if (retry >= maxRetry) {
						logger.error("Unable to open admin app.  Is a valid cert installed?", e);
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
			logger.error("Unable to mobile app.", e);
			throw e;
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
	 * 
	 * 1. For all tests, make sure the startingPage is active
	 * 2. For all tests, make sure the logged in user is 
	 * 
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
		      synchronized (MobileCore.class) {
		         logger.info("---------BeforeMethod-----------------------");
		         logger.info("Test       : " + methodClass
		               + "." + ngMethod.getMethodName());
		         logger.info("Description: " + ngMethod.getDescription());
		         logger.info("----------------------------------------");
		      }
		      break;
		   }
		}


	      // If a startinAccount is defined, then make sure we are authenticated as that user
		if ( startingAccount != null ) {
			logger.debug("coreBeforeMethod: startingAccount is defined");

			if ( !startingAccount.equals(app.zGetActiveAccount())) {

				if ( app.zPageMain.zIsActive() )
					app.zPageMain.zLogout();
				app.zPageLogin.zLogin(startingAccount);

			}

			// Confirm
			if ( !startingAccount.equals(app.zGetActiveAccount())) {
				throw new HarnessException("Unable to authenticate as "+ startingAccount.EmailAddress);
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

	/**
	 * Global AfterSuite
	 * 
	 * @throws HarnessException
	 */
	@AfterSuite( groups = { "always" } )
	public void coreAfterSuite() throws HarnessException {	
		logger.info("coreAfterSuite: start");
		webDriver.quit();
		logger.info("coreAfterSuite: finish");

	}
	
	/**
	 * Global AfterClass
	 * 
	 * @throws HarnessException
	 */
	@AfterClass( groups = { "always" } )
	public void coreAfterClass() throws HarnessException {
		logger.info("coreAfterClass: start");
		
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


      logger.info("coreAfterMethod: finish");
	}

}
