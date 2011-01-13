package com.zimbra.qa.selenium.projects.zcs.tests.mail.compose;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ComposeView;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;



@SuppressWarnings("static-access")
public class Drafts extends CommonTest {
	private String draftSubject;
	private String draftBody;
	private String modifiedSubject;
	private String modifiedBody;
	private String autoDraftSubject;
	private String autoDraftBody;
	private String autoDraftAndSendSubject;
	protected static String showOrignalBodyText = "";

	public Drafts() {
		draftSubject = getLocalizedData(1);
		draftBody = getLocalizedData(5);
		modifiedSubject = "modified " + getLocalizedData(1);
		modifiedBody = "modified " + getLocalizedData(5);
		autoDraftSubject = "autoDraft " + getLocalizedData(1);
		autoDraftBody = "autoDraft " + getLocalizedData(5);
		autoDraftAndSendSubject = "autoDraftAndSend " + getLocalizedData(1);
	}

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "draftsDataProvider")
	public Object[][] createData(Method method) {
		String test = method.getName();
		if (test.equals("modifyDraftAndVerify")) {
			return new Object[][] { { "_selfAccountName_",
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					modifiedSubject, modifiedBody, "data/public/other/testexcelfile.xls" } };
		}
		if (test.equals("autoDraftAndSendMailToSelfInNewWindow")) {
			return new Object[][] { { "_selfAccountName_",
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					autoDraftAndSendSubject, autoDraftBody, "" } };
		}
		if (test.equals("goToDraftAndVerifyAutodraftMsg")
				|| test.equals("autoSaveDraftAfter30Sec")) {
			return new Object[][] { { "_selfAccountName_",
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					autoDraftSubject, autoDraftBody, "" } };
		}

		return new Object[][] { { "_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", draftSubject, draftBody, "" } };
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
	 * Test if canceling after entering some value in mail compose asks for save
	 * draft
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void cancelComposeAsksForSavingDraft(String to, String cc,
			String bcc, String subject, String body, String attachments)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zEnterComposeValues(to, cc, bcc, subject, body,
				attachments);
		obj.zButton.zClick(ComposeView.zCancelIconBtn);
		obj.zDialog.zVerifyAlertMessage(localize(locator.warningMsg),
				localize(locator.askSaveDraft));
		obj.zButton.zClickInDlg(localize(locator.no));
		SelNGBase.needReset.set(false);
	}

	// =================================================
	// DRAFT, EDIT DRAFT, VERIFY DRAFT RELATED TESTS...
	// ==================================================
	/**
	 * Test clicking save-draft shows a toast(green message) indicating draft
	 * was saved
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void saveDraftSaysDraftSaved(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zEnterComposeValues(to, cc, bcc, subject, body,
				attachments);
		obj.zButton.zClick(ComposeView.zSaveDraftsIconBtn);
		obj.zToastAlertMessage.zAlertMsgExists(localize(locator.draftSaved),
				"Draft Saved -msg should be shown");
		SelNGBase.needReset.set(false);
	}

	/**
	 * Test: After draft was saved, cancel shouldn't ask
	 * "do you want to save"-again dependsOn: saveDraftSaysDraftSaved
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void saveDraftNoAlertOnCancel(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		saveDraftNoAlertOnCancel_retry();
		obj.zButton.zClick(ComposeView.zCancelIconBtn);
		obj.zDialog.zNotExists(localize(locator.warningMsg));
		SelNGBase.needReset.set(false);
	}

	/**
	 * Test: Open a draft by clicking on Edit button, then verify if all the
	 * values that were entered earlier(during compose) are intact dependsOn:
	 * saveDraftNoAlertOnCancel
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void goToDraftAndEdit(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		goToDraftAndEdit_retry();
		obj.zFolder.zClick(localize(locator.drafts));
		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(500);
		obj.zButton.zClick(MailApp.zEditDraftIconBtn);
		obj.zButton.zExists(ComposeView.zSendIconBtn);
		page.zComposeView.zVerifyComposeFilledValues("Edit draft", to, cc, bcc,
				subject, body, attachments);
		SelNGBase.needReset.set(false);
	}

	/**
	 * Test: Modify a drafted-message for the 2nd time, modify its contents,
	 * send the mail Verify if the mail has the new-content(and not the
	 * old-drafted content) dependsOn: goToDraftAndEdit
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void modifyDraftAndVerify(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		modifyDraftAndVerify_retry();
		SleepUtil.sleep(500);
		obj.zButton.zClick(MailApp.zEditDraftIconBtn);
		obj.zButton.zWait(ComposeView.zSendIconBtn);
		page.zComposeView.zSendMailToSelfAndVerify(to, cc, bcc,
				modifiedSubject, modifiedBody, attachments);
		SelNGBase.needReset.set(false);
	}

	// =============================
	// AUTO DRAFTS RELATED TESTS...
	// =============================
	/**
	 * Test: Open mail compose, enter something, wait at-least for 30seconds
	 * Verify if auto-draft was saved toast(green-message) is displayed
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void autoSaveDraftAfter30Sec(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zEnterComposeValues(to, cc, bcc, subject, body,
				attachments);
		SleepUtil.sleep(35000);// auto save happens every 30s
		// message has timestamp within it which is difficult to catch.
		// and shows up at different places in different locale.
		// so splitting the msg before & after and verifying them saperately
		String[] tmp = localize(locator.draftSavedAuto).split("{0}");
		obj.zToastAlertMessage
				.zAlertMsgExists(tmp[0],
						"1st part of the auto-draft msg(after timestamp) not isnt proper");
		obj.zToastAlertMessage
				.zAlertMsgExists(tmp[1],
						"2nd part of the auto-draft msg(after timestamp) not isnt proper");
		SelNGBase.needReset.set(false);
	}

	/**
	 * Test: If we click on Inbox(or somewhere else) after auto-draft is done,
	 * verify if "Message was auto-saved, do you wish to keep it?"-dialog is
	 * thrown
	 */
	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void askSaveAutosavedDraftWhenInboxIsClicked() throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		askSaveAutosavedDraftWhenInboxIsClicked_retry();
		obj.zFolder.zClick(page.zMailApp.zInboxFldr);
		obj.zDialog.zNotExists(localize(locator.warningMsg));
		SelNGBase.needReset.set(false);
	}

	/**
	 * Test: After an auto-draft was saved, verify it really exists in
	 * drafts-folder dependsOn: askSaveAutosavedDraftWhenInboxIsClicked
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void goToDraftAndVerifyAutodraftMsg(String to, String cc,
			String bcc, String subject, String body, String attachments)
			throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		goToDraftAndVerifyAutodraftMsg_retry();

		obj.zFolder.zClick(localize(locator.drafts));
		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(500);
		obj.zButton.zClick(MailApp.zEditDraftIconBtn);
		obj.zButton.zExists(ComposeView.zSendIconBtn);
		page.zComposeView.zVerifyComposeFilledValues("Edit Auto-draft", to, cc,
				bcc, subject, body, attachments);
		SelNGBase.needReset.set(false);
	}

	// =============================
	// Drafts in new window...
	// =============================
	/**
	 * Test: Complete auto-draft scenario in New-window. Compose an email in
	 * New-window, wait for auto-draft to kick in, hit-send. verify if the mail
	 * was sent with all the contents.
	 */
	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void autoDraftAndSendMailToSelfInNewWindow(String to, String cc,
			String bcc, String subject, String body, String attachments)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException(
				"na",
				"SF",
				"39446",
				"New window goes blank while typing SHIFT C suddenly after login to web client (SF only)");

		page.zComposeView.zNavigateToComposeByShiftClick();
		page.zComposeView.zEnterComposeValues("", "", "",
				"this subject will be replaced after 30 secs",
				"this body will be replaced after 30 sec", attachments);
		SleepUtil.sleep(35000);// auto save happens every 30s
		String toastMsg = localize(locator.draftSaved);
		obj.zToastAlertMessage.zAlertMsgExists(toastMsg,
				"Draft saved- message not shown");
		page.zComposeView.zSendMailToSelfAndSelectIt(to, cc, bcc, subject,
				body, attachments);
		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void htmlDraftOpenAsPlainText_Bug24431(String to, String cc,
			String bcc, String subject, String body, String attachments)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		zGoToApplication("Preferences");
		zGoToPreferences("Composing");
		obj.zRadioBtn.zClick(localize(locator.composeAsHTML));
		obj.zButton.zClick("id=zb__PREF__SAVE_left_icon");
		SleepUtil.sleep(2000);
		zGoToApplication("Mail");
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneOff));
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zEnterComposeValues("to@testdomain.com",
				"cc@testdomain.com", "bcc@testdomain.com",
				"Bug24431 save draft(html format) subject",
				"Bug24431 save draft(html format) body", "");
		obj.zButton.zClick(page.zComposeView.zSaveDraftsIconBtn);
		SleepUtil.sleep(1500);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		obj.zFolder.zClick(page.zMailApp.zDraftsFldr);
		obj.zMessageItem.zRtClick("Bug24431 save draft(html format) subject");
		obj.zMenuItem.zClick(localize(locator.showOrig));
		SleepUtil.sleep(3000);
		ClientSessionFactory.session().selenium().selectWindow("_blank");
		String messageBody = ClientSessionFactory.session().selenium().getBodyText();
		Boolean htmlBodyExists = messageBody
				.contains("<html><head><style type='text/css'>");
		assertReport("true", htmlBodyExists.toString(),
				"Verifying html formatting in show original");
		ClientSessionFactory.session().selenium().selectWindow(null);
		obj.zMessageItem.zClick("Bug24431 save draft(html format) subject");
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			// IE 8 requires this click event that is why we again wrote same
			// line here
			obj.zMessageItem.zClick("Bug24431 save draft(html format) subject");
		}
		obj.zButton.zClick(page.zMailApp.zEditDraftIconBtn);
		obj.zButton.zExists("ImgBold");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		resetSession();
		page.zLoginpage.zReloginToAjax();
		obj.zFolder.zClick(page.zMailApp.zDraftsFldr);
		obj.zMessageItem.zRtClick("Bug24431 save draft(html format) subject");
		obj.zMenuItem.zClick(localize(locator.showOrig));
		SleepUtil.sleep(3000);
		ClientSessionFactory.session().selenium().selectWindow("_blank");
		messageBody = ClientSessionFactory.session().selenium().getBodyText();
		htmlBodyExists = messageBody
				.contains("<html><head><style type='text/css'>");
		assertReport("true", htmlBodyExists.toString(),
				"Verifying html formatting in show original");
		ClientSessionFactory.session().selenium().selectWindow(null);
		obj.zMessageItem.zClick("Bug24431 save draft(html format) subject");
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			// IE 8 requires this click event that is why we again wrote same
			// line here
			obj.zMessageItem.zClick("Bug24431 save draft(html format) subject");
		}
		obj.zButton.zClick(page.zMailApp.zEditDraftIconBtn);
		obj.zButton.zExists("ImgBold");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			obj.zDialog.zVerifyAlertMessage(localize(locator.warningMsg),
					localize(locator.askSaveDraft));
			obj.zButton.zClickInDlg(localize(locator.no));
		}
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.readingPaneAtBottom));

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "draftsDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void draftLoosesHtmlFormatting_Bug34870(String to, String cc,
			String bcc, String subject, String body, String attachments)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		zGoToApplication("Preferences");
		zGoToPreferences("Composing");
		obj.zRadioBtn.zClick(localize(locator.composeAsHTML));
		zGoToPreferences("Mail");
		obj.zRadioBtn.zClick(localize(locator.composeAsText));
		obj.zButton.zClick("id=zb__PREF__SAVE_left_icon");
		SleepUtil.sleep(2000);
		zGoToApplication("Mail");
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zEnterComposeValues("to@testdomain.com",
				"cc@testdomain.com", "bcc@testdomain.com",
				"Bug34870 save draft(html format) subject",
				"Bug34870 save draft(html format) body", "");
		obj.zButton.zClick(page.zComposeView.zSaveDraftsIconBtn);
		SleepUtil.sleep(1500);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		obj.zFolder.zClick(page.zMailApp.zDraftsFldr);
		obj.zMessageItem.zRtClick("Bug34870 save draft(html format) subject");
		obj.zMenuItem.zClick(localize(locator.showOrig));
		SleepUtil.sleep(3000);
		ClientSessionFactory.session().selenium().selectWindow("_blank");
		String messageBody = ClientSessionFactory.session().selenium().getBodyText();
		Boolean htmlBodyExists = messageBody
				.contains("<html><head><style type='text/css'>");
		assertReport("true", htmlBodyExists.toString(),
				"Verifying html formatting in show original");
		ClientSessionFactory.session().selenium().selectWindow(null);
		obj.zMessageItem.zClick("Bug34870 save draft(html format) subject");
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			// IE 8 requires this is click event that is why we again wrote same
			// line here
			obj.zMessageItem.zClick("Bug34870 save draft(html format) subject");
		}
		ClientSessionFactory.session().selenium().selectFrame(
				"css=iframe[id*='zv__CLV__MSG_body__iframe']");
		Assert.assertTrue(ClientSessionFactory.session().selenium().isElementPresent(
				"xpath=/html/body[contains(@class,'MsgBody MsgBody-text')]"),
				"Reading pane still shows in Html formate");
		ClientSessionFactory.session().selenium().selectFrame("relative=top");
		obj.zMessageItem.zClick("Bug34870 save draft(html format) subject");
		obj.zButton.zClick(page.zMailApp.zEditDraftIconBtn);
		obj.zButton.zExists("ImgBold");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	//--------------------------------------------------------------------------
	// SECTION 4: RETRY-METHODS
	//--------------------------------------------------------------------------

	private void saveDraftNoAlertOnCancel_retry() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		handleRetry();// kill browser and relogin
		saveDraftSaysDraftSaved("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", draftSubject, draftBody, "");

	}

	private void goToDraftAndEdit_retry() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		handleRetry();// kill browser and relogin
		saveDraftSaysDraftSaved("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", draftSubject, draftBody, "");
		saveDraftNoAlertOnCancel("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", draftSubject, draftBody, "");
	}

	private void modifyDraftAndVerify_retry() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		handleRetry();// kill browser and relogin
		saveDraftSaysDraftSaved("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", draftSubject, draftBody, "");
		saveDraftNoAlertOnCancel("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", draftSubject, draftBody, "");
		goToDraftAndEdit("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", draftSubject, draftBody, "");
	}

	private void askSaveAutosavedDraftWhenInboxIsClicked_retry()
			throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		handleRetry();
		autoSaveDraftAfter30Sec("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", autoDraftSubject, autoDraftBody, "");
	}

	private void goToDraftAndVerifyAutodraftMsg_retry() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		handleRetry();
		autoSaveDraftAfter30Sec("_selfAccountName_", "ccuser@testdomain.com",
				"bccuser@testdomain.com", autoDraftSubject, autoDraftBody, "");
		askSaveAutosavedDraftWhenInboxIsClicked();
	}
}
