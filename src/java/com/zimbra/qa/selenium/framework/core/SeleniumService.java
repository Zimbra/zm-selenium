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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zimbra.qa.selenium.framework.util.CommandLineUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.SleepMetrics;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;

@SuppressWarnings("unused")
public class SeleniumService {
	private static Logger logger = LogManager.getLogger(SeleniumService.class);

	public enum SeleniumMode {
		Local, Remote, Grid, SauceLabs
	}

	public void startSeleniumExecution() throws HarnessException {
		Date start = new Date();
		try {
			if ( mode == SeleniumMode.Local ) {
				// stopBrowsers();
			}

		} catch (Exception e) {
			throw new HarnessException("Unable to start selenium service", e);

		} finally {
			SleepMetrics.RecordProcessing((new Throwable()).getStackTrace(), start, new Date());
		}
	}

	public void stopSeleniumExecution() throws HarnessException, IOException {
		// stopBrowsers();

		try {

			if ( mode == SeleniumMode.Local ) {

				BufferedReader in = null;
				try {

					URI stopUri = new URI("http", null, SeleniumServer, SeleniumPort, "/selenium-server/driver", "cmd=shutDownSeleniumServer", null);
					in = new BufferedReader(new InputStreamReader(stopUri.toURL().openStream()));
					if (in.ready()) {
						logger.info("A Selenium Server was not stopped. Attempting to kill");
					}

					String line;
					while ((line = in.readLine()) != null) {
						logger.info(line);
					}

				} catch (IOException e) {
					logger.warn("Selenium server is stopped");
				}
			}

		} catch (Exception e) {
			throw new HarnessException("Unable to stop SeleniumService", e);
		}
	}

	public boolean isSeleniumMode(SeleniumMode m) {
		return (mode.equals(m));
	}

	public String getSeleniumServer() {
		return (SeleniumServer);
	}

	public int getSeleniumPort() {
		return (SeleniumPort);
	}

	public String getSeleniumBrowser() {
		return (SeleniumBrowser);
	}

	public String getSeleniumServerHost() {
		return (SeleniumServerHost);
	}

	public String getSeleniumBrowserVersion() {
		return (SeleniumBrowserVersion);
	}

	private void stopBrowsers() throws HarnessException {
		stopBrowsersWindows();
		stopBrowsersLinux();
		stopBrowsersMac();
		stopExternalUtilities();
	}

