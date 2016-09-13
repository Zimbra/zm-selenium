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
package com.zimbra.qa.selenium.framework.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

/**
 * A <code>ClientSession</code> object contains all session information for the test methods.
 * <p>
 * The Zimbra Selenium harness is designed to
 * execute test cases concurrently at the class level.
 *
 * The {@link ClientSession} objects maintain all session information on
 * a per thread basis, such as the current DefaultSelenium object.  Each
 * TestNG thread uses a single {@link ClientSession} Object.
 * <p>
 * Use the {@link ClientSessionFactory} to retrieve the current {@link ClientSession}.
 * <p>
 *
 * @author Matt Rhoades
 *
 */
public class ClientSession {
	
	private static Logger logger = LogManager.getLogger(ClientSession.class);
	private String sessionName;
	private WebDriver webDriver = null;
	private ZimbraAccount currentAccount = null;

	public WebDriver webDriver() {

		if (webDriver == null) {

			if (ConfigProperties.getCalculatedBrowser().contains("iexplore")) {

				DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer();
				System.setProperty("webdriver.ie.driver", ConfigProperties.getStringProperty("iedriver.path"));
				webDriver = new InternetExplorerDriver(desiredCapabilities);

			} else if(ConfigProperties.getCalculatedBrowser().contains("googlechrome")) {

				ChromeOptions options = new ChromeOptions();
				options.addArguments("chrome.switches","--disable-extensions");
				options.addArguments("--start-maximized");
				System.setProperty("webdriver.chrome.driver", ConfigProperties.getStringProperty("chromedriver.path"));
				webDriver = new ChromeDriver(options);

			} else {

				System.setProperty("webdriver.gecko.driver", ConfigProperties.getStringProperty("geckodriver.path"));
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				FirefoxProfile profile = new FirefoxProfile();
				capabilities.setCapability(FirefoxDriver.PROFILE, profile);
				capabilities.setCapability("marionette", true);
				webDriver =  new FirefoxDriver(capabilities);
			}
		}

		return webDriver;
	}

	public String currentUserName() {
		if ( currentAccount == null ) {
			return ("");
		}
		return (currentAccount.EmailAddress);
	}

	public String setCurrentUser(ZimbraAccount account) {
		currentAccount = account;
		return (currentUserName());
	}

	/**
	 * A unique string ID for this ClientSession object
	 */
	public String toString() {
		logger.debug("ClientSession.toString()="+ sessionName);
		return (sessionName);
	}
}
