/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.ui;

import java.awt.Toolkit;
import java.io.IOException;
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
import com.zimbra.qa.selenium.projects.touch.ui.mail.*;

public class PageMain extends AbsTab {
	
	private WebDriverBackedSelenium _webDriverBackedSelenium = null;
	private WebDriver _webDriver = null;

	public static class Locators {
		
		public static final String zNavigationButton	= "css=span[class='x-button-icon x-shown organizer']";
		public static final String zAppsButton			= "css=span[class='x-button-icon x-shown apps']";
		public static final String zSignOutButton		= "css=div[class='zcs-menu-label']:contains('Sign Out')";
		
		public static final String zMailApp				= "css=div[class='zcs-menu-label']:contains('Mail')";
		public static final String zContactsApp			= "css=div[class='zcs-menu-label']:contains('Contacts')";
		public static final String zCalendarApp			= "css=div[class='zcs-menu-label']:contains('Calendar')";

	}
	
	public PageMain(AbsApplication application) {
		super(application);
		logger.info("new " + PageMain.class.getCanonicalName());
	}
	
	@Override
	public boolean zIsActive() throws HarnessException {
		
		SleepUtil.sleepSmall();
		boolean present = sIsElementPresent(Locators.zNavigationButton);
		if ( !present ) {
			logger.debug("Settings button present = "+ present);
			SleepUtil.sleepSmall();
			return (false);
		}

		logger.debug("isActive() = "+ true);
		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}


	@Override
	public void zNavigateTo() throws HarnessException {


		if ( zIsActive() ) {
			// This page is already active
			return;
		}
			
		// 1. Logout
		// 2. Login as the default account
		if ( !((AppTouchClient)MyApplication).zPageLogin.zIsActive() ) {
			((AppTouchClient)MyApplication).zPageLogin.zNavigateTo();
		}
		((AppTouchClient)MyApplication).zPageLogin.zLogin(ZimbraAccount.AccountZTC());
		
		// June 28, 2013 (9.0.0 Beta)
		// For coverage, it seems to take longer than 60 seconds for the page to load
		// Update the value to 120 seconds, and then make sure the harness doesn't slow
		// down, or else that could mean a slowdown in the client.
		//
		// zWaitForActive();		// Default: 60 seconds
		zWaitForActive(120000);
		
	}

	/**
	 * Click the logout button
	 * @throws HarnessException
	 */
	@SuppressWarnings("deprecation")
	public void zLogout() throws HarnessException {
		
		logger.debug("logout()");
		tracer.trace("Logout of the "+ MyApplication.myApplicationName());
		
		if ( zIsVisiblePerPosition(Locators.zNavigationButton, 10, 10) ) {
			
			if (ZimbraSeleniumProperties.isWebDriver()) {
				getElement("css=div[class=DwtLinkButtonDropDownArrow]").click();
			} else if ( !sIsElementPresent(Locators.zNavigationButton) ) {
				throw new HarnessException("The app navigation button is not present " + Locators.zNavigationButton);
			}

			sClickAt(Locators.zNavigationButton, "0,0");
			SleepUtil.sleepSmall();
			
			if (ZimbraSeleniumProperties.isWebDriver()) {
				getElement("css=tr[id=POPUP_logOff]>td[id=logOff_title]").click();	
			} else if ( !sIsElementPresent(Locators.zAppsButton) ) {
				throw new HarnessException("The application button is not present " + Locators.zAppsButton);
			}

			sClickAt(Locators.zAppsButton, "0,0");
			SleepUtil.sleepSmall();		
			sClickAt(Locators.zSignOutButton, "0,0");
			
			SleepUtil.sleepSmall();
			if ( zIsVisiblePerPosition(PageLogin.Locators.zBtnLogin, 10, 10) ) {
				SleepUtil.sleepSmall();
				if ( zIsVisiblePerPosition(PageLogin.Locators.zBtnLogin, 10, 10) ) {
					SleepUtil.sleepMedium();
				}
			}
			
			((AppTouchClient)MyApplication).zPageLogin.zWaitForActive();
			((AppTouchClient)MyApplication).zSetActiveAcount(null);
			
		} else {
			
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
				ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.TOUCH);

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
				logger.error("Unable to open touch client. Is a valid cert installed?", e);
				throw e;
			}
			