	private void stopBrowsersWindows() throws HarnessException {

		// Only run for windows
		if ( !OperatingSystem.isWindows() )
			return;

		try {
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

	}

	private void stopBrowsersLinux() throws HarnessException {
		// Only run for Linux
		if ( !OperatingSystem.isLinux() ) {
			return;
		}

		try {
			if (SeleniumBrowser.contains("firefox")) {
				CommandLineUtility.CmdExec("killall firefox");
				CommandLineUtility.CmdExec("killall geckodriver");
			} else if (SeleniumBrowser.contains("chrome")) {
				CommandLineUtility.CmdExec("killall googlechrome");
				CommandLineUtility.CmdExec("killall chromedriver");
			}

		} catch (IOException e) {
			throw new HarnessException("Unable to kill browsers", e);
		} catch (InterruptedException e) {
			throw new HarnessException("Unable to kill browsers", e);
		}
	}

	private void stopBrowsersMac() throws HarnessException {
		// Only run for Mac
		if ( !OperatingSystem.isMac() ) {
			return;
		}

		try {
			if (SeleniumBrowser.contains("firefox")) {
				CommandLineUtility.CmdExec("pkill -f firefox");
				CommandLineUtility.CmdExec("pkill -f geckodriver");
			} else if (SeleniumBrowser.contains("chrome")) {
				CommandLineUtility.CmdExec("pkill -f googlechrome");
				CommandLineUtility.CmdExec("pkill -f chromedriver");
			} else if (SeleniumBrowser.contains("safari")) {
				CommandLineUtility.CmdExec("pkill -f safari");
			}

		} catch (IOException e) {
			throw new HarnessException("Unable to kill browsers", e);
		} catch (InterruptedException e) {
			throw new HarnessException("Unable to kill browsers", e);
		}
	}
	
	private void stopExternalUtilities() throws HarnessException {
		// Only run for windows
		if ( !OperatingSystem.isWindows() )
			return;

		try {
			CommandLineUtility.CmdExec("taskkill /f /t /im ClickToOpenButton.exe");
			CommandLineUtility.CmdExec("taskkill /f /t /im CloseFileExplorerWindow.exe");
			CommandLineUtility.CmdExec("taskkill /f /t /im CloseNewWindow.exe");
			CommandLineUtility.CmdExec("taskkill /f /t /im SetFocusToFileNameField.exe");
		} catch (IOException e) {
			throw new HarnessException("Unable to kill external utilities", e);
		} catch (InterruptedException e) {
			throw new HarnessException("Unable to kill external utilities", e);
		}

	}

	private SeleniumMode mode;
	private String SeleniumServer;
	private int SeleniumPort;
	private String SeleniumBrowser;
	private String SeleniumServerHost;
	private String SeleniumBrowserVersion;

	public static SeleniumService getInstance() {
		if (Instance == null) {
			synchronized(SeleniumService.class) {
				if ( Instance == null) {
					Instance = new SeleniumService();
				}
			}
		}
		return (Instance);
	}

	private volatile static SeleniumService Instance;

	public SeleniumService() {
		logger.info("New SeleniumService object");

		String modeProp = ConfigProperties.getStringProperty("seleniumMode").toLowerCase();
		logger.info("New SeleniumService object: "+ modeProp);

		// Set Defaults
		mode = SeleniumMode.Local;
		SeleniumServer = ConfigProperties.getStringProperty("serverName");
		SeleniumPort = ConfigProperties.getIntProperty("serverPort");
		SeleniumBrowser = ConfigProperties.getStringProperty("browser");
		SeleniumServerHost = ConfigProperties.getStringProperty("server.host");

		if (modeProp.equals(SeleniumMode.Local.toString().toLowerCase())) {
			mode = SeleniumMode.Local;

		} else if (modeProp.equals(SeleniumMode.Remote.toString().toLowerCase())) {
			mode = SeleniumMode.Remote;

		} else if (modeProp.equals(SeleniumMode.Grid.toString().toLowerCase())) {
			mode = SeleniumMode.Grid;
			SeleniumServer = ConfigProperties.getStringProperty("grid.serverMachineName", "tbd.lab.zimbra.com");
			SeleniumPort = ConfigProperties.getIntProperty("grid.serverMachinePort", 4444);

		} else if (modeProp.equals(SeleniumMode.SauceLabs.toString().toLowerCase())) {
			mode = SeleniumMode.SauceLabs;
			SeleniumServer = ConfigProperties.getStringProperty("sauce.serverMachineName", "ondemand.saucelabs.com");
			SeleniumPort = ConfigProperties.getIntProperty("sauce.serverMachinePort", 80);
			SeleniumBrowser = "{\"username\": \"" + ConfigProperties.getStringProperty("sauceUsername") + "\"," +
	          "\"access-key\": \"" + ConfigProperties.getStringProperty("sauceAccessKey") + "\"," +
	          "\"os\": \"" + ConfigProperties.getStringProperty("os", "Windows 2003") + "\"," +
	          "\"browser\": \"" + ConfigProperties.getStringProperty("browser") + "\"," +
	          "\"browser-version\": \"" + ConfigProperties.getStringProperty("browserVersion") + "\"," +
	          "\"user-extensions-url\": \"http://" + ConfigProperties.getStringProperty("server.host") + ":8080/user-extensions.js\"}";

		} else {

			logger.error("Unknown seleniumMode "+ modeProp + ".  Using "+ SeleniumMode.Local);

		}
	}
}