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
package com.zimbra.qa.selenium.projects.touch.ui;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Date;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriverException;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ConfigProperties.*;

public class PageLogin extends AbsTab {

	private WebDriver webDriver = ClientSessionFactory.session().webDriver();

	public static class Locators {

		// Buttons
		public static final String zBtnLogin = "css=input[class*=LoginButton]";

		// Text Input
		public static final String zInputUsername = "css=input[id='username']";
		public static final String zInputPassword = "css=input[id='password']";
		public static final String zInputRemember = "css=input[id='remember']";

		// Displayed text
		public static final String zDisplayedusername = "css=form[name='loginForm'] label[for='username']";
		public static final String zDisplayedcopyright = "css=div[class='copyright']";

		// Toolbar links
		public static final String zLogoutLink = "css=[id='skin_container_logoff']>a";

	}



	public PageLogin(AbsApplication application) {
		super(application);

		logger.info("new " + PageLogin.class.getCanonicalName());

	}

	@Override
	public boolean zIsActive() throws HarnessException {
		AppType appType = ConfigProperties.getAppType();
		String locator = null;

		switch (appType) {

		case TOUCH:
			locator = Locators.zBtnLogin;
			break;
		default:
			throw new HarnessException("Please add a support for appType: " + appType);
		}

		// Look for the login button.
		boolean present = sIsElementPresent(locator);
		if ( !present ) {
			logger.debug("isActive() present = "+ present);
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(locator, 0 , 0);
		if ( !visible ) {
			logger.debug("isActive() visible = "+ visible);
			return (false);
		}

		logger.debug("isActive() = " + true);
		return (true);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if ( zIsActive() ) {

			return;

		} else if ( !zIsVisiblePerPosition(Locators.zBtnLogin, 10, 10) ) {

			try {
				String SeleniumBrowser;
				SeleniumBrowser = ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".browser",	ConfigProperties.getStringProperty("browser"));

				if (SeleniumBrowser.contains("edge")) {
				    CommandLineUtility.CmdExec("taskkill /f /t /im MicrosoftEdge.exe");
				    CommandLineUtility.CmdExec("taskkill /f /t /im MicrosoftWebDriver.exe");
				} else if (SeleniumBrowser.contains("firefox")) {
					CommandLineUtility.CmdExec("taskkill /f /t /im firefox.exe");
					CommandLineUtility.CmdExec("taskkill /f /t /im geckodriver.exe");
				} else if (SeleniumBrowser.contains("chrome")) {
					CommandLineUtility.CmdExec("taskkill /f /t /im chrome.exe");
					CommandLineUtility.CmdExec("taskkill /f /t /im chromedriver.exe");
				} else if (SeleniumBrowser.contains("safari")) {
				    CommandLineUtility.CmdExec("taskkill /f /t /im safari.exe");
				}

			} catch (IOException e) {
				throw new HarnessException("Unable to kill browsers", e);
			} catch (InterruptedException e) {
				throw new HarnessException("Unable to kill browsers", e);
			}

			try
			{
				ConfigProperties.setAppType(ConfigProperties.AppType.TOUCH);

				webDriver = ClientSessionFactory.session().webDriver();

				Capabilities cp =  ((RemoteWebDriver)webDriver).getCapabilities();
				if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())) {
					webDriver.manage().window().setPosition(new Point(0, 0));
					webDriver.manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
					webDriver.navigate().to(ConfigProperties.getBaseURL());
				}

			} catch (WebDriverException e) {
				logger.error("Unable to open touch app. Is a valid cert installed?", e);
				throw e;
			}

			((AppTouchClient)MyApplication).zPageLogin.zNavigateTo();

		}

		//Make sure the application is loaded first
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Touch client application is not active!");

		// Look for the login button.
		boolean present = sIsElementPresent(Locators.zBtnLogin);
		if ( !present ) {
			logger.debug("isActive() present = "+ present);
			return;
		}

		boolean visible = zIsVisiblePerPosition(Locators.zBtnLogin, 0 , 0);
		if ( !visible ) {
			logger.debug("isActive() visible = "+ visible);
			return;
		}

		logger.debug("isActive() = "+ true);

		// Logout
		if ( ((AppTouchClient)MyApplication).zPageMain.zIsActive() ) {
			((AppTouchClient)MyApplication).zPageMain.zLogout();
		}

		zWaitForActive();


		// Logout
		if ( ((AppTouchClient)MyApplication).zPageMain.zIsActive() ) {
			((AppTouchClient)MyApplication).zPageMain.zLogout();
		}

		zWaitForActive();

	}



	/**
	 * Login as the specified account
	 * @param account
	 * @throws HarnessException
	 */
	public void zLogin(ZimbraAccount account) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		tracer.trace("Login to the "+ MyApplication.myApplicationName() +" using user/password "+ account.EmailAddress +"/"+ account.Password);

		zNavigateTo();

		Date start = new Date();

		try {

			zSetLoginName(account.EmailAddress);
			zSetLoginPassword(account.Password);

			// Click the Login button
			sClick(Locators.zBtnLogin);

			// Wait for the app to load
			/* TODO: ... debugging to be removed */
			//sWaitForPageToLoad();

			((AppTouchClient)MyApplication).zPageMain.zWaitForActive(180000);

			((AppTouchClient)MyApplication).zSetActiveAcount(account);


		} finally {

			SleepMetrics.RecordProcessing((new Throwable()).getStackTrace(), start, new Date());

		}
	}

	public void zSetLoginName(String name) throws HarnessException {
		String locator = Locators.zInputUsername;
		if ( name == null ) {
			throw new HarnessException("Name is null");
		}

		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Login field does not exist "+ locator);
		}
		clearField(locator);
		sType(locator, name);
	}

	public void zSetLoginPassword(String password) throws HarnessException {
		String locator = Locators.zInputPassword;
		if ( password == null ) {
			throw new HarnessException("Password is null");
		}
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Password field does not exist "+ locator);
		}
		clearField(locator);
		sType(locator, password);
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		throw new HarnessException("Login page does not have a Toolbar");
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		throw new HarnessException("Login page does not have a Toolbar");
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException("Login page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException("Login page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
	throws HarnessException {
		throw new HarnessException("Login page does not have lists");
	}

	@Override
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		throw new HarnessException("No shortcuts supported in the login page");
	}



}