			((AppTouchClient)MyApplication).zPageLogin.zNavigateTo();			
			((AppTouchClient)MyApplication).zPageLogin.zWaitForActive();
			((AppTouchClient)MyApplication).zSetActiveAcount(null);
			
		}

		
	}

	@SuppressWarnings("unused")
	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		// Q. Should the tabs or help or logout be processed here?
		// A. I don't think those are considered "toolbars", so don't handle here for now (Matt)

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		// Default behavior variables
		//
		String locator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if (button == Button.B_REFRESH) {
			
			//locator = Locators.ButtonRefreshLocatorCSS;
			page = null;
			
		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		// Default behavior, process the locator by clicking on it
		//
		this.zClick(locator);
		SleepUtil.sleepSmall();

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		// If page was specified, make sure it is active
		if (page != null) {

			// This function (default) throws an exception if never active
			page.zWaitForActive();

		}

		return (page);
		
	}

	@SuppressWarnings("unused")
	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");
		
		tracer.trace("Click pulldown "+ pulldown +" then "+ option);
		
		
		
		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		
		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned
		

		if (pulldown == Button.B_ACCOUNT) {
			
			
			if (option == Button.O_PRODUCT_HELP) {

				pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
				optionLocator = "css=div[id^='POPUP'] div[id='documentation'] td[id$='_title']";
				
				//SeparateWindow window = new SeparateWindow(this.MyApplication);
				//window.zInitializeWindowNames();
				
				this.zClickAt(pulldownLocator, "0,0");
				this.zWaitForBusyOverlay();

				this.zClickAt(optionLocator, "0,0");
				this.zWaitForBusyOverlay();

				//return (window);
				
			} else if (option == Button.O_ABOUT) {

					pulldownLocator = "css=div#skin_outer td#skin_dropMenu div.DwtLinkButtonDropDownArrow";
					optionLocator = "css=div[id^='POPUP'] div[id='about'] td[id$='_title']";
					//page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, this.MyApplication, this);

					// FALL THROUGH
					
			} else {
				
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}
			

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		
		
		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator, "0,0");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator, "0,0");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
				
			}

		}
		
		// If we click on pulldown/option and the page is specified, then
		// wait for the page to go active
		if (page != null) {
			
			page.zWaitForActive();
			
		}

		// Return the specified page, or null if not set
		return (page);

	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException("Main page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException("Main page does not have lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {
		throw new HarnessException("Main page does not have lists");
	}	
	
	/**
	 * Change the URL (and reload) to access deep-link pages
	 * @param uri The URL to access (e.g. ?to=foo@foo.com&body=MsgContent&subject=MsgSubject&view=compose)
	 * @return the page that opens
	 * @throws HarnessException 
	 */
	public AbsPage zOpenDeeplink(ZimbraURI uri) throws HarnessException {
		logger.info("PageMain.zOpenDeeplink("+ uri.toString() + ")");
		
		AbsPage page = null;
		
		
		if ( !uri.getQuery().containsKey("view") ) {
			throw new HarnessException("query attribute 'view' must be specified");
		}
		
		if ( uri.getQuery().get("view").equals("compose") ) {
			
			page = new FormMailNew(this.MyApplication);
			
			// FALL THROUGH
			
		} else if ( uri.getQuery().get("view").equals("msg") ) {
			
			// page = new DisplayMail(this.MyApplication);
			throw new HarnessException("implement me!");
			
			// FALL THROUGH
			
		} else {
			
			throw new HarnessException("query attribute 'view' must be specified");
			
		}
		
		// Re-open the URL
		this.sOpen(uri.getURL().toString());

		if ( page != null ) {
			page.zWaitForActive();
		}
		
		return (page);

	}

	

}
