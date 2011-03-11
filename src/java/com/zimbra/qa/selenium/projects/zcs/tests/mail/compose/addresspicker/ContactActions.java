package com.zimbra.qa.selenium.projects.zcs.tests.mail.compose.addresspicker;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ActionMethod;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;



/**
 * @author Prashant Jaiswal
 * 
 */
@SuppressWarnings("static-access")
public class ContactActions extends CommonTest {

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.zLogin();
	}

	/**
	 * test to select To/CC/BCC values in Select Address dlg box and verify them
	 * on mail compose
	 * 
	 */
	@Test(description = "test to select To/CC/BCC values in Select Address dlg box and verify them on mail compose", groups = {
			"smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void addressPicker() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("zh_HK,zh_CN", "na", "",
				"zClickInDlg method clicks to button in wrong dialog");

		// Get the 'select addresses dialog'
		obj.zButton.zClick(page.zABCompose.zMailTabIconBtn);
		page.zComposeView.zNavigateToMailCompose();
		obj.zButton.zClick(localize(locator.toLabel));

		String infoDlgExist;
		infoDlgExist = obj.zDialog.zExistsDontWait(localize(locator.infoMsg));
		if (infoDlgExist.equals("true")) {
			obj.zButton.zClickInDlgByName(localize(locator.ok),
					localize(locator.infoMsg), "2");
		}

		obj.zDialog.zExists(localize(locator.selectAddresses));
		obj.zEditField.zActivateInDlg(localize(locator.search), "");

		for (int i = 1; i <= 3; i++) {

			// Create a new account to be the destination
			String emailAddress = "ac" + i + "@testdomain.com";
			Stafzmprov.createAccount(emailAddress);
			SleepUtil.sleep(1000);

			// Search for the account
			obj.zEditField.zTypeInDlg(localize(locator.search), emailAddress);
			obj.zButton.zClickInDlg(localize(locator.search));

			// Add the account to the To, CC, and Bcc fields
			switch (i) {
			case 1:
				obj.zButton.zClickInDlg(localize(locator.to));
				break;
			case 2:
				obj.zButton.zClickInDlg(localize(locator.cc));
				break;
			default:
				obj.zButton.zClickInDlg(localize(locator.bcc));
				break;
			}
		}

		obj.zButton.zClickInDlg(localize(locator.ok));
		Assert.assertTrue(obj.zTextAreaField.zGetInnerText(
				localize(locator.toLabel)).contains("ac1@testdomain.com"),
				"Verify that the To field ("
						+ obj.zTextAreaField
								.zGetInnerText(localize(locator.toLabel))
						+ ") contains ac1@testdomain.com");

		Assert.assertTrue(obj.zTextAreaField.zGetInnerText(
				localize(locator.ccLabel)).contains("ac2@testdomain.com"),
				"Verify that the Cc field ("
						+ obj.zTextAreaField
								.zGetInnerText(localize(locator.ccLabel))
						+ ") contains ac2@testdomain.com");

		Assert.assertTrue(obj.zTextAreaField.zGetInnerText(
				localize(locator.bccLabel)).contains("ac3@testdomain.com"),
				"Verify that the Bcc field ("
						+ obj.zTextAreaField
								.zGetInnerText(localize(locator.bccLabel))
						+ ") contains ac3@testdomain.com");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Test related to address picker w.r.t. bug 20969
	 * 
	 */
	@Test(description = "Test related to address picker w.r.t. bug 20969", groups = {
			"smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void addressPicker_Bug20969() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("zh_HK,zh_CN", "na", "",
				"zClickInDlg method clicks to button in wrong dialog");

		obj.zButton.zClick(page.zABCompose.zMailTabIconBtn);
		page.zComposeView.zNavigateToMailCompose();
		obj.zButton.zClick(localize(locator.toLabel));

		String infoDlgExist;
		SleepUtil.sleep(3000);
		infoDlgExist = obj.zDialog.zExistsDontWait(localize(locator.infoMsg));
		if (infoDlgExist.equals("true")) {
			obj.zButton.zClickInDlg(localize(locator.ok), "2");
			SleepUtil.sleep(1000);
		}

		obj.zDialog.zExists(localize(locator.selectAddresses));
		obj.zEditField.zActivateInDlg(localize(locator.search), "");
		for (int i = 1; i <= 3; i++) {
			Stafzmprov.createAccount("ab" + i + "@testdomain.com");
			obj.zEditField.zTypeInDlg(localize(locator.search), "ab" + i
					+ "@testdomain.com");
			obj.zButton.zClickInDlg(localize(locator.search));
			if (ClientSessionFactory.session().currentBrowserName().contains("Safari")) {
				obj.zButton.zClickInDlg(localize(locator.search));
			}
			if (i == 1) {
				obj.zButton.zClickInDlg(localize(locator.to));
			} else if (i == 2) {
				obj.zButton.zClickInDlg(localize(locator.cc));
			} else if (i == 3) {
				obj.zButton.zClickInDlg(localize(locator.search));
				obj.zButton.zClickInDlg(localize(locator.bcc));
			}
		}

		obj.zButton.zClickInDlg(localize(locator.ok));
		obj.zTextAreaField.zType(localize(locator.toLabel),
				"add@testdomain.com");
		obj.zButton.zClick(localize(locator.toLabel));
		SleepUtil.sleep(3000);
		infoDlgExist = obj.zDialog.zExistsDontWait(localize(locator.infoMsg));
		if (infoDlgExist.equals("true")) {
			obj.zButton.zClickInDlg(localize(locator.ok), "2");
			SleepUtil.sleep(1000);
		}

		obj.zListItem.zClickItemInSpecificList(localize(locator.bcc), "2");
		obj.zButton.zClickInDlg(localize(locator.remove));
		obj.zListItem.zVerifyItemInSpecificListInDlgNotExist(
				localize(locator.bcc), "", "2");
		SleepUtil.sleep(2000);
		infoDlgExist = obj.zDialog
				.zExistsDontWait(localize(locator.selectAddresses));
		if (infoDlgExist.equals("true")) {
			obj.zButton.zClickInDlg(localize(locator.ok));
			SleepUtil.sleep(1000);
		}
		obj.zFolder.zClick(replaceUserNameInStaticId(page.zMailApp.zInboxFldr));

		SelNGBase.needReset.set(false);
	}

	/**
	 * Test to check the new mail right click "Add to Contacts"
	 * functionality.This test performed within the same account login in order
	 * avoid login to a another account.So this test sends the mail to self
	 * account and adds it to contacts
	 * 
	 */
	@Test(description = "Test to check the new mail right click 'Add to Contacts' functionality", groups = {
			"smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void contextAddToContacts() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String subject = getLocalizedData_NoSpecialChar();
		String mailBody = getLocalizedData_NoSpecialChar();

		String contactName = null;
		String[] fromAccount = ClientSessionFactory.session().currentUserName().split("@");
		String[] firstAndLastName = fromAccount[0].split("_");
		String contactFirstName = firstAndLastName[0];
		String contactLastName = firstAndLastName[firstAndLastName.length - 1];
		contactName = contactLastName + ", " + contactFirstName;
		fromAccount[0] = fromAccount[0].replace("_", " ");
		fromAccount[0] = fromAccount[0].toLowerCase();
		obj.zButton.zClick(page.zABCompose.zMailTabIconBtn);
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndSelectIt(
				ClientSessionFactory.session().currentUserName(),
				"", "", subject, mailBody, "");

		MailApp.ClickCheckMailUntilMailShowsUp("", subject);
		obj.zMessageItem.zClick(subject);
		SelNGBase.actOnLabel = true;
		obj.zMessageItem.zRtClick(fromAccount[0]);
		SelNGBase.actOnLabel = false;// reset the flag
		obj.zMenuItem.zClick(localize(locator.AB_ADD_CONTACT));
		obj.zButton.zClick(localize(locator.save), "2");
		obj.zToastAlertMessage.zAlertMsgExists(
				localize(locator.contactCreated),
				"Contact should be created in Address Book");

		page.zABCompose.navigateTo(ActionMethod.DEFAULT);
		obj.zFolder.zClick(localize(locator.contacts));
		if (ZimbraSeleniumProperties.getStringProperty("locale").equals("ar")) {
			obj.zContactListItem.zExists(contactName.substring(0, 5));
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals(
				"zh_HK")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"ja")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"ko")) {
			obj.zContactListItem.zExists(contactLastName);
		} else {
			obj.zContactListItem.zExists(contactName);
		}

		SelNGBase.needReset.set(false);
	}
}