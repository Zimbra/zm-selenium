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

import java.io.File;
import java.util.Set;
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
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * A <code>ClientSession</code> object contains all session information for the
 * test methods.
 * <p>
 * The Zimbra Selenium harness is designed to execute test cases concurrently at
 * the class level.
 *
 * The {@link ClientSession} objects maintain all session information on a per
 * thread basis, such as the current DefaultSelenium object. Each TestNG thread
 * uses a single {@link ClientSession} Object.
 * <p>
 * Use the {@link ClientSessionFactory} to retrieve the current
 * {@link ClientSession}.
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

			String driverPath = null;

			if (ConfigProperties.getCalculatedBrowser().contains("iexplore") ||	ConfigProperties.getCalculatedBrowser().contains("ie")) {

				switch (OperatingSystem.getOSType()) {
				
					case WINDOWS: default:
						driverPath = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/IEDriverServer.exe";
						break;

					case WINDOWS10:
						driverPath = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/MicrosoftWebDriver.exe";
						break;
				}

				DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer();
				System.setProperty("webdriver.ie.driver", driverPath);
				webDriver = new InternetExplorerDriver(desiredCapabilities);

			} else if (ConfigProperties.getCalculatedBrowser().contains("firefox")) {

				switch (OperatingSystem.getOSType()) {
				
					case WINDOWS: default:
						driverPath = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/geckodriver.exe";
						break;

					case LINUX: case MAC:
						driverPath = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/geckodriver";
						break;
				}

				File file = new File(ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/firebug-2.0.17-fx.xpi");
				System.setProperty("webdriver.gecko.driver", driverPath);
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				FirefoxProfile profile = new FirefoxProfile();
				profile.addExtension(file);
				capabilities.setCapability(FirefoxDriver.PROFILE, profile);
				capabilities.setCapability("marionette", true);
				webDriver = new FirefoxDriver(capabilities);
				
				// Close the firebug window
				SleepUtil.sleepLong(); // Firebug window takes time to open it
				Set <String> windows = webDriver.getWindowHandles();
			    String mainwindow = webDriver.getWindowHandle();

			    for (String handle: windows) {
			    	webDriver.switchTo().window(handle);
			        if (!handle.equals(mainwindow)) {
			        	webDriver.close();
			        }
			    }
			    webDriver.switchTo().window(mainwindow);

			} else {

				switch (OperatingSystem.getOSType()) {
				
					case WINDOWS: default:
						driverPath = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/chromedriver.exe";
						break;

					case LINUX: case MAC:
						driverPath = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/chromedriver";
						break;
				}

				ChromeOptions options = new ChromeOptions();
				options.addArguments("chrome.switches", "--disable-extensions");
				options.addArguments("--start-maximized");
				System.setProperty("webdriver.chrome.driver", driverPath);
				webDriver = new ChromeDriver(options);
			}
		}

		return webDriver;
	}

	public String currentUserName() {
		if (currentAccount == null) {
			return ("");
		}
		return (currentAccount.EmailAddress);
	}

	public String setCurrentUser(ZimbraAccount account) {
		currentAccount = account;
		return (currentUserName());
	}

	public String toString() {
		logger.debug("ClientSession.toString()=" + sessionName);
		return (sessionName);
	}
}
