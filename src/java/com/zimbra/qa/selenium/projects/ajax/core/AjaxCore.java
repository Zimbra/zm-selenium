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
package com.zimbra.qa.selenium.projects.ajax.core;

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
import java.util.*;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.testng.*;
import org.testng.annotations.*;
import org.xml.sax.SAXException;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowDisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowFormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.tasks.FormTaskNew;
import com.zimbra.qa.selenium.staf.StafIntegration;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactDistributionListNew;
import com.zimbra.qa.selenium.projects.ajax.pages.contacts.FormContactGroupNew;

public class AjaxCore {

	protected AjaxPages app = null;
	protected AbsTab startingPage = null;

	protected Map<String, String> startingAccountPreferences = null;
	protected Map<String, String> startingUserPreferences = null;
	protected Map<String, String> startingUserZimletPreferences = null;

	public static boolean organizerTest = false;
	public static boolean allDayTest = false;

	WebElement we = null;
	protected WebDriver webDriver = ClientSessionFactory.session().webDriver();
	protected static Logger logger = LogManager.getLogger(AjaxCore.class);

	String sJavaScriptErrorsHtmlFileName = "Javascript-errors-report.html";

	protected AjaxCore() {
		logger.info("New " + AjaxCore.class.getCanonicalName());

		app = AjaxPages.getInstance();
		startingPage = app.zPageMain;
		startingAccountPreferences = new HashMap<String, String>();
		startingUserZimletPreferences = new HashMap<String, String>();
	}

	@BeforeSuite(groups = { "always" })
	public void coreBeforeSuite() throws HarnessException, IOException, InterruptedException, SAXException {

		logger.info("BeforeSuite: start");
		ZimbraAccount.ResetAccountZCS();

		try {

			ConfigProperties.setAppType(ConfigProperties.AppType.AJAX);

			// Dynamic wait for App to be ready
			int maxRetry = 5;
			int retry = 0;
			boolean appIsReady = false;
			while (retry < maxRetry && !appIsReady) {
				try {
					logger.info("Retry #" + retry);
					retry++;
					webDriver.navigate().to(ConfigProperties.getBaseURL());
					appIsReady = true;

				} catch (WebDriverException e) {
					if (retry == maxRetry) {
						logger.error("Unable to open ajax app. Is a valid certificate installed?", e);
						throw e;
					} else {
						logger.info("App is still not ready...", e);
						SleepUtil.sleep(6000);
						continue;
					}
				}
			}
			logger.info("App is ready!");

		} catch (WebDriverException e) {
			logger.error("Unable to open ajax app. Is a valid certificate installed?", e);

		} catch (Exception e) {
			logger.warn(e);
		}

		// Delete javascript error file
		if (fGetJavaScriptErrorsHtmlFile().exists()) {
			fGetJavaScriptErrorsHtmlFile().delete();
		}

		logger.info("BeforeSuite: finish");
	}

	@BeforeClass(groups = { "always" })
	public void coreBeforeClass() throws HarnessException, IOException {
		logger.info("BeforeClass: start");
		logger.info("Class name: " + this.getClass());

		ArrayList<String> availableZimlets= null;

		// Grant createDistList right to domain
		if (!ExecuteHarnessMain.isDLRightGranted
				&& this.getClass().getName().contains(ExecuteHarnessMain.SeleniumBasePackage + ".projects.ajax.tests.contacts.dl")) {
			StafIntegration.logInfo = "Grant createDistList right to domain using CLI utility";
			logger.info(StafIntegration.logInfo);
			CommandLineUtility.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"),
					"zmprov grr domain " + ConfigProperties.getStringProperty("testdomain") + " dom "
							+ ConfigProperties.getStringProperty("testdomain") + " createDistList");
			ExecuteHarnessMain.isDLRightGranted = true;
		}

