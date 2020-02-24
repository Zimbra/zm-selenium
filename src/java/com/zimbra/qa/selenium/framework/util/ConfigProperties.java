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
package com.zimbra.qa.selenium.framework.util;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;

public class ConfigProperties {
	private static final Logger logger = LogManager.getLogger(ConfigProperties.class);

	private static InetAddress localMachine;
	private static ConfigProperties instance = null;
	private File BaseDirectory = null;
	private File PropertiesConfigurationFilename = null;
	private PropertiesConfiguration configProp;

	public static void setStringProperty(String key, String value) {
		ConfigProperties.getInstance().getConfigProp().setProperty(key, value);
	}

	public static String getStringProperty(String key, String defaultValue) {
		return (ConfigProperties.getInstance().getConfigProp().getString(key, defaultValue));
	}

	public static Boolean isZimbra9XEnvironment() {
		String environment = ExecuteHarnessMain.zimbraVersion.split("BETA")[0].split("GA")[0].replaceAll("\\.", "");
		if (environment.toLowerCase().equals("8816") || environment.toLowerCase().equals("900")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getStringProperty(String key) {
		return (getStringProperty(key, null));
	}

	public static int getIntProperty(String key) {
		return (getIntProperty(key, 0));
	}

	public static int getIntProperty(String key, int defaultValue) {
		String value = ConfigProperties.getInstance().getConfigProp().getString(key, null);
		if (value == null)
			return (defaultValue);
		return (Integer.parseInt(value));
	}

	private static int counter = 0;

	public static String getUniqueString() {
		return ("" + System.currentTimeMillis() + (++counter));
	}

	public static ResourceBundle getResourceBundleProperty(String key) {
		return ((ResourceBundle) ConfigProperties.getInstance().getConfigProp().getProperty(key));
	}

	public static PropertiesConfiguration getConfigProperties() {
		return ConfigProperties.getInstance().getConfigProp();
	}

	public static PropertiesConfiguration setConfigProperties(String filename) {
		logger.info("setConfigProperties using: " + filename);
		ConfigProperties.getInstance().PropertiesConfigurationFilename = new File(filename);
		ConfigProperties.getInstance().init();
		return (ConfigProperties.getInstance().getConfigProp());
	}

	public static String getBaseDirectory() {
		if (ConfigProperties.getInstance().BaseDirectory == null)
			return (".");
		return (ConfigProperties.getInstance().BaseDirectory.getAbsolutePath());
	}

	public static File setBaseDirectory(String directory) {
		logger.info("setWorkingDirectory using: " + directory);
		ConfigProperties.getInstance().BaseDirectory = new File(directory);
		return (ConfigProperties.getInstance().BaseDirectory);
	}

	private PropertiesConfiguration getConfigProp() {
		return configProp;
	}

	private static ConfigProperties getInstance() {
		if (instance == null) {
			synchronized (ConfigProperties.class) {
				if (instance == null) {
					instance = new ConfigProperties();
					instance.init();
				}
			}
		}
		return instance;
	}

	private ConfigProperties() {
		logger.debug("new ConfigProperties");
	}

	private void init() {

		// Load the config.properties values
		if (PropertiesConfigurationFilename == null) {
			logger.info("config.properties is default");
			configProp = createDefaultProperties();
		} else {
			try {
				logger.info("config.properties is " + PropertiesConfigurationFilename.getAbsolutePath());
				configProp = new PropertiesConfiguration();
				configProp.load(PropertiesConfigurationFilename);
			} catch (ConfigurationException e) {
				logger.error("Unable to open config file: " + PropertiesConfigurationFilename.getAbsolutePath(), e);
				logger.info("config.properties is default");
				configProp = createDefaultProperties();
			}
		}

		// Load the locale information
		String locale = configProp.getString("locale");
		configProp.setProperty("zmMsg", ResourceBundle.getBundle("ZmMsg", new Locale(locale)));
		configProp.setProperty("zhMsg", ResourceBundle.getBundle("ZhMsg", new Locale(locale)));
		configProp.setProperty("ajxMsg", ResourceBundle.getBundle("AjxMsg", new Locale(locale)));
		configProp.setProperty("i18Msg", ResourceBundle.getBundle("I18nMsg", new Locale(locale)));
		configProp.setProperty("zsMsg", ResourceBundle.getBundle("ZsMsg", new Locale(locale)));
	}

	private PropertiesConfiguration createDefaultProperties() {
		PropertiesConfiguration defaultProp = new PropertiesConfiguration();

		defaultProp.setProperty("browser", "FF3");
		defaultProp.setProperty("runMode", "DEBUG");
		defaultProp.setProperty("product", "zcs");
		defaultProp.setProperty("locale", "en_US");
		defaultProp.setProperty("intl", "us");
		defaultProp.setProperty("testdomain", "testdomain.com");
		defaultProp.setProperty("multiWindow", "true");
		defaultProp.setProperty("objectDataFile", "projects/zcs/data/objectdata.xml");
		defaultProp.setProperty("testDataFile", "projects/zcs/data/testdata.xml");
		defaultProp.setProperty("serverMachineName", "localhost");
		defaultProp.setProperty("serverport", "4444");
		defaultProp.setProperty("mode", "http");
		defaultProp.setProperty("server", "zqa-062.eng.zimbra.com");
		defaultProp.setProperty("testOutputDirectory", "test-output");
		defaultProp.setProperty("very_small_wait", "1000");
		defaultProp.setProperty("small_wait", "1000");
		defaultProp.setProperty("medium_wait", "2000");
		defaultProp.setProperty("long_wait", "4000");
		defaultProp.setProperty("long_medium_wait", "6000");
		defaultProp.setProperty("very_long_wait", "10000");
		String locale = defaultProp.getString("locale");
		defaultProp.setProperty("zmMsg", ResourceBundle.getBundle("ZmMsg", new Locale(locale)));
		defaultProp.setProperty("zhMsg", ResourceBundle.getBundle("ZhMsg", new Locale(locale)));
		defaultProp.setProperty("ajxMsg", ResourceBundle.getBundle("AjxMsg", new Locale(locale)));
		defaultProp.setProperty("i18Msg", ResourceBundle.getBundle("I18nMsg", new Locale(locale)));
		defaultProp.setProperty("zsMsg", ResourceBundle.getBundle("ZsMsg", new Locale(locale)));

		return defaultProp;
	}

	public enum AppType {
		AJAX, HTML, MOBILE, TOUCH, ADMIN, UNIVERSAL
	}

	private static AppType appType = AppType.AJAX;

	public static void setAppType(AppType type) {
		appType = type;
	}

	public static AppType getAppType() {
		return (appType);
	}

	public static String getLocalHost() {
		try {
			localMachine = InetAddress.getLocalHost();
			return localMachine.getHostName();
		} catch (Exception e) {
			logger.info(e.fillInStackTrace());
			return "127.0.0.1";
		}
	}

	private static final String CalculatedBrowser = "CalculatedBrowser";

	public static String getCalculatedBrowser() {
		String browser = getStringProperty(CalculatedBrowser);

		if (browser != null) {
			return (browser);
		}

		browser = ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".browser",
				ConfigProperties.getStringProperty("browser"));

		if (browser.charAt(0) == '*') {
			browser = browser.substring(1);
			if ((browser.indexOf(" ")) > 0) {
				String str = browser.split(" ")[0];
				int i;
				if ((i = browser.lastIndexOf("\\")) > 0) {
					str += "_" + browser.substring(i + 1);
				}
				browser = str;
			}
		}

		ConfigProperties.setStringProperty(CalculatedBrowser, browser);
		return (browser);
	}

	public static String getLogoutURL() {
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
		uri.addQuery("loginOp", "logout");
		return (uri.toString());
	}

	public static String getBaseURL() {
		return (ZimbraURI.getBaseURI().toString());
	}

	public static String zimbraGetVersionString() throws HarnessException {
		String buildType = "NETWORK";

		if (ExecuteHarnessMain.cmdVersion == null || ExecuteHarnessMain.cmdVersion == "") {
			logger.info("Get zimbra server version...");
			for (String zimbraVersion : CommandLineUtility
					.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"), "zmcontrol -v")) {
				ExecuteHarnessMain.cmdVersion = zimbraVersion;
			}
		}

		if (ExecuteHarnessMain.zimbraVersion == null || ExecuteHarnessMain.zimbraVersion == "") {
			if (!ExecuteHarnessMain.cmdVersion.toLowerCase().contains("network")) {
				buildType = "FOSS";
			}

			Date date = new Date();
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("hh.mm");

			// Get hostname
			try {
				ExecuteHarnessMain.hostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			if (!ExecuteHarnessMain.hostname.contains(".") && !ExecuteHarnessMain.hostname.contains("-")) {
				ExecuteHarnessMain.zimbraVersion = ExecuteHarnessMain.cmdVersion.replace("Release ", "")
						.split(" ")[0] + "_" + buildType;
			} else {
				ExecuteHarnessMain.zimbraVersion = ExecuteHarnessMain.cmdVersion.replace("Release ", "")
						.split(" ")[0] + "_" + buildType + "-" + dateTimeFormat.format(date);
			}
		}

		return ExecuteHarnessMain.zimbraVersion;
	}

	public static String zimbraGetReleaseString() throws HarnessException {
		ZimbraAdminAccount.GlobalAdmin().soapSend("<GetVersionInfoRequest xmlns='urn:zimbraAdmin'/>");
		String release = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:info", "release");
		if (release == null)
			throw new HarnessException("Unable to determine version from GetVersionInfoResponse "
					+ ZimbraAdminAccount.GlobalAdmin().soapLastResponse());
		return (release);
	}

	public static void main(String[] args) {
		System.setProperty("log4j.configuration", "file:///C:/log4j.properties");
		System.out.println(System.getProperty("log4j.configuration"));
		System.out.println(System.getProperty("user.dir"));
		String br = (String) ConfigProperties.getInstance().getConfigProp().getProperty("browser");
		System.out.println(br);
		logger.debug(br);
		ResourceBundle zmMsg = (ResourceBundle) ConfigProperties.getInstance().getConfigProp().getProperty("zmMsg");
		System.out.println(zmMsg.getLocale());
		logger.debug(zmMsg.getLocale());
	}
}