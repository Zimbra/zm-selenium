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
/**
 *
 */
package com.zimbra.qa.selenium.framework.core;

import java.awt.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.jar.*;
import java.util.regex.*;
import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.testng.xml.*;
import com.zimbra.qa.selenium.framework.ui.AbsSeleniumObject;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ConfigProperties.AppType;
import com.zimbra.qa.selenium.framework.util.performance.PerfMetrics;
import com.zimbra.qa.selenium.framework.util.staf.*;
import com.zimbra.qa.selenium.projects.admin.ui.AppAdminConsole;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.html.ui.AppHtmlClient;
import com.zimbra.qa.selenium.projects.mobile.ui.AppMobileClient;
import com.zimbra.qa.selenium.projects.touch.ui.AppTouchClient;

public class ExecuteHarnessMain {

	private static Logger logger = LogManager.getLogger(ExecuteHarnessMain.class);
	public static final String TraceLoggerName = "testcase.trace";
	public static Logger tracer = LogManager.getLogger(TraceLoggerName);
	private static HashMap<String, String> configMap = new HashMap<String, String>();

	public static int testsTotal = 0;
	public static int testsPass = 0;
	public static int testsFailed = 0;
	public static int testsSkipped = 0;
	protected static AppAjaxClient app1 = null;
	protected static AppAdminConsole app2 = null;
	protected static AppTouchClient app3 = null;
	protected static AppHtmlClient app4 = null;
	protected static AppMobileClient app5 = null;
	protected AbsTab startingPage = null;

	public ExecuteHarnessMain() {
	}

	public int verbosity = 10;
	public static boolean DO_TEST_CASE_SUM = false;
	public static String TEST_TOKEN = ".tests.";
	public String jarfilename;
	public static String classfilter = null;
	public String excludefilter = null;
	public static ArrayList<String> groups = new ArrayList<String>(Arrays.asList("always", "sanity"));
	public ArrayList<String> excludeGroups = new ArrayList<String>(Arrays.asList("skip", "performance"));

	private static final String OpenQABasePackage = "org.openqa";
	public static final String SeleniumBasePackage = "com.zimbra.qa.selenium";
	public static String testoutputfoldername = null;
	public static ResultListener currentResultListener = null;

	public void setTestOutputFolderName(String path) {

		System.setProperty("outputDirectory", path);

		if (ConfigProperties.getStringProperty("coverage.enabled").equals(true)) {
			File coverage = new File(path + "/coverage");
			if (!coverage.exists())
				coverage.mkdirs();
			CodeCoverage.getInstance().setOutputFolder(coverage.getAbsolutePath());
		}

		// Append the app, browser, locale
		path += "/" + ConfigProperties.getAppType() + "/" + ConfigProperties.getCalculatedBrowser() + "/"
				+ ConfigProperties.getStringProperty("locale");

		// Make sure the path exists
		File output = new File(path);
		if (!output.exists())
			output.mkdirs();

		// Set the property to the absolute path
		try {
			testoutputfoldername = output.getCanonicalPath();
		} catch (IOException e) {
			logger.warn("Unable to get canonical path of the test output folder (" + e.getMessage()
					+ "). Using absolute path.");
			testoutputfoldername = output.getAbsolutePath();
		}

		// Make sure any other dependent folders exist
		File debug = new File(testoutputfoldername + "/debug");
		if (!debug.exists())
			debug.mkdirs();

		File testng = new File(testoutputfoldername + "/TestNG");
		if (!testng.exists())
			testng.mkdirs();

	}

	public String workingfoldername = ".";
	protected List<String> classes = null;

	private static List<String> getClassesFromJar(File jarfile, Pattern pattern, String excludeStr)
			throws FileNotFoundException, IOException, HarnessException {

		logger.debug("getClassesFromJar " + jarfile.getAbsolutePath());

		List<String> classes = new ArrayList<String>();

		JarInputStream jarFile = null;
		try {

			jarFile = new JarInputStream(new FileInputStream(jarfile));

			while (true) {
				JarEntry jarEntry = jarFile.getNextJarEntry();

				if (jarEntry == null)
					break; // All Done!

				if (!jarEntry.getName().endsWith(".class"))
					continue; // Only process classes

				if (jarEntry.getName().contains("CommonTest.class"))
					continue; // Skip CommonTest, since those aren't tests

				String name = jarEntry.getName().replace('/', '.').replaceAll(".class$", "");
				logger.debug("Class: " + name);

				if (pattern != null) {

					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {

						// Class name matched the filter. add it.
						if (!isExcluded(name, excludeStr)) {
							classes.add(name);
						}
					}

				} else {

					// No filter. add all.
					if (!isExcluded(name, excludeStr)) {
						classes.add(name);
					}

				}

			}

		} finally {
			if (jarFile != null) {
				jarFile.close();
				jarFile = null;
			}
		}

		if (classes.size() < 1) {
			throw new HarnessException("no classes matched pattern filter " + pattern.pattern());
		}

		return (classes);
	}

