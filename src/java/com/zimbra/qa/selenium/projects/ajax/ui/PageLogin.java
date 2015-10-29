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
package com.zimbra.qa.selenium.projects.ajax.ui;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Date;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.*;


public class PageLogin extends AbsTab {
	
	private WebDriverBackedSelenium _webDriverBackedSelenium = null;
	private WebDriver _webDriver = null;

	public static class Locators {

		// Buttons
		public static final String zBtnLogin = "css=input[class*=LoginButton]";


		// Desktop-specific
		public static final String zAddNewAccountButton = "css=td div[class*='ZPanel'][onclick*='OnAdd()']";
		public static final String zMyAccountsTab = "css=div[class$='ctive ZPanelFirstTab']";
		public static final String zBtnLoginDesktop = "css=div[id*='loginButton']";
		public static final String zDeleteButton = "css=div[class*='ZPanelInfoInner'] a[href*='OnDelete']";

		// Text Input
		public static final String zInputUsername = "css=input[id='username']";
		public static final String zInputPassword = "css=input[id='password']";
		public static final String zInputCode = "css=input[id='totpcode']";
		public static final String zInputRemember = "css=input[id='remember']";
		public static final String zTrustThisComputer = "css=input[id='trustedDevice']";
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
		AppType appType = ZimbraSeleniumProperties.getAppType();
		String locator = null;

		switch (appType) {
		case AJAX:
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

	@SuppressWarnings("deprecation")
	@Override
	public void zNavigateTo() throws HarnessException {

		if ( !zIsVisiblePerPosition(Locators.zBtnLogin, 10, 10) ) {
			
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
				ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.AJAX);

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
				logger.error("Unable to open ajax app. Is a valid cert installed?", e);
				throw e;
			}
			
			((AppAjaxClient)MyApplication).zPageLogin.zNavigateTo();
			
		}
		
		// Make sure the application is loaded first
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Ajax client application is not active!");
		
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
		if ( ((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zLogout();
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
			
			((AppAjaxClient)MyApplication).zPageMain.zWaitForActive(180000);

			((AppAjaxClient)MyApplication).zSetActiveAcount(account);

		} finally {
			
			SleepMetrics.RecordProcessing((new Throwable()).getStackTrace(), start, new Date());

		}
	}

	public AbsPage zSetupAfterLogin(ZimbraAccount account) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		tracer.trace("Login to the "+ MyApplication.myApplicationName() +" using user/password "+ account.EmailAddress +"/"+ account.Password);
		zNavigateTo();			
			zSetLoginName(account.EmailAddress);
			zSetLoginPassword(account.Password);

			// Click the Login button
			sClick(Locators.zBtnLogin);
			SleepUtil.sleepLong();
			AbsPage page = null;
			page = new Dialog2FactorAuthEnable(MyApplication, ((AppAjaxClient) MyApplication).zPageLogin);
			if ( page.zIsActive() ) {
				return (page);
			}
			
			return(null);
		
	}

	
	public void zLogin(ZimbraAccount account, String totp, boolean trustThisComputer) throws HarnessException {
		logger.debug("login(ZimbraAccount account)" + account.EmailAddress);

		tracer.trace("Login to the "+ MyApplication.myApplicationName() +" using user/password "+ account.EmailAddress +"/"+ account.Password);

		zNavigateTo();

		Date start = new Date();
		
		try {
			
			zSetLoginName(account.EmailAddress);
			zSetLoginPassword(account.Password);
			
			// Click the Login button
			sClick(Locators.zBtnLogin);
			SleepUtil.sleepMedium();
			zSetLoginTOTPCode(totp);
			
			if ( trustThisComputer == true){
				zMarkTrustThisComputer();
			}
			
			sClick(Locators.zBtnLogin);			
			
			((AppAjaxClient)MyApplication).zPageMain.zWaitForActive(180000);

			((AppAjaxClient)MyApplication).zSetActiveAcount(account);


		} finally {
			
			SleepMetrics.RecordProcessing((new Throwable()).getStackTrace(), start, new Date());

		}
	}

	/**
	 * Add the specified name to the login name field
	 * @param name
	 * @throws HarnessException
	 */
	public void zSetLoginName(String name) throws HarnessException {
		String locator = Locators.zInputUsername;
		if ( name == null ) {
			throw new HarnessException("Name is null");
		}

		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Login field does not exist "+ locator);
		}
		if (ZimbraSeleniumProperties.isWebDriver()){
		    clearField(locator);
		}
		sType(locator, name);
	}

	/**
	 * Add the specified password to the login password field
	 * @param name
	 * @throws HarnessException
	 */
	public void zSetLoginPassword(String password) throws HarnessException {
		String locator = Locators.zInputPassword;
		if ( password == null ) {
			throw new HarnessException("Password is null");
		}
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Password field does not exist "+ locator);
		}
		if (ZimbraSeleniumProperties.isWebDriver()){
		    clearField(locator);
		}
		sType(locator, password);
	}

	public void zSetLoginTOTPCode(String totpCode) throws HarnessException {
		String locator = Locators.zInputCode;
		if ( totpCode == null ) {
			throw new HarnessException("totp code is null");
		}
		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("totp code field does not exist "+ locator);
		}
		if (ZimbraSeleniumProperties.isWebDriver()){
		    clearField(locator);
		}
		sType(locator, totpCode);
	}

	public void zMarkTrustThisComputer() throws HarnessException {
		
		tracer.trace("Click on Trust this computer");
		
		SleepUtil.sleepSmall();
		this.sClick(Locators.zTrustThisComputer);
	
	}
	
	public Boolean zVerifyTrustThisComputer() throws HarnessException {
		return sIsElementPresent(Locators.zTrustThisComputer);
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
