package com.zimbra.qa.selenium.projects.zcs.tests.mail.messages;

import java.lang.reflect.Method;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpUtil;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;



/**
 * @author Jitesh Sojitra
 */
@SuppressWarnings( { "static-access" })
public class ForwardMultiMessages extends CommonTest {
	public static final String zMailListItemChkBox = "id=zlhi__CLV__se";

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "MailDataProvider")
	protected Object[][] createData(Method method) throws ServiceException, HarnessException {
		return new Object[][] { { Stafzmprov.getRandomAccount(),
				"_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com" } };
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
	/**
	 * Forward multiple messages and verify received message
	 */
	@Test(dataProvider = "MailDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void forwardMultipleMessages_Bug44236(String from, String to,
			String cc, String bcc) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String subject1, subject2, acc1, acc2, fwdSubject, fwdBody;
		acc1 = ClientSessionFactory.session().currentUserName();
		subject1 = "subject1";
		subject2 = "subject2";
		fwdSubject = "fwdSubject";
		fwdBody = "fwdBody";
		acc2 = Stafzmprov.getRandomAccount();
		String subject[] = { subject1, subject2 };
		String body[] = { "body1", "body2" };
		for (int i = 0; i <= 1; i++) {
			commonInjectMessage(from, to, cc, bcc, subject[i], body[i]);
		}
		SleepUtil.sleep(1500);
		obj.zMessageItem.zCtrlClick(subject1);
		obj.zMessageItem.zCtrlClick(subject2);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zMailApp.zForwardIconBtn);
		obj.zTextAreaField.zType(page.zComposeView.zToField, acc2);
		obj.zEditField.zType(page.zComposeView.zSubjectField, fwdSubject);
		obj.zEditor.zType(fwdBody);
		obj.zButton.zClick(page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(2500);

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(acc2);
		page.zMailApp.ClickCheckMailUntilMailShowsUp(fwdSubject);
		obj.zMessageItem.zClick(fwdSubject);
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().click("link=subject1");
		SleepUtil.sleep(5000);
		ClientSessionFactory.session().selenium().selectWindow("_blank");
		String msgBody = null;
		msgBody = ClientSessionFactory.session().selenium().getBodyText();
		assertReport(msgBody, localize(locator.from), "Verifying From header");
		assertReport(msgBody, localize(locator.to), "Verifying To header");
		assertReport(msgBody, localize(locator.cc), "Verifying Cc header");
		assertReport(msgBody.toLowerCase(), from.toLowerCase(),
				"Verifying From field value");
		assertReport(msgBody.toLowerCase(), acc1.toLowerCase(),
				"Verifying To field value");
		assertReport(msgBody.toLowerCase(), "ccuser@testdomain.com",
				"Verifying Cc field value");
		assertReport(msgBody, subject1, "Verifying Subject field value");
		Assert.assertTrue(ClientSessionFactory.session().selenium().isElementPresent(
				"//html/body[contains(text(), 'body1')]"));
		Assert
				.assertFalse(msgBody.contains("HTTP ERROR: 404"),
						"Clicking to 'View entire message' link throws HTTP ERROR: 404");
		Assert
				.assertFalse(
						msgBody
								.contains("The page you were trying to access does not exist."),
						"Clicking to 'View entire message' link doesn't open message");
		Assert
				.assertFalse(msgBody.contains("Internal Server Error"),
						"Verifying message data after clicking to 'View entire message' link");
		obj.zButton.zClick(page.zMailApp.zCloseIconBtn_newWindow);
		ClientSessionFactory.session().selenium().selectWindow(null);

		ClientSessionFactory.session().selenium().click("link=subject2");
		SleepUtil.sleep(5000);
		ClientSessionFactory.session().selenium().selectWindow("_blank");
		msgBody = ClientSessionFactory.session().selenium().getBodyText();
		assertReport(msgBody, localize(locator.from), "Verifying From header");
		assertReport(msgBody, localize(locator.to), "Verifying To header");
		assertReport(msgBody, localize(locator.cc), "Verifying Cc header");
		assertReport(msgBody.toLowerCase(), from.toLowerCase(),
				"Verifying From field value");
		assertReport(msgBody.toLowerCase(), acc1.toLowerCase(),
				"Verifying To field value");
		assertReport(msgBody.toLowerCase(), "ccuser@testdomain.com",
				"Verifying Cc field value");
		assertReport(msgBody, subject2, "Verifying Subject field value");
		Assert.assertTrue(ClientSessionFactory.session().selenium().isElementPresent(
				"//html/body[contains(text(), 'body2')]"));
		Assert
				.assertFalse(msgBody.contains("HTTP ERROR: 404"),
						"Clicking to 'View entire message' link throws HTTP ERROR: 404");
		Assert
				.assertFalse(
						msgBody
								.contains("The page you were trying to access does not exist."),
						"Clicking to 'View entire message' link doesn't open message");
		Assert
				.assertFalse(msgBody.contains("Internal Server Error"),
						"Verifying message data after clicking to 'View entire message' link");
		obj.zButton.zClick(page.zMailApp.zCloseIconBtn_newWindow);
		ClientSessionFactory.session().selenium().selectWindow(null);

		SelNGBase.needReset.set(false);
	}

	private void commonInjectMessage(String from, String to, String cc,
			String bcc, String subject, String body) throws Exception {
		to = ClientSessionFactory.session().currentUserName();
		String[] recipients = { to };
		LmtpUtil.injectMessage(from, recipients, cc, subject, body);
		MailApp.ClickCheckMailUntilMailShowsUp(
				replaceUserNameInStaticId(page.zMailApp.zInboxFldr), subject);
		obj.zFolder.zClick(replaceUserNameInStaticId(page.zMailApp.zInboxFldr));
	}
}