	private static boolean isExcluded(String name, String excludeStr) {
		boolean result = false;

		if (excludeStr == null) {
			return result;
		}

		if (excludeStr.indexOf(";") == -1) {
			result = (name.indexOf(excludeStr) != -1);
		} else {
			String[] splitStr = excludeStr.split(";");
			for (int j = 0; j < splitStr.length; j++) {
				if (result = (name.indexOf(splitStr[j]) != -1)) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Get the testname for a given class, per Zimbra standard formatting The
	 * test name is the package part after .tests.
	 */
	private static String getTestName(String classname) throws HarnessException {
		String token = TEST_TOKEN;

		int indexOfTests = classname.indexOf(token);
		if (indexOfTests < 0)
			throw new HarnessException("class names must contain " + token + " (" + classname + ")");

		int indexOfDot = classname.indexOf('.', indexOfTests + token.length());
		if (indexOfDot < 0)
			throw new HarnessException("class name doesn't contain ending dot (" + classname + ")");

		String testname = classname.substring(indexOfTests + token.length(), indexOfDot);
		logger.debug("testname: " + testname);

		return (testname);
	}

	protected List<String> getXmlTestNames() throws HarnessException {

		List<String> testnames = new ArrayList<String>();
		for (String c : classes) {
			String testname = getTestName(c);
			if (!testnames.contains(testname)) {
				logger.debug("Add new testiname " + testname);
				testnames.add(testname);
			}
		}
		return (testnames);
	}

	protected List<XmlSuite> getXmlSuiteList() throws HarnessException {

		// Add network or foss based on the server version
		if (ConfigProperties.zimbraGetVersionString().toLowerCase().contains("network")) {
			excludeGroups.add("foss");
		} else {
			excludeGroups.add("network");
		}

		PerfMetrics.getInstance().Enabled = groups.contains("performance");

		// Only one suite per run in the zimbra process (subject to change)
		XmlSuite suite = new XmlSuite();
		suite.setName("zimbra");
		suite.setVerbose(verbosity);
		suite.setThreadCount(4);
		suite.setParallel(XmlSuite.PARALLEL_NONE);

		// Add all the names per the list of classes
		for (String testname : getXmlTestNames()) {
			XmlTest test = new XmlTest(suite);
			test.setName(testname);
			test.setIncludedGroups(groups);
			test.setExcludedGroups(excludeGroups);
		}

		// Add all the classes per the appropriate test name
		for (String c : classes) {
			String testname = getTestName(c);
			for (XmlTest test : suite.getTests()) {
				if (test.getName().equals(testname)) {

					XmlClass x = new XmlClass(c);
					test.getXmlClasses().add(x);

					break;
				}
			}
		}
		return (Arrays.asList(suite));
	}

	public String execute() throws HarnessException, FileNotFoundException, IOException {
		logger.info("Execute ...");

		Date start = new Date();
		Date finish;

		StringBuilder result = new StringBuilder();

		try {
			String response;
			if (ConfigProperties.getStringProperty("coverage.enabled").equals(true)) {
				response = executeCodeCoverage();
			} else {
				response = executeSelenium();
			}
			result.append(response).append('\n');

		} finally {
			finish = new Date();
		}

		// calculate how long the tests took
		long duration = finish.getTime() - start.getTime();
		result.append("Duration: ").append(duration / 1000).append(" seconds\n");
		result.append("Browser: ").append(ConfigProperties.getCalculatedBrowser()).append('\n');

		return (result.toString());
	}

	protected String executeCodeCoverage() throws FileNotFoundException, HarnessException, IOException {
		try {
			try {
				CodeCoverage.getInstance().instrumentServer();
				return (executeSelenium());

			} finally {
				CodeCoverage.getInstance().writeXml();
				CodeCoverage.getInstance().instrumentServerUndo();
			}

		} finally {
			CodeCoverage.getInstance().writeCoverage();
		}
	}

	protected String executeSelenium() throws HarnessException, FileNotFoundException, IOException {
		try {
			SeleniumService.getInstance().startSeleniumExecution();
			return (executeTests());

		} finally {
			SeleniumService.getInstance().stopSeleniumExecution();
		}
	}

	protected void getRemoteFile(String remotehost, String fromfile, String tofile) throws HarnessException {
		logger.info("getRemoteFile(" + remotehost + ", " + fromfile + ", " + tofile + ")");

		// Create the folder, if it doesn't exist
		File file = new File(tofile);
		file.getParentFile().mkdirs();

		String localhost = getLocalMachineName();

		StafServiceFS staf = new StafServiceFS(remotehost);
		staf.execute(" COPY FILE " + fromfile + " TOFILE " + tofile + " TOMACHINE " + localhost);

	}

	protected static String getLocalMachineName() throws HarnessException {
		logger.info("getLocalMachineName()");

		StafServiceVAR staf = new StafServiceVAR();
		staf.execute();
		return (staf.getStafResponse());
	}

	protected String executeTests() throws FileNotFoundException, IOException, HarnessException {

		logger.info("Execute tests ...");

		try {

			// Build the class list
			classes = getClassesFromJar(new File(jarfilename),
					(classfilter == null ? null : Pattern.compile(classfilter)), excludefilter);

			// Build the list of XmlSuites
			List<XmlSuite> suites = getXmlSuiteList();

			// Create the TestNG test runner
			TestNG testNG = new TestNG();

			for (String st : configMap.keySet()) {
				ConfigProperties.setStringProperty(st, configMap.get(st));
			}

			// keep checking for server down
			while (ConfigProperties.zimbraGetVersionString().indexOf("unknown") != -1) {
				SleepUtil.sleep(100000);
			}

			// Configure the runner
			testNG.setXmlSuites(suites);
			testNG.addListener(new MethodListener(ExecuteHarnessMain.testoutputfoldername));
			testNG.addListener(new ErrorDialogListener());
			testNG.addListener(currentResultListener = new ResultListener(ExecuteHarnessMain.testoutputfoldername));
			testNG.addListener(new AnnotationTransformer());
			testNG.addListener(new TestListener());

			try {
				testNG.setOutputDirectory(ExecuteHarnessMain.testoutputfoldername + "/TestNG");
			} catch (Exception e) {
				throw new HarnessException(e);
			}

			// Run!
			testNG.run();

			// finish inProgress - overwrite inProgress/index.html
			TestStatusReporter.copyFile(testoutputfoldername + "\\TestNG\\emailable-report.html",
					testoutputfoldername + "\\TestNG\\index.html");

			logger.info("Execute tests ... completed");

			SleepMetrics.report();

			if (!ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".emailTo",
					ConfigProperties.getStringProperty("emailTo")).contains("qa-automation@zimbra.com")
					&& ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".emailTo",
							ConfigProperties.getStringProperty("emailTo")).contains("jsojitra@zimbra.com")) {

				String project = classfilter.toString().replace("com.zimbra.qa.selenium.", "").replace("projects.", "");
				project = project.substring(0, 1).toUpperCase() + project.substring(1);
				String[] projectSplit = project.split(".tests.");

				String suite = groups.toString().replace("always, ", "").replace("[", "").replace("]", "").trim();
				if (suite.equals("sanity,smoke,functional") || suite.equals("sanity, smoke, functional")) {
					suite = "Full";
				}
				suite = suite.substring(0, 1).toUpperCase() + suite.substring(1).replace(".tests", "");

				SendEmail.main(new String[] {
						"Selenium: " + projectSplit[0].replace(".tests", "") + " " + suite + " | "
								+ ConfigProperties.getLocalHost() + " | " + ConfigProperties.zimbraGetVersionString()
								+ " ("
								+ ConfigProperties
										.getStringProperty(ConfigProperties.getLocalHost() + ".server.host",
												ConfigProperties.getStringProperty("server.host"))
										.replace(".eng.zimbra.com", "").replace(".lab.zimbra.com", "")
								+ ")" + " | " + "Total Tests: " + String.valueOf(testsTotal) + " (Passed: "
								+ String.valueOf(testsPass) + ", Failed: " + String.valueOf(testsFailed) + ", Skipped: "
								+ String.valueOf(testsSkipped) + ")",
						currentResultListener.getCustomResult(),
						testoutputfoldername + "\\TestNG\\emailable-report.html",
						testoutputfoldername + "\\TestNG\\index.html" });
			}

			return (currentResultListener == null ? "Done" : currentResultListener.getResults());

		} finally {

			testsTotal = 0;
			testsPass = 0;
			testsFailed = 0;
			testsSkipped = 0;

			currentResultListener = null;

		}
	}

	public String sumTestCounts() throws FileNotFoundException, IOException, HarnessException {

		logger.debug("sumTestCounts");

		StringBuilder sb = new StringBuilder();
		int sum = 0;

		List<String> classes = ExecuteHarnessMain.getClassesFromJar(new File(jarfilename),
				(classfilter == null ? null : Pattern.compile(classfilter)), excludefilter);

		for (String s : classes) {

			try {

				Class<?> c = Class.forName(s);
				logger.debug("sumTestCounts: checking class: " + c.getCanonicalName());

				for (Method m : Arrays.asList(c.getDeclaredMethods())) {

					logger.debug("sumTestCounts: checking method: " + m.getName());

					for (Annotation a : Arrays.asList(m.getAnnotations())) {

						logger.debug("sumTestCounts: checking annotation: " + a.toString());

						if (a instanceof org.testng.annotations.Test) {

							org.testng.annotations.Test t = (org.testng.annotations.Test) a;

							// Check the groups to make sure they match
							for (String g : Arrays.asList(t.groups())) {

								if (ExecuteHarnessMain.groups.contains(g)) {

									logger.debug("sumTestCounts: matched: " + g);

									sb.append(++sum).append(": ").append(t.description()).append('\n');
									continue; // for (Annotation a ...

								}
							}

						}

					}

				}

			} catch (ClassNotFoundException e) {
				logger.warn("sumTestCounts: Unable to find class", e);
			}

		}

		logger.debug("sumTestCounts: found: " + sum);

		sb.append("Number of matching test cases: " + sum);
		return (sb.toString());

	}

	protected static class ErrorDialogListener extends AbsSeleniumObject implements IInvokedMethodListener {

		@Override
		public void afterInvocation(IInvokedMethod method, ITestResult result) {

			if (method.isTestMethod()) {

				logger.info("ErrorDialogListener:afterInvocation ...");

				String locator = "css=div#ErrorDialog";

				try {

					boolean present = sIsElementPresent(locator);
					if (present) {

						logger.info("ErrorDialogListener:afterInvocation ... present=" + present);

						Number left = sGetElementPositionLeft(locator);
						if (left.intValue() > 0) {

							logger.info("ErrorDialogListener:afterInvocation ... left=" + left);

							Number top = sGetElementPositionTop(locator);

							if (top.intValue() > 0) {

								logger.info("ErrorDialogListener:afterInvocation ... top=" + top);

								// Log the error
								logger.error(new HarnessException("ExecuteHarnessMain: Error dialog is visible"));

								// Take screenshot
								getScreenCapture(result);

								// Set the test as failed
								result.setStatus(ITestResult.FAILURE);
							}
						}
					}

				} catch (Exception ex) {
					logger.error(new HarnessException("ErrorDialogListener:afterInvocation ", ex), ex);
				}

				logger.info("ErrorDialogListener:afterInvocation ... done");
			}

		}

		public static void getScreenCapture(ITestResult result) {

			String coreFolderPath, screenShotFilePath, testcase;
			testcase = result.getName().toString();

			coreFolderPath = testoutputfoldername + "/debug/projects/"
					+ ConfigProperties.getAppType().toString().toLowerCase() + "/core/";
			screenShotFilePath = coreFolderPath + testcase + ".png";

			// Make sure required folders exist
			File coreFolder = new File(testoutputfoldername + "/debug/projects/"
					+ ConfigProperties.getAppType().toString().toLowerCase() + "/core/");
			if (!coreFolder.exists())
				coreFolder.mkdirs();

			logger.info("Creating screenshot: " + screenShotFilePath);
			try {
				File scrFile = ((TakesScreenshot) ClientSessionFactory.session().webDriver())
						.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(screenShotFilePath));

			} catch (HeadlessException e) {
				logger.error("Unable to create screenshot", e);
			} catch (IOException e) {
				logger.error("IE exception when creating image file at " + screenShotFilePath, e);
			} catch (WebDriverException e) {
				logger.error("Webdriver exception when creating image file at " + screenShotFilePath, e);
			}

		}

		@Override
		public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		}

	}

	protected static class MethodListener implements IInvokedMethodListener {

		private static Logger logger = LogManager.getLogger(MethodListener.class);
		private static final Logger OpenQALogger = LogManager.getLogger(OpenQABasePackage);
		private static final Logger Logger = LogManager.getLogger(SeleniumBasePackage);

		private final Map<String, Appender> appenders = new HashMap<String, Appender>();
		private static final Layout layout = new PatternLayout("%-4r [%t] %-5p %c %x - %m%n");

		public String outputFolder = null;

		protected MethodListener(String folder) {
			outputFolder = (folder == null ? "logs" : folder);
		}

		protected String getKey(Method method) {
			return (method.getDeclaringClass().getCanonicalName());
		}

		protected String getFilename(Method method) {
			String c = method.getDeclaringClass().getCanonicalName().replace(SeleniumBasePackage, "").replace('.', '/');
			String m = method.getName();
			return (String.format("%s/debug/%s/%s.txt", outputFolder, c, m));
		}

		protected String getTestCaseID(Method method) {
			String c = method.getDeclaringClass().getCanonicalName();
			String m = method.getName();
			return (c + "." + m);
		}

		/**
		 * Add a new FileAppender for each class before invocation
		 */
		@Override
		public void beforeInvocation(IInvokedMethod method, ITestResult result) {

			if (method.isTestMethod()) {

				try {
					String key = getKey(method.getTestMethod().getMethod());
					if (!appenders.containsKey(key)) {
						String filename = getFilename(method.getTestMethod().getMethod());
						Appender a = new FileAppender(layout, filename, false);
						appenders.put(key, a);
						OpenQALogger.addAppender(a);
						Logger.addAppender(a);
					}
					logger.info("MethodListener: START: " + getTestCaseID(method.getTestMethod().getMethod()));

					// Log the associated bugs
					Bugs b = method.getTestMethod().getMethod().getAnnotation(Bugs.class);
					if (b != null) {
						logger.info("Associated bugs: " + b.ids());
					}

					// Log the test case trace
					tracer.trace("/***");
					tracer.trace("## ID: " + getTestCaseID(method.getTestMethod().getMethod()));
					tracer.trace("# Objective: " + method.getTestMethod().getDescription());
					tracer.trace("# Group(s): " + Arrays.toString(method.getTestMethod().getGroups()));
					tracer.trace("");

				} catch (IOException e) {
					logger.warn("Unable to add test class appender", e);
				}

			}
		}

		/**
		 * Remove any FileAppenders after invocation
		 */
		@Override
		public void afterInvocation(IInvokedMethod method, ITestResult result) {

			if (method.isTestMethod()) {

				try {
					CodeCoverage.getInstance().calculateCoverage(getTestCaseID(method.getTestMethod().getMethod()));
				} catch (HarnessException e) {
					logger.error("Skip logging calculation", e);
				}

				logger.info("MethodListener: FINISH: " + getTestCaseID(method.getTestMethod().getMethod()));

				tracer.trace("");
				tracer.trace("# Pass: " + result.isSuccess());
				tracer.trace("# End ID: " + getTestCaseID(method.getTestMethod().getMethod()));
				tracer.trace("***/");
				tracer.trace("");
				tracer.trace("");

				Appender a = null;
				String key = getKey(method.getTestMethod().getMethod());
				if (appenders.containsKey(key)) {
					a = appenders.get(key);
					appenders.remove(key);
				}
				if (a != null) {
					OpenQALogger.removeAppender(a);
					Logger.removeAppender(a);
					a.close();
					a = null;
				}
			}
		}

	}

	public static class  RetryAnalyzer implements IRetryAnalyzer {
		 
		int counter = 0;
		int retryLimit = 2;//Retry the test case when it goes fail on first time
	 
		@Override
		public boolean retry(ITestResult result) {
	 
			if(counter < retryLimit)
			{
				counter++;
				return true;
			}
			return false;
		}
	}
	
	protected class AnnotationTransformer implements IAnnotationTransformer {
		 // Dynamically add retry class annotation to each test cases
		@Override
		public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
				annotation.setRetryAnalyzer(ExecuteHarnessMain.RetryAnalyzer.class);
		}

	
	}
	
	// Listener is use to avoid conflict of count in the TestNG report 
	protected class TestListener implements ITestListener {
	    @Override
		public void onFinish(ITestContext context) {
			Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
			for (ITestResult temp : failedTests) {
				ITestNGMethod method = temp.getMethod();
				if (context.getFailedTests().getResults(method).size() > 1) {
					failedTests.remove(temp);
				} else {
					if (context.getPassedTests().getResults(method).size() > 0) {
						failedTests.remove(temp);
					}
				}
			}
		}
	  
	    public void onTestStart(ITestResult result) {   }
	  
	    public void onTestSuccess(ITestResult result) {   }
	  
	    public void onTestFailure(ITestResult result) {   }

	    public void onTestSkipped(ITestResult result) {   }

	    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {   }

	    public void onStart(ITestContext context) {   }
	}  
	
	
	
	/**
	 * A TestNG TestListener that tracks the pass/fail/skip counts
	 * <p>
	 *
	 * @author Matt Rhoades
	 */
	public static class ResultListener extends TestListenerAdapter {

		private static List<String> failedTests = new ArrayList<String>();
		private static List<String> skippedTests = new ArrayList<String>();

		protected ResultListener(String folder) {
			ResultListener.setOutputfolder(folder);
		}

		public String getResults() {
			StringBuilder sb = new StringBuilder();

			sb.append("Total Tests:   ").append(testsTotal).append('\n');
			sb.append("Total Passed:  ").append(testsPass).append('\n');
			sb.append("Total Failed:  ").append(testsFailed).append('\n');
			sb.append("Total Skipped: ").append(testsSkipped).append('\n');
			if (!failedTests.isEmpty()) {
				sb.append("\n\nFailed tests:\n");
				for (String s : failedTests) {
					sb.append(s).append('\n');
				}
			}
			if (!skippedTests.isEmpty()) {
				sb.append("\n\nSkipped tests:\n");
				for (String s : skippedTests) {
					sb.append(s).append('\n');
				}
			}
			return (sb.toString());
		}

		public String getCustomResult() throws HarnessException, FileNotFoundException, IOException {

			StringBuilder emailBody = new StringBuilder();
			StringBuilder bugzillaBody = new StringBuilder();
			StringBuilder formatter = new StringBuilder();

			String machineName, resultDirectory = null, seleniumProject = null, resultRootDirectory = null,
					labScriptFile, labResultURL;
			String zimbraTestNGResultsJar = "c:/opt/qa/BugReports/zimbratestngresults.jar";

			machineName = getLocalMachineName().replace(".corp.telligent.com", "").replace(".lab.zimbra.com", "");
			emailBody.append("Selenium Automation Report: ")
					.append(ConfigProperties.zimbraGetVersionString() + "_" + ConfigProperties.zimbraGetReleaseString())
					.append('\n').append('\n');

			emailBody.append("Client  :  ").append(getLocalMachineName().replace(".lab.zimbra.com", "")).append('\n');
			emailBody.append("Server  :  ")
					.append(ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".server.host",
							ConfigProperties.getStringProperty("server.host").replace(".lab.zimbra.com", "")))
					.append('\n').append('\n');

			emailBody.append("Browser :  ")
					.append(ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".browser",
							ConfigProperties.getStringProperty("browser")).replace("*", ""))
					.append('\n');
			emailBody.append("Pattern :  ").append(classfilter.toString().replace("com.zimbra.qa.selenium.", ""))
					.append('\n');
			emailBody.append("Groups  :  ")
					.append(WordUtils.capitalize(
							groups.toString().replace("always, ", "").trim().replace("[", "").replace("]", "")))
					.append('\n');
			emailBody.append('\n');

			if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
					ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
				emailBody.append('\n');
				emailBody.append("Coverage   :  ").append("true").append('\n').append('\n');
			} else {
				emailBody.append('\n');
			}

			resultDirectory = testoutputfoldername.replaceAll("[^a-zA-Z0-9/._]", "/")
					.replaceAll("C//opt/qa/dev/ZimbraSelenium/test/output/", "").replaceAll("C//opt/qa/dev/", "")
					.replaceAll("C//opt/qa/master/ZimbraSelenium/test/output/", "").replaceAll("C//opt/qa/master/", "");

			// Get selenium project
			if (testoutputfoldername.indexOf("AJAX") > 0) {
				seleniumProject = "ajax";
			} else if (testoutputfoldername.indexOf("ADMIN") > 0) {
				seleniumProject = "admin";
			} else if (testoutputfoldername.indexOf("TOUCH") > 0) {
				seleniumProject = "touch";
			} else if (testoutputfoldername.indexOf("HTML") > 0) {
				seleniumProject = "html";
			} else if (testoutputfoldername.indexOf("MOBILE") > 0) {
				seleniumProject = "mobile";
			}

			if (machineName.contains("pnq-")) {
				labScriptFile = ConfigProperties.getStringProperty("webPortal") + "/machines/" + machineName
						+ "/selenium/" + seleniumProject + "/logs/" + resultDirectory.split("/")[1] + ".log";
				emailBody.append("Script Log File :  ").append(labScriptFile).append('\n').append('\n');

				labResultURL = ConfigProperties.getStringProperty("webPortal") + "/machines/" + machineName
						+ "/selenium/" + seleniumProject + "/" + resultDirectory.replace("Results", "results");
				emailBody.append("Lab Result URL  :  ").append(labResultURL).append('\n').append('\n');
			}

			emailBody.append("Total Tests     :  ").append(testsTotal).append('\n');
			emailBody.append("Total Passed    :  ").append(testsPass).append('\n');
			emailBody.append("Total Failed    :  ").append(testsFailed).append('\n');
			emailBody.append("Total Skipped   :  ").append(testsSkipped).append('\n');

			if (!failedTests.isEmpty()) {
				emailBody.append("\n\nFailed tests:\n");
				for (String s : failedTests) {
					emailBody.append(s).append('\n');
				}
			}

			if (!skippedTests.isEmpty()) {
				emailBody.append("\n\nSkipped tests:\n");
				for (String s : skippedTests) {
					emailBody.append(s).append('\n');
				}
			}

			// Check bug status via bugzilla files

			int resultRootDirectoryLocation = testoutputfoldername.indexOf("AJAX");
			if (resultRootDirectoryLocation > 0) {
				resultRootDirectory = testoutputfoldername.substring(0, resultRootDirectoryLocation - 1);
			}
			resultRootDirectoryLocation = testoutputfoldername.indexOf("ADMIN");
			if (resultRootDirectoryLocation > 0) {
				resultRootDirectory = testoutputfoldername.substring(0, resultRootDirectoryLocation - 1);
			}
			resultRootDirectoryLocation = testoutputfoldername.indexOf("TOUCH");
			if (resultRootDirectoryLocation > 0) {
				resultRootDirectory = testoutputfoldername.substring(0, resultRootDirectoryLocation - 1);
			}

			StafExecute staf = new StafExecute("SERVICE",
					"ADD SERVICE BUGZILLA LIBRARY JSTAF EXECUTE " + zimbraTestNGResultsJar);
			staf.execute();

			staf = new StafExecute("BUGZILLA",
					"REPORT ROOT " + resultRootDirectory.replaceAll("[^a-zA-Z0-9/._:-]", "/"));
			staf.execute();

			// Read bug report text file and append to the email report

			try (BufferedReader br = new BufferedReader(
					new FileReader(resultRootDirectory + "/BugReports/BugReport.txt"))) {
				String line = null;
				try {
					line = br.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				while (line != null) {
					bugzillaBody.append(line);
					bugzillaBody.append(System.lineSeparator());
					try {
						line = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			staf = new StafExecute("SERVICE", "REMOVE SERVICE BUGZILLA");
			staf.execute();

			return (emailBody.toString() + formatter.append('\n')
					+ formatter
							.append("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n\n\n")
					+ bugzillaBody.toString());
		}

		private static ITestResult runningTestCase = null;

		private static void setRunningTestCase(ITestResult result) {
			runningTestCase = result;
		}

		private static String outputFolder = null;

		private static void setOutputfolder(String folder) {
			outputFolder = (folder == null ? "logs" : folder);
		}

		private static int screenshotcount = 0;

		public static String getScreenCaptureFilename(Method method) {
			String c = method.getDeclaringClass().getCanonicalName().replace(SeleniumBasePackage, "").replace('.', '/');
			String m = method.getName();
			return (String.format("%s/debug/%s/%sss%d.png", outputFolder, c, m, ++screenshotcount));
		}

		public static void getScreenCapture(ITestResult result) {

			String filename = getScreenCaptureFilename(result.getMethod().getMethod());

			logger.info("Creating screenshot: " + filename);
			try {
				File scrFile = ((TakesScreenshot) ClientSessionFactory.session().webDriver())
						.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(filename));

			} catch (HeadlessException e) {
				logger.error("Unable to create screenshot", e);
			} catch (IOException e) {
				logger.error("IE exception when creating image file at " + filename, e);
			} catch (WebDriverException e) {
				logger.error("Webdriver exception when creating image file at " + filename, e);
			}

		}

		private static int mailboxlogcount = 0;

		public static String getMailboxLogFilename(Method method) {
			String c = method.getDeclaringClass().getCanonicalName().replace(SeleniumBasePackage, "").replace('.', '/');
			String m = method.getName();
			return (String.format("%s/debug/%s/%smailbox%d.txt", outputFolder, c, m, ++mailboxlogcount));
		}

		public static void getMailboxLog(ITestResult result) throws HarnessException {

			logger.warn("Copying mailbox.log...");

			String command = null;
			try {
				final String tomachine = InetAddress.getLocalHost().getHostAddress();
				final String tofile = getMailboxLogFilename(result.getMethod().getMethod());
				final String file = "/opt/zimbra/log/mailbox.log";

				command = "COPY FILE " + file + " TOFILE " + tofile + " TOMACHINE " + tomachine;

			} catch (UnknownHostException e) {
				throw new HarnessException("Unable to copy mailbox.log", e);
			}

			StafServiceFS staf = new StafServiceFS();
			staf.execute(command);
			if (staf.getSTAFResult().rc != com.ibm.staf.STAFResult.Ok) {
				throw new HarnessException("Unable to copy mailbox.log " + staf.getSTAFResult().result);
			}

		}

		@Override
		public void onFinish(ITestContext context) {
		}

		@Override
		public void onStart(ITestContext context) {

		}

		@Override
		public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
			getScreenCapture(result);
		}

		/**
		 * Add failed tests
		 */
		@Override
		public void onTestFailure(ITestResult result) {
			testsFailed++;
			String fullname = result.getMethod().getMethod().getDeclaringClass().getName() + "."
					+ result.getMethod().getMethod().getName();
			failedTests.add(fullname); // failedTests.add(fullname.replace("com.zimbra.qa.selenium.projects.",
										// "main.projects."));
			getScreenCapture(result);
		}

		/**
		 * Add skipped tests
		 */
		@Override
		public void onTestSkipped(ITestResult result) {
			testsSkipped++;
			String fullname = result.getMethod().getMethod().getDeclaringClass().getName() + "."
					+ result.getMethod().getMethod().getName();
			skippedTests.add(fullname); // skippedTests.add(fullname.replace("com.zimbra.qa.selenium.projects.",
										// "main.projects."));
			getScreenCapture(result);
		}

		/**
		 * Add total tests
		 */
		@Override
		public void onTestStart(ITestResult result) {
			setRunningTestCase(result);
			// Below will handle retry test cases behavior's conflict 
			if(testsTotal==0)
				testsTotal++;
			if(testsTotal == testsFailed+testsPass+testsSkipped)
				testsTotal++;
		}

		public static void captureScreen() {
			getScreenCapture(runningTestCase);
		}

		public static void captureMailboxLog() throws HarnessException {
			getMailboxLog(runningTestCase);
		}

		@Override
		public void onTestSuccess(ITestResult result) {
			testsPass++;
		}

		@Override
		public void onConfigurationFailure(ITestResult result) {
			getScreenCapture(result);
		}

		@Override
		public void onConfigurationSkip(ITestResult result) {
		}

		@Override
		public void onConfigurationSuccess(ITestResult result) {
		}

	}

	private boolean parseArgs(String arguments[]) throws ParseException, HarnessException {

		// Build option list
		Options options = new Options();
		options.addOption(new Option("h", "help", false, "print usage"));
		options.addOption(new Option("l", "log4j", true, "log4j file containing log4j configuration"));
		options.addOption(new Option("j", "jarfile", true, "jarfile containing test cases"));
		options.addOption(new Option("p", "pattern", true, "class filter regex, i.e. projects.ajax.tests."));
		options.addOption(new Option("g", "groups", true,
				"comma separated list of groups to execute (always, sanity, smoke, functional)"));
		options.addOption(new Option("v", "verbose", true, "set suite verbosity (default: " + verbosity + ")"));
		options.addOption(new Option("o", "output", true, "output foldername"));
		options.addOption(new Option("w", "working", true, "current working foldername"));
		options.addOption(new Option("c", "config", true,
				"dynamic setting config properties i.e browser, server, locale... ( -c 'locale=en_US,browser=firefox' "));
		options.addOption(
				new Option("s", "sum", false, "run harness in mode to count the number of matching test cases"));
		options.addOption(new Option("e", "exclude", true, "exclude pattern  "));
		options.addOption(new Option("eg", "exclude_groups", true,
				"comma separated list of groups to exclude when execute (skip)"));

		// Set required options
		options.getOption("j").setRequired(true);

		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, arguments);

			if (cmd.hasOption('h')) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ExecuteTests", options);
				return false;
			}

			if (cmd.hasOption('s')) {
				DO_TEST_CASE_SUM = true;
			}

			if (cmd.hasOption('c')) {

				String[] confArray = cmd.getOptionValues('c');

				for (int i = 0; i < confArray.length; i++) {
					// could have form: 'browser=firefox;locale=en_US'
					String[] confItems = confArray[i].split(",");

					for (int j = 0; j < confItems.length; j++) {
						String[] confItem = confItems[j].split("=");

						// check form config=value and if a valid config name
						if ((confItem.length > 1) && (ConfigProperties.getStringProperty(confItem[0]) != null)) {
							configMap.put(confItem[0], confItem[1]);

						}
					}
				}
			}

			if (cmd.hasOption('p')) {
				String filter = cmd.getOptionValue('p');
				ExecuteHarnessMain.classfilter = filter;

				// Set the app type on the properties
				for (AppType t : AppType.values()) {
					// Look for ".type." (e.g. ".ajax.") in the pattern
					if (ExecuteHarnessMain.classfilter.contains(t.toString().toLowerCase())) {
						ConfigProperties.setAppType(t);
						break;
					}
				}

				if (ExecuteHarnessMain.classfilter.equals("com.zimbra.qa.selenium.projects.html.tests")) {
					throw new HarnessException("Currently Html client tests are not being executed.");
				}

				if (ExecuteHarnessMain.classfilter.equals("com.zimbra.qa.selenium.projects.mobile.tests")) {
					throw new HarnessException("Currently Mobile client tests are not being executed.");
				}

			}

			if (cmd.hasOption('e')) {
				if (cmd.getOptionValue('e').length() > 0) {
					this.excludefilter = cmd.getOptionValue('e');
				}
			}

			// 'o' check should be after 'p' check to avoid code redundancy
			if (cmd.hasOption('o')) {
				this.setTestOutputFolderName(cmd.getOptionValue('o'));
			} else {
				this.setTestOutputFolderName(ConfigProperties.getStringProperty("testOutputDirectory") + "/"
						+ ConfigProperties.zimbraGetVersionString());
			}

			// Processing log4j must come first so debugging can happen
			if (cmd.hasOption('l')) {
				PropertyConfigurator.configure(cmd.getOptionValue('l'));
			} else {
				BasicConfigurator.configure();
			}

			if (cmd.hasOption('j')) {
				this.jarfilename = cmd.getOptionValue('j');
			}

			if (cmd.hasOption('g')) {
				// Remove spaces and split on commas
				String[] values = cmd.getOptionValue('g').replaceAll("\\s+", "").split(",");
				ExecuteHarnessMain.groups = new ArrayList<String>(Arrays.asList(values));
			}

			if (cmd.hasOption("eg")) {
				// Remove spaces and split on commas
				String[] values = cmd.getOptionValue("eg").replaceAll("\\s+", "").split(",");
				this.excludeGroups = new ArrayList<String>(Arrays.asList(values));
			}

			if (cmd.hasOption('v')) {
				this.verbosity = Integer.parseInt(cmd.getOptionValue('v'));
			}

			if (cmd.hasOption('w')) {
				workingfoldername = cmd.getOptionValue('w');
			}

		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ExecuteTests", options);
			throw e;
		}

		return (true);
	}

	public static void main(String[] args) {

		BasicConfigurator.configure();

		String result = "No results";
		try {

			// Set the working conditions
			ConfigProperties.setBaseDirectory("");
			ConfigProperties.setConfigProperties("conf/config.properties");

			// Create the harness object and execute it
			ExecuteHarnessMain harness = new ExecuteHarnessMain();
			if (harness.parseArgs(args)) {
				if (DO_TEST_CASE_SUM) {
					result = harness.sumTestCounts();
				} else {
					result = harness.execute();
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			DO_TEST_CASE_SUM = false;
		}

		logger.info(result);
		System.out.println("*****\n" + result);
		System.exit(0);

	}

}
