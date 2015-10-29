/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;

import java.awt.Toolkit;
import java.io.IOException;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


/**
 * This class defines the login page
 * @author Matt Rhoades
 *
 */
public class PageLogin extends AbsTab {
	
	private WebDriverBackedSelenium _webDriverBackedSelenium = null;
	private WebDriver _webDriver = null;

	public static class Locators {

		public static final String zLoginDialog = "css=div[class='ZaLoginDialog']";
		public static final String zLoginUserName = "ZLoginUserName";
		public static final String zLoginPassword = "ZLoginPassword";
		public static final String zLoginButtonContainer = "ZLoginButton";
		public static final String zLoginLicenseContainer = "ZLoginLicenseContainer";

	}

	/**
	 * An object that controls the Admin Console Login Page
	 */
	public PageLogin(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/**
	 * If the "Login" button is visible, assume the LoginPage is active
	 */
	@SuppressWarnings("deprecation")
	public boolean zIsActive() throws HarnessException {
		
		if ( zIsActive() ) {
			// This page is already active.
			return true;
			
		} else if ( !zIsVisiblePerPosition(Locators.zLoginButtonContainer, 10, 10) ) {
			
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
				throw new HarnessException("Unable to kill browsers", e);
			} catch (InterruptedException e) {
				throw new HarnessException("Unable to kill browsers", e);
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
						//_webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
						_webDriver.navigate().to(ZimbraSeleniumProperties.getBaseURL());
					}
					
				} else if (ZimbraSeleniumProperties.isWebDriverBackedSelenium()) {
					
					_webDriverBackedSelenium = ClientSessionFactory.session().webDriverBackedSelenium();
					_webDriverBackedSelenium.windowMaximize();
					_webDriverBackedSelenium.windowFocus();
					_webDriverBackedSelenium.setTimeout("60000");
					_webDriverBackedSelenium.open(ZimbraSeleniumProperties.getBaseURL());
					
				} else {

					@SuppressWarnings("unused")
					String timeout = ZimbraSeleniumProperties.getStringProperty("selenium.maxpageload.msec", "30000");

					ClientSessionFactory.session().selenium().start();
					ClientSessionFactory.session().selenium().windowMaximize();
					ClientSessionFactory.session().selenium().windowFocus();
					ClientSessionFactory.session().selenium().allowNativeXpath("true");
					ClientSessionFactory.session().selenium().setTimeout("60000");
					ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getBaseURL());
				}
				
			} catch (SeleniumException e) {
				logger.error("Unable to open admin app. Is a valid cert installed?", e);
				throw e;
			}
			
			((AppAdminConsole)MyApplication).zPageLogin.zNavigateTo();
			
		}
		
		// Make sure the application is loaded first
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");
		
		// Look for the login button.
		boolean present = sIsElementPresent(Locators.zLoginButtonContainer);
		if ( !present ) {
			logger.debug("isActive() present = "+ present);
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.zLoginButtonContainer, 0 , 0);
		if ( !visible ) {
			logger.debug("isActive() visible = "+ visible);
			return (false);
		}

		logger.debug("isActive() = "+ true);
		return (true);
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {
			// This page is already active.
			return;
		}


		// Logout
		if ( ((AppAdminConsole)MyApplication).zPageMain.zIsActive() ) {
			((AppAdminConsole)MyApplication).zPageMain.logout();
		}

		zWaitForActive();
	}


	/**
	 * Login as the GlobalAdmin
	 * @throws HarnessException
	 */
	public void login() throws HarnessException {
		logger.debug("login()");

		login(ZimbraAdminAccount.AdminConsoleAdmin());
	}

	/**
	 * Login as the specified account
	 * @param account
	 * @throws HarnessException
	 */
	public void login(ZimbraAccount account) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		zNavigateTo();

		// Fill out the form
		fillLoginFormFields(account);

		// Click the Login button
		sClick(Locators.zLoginButtonContainer);

		// Wait for the app to load
		// sWaitForPageToLoad();
		((AppAdminConsole)MyApplication).zPageMain.zWaitForActive();

		((AppAdminConsole)MyApplication).zSetActiveAcount(account);

		SleepUtil.sleep(10000);
	}

	/**
	 * Fill the form with the specified user
	 * @throws HarnessException
	 */
	public void fillLoginFormFields(ZimbraAccount account) throws HarnessException {
		logger.debug("fillFields(ZimbraAccount account)" + account.EmailAddress);

		if ( !zIsActive() )
			throw new HarnessException("LoginPage is not active");

		sType(Locators.zLoginUserName, account.EmailAddress);
		sType(Locators.zLoginPassword, account.Password);
	}

	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item)
			throws HarnessException {
		return null;
	}
	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		return null;
	}



}
