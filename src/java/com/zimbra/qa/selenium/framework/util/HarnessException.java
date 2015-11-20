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
package com.zimbra.qa.selenium.framework.util;

import java.awt.Toolkit;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.thoughtworks.selenium.SeleniumException;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;
import com.zimbra.qa.selenium.projects.admin.ui.AppAdminConsole;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.html.ui.AppHtmlClient;
import com.zimbra.qa.selenium.projects.mobile.ui.AppMobileClient;
import com.zimbra.qa.selenium.projects.touch.ui.AppTouchClient;

@SuppressWarnings("deprecation")
public class HarnessException extends Exception {
	
	Logger logger = LogManager.getLogger(HarnessException.class);
	private static final long serialVersionUID = 4657095353247341818L;
	
	private WebDriver _webDriver = null;
	
	protected AppAjaxClient app1 = null;
	protected AppAdminConsole app2 = null;
	protected AppTouchClient app3 = null;
	protected AppHtmlClient app4 = null;
	protected AppMobileClient app5 = null;
	
	protected AbsTab startingPage = null;

	protected void resetAccounts() {
		
		if (ZimbraSeleniumProperties.getAppType() == AppType.AJAX) {
			app1 = new AppAjaxClient();
		} else if (ZimbraSeleniumProperties.getAppType() == AppType.ADMIN) {
			app2 = new AppAdminConsole();
		} else if (ZimbraSeleniumProperties.getAppType() == AppType.TOUCH) {
			app3 = new AppTouchClient();
		} else if (ZimbraSeleniumProperties.getAppType() == AppType.HTML) {
			app4 = new AppHtmlClient();
		} else if (ZimbraSeleniumProperties.getAppType() == AppType.MOBILE) {
			app5 = new AppMobileClient();
		}
		
		logger.error("HarnessException: Kill the browser and relogin");
		killBrowserAndRelogin();
		
		logger.error("HarnessException: Reset account due to exception");
		ZimbraAccount.ResetAccountZWC();
		ZimbraAdminAccount.ResetAccountAdminConsoleAdmin();
		ZimbraAccount.ResetAccountHTML();
		ZimbraAccount.ResetAccountZMC();
		ZimbraAccount.ResetAccountZTC();

	}
	
	public HarnessException(String message) {
		super(message);
		logger.error(message, this);
		resetAccounts();
	}

	public HarnessException(Throwable cause) {
		super(cause);
		logger.error(cause.getMessage(), cause);
		resetAccounts();
	}

	public HarnessException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message, cause);
		resetAccounts();
	}
	
	
	public void killBrowserAndRelogin () {
		
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

				@SuppressWarnings("unused")
				String timeout = ZimbraSeleniumProperties.getStringProperty("selenium.maxpageload.msec", "100000");

				ClientSessionFactory.session().selenium().start();
				ClientSessionFactory.session().selenium().windowMaximize();
				ClientSessionFactory.session().selenium().windowFocus();
				ClientSessionFactory.session().selenium().allowNativeXpath("true");
				ClientSessionFactory.session().selenium().setTimeout("100000");
				ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getBaseURL());
			}
			
		} catch (SeleniumException e) {
			logger.error("Unable to open app.", e);
			throw e;
		}
		
		try {
			
			if (ZimbraSeleniumProperties.getAppType() == AppType.AJAX) {
				if (ZimbraAccount.AccountZWC() != null) {
					((AppAjaxClient)app1).zPageLogin.zLogin(ZimbraAccount.AccountZWC());
				} else {
					((AppAjaxClient)app1).zPageLogin.zLogin(ZimbraAccount.Account10());
				}
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.ADMIN) {
				if (ZimbraAdminAccount.GlobalAdmin() != null) {
					((AppAdminConsole)app2).zPageLogin.login(ZimbraAdminAccount.GlobalAdmin());
				} else {
					((AppAdminConsole)app2).zPageLogin.login(ZimbraAccount.Account10());
				}
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.TOUCH) {
				if (ZimbraAccount.AccountZTC() != null) {
					((AppTouchClient)app3).zPageLogin.zLogin(ZimbraAccount.AccountZTC());
				} else {
					((AppTouchClient)app3).zPageLogin.zLogin(ZimbraAccount.Account10());
				}
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.HTML) {
				if (ZimbraAccount.AccountHTML() != null) {
					((AppHtmlClient)app4).zPageLogin.zLogin(ZimbraAccount.AccountHTML());
				} else {
					((AppHtmlClient)app4).zPageLogin.zLogin(ZimbraAccount.Account10());
				}
			} else if (ZimbraSeleniumProperties.getAppType() == AppType.MOBILE) {
				if (ZimbraAccount.AccountZMC() != null) {
					((AppMobileClient)app5).zPageLogin.zLogin(ZimbraAccount.AccountZMC());
				} else {
					((AppMobileClient)app5).zPageLogin.zLogin(ZimbraAccount.Account10());
				}
			}
			
		} catch (HarnessException e) {
			logger.error("Unable to navigate to app.", e);
		}
		
	}

}
