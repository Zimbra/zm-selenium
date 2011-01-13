package com.zimbra.qa.selenium.projects.zcs.tests.mail.compose.newwindow;

import java.lang.reflect.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ComposeView;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;



@SuppressWarnings("static-access")
public class ReplyMail extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "composeDataProvider")
	public Object[][] createData(Method method) {
		String test = method.getName();
		if (test.equals("replyHtmlMail_NewWindow")
				|| test.equals("replyPlainTextMail_NewWindow")) {
			return new Object[][] { { "_selfAccountName_",
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					getLocalizedData(2), getLocalizedData(5), "" } };
		} else {
			return new Object[][] { { "" }, };
		}

	}

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.zLogin();
	}

	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	/**
	 * Test: Reply to an email in html-mode and in new-window and verify if the
	 * mail-compose and verify that cc and bcc is empty,to is filled, subject
	 * has Re appended
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void replyHtmlMail_NewWindow(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefComposeFormat", "html");
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(2500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");

		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndSelectIt("_selfAccountName_", cc,
				bcc, subject, body, attachments);
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.byMessage));
		obj.zButton.zClick(MailApp.zReplyIconBtn);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zMailApp.zDetachBtn_ComposedMessage);
		SleepUtil.sleep(3000);
		ClientSessionFactory.session().selenium().selectWindow("_blank");
		obj.zButton.zExists(ComposeView.zSendIconBtn);
		page.zComposeView.zVerifyComposeFilledValues("Reply", "", "", "",
				"Re: " + subject, body, attachments);
		ClientSessionFactory.session().selenium().selectWindow(null);
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.byConversation));

		SelNGBase.needReset.set(false);
	}

	/**
	 * Test: Reply to an email in plain text mode and in new-window and verify
	 * if the mail-compose and verify that cc and bcc is empty,to is filled,
	 * subject has Re appended
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void replyPlainTextMail_NewWindow(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefComposeFormat", "text");
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(2500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");

		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndSelectIt("_selfAccountName_", cc,
				bcc, subject, body, attachments);
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.byMessage));
		obj.zButton.zClick(MailApp.zReplyIconBtn);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zMailApp.zDetachBtn_ComposedMessage);
		SleepUtil.sleep(3000);
		ClientSessionFactory.session().selenium().selectWindow("_blank");
		obj.zButton.zExists(ComposeView.zSendIconBtn);
		page.zComposeView.zVerifyComposeFilledValues("Reply", "", "", "",
				"Re: " + subject, body, attachments);
		ClientSessionFactory.session().selenium().selectWindow(null);
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.byConversation));

		SelNGBase.needReset.set(false);
	}
}