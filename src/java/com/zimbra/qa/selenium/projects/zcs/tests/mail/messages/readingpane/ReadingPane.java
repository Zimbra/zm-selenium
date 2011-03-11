package com.zimbra.qa.selenium.projects.zcs.tests.mail.messages.readingpane;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.LmtpUtil;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;




/**
 * @author Jitesh Sojitra
 */
@SuppressWarnings("static-access")
public class ReadingPane extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "dataProvider")
	public Object[][] createData(Method method) throws Exception {
		String test = method.getName();
		if (test.equals("readingPaneOnRight")) {
			return new Object[][] {
					{ ClientSessionFactory.session().currentUserName(), ClientSessionFactory.session().currentUserName(),
							"ccuser@testdomain.com", "bccuser@testdomain.com",
							"subject_readingPaneOnRight",
							"body_readingPaneOnRight", "byNormalMethod" },
					{ ClientSessionFactory.session().currentUserName(), ClientSessionFactory.session().currentUserName(),
							"ccuser@testdomain.com", "bccuser@testdomain.com",
							"subject_readingPaneOnRight",
							"body_readingPaneOnRight", "byKeyboardShortCut" } };
		} else if (test.equals("readingPaneOnBottom")) {
			return new Object[][] {
					{ ClientSessionFactory.session().currentUserName(), ClientSessionFactory.session().currentUserName(),
							"ccuser@testdomain.com", "bccuser@testdomain.com",
							"subject_readingPaneOnBottom",
							"body_readingPaneOnBottom", "byNormalMethod" },
					{ ClientSessionFactory.session().currentUserName(), ClientSessionFactory.session().currentUserName(),
							"ccuser@testdomain.com", "bccuser@testdomain.com",
							"subject_readingPaneOnBottom",
							"body_readingPaneOnBottom", "byKeyboardShortCut" } };
		} else if (test.equals("readingPaneOff")) {
			return new Object[][] {
					{ ClientSessionFactory.session().currentUserName(), ClientSessionFactory.session().currentUserName(),
							"ccuser@testdomain.com", "bccuser@testdomain.com",
							"subject_readingPaneOff", "body_readingPaneOff",
							"byNormalMethod" },
					{ ClientSessionFactory.session().currentUserName(), ClientSessionFactory.session().currentUserName(),
							"ccuser@testdomain.com", "bccuser@testdomain.com",
							"subject_readingPaneOff", "body_readingPaneOff",
							"byKeyboardShortCut" } };
		} else {
			return new Object[][] { { ClientSessionFactory.session().currentUserName(),
					ClientSessionFactory.session().currentUserName(), "ccuser@testdomain.com",
					"bccuser@testdomain.com", "testsubject", "testbody", "" } };
		}
	}
	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="mail";
		super.zLogin();
	}
	

	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	@Test(dataProvider = "dataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void readingPaneOnRight(String from, String to, String cc,
			String bcc, String subject, String body, String method)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		/**
		 * This step is added for bug 36935 : Multiple issues after setting
		 * reading pane on right.
		 */
		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneOnRight));

		/**
		 * Following redundant code is added because
		 * MailApp.ClickCheckMailUntilMailShowsUp(subject); is not working with
		 * Reading pane set to right.
		 */
		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneAtBottom));

		to = ClientSessionFactory.session().currentUserName();
		String recipients[] = { to };
		LmtpUtil.injectMessage(from, recipients, cc, subject, body);
		MailApp.ClickCheckMailUntilMailShowsUp(subject);

		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneOff));

		if (method.equals("byNormalMethod")) {
			obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
			obj.zMenuItem.zClick(localize(locator.readingPaneOnRight));
		}

		if (method.equals("byKeyboardShortCut")) {
			ClientSessionFactory.session().selenium().windowFocus();
			SleepUtil.sleep(3000);
			Robot zRobot = new Robot();
			zRobot.keyPress(KeyEvent.VK_M);
			zRobot.keyRelease(KeyEvent.VK_M);
			zRobot.keyPress(KeyEvent.VK_P);
			zRobot.keyRelease(KeyEvent.VK_P);
			zRobot.keyPress(KeyEvent.VK_R);
			zRobot.keyRelease(KeyEvent.VK_R);
			SleepUtil.sleep(2000);
		}

		Boolean isVisible = ClientSessionFactory.session().selenium().isVisible("id=DWT6");
		Boolean isNotVisible = ClientSessionFactory.session().selenium().isVisible("id=DWT7");
		Boolean isSubDblRowClassExists = ClientSessionFactory.session().selenium()
				.isElementPresent("class=SubjectDoubleRow");
		assertReport("true", isVisible.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isNotVisible.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("true", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3000);
		assertReport("true", isVisible.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isNotVisible.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("true", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");
		assertReport(
				"right",
				Stafzmprov.getAccountPreferenceValue(ClientSessionFactory.session().currentUserName(),
						"zimbraPrefReadingPaneLocation"),
				"Verifying whether db value set properly or not for this account (zimbraPrefReadingPaneLocation)");

		ClientSessionFactory.session().selenium().click("link=" + localize(locator.logOff));
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(to);
		SleepUtil.sleep(3000);
		assertReport("true", isVisible.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isNotVisible.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("true", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");
		assertReport(
				"right",
				Stafzmprov.getAccountPreferenceValue(ClientSessionFactory.session().currentUserName(),
						"zimbraPrefReadingPaneLocation"),
				"Verifying whether db value set properly or not for this account (zimbraPrefReadingPaneLocation)");
		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneAtBottom));

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "dataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void readingPaneOff(String from, String to, String cc, String bcc,
			String subject, String body, String method) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		to = ClientSessionFactory.session().currentUserName();
		String recipients[] = { to };
		LmtpUtil.injectMessage(from, recipients, cc, subject, body);
		MailApp.ClickCheckMailUntilMailShowsUp(
				replaceUserNameInStaticId(page.zMailApp.zInboxFldr), subject);

		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneOnRight));

		if (method.equals("byNormalMethod")) {
			obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
			obj.zMenuItem.zClick(localize(locator.readingPaneOff));
		}

		if (method.equals("byKeyboardShortCut")) {
			ClientSessionFactory.session().selenium().windowFocus();
			SleepUtil.sleep(3000);
			Robot zRobot = new Robot();
			zRobot.keyPress(KeyEvent.VK_M);
			zRobot.keyRelease(KeyEvent.VK_M);
			zRobot.keyPress(KeyEvent.VK_P);
			zRobot.keyRelease(KeyEvent.VK_P);
			zRobot.keyPress(KeyEvent.VK_O);
			zRobot.keyRelease(KeyEvent.VK_O);
			SleepUtil.sleep(2000);
		}

		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(1500);
		obj.zMessageItem.zVerifyIsUnRead(subject);
		Boolean isNotVisible1 = ClientSessionFactory.session().selenium().isVisible("id=DWT6");
		Boolean isNotVisible2 = ClientSessionFactory.session().selenium().isVisible("id=DWT7");
		Boolean isSubDblRowClassExists = ClientSessionFactory.session().selenium()
				.isElementPresent("class=SubjectDoubleRow");
		assertReport("false", isNotVisible1.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isNotVisible2.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("false", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3000);
		assertReport("false", isNotVisible1.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isNotVisible2.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("false", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");
		assertReport(
				"off",
				Stafzmprov.getAccountPreferenceValue(ClientSessionFactory.session().currentUserName(),
						"zimbraPrefReadingPaneLocation"),
				"Verifying whether db value set properly or not for this account (zimbraPrefReadingPaneLocation)");

		ClientSessionFactory.session().selenium().click("link=" + localize(locator.logOff));
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(to);
		SleepUtil.sleep(3000);
		assertReport("false", isNotVisible1.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isNotVisible2.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("false", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");
		assertReport(
				"off",
				Stafzmprov.getAccountPreferenceValue(ClientSessionFactory.session().currentUserName(),
						"zimbraPrefReadingPaneLocation"),
				"Verifying whether db value set properly or not for this account (zimbraPrefReadingPaneLocation)");
		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneAtBottom));

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "dataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void readingPaneOnBottom(String from, String to, String cc,
			String bcc, String subject, String body, String method)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		to = ClientSessionFactory.session().currentUserName();
		String recipients[] = { to };
		LmtpUtil.injectMessage(from, recipients, cc, subject, body);
		MailApp.ClickCheckMailUntilMailShowsUp(
				replaceUserNameInStaticId(page.zMailApp.zInboxFldr), subject);

		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneOff));

		if (method.equals("byNormalMethod")) {
			obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
			obj.zMenuItem.zClick(localize(locator.readingPaneAtBottom));
		}

		if (method.equals("byKeyboardShortCut")) {
			ClientSessionFactory.session().selenium().windowFocus();
			SleepUtil.sleep(3000);
			Robot zRobot = new Robot();
			zRobot.keyPress(KeyEvent.VK_M);
			zRobot.keyRelease(KeyEvent.VK_M);
			zRobot.keyPress(KeyEvent.VK_P);
			zRobot.keyRelease(KeyEvent.VK_P);
			zRobot.keyPress(KeyEvent.VK_B);
			zRobot.keyRelease(KeyEvent.VK_B);
			SleepUtil.sleep(2000);
		}

		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(1500);
		obj.zMessageItem.zVerifyIsRead(subject);
		Boolean isNotVisible = ClientSessionFactory.session().selenium().isVisible("id=DWT6");
		Boolean isVisible = ClientSessionFactory.session().selenium().isVisible("id=DWT7");
		Boolean isSubDblRowClassExists = ClientSessionFactory.session().selenium()
				.isElementPresent("class=SubjectDoubleRow");
		assertReport("false", isNotVisible.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("true", isVisible.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3000);
		assertReport("false", isNotVisible.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("true", isVisible.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");
		assertReport(
				"bottom",
				Stafzmprov.getAccountPreferenceValue(ClientSessionFactory.session().currentUserName(),
						"zimbraPrefReadingPaneLocation"),
				"Verifying whether db value set properly or not for this account (zimbraPrefReadingPaneLocation)");

		ClientSessionFactory.session().selenium().click("link=" + localize(locator.logOff));
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(to);
		SleepUtil.sleep(3000);
		assertReport("false", isNotVisible.toString(),
				"Verifying whether horizontal scrollbar exists or not");
		assertReport("true", isVisible.toString(),
				"Verifying whether vertical scrollbar exists or not");
		assertReport("false", isSubDblRowClassExists.toString(),
				"Verifying subject double row view for reading pane on right");
		assertReport(
				"bottom",
				Stafzmprov.getAccountPreferenceValue(ClientSessionFactory.session().currentUserName(),
						"zimbraPrefReadingPaneLocation"),
				"Verifying whether db value set properly or not for this account (zimbraPrefReadingPaneLocation)");
		obj.zButtonMenu.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneAtBottom));

		SelNGBase.needReset.set(false);
	}
}