package com.zimbra.qa.selenium.projects.zcs.tests.mail.messages.readreceipt;

import java.lang.reflect.Method;

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
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;




/**
 * @author Jitesh Sojitra
 */
@SuppressWarnings("static-access")
public class ReadReceipt extends CommonTest {

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "mailDataProvider")
	public Object[][] createData(Method method) throws ServiceException, HarnessException {
		String test = method.getName();
		if (test.equals("neverSendReadReceipt")
				|| test.equals("alwaysSendReadReceipt")
				|| test.equals("askMeForReadReceipt")) {
			return new Object[][] { { Stafzmprov.getRandomAccount(),
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					getLocalizedData(5), getLocalizedData(5), "" } };
		} else if (test.equals("unwantedReadReceiptDlgWhileMarkRead_Bug41499")) {
			return new Object[][] { { Stafzmprov.getRandomAccount(),
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					getLocalizedData(1), getLocalizedData(5), "" } };
		} else {
			return new Object[][] { { Stafzmprov.getRandomAccount(),
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					getLocalizedData(5), getLocalizedData(5), "" } };
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
	@Test(dataProvider = "mailDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void neverSendReadReceipt(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String currentloggedinuser = ClientSessionFactory.session().currentUserName();
		Stafzmprov.modifyAccount(currentloggedinuser,
				"zimbraPrefMailSendReadReceipts", "never");

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(to);
		page.zComposeView.zNavigateToMailCompose();
		obj.zButtonMenu.zClick(page.zComposeView.zOptionsDownArrowBtn);
		obj.zMenuItem.zClick(page.zComposeView.zRequestReadReceiptMenuItem);
		page.zComposeView.zEnterComposeValues(currentloggedinuser, cc, bcc,
				subject, body, attachments);
		obj.zButton.zClick(page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(2000);

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(currentloggedinuser);
		page.zMailApp.ClickCheckMailUntilMailShowsUp(subject);
		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(3000);
		obj.zDialog.zNotExists(localize(locator.warningMsg));

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "mailDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void alwaysSendReadReceipt(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String currentloggedinuser = ClientSessionFactory.session().currentUserName();
		Stafzmprov.modifyAccount(currentloggedinuser,
				"zimbraPrefMailSendReadReceipts", "always");

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(to);
		page.zComposeView.zNavigateToMailCompose();
		obj.zButtonMenu.zClick(page.zComposeView.zOptionsDownArrowBtn);
		obj.zMenuItem.zClick(page.zComposeView.zRequestReadReceiptMenuItem);
		page.zComposeView.zEnterComposeValues(currentloggedinuser, cc, bcc,
				subject, body, attachments);
		obj.zButton.zClick(page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(2000);

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(currentloggedinuser);
		page.zMailApp.ClickCheckMailUntilMailShowsUp(subject);
		obj.zMessageItem.zClick(subject);
		assertReport(localize(locator.readReceiptSent), obj.zToastAlertMessage
				.zGetMsg(), "Verifying toast message for sending read receipt");

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(to);
		page.zMailApp
				.ClickCheckMailUntilMailShowsUp("Read-Receipt: " + subject);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "mailDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void askMeForReadReceiptAndBug36344(String to, String cc,
			String bcc, String subject, String body, String attachments)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String currentloggedinuser = ClientSessionFactory.session().currentUserName();
		Stafzmprov.modifyAccount(currentloggedinuser,
				"zimbraPrefMailSendReadReceipts", "prompt");

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(to);
		page.zComposeView.zNavigateToMailCompose();
		obj.zButtonMenu.zClick(page.zComposeView.zOptionsDownArrowBtn);
		obj.zMenuItem.zClick(page.zComposeView.zRequestReadReceiptMenuItem);
		page.zComposeView.zEnterComposeValues(currentloggedinuser, cc, bcc,
				subject, body, attachments);
		obj.zButton.zClick(page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(2000);
		obj.zFolder.zClick(page.zMailApp.zSentFldr);
		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(2000);
		obj.zDialog.zNotExists(localize(locator.warningMsg));

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(currentloggedinuser);
		page.zMailApp.ClickCheckMailUntilMailShowsUp(subject);
		obj.zMessageItem.zClick(subject);
		zWaitTillObjectExist("dialog", localize(locator.warningMsg));
		if (!ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			assertReport(localize(locator.readReceiptSend).replaceAll("<br>",
					""), obj.zDialog.zGetMessage(localize(locator.warningMsg)),
					"Verifying dialog text for notifying read receipt");
		}
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.warningMsg));
		assertReport(localize(locator.readReceiptSent), obj.zToastAlertMessage
				.zGetMsg(), "Verifying toast message for sending read receipt");

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(to);
		page.zMailApp
				.ClickCheckMailUntilMailShowsUp("Read-Receipt: " + subject);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "mailDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void unwantedReadReceiptDlgWhileMarkRead_Bug41499(String to,
			String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		zGoToApplication("Preferences");
		zGoToPreferences("Mail");
		obj.zRadioBtn.zClick(localize(locator.messageReadNone));
		obj.zRadioBtn.zClick(localize(locator.readReceiptAsk));
		obj.zButton.zClick("id=zb__PREF__SAVE_left_icon");
		SleepUtil.sleep(1000);
		zGoToApplication("Mail");
		to = ClientSessionFactory.session().currentUserName();
		String[] recipients = { ClientSessionFactory.session().currentUserName() };
		LmtpUtil.injectMessage(ClientSessionFactory.session().currentUserName(), recipients, cc,
				subject, body);
		MailApp.ClickCheckMailUntilMailShowsUp(
				replaceUserNameInStaticId(page.zMailApp.zInboxFldr), subject);
		obj.zMessageItem.zRtClick(subject);
		obj.zMenuItem.zClick(localize(locator.markAsRead));
		SleepUtil.sleep(1000);
		obj.zDialog.zNotExists(localize(locator.warningMsg));
		obj.zDialog.zNotExists(localize(locator.infoMsg));

		SelNGBase.needReset.set(false);
	}
}