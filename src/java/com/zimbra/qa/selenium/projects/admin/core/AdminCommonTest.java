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
package com.zimbra.qa.selenium.projects.admin.core;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.staf.StafServicePROCESS;
import com.zimbra.qa.selenium.projects.admin.ui.AppAdminConsole;

public class AdminCommonTest {

	protected static Logger logger = LogManager.getLogger(AdminCommonTest.class);
	protected final ZimbraAdminAccount gAdmin = ZimbraAdminAccount.AdminConsoleAdmin();
	protected AppAdminConsole app = null;

	protected AbsTab startingPage = null;
	protected ZimbraAdminAccount startingAccount = null;

	private WebDriver webDriver = ClientSessionFactory.session().webDriver();
	WebElement we = null;

	String sJavaScriptErrorsHtmlFileName = "Javascript-errors-report.html";

	protected AdminCommonTest() {
		logger.info("New " + AdminCommonTest.class.getCanonicalName());

		app = new AppAdminConsole();

		startingPage = app.zPageMain;
		startingAccount = gAdmin;
	}

	@BeforeSuite(groups = { "always" })
	public void commonTestBeforeSuite() throws HarnessException {

		logger.info("BeforeSuite: start");

		// For coverage ?mode=mjsf&gzip=false
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			StafServicePROCESS staf = new StafServicePROCESS();
			try {
				staf.execute("zmprov mcf +zimbraHttpThrottleSafeIPs " + InetAddress.getLocalHost().getHostAddress());
				staf.execute("zmmailboxdctl restart");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		try {
			ConfigProperties.setAppType(ConfigProperties.AppType.ADMIN);
			Capabilities cp = ((RemoteWebDriver) webDriver).getCapabilities();
			if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())
					|| cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())
					|| cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())) {
				webDriver.manage().window().setPosition(new Point(0, 0));
				webDriver.manage().window()
						.setSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
								(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
				webDriver.navigate().to(ConfigProperties.getBaseURL());
			}

		} catch (WebDriverException e) {
			logger.error("Unable to open admin app.  Is a valid cert installed?", e);
			throw e;
		}

		// Delete javascript error file
		if (fGetJavaScriptErrorsHtmlFile().exists()) {
			fGetJavaScriptErrorsHtmlFile().delete();
		}

		logger.info("BeforeSuite: finish");
	}

	@BeforeClass(groups = { "always" })
	public void commonTestBeforeClass() throws HarnessException {
		logger.info("BeforeClass: start");
		logger.info("BeforeClass: finish");
	}

	@BeforeMethod(groups = { "always" })
	public void commonTestBeforeMethod(Method method, ITestContext testContext) throws HarnessException {
		logger.info("BeforeMethod: start");

		// Get the test name & description
		for (ITestNGMethod testngMethod : testContext.getAllTestMethods()) {
			String methodClass = testngMethod.getRealClass().getSimpleName();
			if (methodClass.equals(method.getDeclaringClass().getSimpleName())
					&& testngMethod.getMethodName().equals(method.getName())) {
				synchronized (AdminCommonTest.class) {
					logger.info("---------BeforeMethod-----------------------");
					logger.info("Test       : " + methodClass + "." + testngMethod.getMethodName());
					logger.info("Description: " + testngMethod.getDescription());
					logger.info("----------------------------------------");
				}
				break;
			}
		}

		// Close all the dialogs left opened by the previous test
		app.zPageMain.zHandleDialogs();

		// If a startinAccount is defined, then make sure we are authenticated
		// as that user
		if (startingAccount != null) {
			logger.debug("commonTestBeforeMethod: startingAccount is defined");

			if (!startingAccount.equals(app.zGetActiveAccount())) {
				if (app.zPageMain.zIsActive()) {
					app.zPageMain.logout();
				}
				app.zPageLogin.login(startingAccount);
			}

			// For coverage ?mode=mjsf&gzip=false
			if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
					ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
				for (int i = 0; i <= 10; i++) {
					if (ClientSessionFactory.session().webDriver()
							.findElement(By.cssSelector("css=div[id='ztih__AppAdmin__Home_textCell']")) != null) {
						app.zPageLogin.sRefresh();
						SleepUtil.sleepVeryVeryLong();
						SleepUtil.sleepVeryVeryLong();
						if (ClientSessionFactory.session().webDriver()
								.findElement(By.cssSelector("css=div[id='ztih__AppAdmin__Home_textCell']")) != null) {
							break;
						}
					}
				}
			}

			// Confirm
			if (!startingAccount.equals(app.zGetActiveAccount())) {
				throw new HarnessException("Unable to authenticate as " + startingAccount.EmailAddress);
			}
		}

		// If a startingPage is defined, then make sure we are on that page
		if (startingPage != null) {
			logger.debug("commonTestBeforeMethod: startingPage is defined");

			// If the starting page is not active, navigate to it
			if (!startingPage.zIsActive()) {
				startingPage.zNavigateTo();
			}

			// Confirm that the page is active
			if (!startingPage.zIsActive()) {
				throw new HarnessException("Unable to navigate to " + startingPage.myPageName());
			}
		}

		logger.info("BeforeMethod: finish");
	}

	public File fGetJavaScriptErrorsHtmlFile() throws HarnessException {
		String sJavaScriptErrorsFolderPath = ExecuteHarnessMain.testoutputfoldername
				+ "\\debug\\projects\\javascript-errors";
		String sJavaScriptErrorsHtmlFilePath = sJavaScriptErrorsFolderPath + "\\" + sJavaScriptErrorsHtmlFileName;
		File fJavaScriptErrorsHtmlFile = new File(sJavaScriptErrorsHtmlFilePath);
		return fJavaScriptErrorsHtmlFile;
	}

	public Path pGetJavaScriptErrorsHtmlFilePath() throws HarnessException {
		String sJavaScriptErrorsFolderPath = ExecuteHarnessMain.testoutputfoldername
				+ "\\debug\\projects\\javascript-errors";
		Path pJavaScriptErrorsHtmlFile = Paths.get(sJavaScriptErrorsFolderPath, sJavaScriptErrorsHtmlFileName);
		return pJavaScriptErrorsHtmlFile;
	}

	@AfterSuite(groups = { "always" })
	public void commonTestAfterSuite() throws HarnessException, IOException {
		logger.info("AfterSuite: start");

		// Javascript errors html file
		if (fGetJavaScriptErrorsHtmlFile().exists()) {
			List<String> lines;
			lines = Arrays.asList("</table>", "</body>",
					"<br/><h2 style='font-family:calibri; font-size:15px;'>** Selenium testcase error screenshot path may or may not be exists, it actually depends on the nature of the javascript error.</h2>",
					"</html>");
			Files.write(pGetJavaScriptErrorsHtmlFilePath(), lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		}

		webDriver.quit();
		logger.info("AfterSuite: finished by closing selenium session");

	}

	@AfterClass(groups = { "always" })
	public void commonTestAfterClass() throws HarnessException {
		logger.info("AfterClass: start");
		logger.info("AfterClass: finish");
	}

	@AfterMethod(groups = { "always" })
	public void commonTestAfterMethod(Method method, ITestResult testResult) throws HarnessException, IOException {
		logger.info("AfterMethod: start");

		// **************** Capture JavaScript Errors ****************
		logger.info("AfterMethod: Capture javascript errors");

		// Logs, Javascript error folder
		List<String> lines;
		Logs webDriverLog = webDriver.manage().logs();
		LogEntries[] logEntries = { webDriverLog.get(LogType.BROWSER) };

		for (int i = 0; i <= logEntries.length - 1; i++) {

			// Get hostname
			String hostname = null;
			try {
				InetAddress addr;
				addr = InetAddress.getLocalHost();
				hostname = addr.getHostName();
			} catch (UnknownHostException ex) {
				logger.info("Hostname can not be resolved");
			}

			// Configuration parameters
			String application = WordUtils.capitalize(method.getDeclaringClass().toString().split("\\.")[7]);
			String seleniumTestcase = method.getName().toString();
			String testOutputFolderName = ExecuteHarnessMain.testoutputfoldername;

			// Javascript error html file configuration
			String sJavaScriptErrorsFolderPath = testOutputFolderName + "\\debug\\projects\\javascript-errors";
			String sJavaScriptErrorsHtmlFile = sJavaScriptErrorsFolderPath + "\\" + sJavaScriptErrorsHtmlFileName;
			Path pJavaScriptErrorsHtmlFilePath = Paths.get(sJavaScriptErrorsFolderPath, sJavaScriptErrorsHtmlFileName);
			File fJavaScriptErrorsHtmlFile = new File(sJavaScriptErrorsHtmlFile);

			// Create javascript-errors folder
			File fJavaScriptErrorsFolder = new File(sJavaScriptErrorsFolderPath);
			if (!fJavaScriptErrorsFolder.exists())
				fJavaScriptErrorsFolder.mkdirs();

			// Screenshot
			String screenShotFilePath;
			if (testOutputFolderName.contains(ConfigProperties.getStringProperty("testOutputDirectory"))) {
				screenShotFilePath = "file:///"
						+ testOutputFolderName + "/debug" + method.getDeclaringClass().toString()
								.replace("class com.zimbra.qa.selenium", "").replace(".", "/")
						+ "/" + seleniumTestcase + "ss1.png";
			} else {
				int appPosition = testOutputFolderName.indexOf(ConfigProperties.getAppType().toString());
				screenShotFilePath = ConfigProperties.getStringProperty("webPortal") + "/portal/machines/" + hostname
						+ "/selenium/" + ConfigProperties.getAppType().toString().toLowerCase() + "/results/"
						+ testOutputFolderName.substring(appPosition) + "/debug" + method.getDeclaringClass().toString()
								.replace("class com.zimbra.qa.selenium", "").replace(".", "/")
						+ "/" + seleniumTestcase + "ss1.png";
			}
			screenShotFilePath = screenShotFilePath.replace("\\", "/");

			// Bug summary
			logger.info("AfterMethod: Get bug summary from bug tracking tool");
			String bugNos = null, commaSeparatedBugSummary = "", commaSeparatedBugStatus = "";
			try {
				bugNos = method.getAnnotation(Bugs.class).ids();
			} catch (NullPointerException e) {
				logger.info("Bugs are not associated for " + method.getName() + " test");
			} finally {
				if (bugNos != null) {
					logger.info("Associated bugs for " + method.getName() + " test: " + bugNos);
				}
			}
			if (bugNos != null && bugNos.contains(",")) {
				bugNos = bugNos.replaceAll(" ", "");
				String[] bugNumbers = bugNos.split(",");
				for (String bugNo : bugNumbers) {
					URL url = new URL(
							ConfigProperties.getStringProperty("bugTrackingTool") + "/show_bug.cgi?id=" + bugNo);
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
						for (String line; (line = reader.readLine()) != null;) {
							if (line.contains("bz_status_")) {
								commaSeparatedBugStatus = line.substring(line.indexOf("bz_bug bz_status_") + 17,
										line.indexOf(" bz_product_"));
								commaSeparatedBugSummary += "<a target='_blank' href='"
										+ ConfigProperties.getStringProperty("bugTrackingTool") + "/show_bug.cgi?id="
										+ bugNo + "'>" + "Bug " + bugNo + "</a> (" + commaSeparatedBugStatus + "), ";
								break;
							}
						}
					}
				}
				commaSeparatedBugSummary = commaSeparatedBugSummary.substring(0, commaSeparatedBugSummary.length() - 2);

			} else if (bugNos != null && !bugNos.isEmpty()) {
				URL url = new URL(ConfigProperties.getStringProperty("bugTrackingTool") + "/show_bug.cgi?id=" + bugNos);
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
					for (String line; (line = reader.readLine()) != null;) {
						if (line.contains("bz_status_")) {
							commaSeparatedBugStatus = line.substring(line.indexOf("bz_bug bz_status_") + 17,
									line.indexOf(" bz_product_"));
							commaSeparatedBugSummary = "<a target='_blank' href='"
									+ ConfigProperties.getStringProperty("bugTrackingTool") + "/show_bug.cgi?id="
									+ bugNos + "'>" + "Bug " + bugNos + "</a> (" + commaSeparatedBugStatus + ")";
							break;
						}
					}
				}

			} else {
				commaSeparatedBugSummary = "<a target='_blank' style='color:brown;' href='"
						+ ConfigProperties.getStringProperty("bugTrackingTool") + "/enter_bug.cgi?product=ZCS'>"
						+ "File a bug</a>";
			}

			if (fJavaScriptErrorsHtmlFile.createNewFile()) {
				logger.info("Javascript errors file is created");

				// Javascript errors html file
				lines = Arrays.asList(
						"<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>",
						"<html>", "<head>", "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>",
						"<link rel='icon' href='" + ConfigProperties.getStringProperty("webPortal")
								+ "/portal/web/wp-content/themes/iconic-one/images/favicon.ico' type='image/x-icon'/>",
						"<title>JavaScript Error Report</title>", "</head>", "<body>",
						"<h2 style='font-family:calibri; font-size:26px;'>Admin JavaScript Errors Report Generated by Selenium</h2>",
						"<table style='font-family:calibri; font-size:15px;' border='1'>",
						"<tr><th>Application</th><th>Selenium testcase</th><th>Javascript error</th><th>**Screenshot path</th><th>Bug Summary</th></tr>");
				Files.write(pJavaScriptErrorsHtmlFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
			} else {
				logger.info("Javascript errors file already exists");
			}

			for (LogEntry entry : logEntries[i]) {

				// Parse javascript error
				String javaScriptError = new Date(entry.getTimestamp()) + " " + entry.getLevel() + " "
						+ entry.getMessage();
				String seleniumTestcasePath = method.getDeclaringClass().toString().replaceFirst("class ", "") + "."
						+ method.getName();
				logger.info("JavaScript error: " + javaScriptError);

				// Javascript error
				lines = Arrays.asList("<tr><td style='text-align:center'>" + application + "</td><td>"
						+ seleniumTestcasePath + "</td><td style='color:brown;'>" + javaScriptError
						+ "</td><td><a target='_blank' href='" + screenShotFilePath + "'>" + "Navigate to "
						+ method.getName() + " Screenshot" + "</a></td><td style='text-align:center'>"
						+ commaSeparatedBugSummary + "</td></tr>");
				Files.write(pJavaScriptErrorsHtmlFilePath, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
			}
		}

		logger.info("AfterMethod: finish");
	}

	public void zUpload(String filePath) throws HarnessException {

		SleepUtil.sleepLong();

		StringSelection ss = new StringSelection(filePath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			SleepUtil.sleepSmall();
			robot.keyPress(KeyEvent.VK_END);
			robot.keyRelease(KeyEvent.VK_END);
			SleepUtil.sleepMedium();

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		SleepUtil.sleepVeryVeryLong();
	}

	public void zDownload() throws HarnessException {

		SleepUtil.sleepLong();

		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		SleepUtil.sleepVeryLong();
	}
}
