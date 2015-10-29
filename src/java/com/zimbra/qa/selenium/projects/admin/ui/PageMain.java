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
package com.zimbra.qa.selenium.projects.admin.ui;

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
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.CommandLine;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;


/**
 * This class defines the top menu bar of the admin console application
 * @author Matt Rhoades
 *
 */
public class PageMain extends AbsTab {
	
	private WebDriverBackedSelenium _webDriverBackedSelenium = null;
	private WebDriver _webDriver = null;

	public static class Locators {
		public static final String zSkinContainerLogo		= "xpath=//*[@id='skin_container_logo']";
		public static final String zSkinContainerUsername	= "css=div[id='skin_container_username']";
		//public static final String zSkinContainerLogoff		= "css=table[class='skin_table'] span[onclick='ZaZimbraAdmin.logOff();']";
		public static final String zLogoffDropDownArrow		="css=div.ImgNodeExpandedWhite";
		public static final String zLogOff = "zmi__ZA_LOGOFF__LOGOFF_title";
		public static final String zSkinContainerHelp		= "xpath=//*[@id='skin_container_help']";
		public static final String zSkinContainerDW			= "xpath=//*[@id='skin_container_dw']";
		public static final String REFRESH_BUTTON = "css=div.ImgSearchRefreshWhite";
		
		public static final String HomeInstallLicense = "css=#ztabv__HOMEV_output_9 > div";
		public static final String HomeConfigureBackups = "css=#ztabv__HOMEV_output_10 > div";

		public static final String HomeInstallCertificate = "css=#ztabv__HOMEV_output_11 > div";

		public static final String HomeConfigureDefaultCos = "css=#ztabv__HOMEV_output_12 > div";

		public static final String HomeCreateDomain = "css=#ztabv__HOMEV_output_14 > div";

		public static final String HomeConfigureGal = "css=#ztabv__HOMEV_output_15 > div";

		public static final String HomeConfigureAuthentication = "css=#ztabv__HOMEV_output_16 > div";

		public static final String HomeAddAcoount = "css=#ztabv__HOMEV_output_18 > div";
		
		public static final String HomeManageAccount = "css=#ztabv__HOMEV_output_19 > div";

		public static final String HomeMigrationCoexistance = "css=#ztabv__HOMEV_output_20 > div";

	}

	public PageMain(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/**
	 * If the "Logout" button is visible, assume the MainPage is active
	 */
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Admin Console application is not active!");


		// Look for the Refresh Button
		boolean present = sIsElementPresent(Locators.zLogoffDropDownArrow);
		if ( !present ) {
			logger.debug("isActive() present = "+ present);
			return (false);
		}


		// Look for the Refresh Button.
		boolean visible = zIsVisiblePerPosition(Locators.zLogoffDropDownArrow, 0, 0);
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
			// This page is already active
			return;
		}

		// 1. Logout
		// 2. Login as the default account
		if ( !((AppAdminConsole)MyApplication).zPageLogin.zIsActive() ) {
			((AppAdminConsole)MyApplication).zPageLogin.zNavigateTo();
		}
		((AppAdminConsole)MyApplication).zPageLogin.login();
		SleepUtil.sleepLong();
	}

	/**
	 * Click the logout button
	 * @throws HarnessException
	 */
	@SuppressWarnings("deprecation")
	public void logout() throws HarnessException {
		
		logger.debug("logout()");
		tracer.trace("Logout of the "+ MyApplication.myApplicationName());
		
		if ( zIsVisiblePerPosition(Locators.zLogoffDropDownArrow, 10, 10) ) {
			
			// Click on logout
			sClickAt(Locators.zLogoffDropDownArrow,"");
			sClickAt(Locators.zLogOff,"");
			
			SleepUtil.sleepSmall();
			if ( zIsVisiblePerPosition(PageLogin.Locators.zLoginButtonContainer, 10, 10) ) {
				SleepUtil.sleepSmall();
				if ( zIsVisiblePerPosition(PageLogin.Locators.zLoginButtonContainer, 10, 10) ) {
					SleepUtil.sleepMedium();
				}
			}

			((AppAdminConsole)MyApplication).zPageLogin.zWaitForActive();
			((AppAdminConsole)MyApplication).zSetActiveAcount(null);
			
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
				logger.error("Unable to open admin console. Is a valid cert installed?", e);
				throw e;
			}
			
			((AppAdminConsole)MyApplication).zPageLogin.zNavigateTo();			
			((AppAdminConsole)MyApplication).zPageLogin.zWaitForActive();
			((AppAdminConsole)MyApplication).zSetActiveAcount(null);
			
		}

	}

	public String getContainerUsername() throws HarnessException {
		logger.debug("getLoggedInAccount()");

		if ( !zIsActive() )
			throw new HarnessException("MainPage is not active");

		String username = sGetText(Locators.zSkinContainerUsername);
		return (username);

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


	public boolean zVerifyHeader (String header) throws HarnessException {
		if(this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}
}