		// Disable zimbraSmimeOCSPEnabled attribute for S/MIME	
		if (!ExecuteHarnessMain.isSmimeOcspDisabled
				&& this.getClass().getName().contains(ExecuteHarnessMain.SeleniumBasePackage + ".projects.ajax.tests.network.zimlets.smime")) {
			StafIntegration.logInfo = "Disable zimbraSmimeOCSPEnabled attribute for S/MIME using CLI utility";
			logger.info(StafIntegration.logInfo);
			CommandLineUtility.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"), "zmprov mcf zimbraSmimeOCSPEnabled FALSE");
			for (int i=0; i<ExecuteHarnessMain.storeServers.size(); i++) {
				CommandLineUtility.runCommandOnZimbraServer(ExecuteHarnessMain.storeServers.get(i), "zmprov ms '" + ExecuteHarnessMain.storeServers.get(i) + "' zimbraSmimeOCSPEnabled FALSE" );
			}
			ExecuteHarnessMain.isSmimeOcspDisabled = true;
		}

		// Configure Chat
		if (!ExecuteHarnessMain.isChatConfigured
				&& this.getClass().getName().contains(ExecuteHarnessMain.SeleniumBasePackage + ".projects.ajax.tests.zextras.chat")) {
			availableZimlets = CommandLineUtility.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"),
					"zmprov -l gc default zimbraZimletAvailableZimlets | grep zimbraZimletAvailableZimlets | cut -c 32-");
			if (!availableZimlets.contains("com_zextras_chat_open")) {
				logger.info("Enable zimbra chat zimlet on default COS");
				CommandLineUtility.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"),
						"zmprov mc default +zimbraZimletAvailableZimlets '+com_zextras_chat_open'");
				for (int i=0; i<ExecuteHarnessMain.storeServers.size(); i++) {
					CommandLineUtility.runCommandOnZimbraServer(ExecuteHarnessMain.storeServers.get(i), "zmprov fc -a all");
				}
			}
			ExecuteHarnessMain.isChatConfigured = true;
		}


		// Configure drive
		if (!ExecuteHarnessMain.isDriveConfigured
				&& this.getClass().getName().contains(ExecuteHarnessMain.SeleniumBasePackage + ".projects.ajax.tests.zextras.drive")) {
			availableZimlets = CommandLineUtility.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"),
					"zmprov -l gc default zimbraZimletAvailableZimlets | grep zimbraZimletAvailableZimlets | cut -c 32-");
			if (!availableZimlets.contains("com_zextras_drive_open")) {
				logger.info("Enable zimbra drive zimlet on default COS");
				CommandLineUtility.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"),
						"zmprov mc default +zimbraZimletAvailableZimlets '+com_zextras_drive_open'");
				for (int i=0; i<ExecuteHarnessMain.storeServers.size(); i++) {
					CommandLineUtility.runCommandOnZimbraServer(ExecuteHarnessMain.storeServers.get(i), "zmprov fc -a all");
				}
			}

			// Zimbra settings
			CommandLineUtility.runCommandOnZimbraServer(ConfigProperties.getStringProperty("server.host"),
					"zmprov md '" + ConfigProperties.getStringProperty("testdomain") + "' zimbraDriveOwnCloudURL '" + ConfigProperties.getStringProperty("driveServer") + "/nextcloud/index.php'");

			// Nextcloud settings
			try {
				app.zPageDrive.sOpen(ConfigProperties.getStringProperty("driveServer") + "/nextcloud/index.php/login");
				SleepUtil.sleepLong();
				app.zPageDrive.sType("css=input[id='user']", "admin@" + ConfigProperties.getStringProperty("driveServer").split("http://")[1]);
				app.zPageDrive.sType("css=input[id='password']", "zimbra");
				app.zPageDrive.sClick("css=input[id='submit']");
				SleepUtil.sleepMedium();
				app.zPageDrive.sOpen(ConfigProperties.getStringProperty("driveServer") + "/nextcloud/index.php/settings/admin/zimbradrive");
				app.zPageDrive.sType("css=input[id='zimbra_url']", ExecuteHarnessMain.storeServers.get(0));
				app.zPageDrive.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
				SleepUtil.sleepMedium();
			} finally {
				app.zPageMain.sOpen(ConfigProperties.getBaseURL());
			}

			ExecuteHarnessMain.isDriveConfigured = true;
		}
		logger.info("BeforeClass: finish");
	}

	@BeforeMethod(groups = { "always" })
	public void coreBeforeMethod(Method method, ITestContext testContext) throws HarnessException {
		logger.info("BeforeMethod: start");

		// Get the test name & description
		for (ITestNGMethod testngMethod : testContext.getAllTestMethods()) {
			String methodClass = testngMethod.getRealClass().getSimpleName();
			if (methodClass.equals(method.getDeclaringClass().getSimpleName())
					&& testngMethod.getMethodName().equals(method.getName())) {
				synchronized (AjaxCore.class) {
					logger.info("---------BeforeMethod-----------------------");
					logger.info("Test       : " + methodClass + "." + testngMethod.getMethodName());
					logger.info("Description: " + testngMethod.getDescription());
					logger.info("----------------------------------------");
				}
				break;
			}
		}

		// Set the preferences accordingly
		ZimbraAccount.AccountZCS().modifyAccountPreferences(startingAccountPreferences);
		ZimbraAccount.AccountZCS().modifyUserZimletPreferences(startingUserZimletPreferences);

		// If AccountZCS is not currently logged in, then login now
		if (!ZimbraAccount.AccountZCS().equals(app.zGetActiveAccount())) {
			logger.info("BeforeMethod: AccountZCS is not currently logged in");

			if (app.zPageMain.zIsActive())
				try {
					app.zPageLogin.sOpen(ConfigProperties.getLogoutURL());
					app.zPageLogin.sOpen(ConfigProperties.getBaseURL());

				} catch (Exception ex) {
					if (!app.zPageLogin.zIsActive()) {
						logger.error("Login page is not active ", ex);

						app.zPageLogin.sOpen(ConfigProperties.getLogoutURL());
						app.zPageLogin.sOpen(ConfigProperties.getBaseURL());
					}
				}
		}

		// If a startingPage is defined, then make sure we are on that page
		if (startingPage != null) {
			logger.info("BeforeMethod: startingPage is defined -> " + startingPage.myPageName());

			// If the starting page is not active, navigate to it
			if (!startingPage.zIsActive()) {
				startingPage.zNavigateTo();
			}

			// Confirm that the page is active
			if (!startingPage.zIsActive()) {
				throw new HarnessException("Unable to navigate to " + startingPage.myPageName());
			}

			logger.info("BeforeMethod: startingPage navigation done -> " + startingPage.myPageName());
		}

		// Handle open dialogs and tabs
		logger.info("BeforeMethod: Handle open dialogs and tabs");
		app.zPageMain.zHandleDialogs(startingPage);
		app.zPageMain.zHandleComposeTabs();

		logger.info("BeforeMethod: finish");
	}

	public File fGetJavaScriptErrorsHtmlFile() throws HarnessException {
		String sJavaScriptErrorsFolderPath = ExecuteHarnessMain.testoutputfoldername
				+ "/debug/projects/javascript-errors";
		String sJavaScriptErrorsHtmlFilePath = sJavaScriptErrorsFolderPath + "/" + sJavaScriptErrorsHtmlFileName;
		File fJavaScriptErrorsHtmlFile = new File(sJavaScriptErrorsHtmlFilePath);
		return fJavaScriptErrorsHtmlFile;
	}

	public Path pGetJavaScriptErrorsHtmlFilePath() throws HarnessException {
		String sJavaScriptErrorsFolderPath = ExecuteHarnessMain.testoutputfoldername
				+ "/debug/projects/javascript-errors";
		Path pJavaScriptErrorsHtmlFile = Paths.get(sJavaScriptErrorsFolderPath, sJavaScriptErrorsHtmlFileName);
		return pJavaScriptErrorsHtmlFile;
	}

	@AfterSuite(groups = { "always" })
	public void coreAfterSuite() throws HarnessException, IOException {
		logger.info("AfterSuite: start");

		if (ConfigProperties.getStringProperty("javascript.errors.report").equals("true")) {
			if (fGetJavaScriptErrorsHtmlFile().exists()) {
				List<String> lines;
				lines = Arrays.asList("</table>", "</body>",
						"<br/><h2 style='font-family:calibri; font-size:15px;'>** Selenium testcase error screenshot path may or may not be exists, it actually depends on the nature of the javascript error.</h2>",
						"</html>");
				Files.write(pGetJavaScriptErrorsHtmlFilePath(), lines, Charset.forName("UTF-8"),
						StandardOpenOption.APPEND);
			}
		}

		webDriver.quit();
		logger.info("AfterSuite: finished by closing selenium session");
	}

	@AfterClass(groups = { "always" })
	public void coreAfterClass() throws HarnessException {
		logger.info("AfterClass: start");

		ZimbraAccount currentAccount = app.zGetActiveAccount();
		if (currentAccount != null && currentAccount.accountIsDirty && currentAccount == ZimbraAccount.AccountZCS()) {
			ZimbraAccount.ResetAccountZCS();
		}

		logger.info("AfterClass: finish");
	}

	@AfterMethod(groups = { "always" })
	public void coreAfterMethod(Method method, ITestResult testResult) throws HarnessException, IOException {
		logger.info("AfterMethod: start");

		if (ZimbraURI.needsReload()) {
			logger.error("The URL does not match the base URL. Reload app.");
			app.zPageLogin.sOpen(ConfigProperties.getLogoutURL());
			app.zPageLogin.sOpen(ConfigProperties.getBaseURL());
		}

		if ((!app.zPageMain.zIsActive()) && (!app.zPageLogin.zIsActive())) {
			logger.error("Neither login page nor main page were active. Reload app.", new Exception());
			app.zPageLogin.sOpen(ConfigProperties.getLogoutURL());
			app.zPageLogin.sOpen(ConfigProperties.getBaseURL());
		}

		if (ConfigProperties.getStringProperty("javascript.errors.report").equals("true")) {

			// **************** Capture JavaScript Errors ****************
			logger.info("AfterMethod: Capture javascript errors");

			// Logs, Java script error folder
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
				String application = WordUtils.capitalize(method.getDeclaringClass().toString().split("/.")[7]);
				String seleniumTestcase = method.getName().toString();
				String testOutputFolderName = ExecuteHarnessMain.testoutputfoldername;

				// Java script error html file configuration
				String sJavaScriptErrorsFolderPath = testOutputFolderName + "/debug/projects/javascript-errors";
				String sJavaScriptErrorsHtmlFile = sJavaScriptErrorsFolderPath + "/" + sJavaScriptErrorsHtmlFileName;
				Path pJavaScriptErrorsHtmlFilePath = Paths.get(sJavaScriptErrorsFolderPath,
						sJavaScriptErrorsHtmlFileName);
				File fJavaScriptErrorsHtmlFile = new File(sJavaScriptErrorsHtmlFile);

				// Create javascript-errors folder
				File fJavaScriptErrorsFolder = new File(sJavaScriptErrorsFolderPath);
				if (!fJavaScriptErrorsFolder.exists())
					fJavaScriptErrorsFolder.mkdirs();

				// Screenshot
				String screenShotFilePath;
				if (testOutputFolderName.contains(ConfigProperties.getStringProperty("testOutputDirectory"))) {
					screenShotFilePath = "file:///" + testOutputFolderName
							+ "/debug" + method.getDeclaringClass().toString()
									.replace("class com.zimbra.qa.selenium", "").replace(".", "/")
							+ "/" + seleniumTestcase + "ss1.png";
				} else {
					int appPosition = testOutputFolderName.indexOf(ConfigProperties.getAppType().toString());
					screenShotFilePath = ConfigProperties.getStringProperty("webPortal") + "/portal/machines/"
							+ hostname + "/selenium/" + ConfigProperties.getAppType().toString().toLowerCase()
							+ "/results/" + testOutputFolderName.substring(appPosition)
							+ "/debug" + method.getDeclaringClass().toString()
									.replace("class com.zimbra.qa.selenium", "").replace(".", "/")
							+ "/" + seleniumTestcase + "ss1.png";
				}

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
						try (BufferedReader reader = new BufferedReader(
								new InputStreamReader(url.openStream(), "UTF-8"))) {
							for (String line; (line = reader.readLine()) != null;) {
								if (line.contains("bz_status_")) {
									commaSeparatedBugStatus = line.substring(line.indexOf("bz_bug bz_status_") + 17,
											line.indexOf(" bz_product_"));
									commaSeparatedBugSummary += "<a target='_blank' href='"
											+ ConfigProperties.getStringProperty("bugTrackingTool")
											+ "/show_bug.cgi?id=" + bugNo + "'>" + "Bug " + bugNo + "</a> ("
											+ commaSeparatedBugStatus + "), ";
									break;
								}
							}
						}
					}
					commaSeparatedBugSummary = commaSeparatedBugSummary.substring(0,
							commaSeparatedBugSummary.length() - 2);

				} else if (bugNos != null && !bugNos.isEmpty()) {
					URL url = new URL(
							ConfigProperties.getStringProperty("bugTrackingTool") + "/show_bug.cgi?id=" + bugNos);
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
					logger.info("Java script errors file is created");

					// Java script errors html file
					lines = Arrays.asList(
							"<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>",
							"<html>", "<head>", "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>",
							"<link rel='icon' href='" + ConfigProperties.getStringProperty("webPortal")
									+ "/portal/web/wp-content/themes/iconic-one/images/favicon.ico' type='image/x-icon'/>",
							"<title>JavaScript Error Report</title>", "</head>", "<body>",
							"<h2 style='font-family:calibri; font-size:26px;'>Ajax JavaScript Errors Report Generated by Selenium</h2>",
							"<table style='font-family:calibri; font-size:15px;' border='1'>",
							"<tr><th>Application</th><th>Selenium testcase</th><th>Javascript error</th><th>**Screenshot path</th><th>Bug Summary</th></tr>");
					Files.write(pJavaScriptErrorsHtmlFilePath, lines, Charset.forName("UTF-8"),
							StandardOpenOption.APPEND);
				} else {
					logger.info("Java script errors file already exists");
				}

				for (LogEntry entry : logEntries[i]) {

					// Parse javascript error
					String javaScriptError = new Date(entry.getTimestamp()) + " " + entry.getLevel() + " "
							+ entry.getMessage();
					String seleniumTestcasePath = method.getDeclaringClass().toString().replaceFirst("class ", "") + "."
							+ method.getName();
					logger.info("JavaScript error: " + javaScriptError);

					// Java script error
					lines = Arrays.asList("<tr><td style='text-align:center'>" + application + "</td><td>"
							+ seleniumTestcasePath + "</td><td style='color:brown;'>" + javaScriptError
							+ "</td><td style='text-align:center'><a target='_blank' href='" + screenShotFilePath + "'>"
							+ "Screenshot" + "</a></td><td style='text-align:center'>" + commaSeparatedBugSummary
							+ "</td></tr>");
					Files.write(pJavaScriptErrorsHtmlFilePath, lines, Charset.forName("UTF-8"),
							StandardOpenOption.APPEND);
				}
			}
		}

		// Get test PASSED/FAILED status
		if (testResult.getStatus() == ITestResult.FAILURE){
			ZimbraAccount.ResetAccountZCS();
		}

		logger.info("AfterMethod: finish");
	}

	@AfterMethod(groups = { "performance" })
	public void performanceTestAfterMethod() {
		ZimbraAccount.ResetAccountZCS();
	}

	@DataProvider(name = "DataProviderSupportedCharsets")
	public Object[][] DataProviderSupportedCharsets() throws HarnessException {
		return (ZimbraCharsets.getInstance().getSampleTable());
	}

	public void ModifyAccountPreferences(String string) throws HarnessException {
		StringBuilder settings = new StringBuilder();
		for (Map.Entry<String, String> entry : startingAccountPreferences.entrySet()) {
			settings.append(String.format("<a n='%s'>%s</a>", entry.getKey(), entry.getValue()));
		}
		ZimbraAdminAccount.GlobalAdmin().soapSend("<ModifyAccountRequest xmlns='urn:zimbraAdmin'>" + "<id>" + string
				+ "</id>" + settings.toString() + "</ModifyAccountRequest>");
	}

	public void zUpload(String filePath) throws HarnessException {
		zUploadFile(filePath, "file");
	}

	public void zUpload(String filePath, SeparateWindowFormMailNew window) throws HarnessException {
		zUploadFile(filePath, "file");
	}

	public void zUpload(String filePath, SeparateWindowDisplayMail window) throws HarnessException {
		zUploadFile(filePath, "file");
	}

	public void zUploadInlineImageAttachment(String filePath) throws HarnessException {
		zUploadFile(filePath, "inline");
	}

	public void zUploadFile(String filePath, String attachmentType) throws HarnessException {
		Boolean isFileAttached = false;
		String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);

		try {

			for (int i = 1; i <= 3; i++) {

				// Put path to your image in a clipboard
				SleepUtil.sleepMedium();
				StringSelection ss = new StringSelection(filePath);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
				SleepUtil.sleepMedium();

				// AutoIt script to set focus to file name field in browse window dialog
				try {
					Runtime.getRuntime().exec(ConfigProperties.getBaseDirectory() + "\\conf\\windows\\autoit\\SetFocusToFileNameField.exe");
				} catch (IOException e) {
					logger.info("Couldn't execute or set focus to file name field using AutoIt script: " + e.toString());
				}
				SleepUtil.sleepSmall();

				// Imitate mouse events like ENTER, CTRL+C, CTRL+V
				Robot robot;
				try {
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					SleepUtil.sleepMedium();

				} catch (AWTException e) {
					e.printStackTrace();
				}

				// AutoIt script to click to open button to attach file
				try {
					Runtime.getRuntime().exec(ConfigProperties.getBaseDirectory() + "\\conf\\windows\\autoit\\ClickToOpenButton.exe");
					SleepUtil.sleepLongMedium();
				} catch (IOException e) {
					logger.info("Couldn't click to open button to attach file using AutoIt script: " + e.toString());
				}

				// Check for S/MIME certificate password dialog
				if (!app.zPageMain.sIsVisible("id=CertificatePasswordDialog") && attachmentType.equals("file")) {

					// File locator
					String fileLocator = null;
					Boolean isMailApp = false, isContactsApp = false, isCalendarApp = false, isTasksApp = false, isBriefcaseApp = false, isPreferencesApp = false, isSmimeZimlet = false, isZimbraDrive = false;

					isMailApp = app.zPageMain.zIsVisiblePerPosition("div[id^='ztb__COMPOSE']", 0, 0);
					if (isMailApp != true) {
						isContactsApp = app.zPageMain.zIsVisiblePerPosition("span[id$='certificateBubble']", 0, 0);
						isCalendarApp = app.zPageMain.zIsVisiblePerPosition("div[id^='ztb__APPT']", 0, 0);
						isTasksApp = app.zPageMain.zIsVisiblePerPosition("div[id^='ztb__TKE']", 0, 0);
						isBriefcaseApp = app.zPageMain.zIsVisiblePerPosition("div[class='ZmUploadDialog']", 0, 0);
						isPreferencesApp = app.zPageMain.zIsVisiblePerPosition("div[id='ztb__PREF']", 0, 0);
						isSmimeZimlet = app.zPageMain.zIsVisiblePerPosition("td[class='ZmSecureMailCertificateRow']", 0, 0);
						isZimbraDrive = app.zPageMain.zIsVisiblePerPosition("div[class='ZmUploadDialog'] td[class='DwtDialogTitle']:contains('Upload Files - Drive')", 0, 0);
					}

					// Get attached file locator
					if (isMailApp == true) {
						fileLocator = "css=a[id^='COMPOSE']:contains(" + fileName + ")";
					} else if (isContactsApp == true) {
						fileLocator = "css=span[id$='certificateBubble']:contains(" + fileName + ")";
					} else if (isCalendarApp == true || isTasksApp == true) {
						we = webDriver.findElement(By.name("__calAttUpload__"));
					} else if (isBriefcaseApp == true) {
						we = webDriver.findElement(By.name("uploadFile"));
					} else if (isZimbraDrive == true) {
						we = webDriver.findElement(By.name("uploadFile"));
					} else if (isSmimeZimlet == true) {
						fileLocator = "css=td[class='ZmSecureMailCertificateRow'] td[id$='_title']:contains(" + ConfigProperties.getStringProperty("testdomain") + ")";
					} else if (isPreferencesApp == true) {
						we = webDriver.findElement(By.name("file"));
					}

					if (isMailApp == true || isContactsApp == true || isSmimeZimlet == true) {
						isFileAttached = app.zPageMain.zIsVisiblePerPosition(fileLocator, 0, 0);
					} else {
						isFileAttached = we.getAttribute("value").contains(fileName);
					}

					if (isFileAttached == true) {
						logger.info("File " + fileName + " attached fine");
						break;
					} else {
						logger.info("Couldn't attach " + fileName + " file in #" + i + " attempt");
						SleepUtil.sleepSmall();
					}

				} else if (attachmentType.equals("inline")) {
					try {
						WebElement we = null;
						WebDriver webDriver = ClientSessionFactory.session().webDriver();

						webDriver.switchTo().defaultContent();
						webDriver.switchTo().frame("ZmHtmlEditor1_body_ifr");
						we = webDriver.findElement(By.cssSelector("body#tinymce img"));

						if (we.getAttribute("src").contains("/service/home/~/?auth=co")
								&& we.getAttribute("data-mce-src").startsWith("cid:")
								&& we.getAttribute("data-mce-src").endsWith("@zimbra")
								&& we.getAttribute("dfsrc").startsWith("cid:") && we.getAttribute("dfsrc").endsWith("@zimbra")) {
							isFileAttached = true;
						}

						if (isFileAttached == true) {
							logger.info("File " + fileName + " inline attached fine");
							break;
						} else {
							logger.info("Couldn't inline attach " + fileName + " file in #" + i + " attempt");
							SleepUtil.sleepSmall();
						}

					} finally {
						webDriver.switchTo().defaultContent();
					}

				} else {
					break;
				}
			}

		} finally {

			// AutoIt script to close the file explorer window (if any)
			try {
				Runtime.getRuntime().exec(ConfigProperties.getBaseDirectory() + "\\conf\\windows\\autoit\\CloseFileExplorerWindow.exe");
				SleepUtil.sleepSmall();
			} catch (IOException e) {
				logger.info("Couldn't close the file explorer window using AutoIt script: " + e.toString());
			}
		}
	}

	public AbsPage zToolbarPressPulldown(Button button, Button option) throws HarnessException {

		logger.info("Click to zToolbarPressPulldown(" + option + ")");

		if (option == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (option == Button.O_NEW_MESSAGE) {
			locator = "css=td[id='zb__NEW_MENU_NEW_MESSAGE_title']";
			page = new FormMailNew(this.app);

		} else if (option == Button.O_NEW_CONTACT) {
			locator = "css=td[id='zb__NEW_MENU_NEW_CONTACT_title']";
			page = new FormContactNew(this.app);

		} else if (option == Button.O_NEW_CONTACTGROUP) {
			locator = "css=td[id='zb__NEW_MENU_NEW_GROUP_title']";
			page = new FormContactGroupNew(this.app);

		} else if (option == Button.O_NEW_DISTRIBUTION_LIST) {
			locator = "css=td[id='zb__NEW_MENU_NEW_DISTRIBUTION_LIST_title']";
			page = new FormContactDistributionListNew(this.app);

		} else if (option == Button.O_NEW_APPOINTMENT) {
			locator = "css=td[id='zb__NEW_MENU_NEW_APPT_title']";
			page = new FormApptNew(this.app);

		} else if (option == Button.O_NEW_TASK) {
			locator = "css=td[id='zb__NEW_MENU_NEW_TASK_title']";
			page = new FormTaskNew(this.app);

		} else if (option == Button.O_NEW_DOCUMENT) {
			locator = "css=td[id='zb__NEW_MENU_NEW_DOC_title']";
			page = new FormContactNew(this.app);

		} else if (option == Button.O_NEW_FOLDER) {
			locator = "css=td[id='zb__NEW_MENU_NEW_FOLDER_title']";
			page = new FormContactNew(this.app);

		} else if (option == Button.O_NEW_TAG) {
			locator = "css=td[id='zb__NEW_MENU_NEW_TAG_title']";
			page = new FormContactNew(this.app);

		} else if (option == Button.O_NEW_CONTACTS_FOLDER) {
			locator = "css=td[id='zb__NEW_MENU_NEW_ADDRBOOK_title']";
			page = new FormContactNew(this.app);

		} else if (option == Button.O_NEW_CALENDAR) {
			locator = "css=td[id='zb__NEW_MENU_NEW_CALENDAR_title']";
			page = new FormContactNew(this.app);

		} else if (option == Button.O_NEW_TASK_FOLDER) {
			locator = "css=td[id='zb__NEW_MENU_NEW_TASK_FOLDER_title']";
			page = new FormContactNew(this.app);

		} else if (option == Button.O_NEW_BRIEFCASE) {
			locator = "css=td[id='zb__NEW_MENU_NEW_BRIEFCASE_title']";
			page = new FormContactNew(this.app);
		}

		if (locator == null)
			throw new HarnessException("locator was null for option " + option);

		if (!app.zPageMail.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + option);

		// Click to New dropdown
		SleepUtil.sleepMedium();
		app.zPageMail.sClickAt("css=td[id='zb__NEW_MENU_dropdown'] div[class='ImgSelectPullDownArrow']", "");
		SleepUtil.sleepSmall();
		app.zPageMain.zHandleComposeTabs();
		SleepUtil.sleepSmall();

		// Select option
		app.zPageMail.sClickAt(locator, "");
		app.zPageMail.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();
		}

		return (page);
	}

	public void zFreshLogin() {
		ZimbraAccount.ResetAccountZCS();

		try {
			ConfigProperties.setAppType(ConfigProperties.AppType.AJAX);
			webDriver.navigate().to(ConfigProperties.getLogoutURL());

		} catch (WebDriverException e) {
			logger.error("Unable to open app.", e);
			throw e;
		}

		try {
			((AjaxPages) app).zPageLogin.sOpen(ConfigProperties.getLogoutURL());
			if (ZimbraAccount.AccountZCS() != null) {
				((AjaxPages) app).zPageLogin.zLogin(ZimbraAccount.AccountZCS());
			} else {
				((AjaxPages) app).zPageLogin.zLogin(ZimbraAccount.Account10());
			}

		} catch (HarnessException e) {
			logger.error("Unable to navigate to app.", e);
		}
	}

	// Inject message using REST upload & SOAP AddMsgRequest
	public void injectMessage(ZimbraAccount account, String filePath) throws HarnessException {
		String attachmentId = account.uploadFileUsingRestUtil(filePath);

		// Add message in selected account
		try {
			account.soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
					"<m l='Inbox' f='u' aid='"+ attachmentId + "'></m>" +
				"</AddMsgRequest>");
		} catch (HarnessException e) {
			throw new HarnessException("Unable to inject message: " + e);
		}
		SleepUtil.sleepMedium();
	}

	// Flush mail queue
	public static void flushMailQueue() throws HarnessException {
		coreFlushMailQueue("");
	}
	public static void flushMailQueue(String domain) throws HarnessException {
		coreFlushMailQueue(domain);
	}
	public static void coreFlushMailQueue(String domain) throws HarnessException {
		try {
			for (int i=0; i<ExecuteHarnessMain.mtaServers.size(); i++) {
				if (domain == "") {
					ZimbraAdminAccount.GlobalAdmin().soapSend("<MailQueueActionRequest xmlns='urn:zimbraAdmin'>"
							+ "<server name='" + ExecuteHarnessMain.mtaServers.get(i) + "'>"
							+ "<queue name='deferred'><action op='delete' by='query'><query>"
							+ "</query></action></queue></server>"
							+ "</MailQueueActionRequest>");
				} else {
					ZimbraAdminAccount.GlobalAdmin().soapSend("<MailQueueActionRequest xmlns='urn:zimbraAdmin'>"
							+ "<server name='" + ExecuteHarnessMain.mtaServers.get(i) + "'>"
							+ "<queue name='deferred'><action op='delete' by='query'><query>"
							+ "<field name='todomain'><match value='" + domain + "'/></field></query></action></queue></server>"
							+ "</MailQueueActionRequest>");
				}
			}
		} catch (HarnessException e) {
			throw new HarnessException("Unable to flush mail queue: " + e);
		}
	}
}