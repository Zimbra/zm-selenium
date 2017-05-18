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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.common.util.tar.TarEntry;
import com.zimbra.common.util.tar.TarInputStream;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import java.util.zip.GZIPInputStream;


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

			URL driverZipURL = null;
			File driverBinary = null;
			String driverFile = null, driverZipFile = null, driverVersion = null, driverDirectory = null;

			LoggingPreferences logs = new LoggingPreferences();
	        logs.enable(LogType.BROWSER, Level.SEVERE);
	        logs.enable(LogType.DRIVER, Level.SEVERE);
	        logs.enable(LogType.PERFORMANCE, Level.SEVERE);

	        if (ConfigProperties.getCalculatedBrowser().contains("firefox")) {

	        	driverVersion = ConfigProperties.getStringProperty("geckoDriverURL").split("/")[7];
	        	driverDirectory = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/" + ConfigProperties.getCalculatedBrowser() + "/" + driverVersion;

				switch (OperatingSystem.getOSType()) {

					// Note: Running Selenium tests on firefox are not fully supported (see https://bugzilla.mozilla.org/show_bug.cgi?id=1303234).
					case WINDOWS: default:
						driverZipFile = "geckodriver-" + driverVersion + "-win64.zip";
						driverFile = driverDirectory + "/geckodriver.exe";
						driverBinary = new File(driverFile);
						try {
							driverZipURL = new URL(ConfigProperties.getStringProperty("geckoDriverURL") + "/" + driverZipFile);
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}
						break;

					case MAC:
						driverZipFile = "geckodriver-" + driverVersion + "-macos.tar.gz";
						driverFile = driverDirectory + "/geckodriver";
						driverBinary = new File(driverFile);
						try {
							driverZipURL = new URL(ConfigProperties.getStringProperty("geckoDriverURL") + "/" + driverZipFile);
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}
						break;

					case LINUX:
						driverZipFile = "geckodriver-" + driverVersion + "-linux64.tar.gz";
						driverFile = driverDirectory + "/geckodriver";
						driverBinary = new File(driverFile);
						try {
							driverZipURL = new URL(ConfigProperties.getStringProperty("geckoDriverURL") + "/" + driverZipFile);
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}
						break;
				}

				try {
					if (!driverBinary.exists() && !driverBinary.isFile()) {
						setupDir(driverDirectory);
						downloadFile(driverBinary, driverZipURL, driverDirectory, driverZipFile);
						if (driverZipFile.contains("tar")) {
							untarFile(driverDirectory + "/" + driverZipFile, driverDirectory);
						} else {
							unzipFile(driverBinary, driverDirectory + "/" + driverZipFile, driverDirectory);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("extensions.firebug.showFirstRunPage", false);

				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				System.setProperty("webdriver.gecko.driver", driverFile);
				capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
				webDriver = new FirefoxDriver(capabilities);
				webDriver.manage().window().maximize();

			} else {

				driverVersion = ConfigProperties.getStringProperty("chromeDriverURL").split("/")[3];
				driverDirectory = ConfigProperties.getBaseDirectory() + "/conf/" + OperatingSystem.getOSType().toString().toLowerCase() + "/" + ConfigProperties.getCalculatedBrowser() + "/" + driverVersion;

				switch (OperatingSystem.getOSType()) {

					case WINDOWS: default:
						driverZipFile = "chromedriver_win32.zip";
						driverFile = driverDirectory + "/chromedriver.exe";
						driverBinary = new File(driverFile);
						try {
							driverZipURL = new URL(ConfigProperties.getStringProperty("chromeDriverURL") + "/" + driverZipFile);
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}
						break;

					case MAC:
						driverZipFile = "chromedriver_mac64.zip";
						driverFile = driverDirectory + "/chromedriver";
						driverBinary = new File(driverFile);
						try {
							driverZipURL = new URL(ConfigProperties.getStringProperty("chromeDriverURL") + "/" + driverZipFile);
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}
						break;

					case LINUX:
						driverZipFile = "chromedriver_linux64.zip";
						driverFile = driverDirectory + "/chromedriver";
						driverBinary = new File(driverFile);
						try {
							driverZipURL = new URL(ConfigProperties.getStringProperty("chromeDriverURL") + "/" + driverZipFile);
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}
						break;
				}

				try {
					if (!driverBinary.exists() && !driverBinary.isFile()) {
						setupDir(driverDirectory);
						downloadFile(driverBinary, driverZipURL, driverDirectory, driverZipFile);
						if (driverZipFile.contains("tar")) {
							untarFile(driverDirectory + "/" + driverZipFile, driverDirectory);
						} else {
							unzipFile(driverBinary, driverDirectory + "/" + driverZipFile, driverDirectory);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				ChromeOptions options = new ChromeOptions();
				Map<String, Object> preferences = new Hashtable<String, Object>();

				preferences.put("plugins.plugins_disabled", new String[] { "Adobe Flash Player", "Chrome PDF Viewer" });
				preferences.put("credentials_enable_service", false);
				preferences.put("password_manager_enabled", false);
				options.setExperimentalOption("prefs", preferences);
				options.addArguments("disable-infobars");
				options.addArguments("start-maximized");

		        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		        System.setProperty("webdriver.chrome.driver", driverFile);
		        capabilities.setCapability("chrome.switches", Arrays.asList("--disable-extensions"));
		        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
		        webDriver = new ChromeDriver(capabilities);
			}
		}
		return webDriver;
	}

	public void setupDir(String driverDirectory) throws IOException {
		File driverOSDirectory = new File(driverDirectory);
        driverOSDirectory.mkdirs();
	}

	public void downloadFile(File driverBinary, URL driverZipURL, String driverDirectory, String driverZipFile) throws IOException {

		System.out.println("Downloading driver from " + driverZipURL + " ...");
		System.out.println("\t & putting driver at " + driverDirectory + "/" + driverZipFile + " ...");

		ReadableByteChannel rbc = null;
		try {
			rbc = Channels.newChannel(driverZipURL.openStream());
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(driverDirectory + "/" + driverZipFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void unzipFile(File driverBinary, String zipFilePath, String driverDirectory) throws IOException {

		int BUFFER_SIZE = 4096;
        File destDir = new File(driverDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();

        while (entry != null) {
            String filePath = driverDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
            	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                byte[] bytesIn = new byte[BUFFER_SIZE];
                int read = 0;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();

                Runtime runTimeProcess = Runtime.getRuntime();
        		if (!OperatingSystem.getOSType().toString().equals("WINDOWS")) {
        			runTimeProcess.exec("chmod +x " + filePath);
        		}
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

	private void untarFile(String driverTarFile, String driverDirectory) throws FileNotFoundException, IOException {

		TarInputStream tin = new TarInputStream (new GZIPInputStream (new FileInputStream(new File(driverTarFile))));
		TarEntry tarEntry = tin.getNextEntry();

		while (tarEntry != null) {
			File filePath = new File(driverDirectory.toString() + File.separatorChar + tarEntry.getName());

			if(tarEntry.isDirectory()){
				filePath.mkdir();
			} else {
				FileOutputStream fout = new FileOutputStream(filePath);
				tin.copyEntryContents(fout);
				fout.close();

				Runtime runTimeProcess = Runtime.getRuntime();
				if (!OperatingSystem.getOSType().toString().equals("WINDOWS")) {
					runTimeProcess.exec("chmod +x " + filePath);
				}
			}
			tarEntry = tin.getNextEntry();
		}
		tin.close();
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
