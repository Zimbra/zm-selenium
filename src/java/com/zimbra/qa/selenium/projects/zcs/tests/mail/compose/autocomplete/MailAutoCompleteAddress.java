package com.zimbra.qa.selenium.projects.zcs.tests.mail.compose.autocomplete;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.ActionMethod;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;


/**
 * @author Jitesh Sojitra
 */
@SuppressWarnings("static-access")
public class MailAutoCompleteAddress extends CommonTest {
	static String acc1, acc2, acc3, acc4, acc5;
	static String first = null, second = null, third = null, fourth = null,
			fifth = null, sixth = null, seventh = null, eighth = null;

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "composeDataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("verifyAutoCompleteAndRankOnlyWithContactsFolder")
				|| test.equals("verifyAutoCompleteAndRankOnlyWithGAL")
				|| test.equals("verifyAutoCompleteAndRankWithContactsAndGAL")
				|| test
						.equals("verifyAutoCompleteAndRankWithContactsAndGALONOFF")
				|| test.equals("verifyAutoCompleteAndRankWithContactsFolder")
				|| test
						.equals("zwcHangsDuringAutoCompOnApostropheChar_Bug45815")
				|| test.equals("autocompleteReturnsTooManyResults_Bug40959")
				|| test.equals("verifyContactGroupAutoComplete_Bug45545")
				|| test.equals("autocompleteOnComma_Bug43179")
				|| test.equals("unableToGetRidOfEmailedContacts_Bug40081")
				|| test.equals("autocompleteOnSpecialCharacters")
				|| test.equals("luceneStopWordsAutoComplete_Bug46718")
				|| test.equals("unableToGetRidOfEmailedContacts_Bug40081")
				|| test
						.equals("galAutoCompleteDoesntWorkAfterPrefChange_Bug45337_Bug37377")
				|| test
						.equals("optionNotToAutoCompleteContactGroupsByMember_Bug44509")
				|| test.equals("autocompleteDoesNotWorkAfterPeriod_Bug47045")
				|| test
						.equals("autocompleteShowsContactFromTrashedABFolders_Bug47044")
				|| test
						.equals("verifyAutocompleteFromContactsSubAndSubSubFolders_Bug47044")
				|| test
						.equals("verifyAutocompleteFromSharedSubAndSubSubFolders_Bug45550")
				|| test
						.equals("verifyAutoCompleteWithLargeAddressBook_1KContacts")) {
			return new Object[][] { { "_selfAccountName_", "_selfAccountName_",
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					"commonsubject", "commonbody", "" } };
		} else if (test
				.equals("forgetAutocompleteWithGALAndEmailedAddressOnOff")) {
			return new Object[][] {
					{ "GALOFFAndEmailedContactsOFF", "_selfAccountName_",
							"_selfAccountName_", "ccuser@testdomain.com",
							"bccuser@testdomain.com", "commonsubject",
							"commonbody", "" },
					{ "GALOFFAndEmailedContactsON", "_selfAccountName_",
							"_selfAccountName_", "ccuser@testdomain.com",
							"bccuser@testdomain.com", "commonsubject",
							"commonbody", "" },
					{ "GALONAndEmailedContactsOFF", "_selfAccountName_",
							"_selfAccountName_", "ccuser@testdomain.com",
							"bccuser@testdomain.com", "commonsubject",
							"commonbody", "" },
					{ "GALONAndEmailedContactsON", "_selfAccountName_",
							"_selfAccountName_", "ccuser@testdomain.com",
							"bccuser@testdomain.com", "commonsubject",
							"commonbody", "" } };
		} else {
			return new Object[][] { { "_selfAccountName_", "_selfAccountName_",
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					"commonsubject", "commonbody", "" } };
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
	 * Verify autocomplete and rank with local Contacts address book folder.
	 * Steps, 1.Create 5 accounts 2.Send 3 mails to account3, 3 mails to
	 * account5, 2 mails to account1, 2 mails to account4 and 1 mail to account2
	 * 3.Verify autocomplete and rank 4.Send few mails to those accounts again
	 * 5.Verify updated autocomplete and rank
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutoCompleteAndRankOnlyWithContactsFolder(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		first = ZimbraSeleniumProperties.getStringProperty("locale").substring(
				0, 1);
		second = ZimbraSeleniumProperties.getStringProperty("locale")
				.substring(1, 2);
		acc1 = Stafzmprov.getRandomAccount();
		acc2 = Stafzmprov.getRandomAccount();
		acc3 = Stafzmprov.getRandomAccount();
		acc4 = Stafzmprov.getRandomAccount();
		acc5 = Stafzmprov.getRandomAccount();
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		sendMails();
		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		pressKeys(first + "," + second);
		verifyAutocomplete(true);

		sendMailsUpdated();
		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		pressKeys(first + "," + second);
		verifyAutocompleteUpdated();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete and rank with local Contacts address book folder.
	 * Steps, 1.Create 5 accounts 2.Send 3 mails to account3, 3 mails to
	 * account5, 2 mails to account1, 2 mails to account4 and 1 mail to account2
	 * 3.Verify autocomplete and rank 4.Send few mails to those accounts again
	 * 5.Verify updated autocomplete and rank
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutoCompleteAndRankOnlyWithContactsFolder_NewWindow(
			String from, String to, String cc, String bcc, String subject,
			String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		first = ZimbraSeleniumProperties.getStringProperty("locale").substring(
				0, 1);
		second = ZimbraSeleniumProperties.getStringProperty("locale")
				.substring(1, 2);
		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "neww1@testdomain.com";
		acc1 = acc1.toLowerCase();
		getKeyboardKeys(acc1);
		Stafzmprov.createAccount(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefGalAutoCompleteEnabled", "TRUE");

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		page.zComposeView.zNavigateToComposeByShiftClick();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_GB")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_AU")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"pt_BR")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_CN")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_HK")) {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth + "," + seventh + "," + eighth);
		} else {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth);
		}
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		for (int i = 0; i <= 9; i++) {
			pressKeys("backspace");
		}
		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_GB")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_AU")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"pt_BR")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_CN")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_HK")) {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth + "," + seventh + "," + eighth);
		} else {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth);
		}
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete and rank with GAL. Steps, 1.Create 5 accounts 2.Send
	 * 3 mails to account3, 3 mails to account5, 2 mails to account1, 2 mails to
	 * account4 and 1 mail to account2 3.Verify autocomplete and rank 4.Send few
	 * mails to those accounts again 5.Refresh UI 6.Verify updated autocomplete
	 * and rank
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutoCompleteAndRankOnlyWithGAL(String from, String to,
			String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "blah1@testdomain.com";
		acc1 = acc1.toLowerCase();
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "blah2@testdomain.com";
		acc2 = acc2.toLowerCase();
		acc3 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "blah3@testdomain.com";
		acc3 = acc3.toLowerCase();
		acc4 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "blah4@testdomain.com";
		acc4 = acc4.toLowerCase();
		acc5 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "blah5@testdomain.com";
		acc5 = acc5.toLowerCase();
		Stafzmprov.createAccount(acc1);
		Stafzmprov.createAccount(acc2);
		Stafzmprov.createAccount(acc3);
		Stafzmprov.createAccount(acc4);
		Stafzmprov.createAccount(acc5);
		getKeyboardKeys(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefGalAutoCompleteEnabled", "TRUE");
		sendMails();
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		verifyAutocomplete(true);

		sendMailsUpdated();
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		verifyAutocompleteUpdated();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete and rank with Contacts & GAL both. Steps, 1.Create 5
	 * accounts 2.Send 3 mails to account3, 3 mails to account5, 2 mails to
	 * account1, 2 mails to account4 and 1 mail to account2 3.Verify
	 * autocomplete and rank 4.Send few mails to those accounts again 5.Refresh
	 * UI 6.Verify updated autocomplete and rank
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutoCompleteAndRankWithContactsAndGAL(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "zimb1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "zimb2@testdomain.com";
		acc3 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "zimb3@testdomain.com";
		acc4 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "zimb4@testdomain.com";
		acc5 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "zimb5@testdomain.com";
		Stafzmprov.createAccount(acc1);
		Stafzmprov.createAccount(acc2);
		Stafzmprov.createAccount(acc3);
		Stafzmprov.createAccount(acc4);
		Stafzmprov.createAccount(acc5);
		getKeyboardKeys(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefGalAutoCompleteEnabled", "TRUE");
		sendMails();
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		verifyAutocomplete(true);

		sendMailsUpdated();
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		verifyAutocompleteUpdated();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete and rank with Contacts & GAL both by ON and OFF.
	 * Steps, 1.Create 5 accounts 2.Send 3 mails to account3, 3 mails to
	 * account5, 2 mails to account1, 2 mails to account4 and 1 mail to account2
	 * 3.Verify autocomplete and rank 4.Delete contacts from Emailed contacts
	 * folder and Trash both 5.Send few mails to those accounts again 6.Refresh
	 * UI 7.Verify updated autocomplete and rank
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutoCompleteAndRankWithContactsAndGALONOFF(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "vmwa1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "vmwa2@testdomain.com";
		acc3 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "vmwa3@testdomain.com";
		acc4 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "vmwa4@testdomain.com";
		acc5 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "vmwa5@testdomain.com";
		Stafzmprov.createAccount(acc1);
		Stafzmprov.createAccount(acc2);
		Stafzmprov.createAccount(acc3);
		Stafzmprov.createAccount(acc4);
		Stafzmprov.createAccount(acc5);
		getKeyboardKeys(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefGalAutoCompleteEnabled", "TRUE");
		sendMails();
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		verifyAutocomplete(true);

		zGoToApplication("Address Book");
		obj.zFolder.zClick(page.zABCompose.zEmailedContactsFolder);
		SleepUtil.sleep(2000);
		Robot zRobot = new Robot();
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		SleepUtil.sleep(500);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		SleepUtil.sleep(500);
		zRobot.keyPress(KeyEvent.VK_DELETE);
		zRobot.keyRelease(KeyEvent.VK_DELETE);
		SleepUtil.sleep(500);
		typeKeyboardKeys();
		// verification
		Assert
				.assertTrue(
						"Verifying first autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_0')]//td[contains(text(), '"
												+ acc3.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying second autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_1')]//td[contains(text(), '"
												+ acc5.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying third autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_2')]//td[contains(text(), '"
												+ acc1.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying fourth autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_3')]//td[contains(text(), '"
												+ acc4.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying fifth autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_4')]//td[contains(text(), '"
												+ acc2.toLowerCase() + "')]"));
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete and rank with Contacts. Steps, 1.Create 5 contacts in
	 * Contacts address book folder 2.Send 3 mails to account3, 3 mails to
	 * account5, 2 mails to account1, 2 mails to account4 and 1 mail to account2
	 * 3.Verify autocomplete and rank 4.Send few mails to those accounts again
	 * 5.Verify updated autocomplete and rank
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutoCompleteAndRankWithContactsFolder(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "cont1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "cont2@testdomain.com";
		acc3 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "cont3@testdomain.com";
		acc4 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "cont4@testdomain.com";
		acc5 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "cont5@testdomain.com";
		String[] contacts = { acc1, acc2, acc3, acc4, acc5 };
		createContacts(contacts, false);
		getKeyboardKeys(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		typeKeyboardKeys();
		verifyAutocomplete(false);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * ZWC hangs during autocomplete on apostrophe character - bug 45815. Steps,
	 * 1.Create 2 accounts 2.Send few mails 3.Compose mail and verify
	 * autocomplete with apostrophe character. Also verify autocomplete by
	 * removing some character and checking back autocomplete and rank works
	 * fine
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zwcHangsDuringAutoCompOnApostropheChar_Bug45815(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "hang1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "hang2@testdomain.com";
		Stafzmprov.createAccount(acc1);
		Stafzmprov.createAccount(acc2);
		getKeyboardKeys(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefGalAutoCompleteEnabled", "TRUE");

		page.zComposeView.zComposeAndSendMail(acc1 + ";" + acc2, "", "",
				"testSubject", "testBody", "");

		page.zComposeView.zComposeAndSendMail(acc2, "", "", "testSubject",
				"testBody", "");
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zCcField);
		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_GB")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_AU")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"pt_BR")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_CN")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_HK")) {
			pressKeys("'");
			pressKeys("backspace");
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth + "," + seventh + "," + eighth);
		} else {
			pressKeys("'");
			pressKeys("backspace");
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth);
		}
		Assert
				.assertTrue(
						"Verifying first autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_0')]//td[contains(text(), '"
												+ acc2.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying second autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_1')]//td[contains(text(), '"
												+ acc1.toLowerCase() + "')]"));
		pressKeys("'");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Autocomplete returns too many results - bug 40959. Steps, 1.Create 5
	 * contacts in Contacts address book folder 2.Verify autocomplete with
	 * partial character & space and verify. For e.g en US <enUS@testdomain.com>
	 * then verify autocomplete by en US and also verify autocomplete not exists
	 * where it should not
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void autocompleteReturnsTooManyResults_Bug40959(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "toom1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "toom2@testdomain.com";
		acc3 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "toom3@testdomain.com";
		acc4 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "toom4@testdomain.com";
		acc5 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "toom5@testdomain.com";
		String[] contacts = { acc1, acc2, acc3, acc4, acc5 };
		createContacts(contacts, true);
		getKeyboardKeys(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		typeKeyboardKeys();
		verifyAutocomplete(false);
		pressKeys("1");
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc4, 4, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc5, 5, 0);
		pressKeys("space");
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc4, 4, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc5, 5, 0);
		pressKeys(ZimbraSeleniumProperties.getStringProperty("locale")
				.substring(0, 1));
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc4, 4, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc5, 5, 0);
		pressKeys(ZimbraSeleniumProperties.getStringProperty("locale")
				.substring(1, 2));
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc4, 4, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc5, 5, 0);
		pressKeys("x");
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc4, 4, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc5, 5, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify contact group autocomplete with 'VM1Ware Fi3nance' - bug 45545.
	 * Steps, 1.Create contact group called 'VM1Ware Fi3nance' 2.Type character
	 * one by one in email To: field and verify autocomplete works
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyContactGroupAutoComplete_Bug45545(String from, String to,
			String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String groupName = "VM1Ware Fi3nance";
		zGoToApplication("Address Book");
		obj.zButtonMenu.zClick(page.zABCompose.zNewMenuDropdownIconBtn);
		obj.zMenuItem.zClick(localize(locator.group));
		obj.zEditField.zType(
				getNameWithoutSpace(localize(locator.groupNameLabel)),
				groupName);
		obj.zEditField.zType(localize(locator.findLabel),
				"ccuser@testdomain.com");
		obj.zButton.zClick(localize(locator.search), "2");
		SleepUtil.sleep(1500);
		if (ClientSessionFactory.session().currentBrowserName().contains("Safari")) {
			obj.zButton.zClick(localize(locator.search), "2");
			obj.zButton.zClick(localize(locator.search), "2");
			SleepUtil.sleep(1000);
		}
		obj.zListItem.zDblClickItemInSpecificList("ccuser@testdomain.com", "2");
		obj.zButton.zClick(localize(locator.add));
		obj.zButton.zClick(localize(locator.save), "2");
		obj.zContactListItem.zExists(groupName);

		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		pressKeys("v,m");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("1");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("w,a,r,e");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("space");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("f");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("i");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("3");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify lucene stop words autocomplete with 'IT Department' - bug 46718.
	 * Steps, 1.Create contact group called 'IT Department' 2.Type character one
	 * by one in email To: field and verify autocomplete works
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void luceneStopWordsAutoComplete_Bug46718(String from, String to,
			String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String groupName = "IT Department";
		zGoToApplication("Address Book");
		obj.zButtonMenu.zClick(page.zABCompose.zNewMenuDropdownIconBtn);
		obj.zMenuItem.zClick(localize(locator.group));
		obj.zEditField.zType(
				getNameWithoutSpace(localize(locator.groupNameLabel)),
				groupName);
		obj.zEditField.zType(localize(locator.findLabel),
				"ccuser@testdomain.com");
		obj.zButton.zClick(localize(locator.search), "2");
		SleepUtil.sleep(2500);
		if (ClientSessionFactory.session().currentBrowserName().contains("Safari")) {
			obj.zButton.zClick(localize(locator.search), "2");
			obj.zButton.zClick(localize(locator.search), "2");
			SleepUtil.sleep(1000);
		}
		obj.zListItem.zDblClickItemInSpecificList("ccuser@testdomain.com", "2");
		obj.zButton.zClick(localize(locator.add));
		obj.zButton.zClick(localize(locator.save), "2");
		obj.zContactListItem.zExists(groupName);

		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		pressKeys("i,t");
		pressKeys("space");
		pressKeys("d");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete with special characters. Steps, 1.Create accounts
	 * with special character 2.Verify autocomplete with those chracters
	 * 3.Verify email address is filled automatically with semi-colon (;)
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void autocompleteOnSpecialCharacters_Bug41512(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "-" + "chr1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "." + "chr2@testdomain.com";
		String[] contacts = { acc1, acc2 };
		createContacts(contacts, true);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		verifySpecialCharAutoComplete("acc1");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		getKeyboardKeys(acc2);
		typeKeyboardKeys();
		verifySpecialCharAutoComplete("acc2");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")) {
			getKeyboardKeys(acc1);
			Robot zRobot = new Robot();
			zRobot.keyPress(KeyEvent.VK_SHIFT);
			typeKeyboardKeys();
			zRobot.keyRelease(KeyEvent.VK_SHIFT);
			verifySpecialCharAutoComplete("acc1");
			obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		}

		obj.zButton.zClick(MailApp.zNewMenuIconBtn);
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		pressKeys("z, i, m, b, r, a, ;");
		SleepUtil.sleep(1000);
		Assert.assertTrue("Expected value(" + "zimbra@testdomain.com"
				+ "), Actual Value("
				+ obj.zTextAreaField.zGetInnerText(page.zComposeView.zToField)
				+ ")",
				obj.zTextAreaField.zGetInnerText(page.zComposeView.zToField)
						.indexOf("zimbra@testdomain.com") >= 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Unable to get rid of emailed contacts - bug 40081. Steps, 1.Create
	 * account with zimbraPrefAutoAddAddressEnabled TRUE 2.Send mail to that
	 * account so automatically email address would be added in
	 * "Emailed contacts" folder 3.Compose mail and check autocomplete 4.Move
	 * contact to Trash folder 5.Verify autocomplete not exists 6.Permanently
	 * delete contact from Trash folder, verify autocomplete not exists.
	 * 6.Refresh UI and again verify autocomplete not exists
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void unableToGetRidOfEmailedContacts_Bug40081(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "ridc1@testdomain.com";
		Stafzmprov.createAccount(acc1);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefAutoAddAddressEnabled", "TRUE");
		zGoToApplication("Mail");
		page.zComposeView.zComposeAndSendMail(acc1, "", "", "testSubject",
				"testBody", "");
		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteExists(acc1.toLowerCase(), 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Address Book");
		obj.zFolder.zClick(page.zABCompose.zEmailedContactsFolder);
		Robot zRobot = new Robot();
		SleepUtil.sleep(1000);
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		pressKeys("delete");

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Address Book");
		obj.zFolder.zClick(localize(locator.trash));
		SleepUtil.sleep(1000);
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		pressKeys("delete");

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * GAL autocomplete doesn't work after pref changed - bug 45337. Steps,
	 * 1.Create account 2.Compose mail and verify autocomplete doesn't exists
	 * for gal 3.mark TRUE to address book preference
	 * "Include addresses in the Global Address List" 4.Re-compose mail and
	 * verify gal autocomplete exists.
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void galAutoCompleteDoesntWorkAfterPrefChange_Bug45337_Bug37377(
			String from, String to, String cc, String bcc, String subject,
			String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "pref1@testdomain.com";
		Stafzmprov.createAccount(acc1);
		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Preferences");
		zGoToPreferences("Address Book");
		obj.zCheckbox.zClick(localize(locator.galAutocomplete));
		obj.zButton.zClick("id=zb__PREF__SAVE_left_icon");
		SleepUtil.sleep(1000);

		getKeyboardKeys(acc1);
		page.zComposeView.zNavigateToMailCompose();
		ClientSessionFactory.session().selenium().click("link=" + localize(locator.showBCC));
		obj.zTextAreaField.zActivate(page.zComposeView.zBccField);
		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_GB")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_AU")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"pt_BR")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_CN")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_HK")) {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth + "," + seventh + "," + eighth);
		} else {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth);
		}
		page.zMailApp.zVerifyAutocompleteExists(acc1.toLowerCase(), 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Option not to AutoComplete contact groups by member - bug 44509. Steps,
	 * 1.Create 2 account and 1 contact group 2.Compose mail and verify all 3
	 * autocomplete (group followed by both account) 3.mark TRUE to address book
	 * preference "Don't show contact group if one of its members matches"
	 * 4.Re-compose mail and verify contact group not exists 5.Verify
	 * autocomplete for contactgroup using proper contact group also.
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void optionNotToAutoCompleteContactGroupsByMember_Bug44509(
			String from, String to, String cc, String bcc, String subject,
			String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "grop1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "grop2@testdomain.com";
		Stafzmprov.createAccount(acc1);
		Stafzmprov.createAccount(acc2);
		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefGalAutoCompleteEnabled", "TRUE");

		String groupName = "1ABc 2DeF";
		zGoToApplication("Address Book");
		obj.zButtonMenu.zClick(page.zABCompose.zNewMenuDropdownIconBtn);
		obj.zMenuItem.zClick(localize(locator.group));
		obj.zEditField.zType(
				getNameWithoutSpace(localize(locator.groupNameLabel)),
				groupName);
		SleepUtil.sleep(1000);
		obj.zEditField.zType(localize(locator.findLabel), "grop");
		obj.zButton.zClick(localize(locator.search), "2");
		SleepUtil.sleep(1500);
		if (ClientSessionFactory.session().currentBrowserName().contains("Safari")) {
			obj.zButton.zClick(localize(locator.search), "2");
			obj.zButton.zClick(localize(locator.search), "2");
			SleepUtil.sleep(1000);
		}
		obj.zButton.zClick(localize(locator.addAll));
		obj.zButton.zClick(localize(locator.save), "2");
		obj.zContactListItem.zExists(groupName);

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		page.zMailApp.zVerifyAutocompleteExists(acc1.toLowerCase(), 2, 0);
		page.zMailApp.zVerifyAutocompleteExists(acc2.toLowerCase(), 3, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Preferences");
		zGoToPreferences("Address Book");
		obj.zCheckbox.zClick(localize(locator.autocompleteNoGroupMatch));
		obj.zButton.zClick("id=zb__PREF__SAVE_left_icon");
		SleepUtil.sleep(1000);

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(groupName, 1, 1);
		page.zMailApp.zVerifyAutocompleteExists(acc1.toLowerCase(), 1, 0);
		page.zMailApp.zVerifyAutocompleteExists(acc2.toLowerCase(), 2, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		pressKeys("1, A");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("b");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("c");
		page.zMailApp.zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2.toLowerCase(), 2, 0);
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("space");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("2");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("d");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("e");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		pressKeys("f");
		page.zMailApp.zVerifyAutocompleteExists(groupName, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2.toLowerCase(), 2, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}


	/**
	 * Autocomplete shows contact from deleted(Trashed) address book folders -
	 * bug 47044. Steps, 1.Create account 2.Create addressbook with one contact
	 * with this email address 3.Check autocomplete exists 3.Move addressbook to
	 * Trash folder 4.Verify autocomplete not exists 5.Refresh UI and again
	 * verify autocomplete not exists
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void autocompleteShowsContactFromTrashedABFolders_Bug47044(
			String from, String to, String cc, String bcc, String subject,
			String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String newAddressBook, lastName, firstName;
		newAddressBook = getLocalizedData_NoSpecialChar();
		lastName = getLocalizedData_NoSpecialChar();
		firstName = getLocalizedData_NoSpecialChar();
		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "tras1@testdomain.com";
		Stafzmprov.createAccount(acc1);
		zGoToApplication("Address Book");
		page.zABCompose.zCreateNewAddBook(newAddressBook);

		FolderItem folder = new FolderItem();
		folder.setName(newAddressBook);

		ContactItem contact = new ContactItem();
		contact.lastName = lastName;
		contact.middleName = "";
		contact.firstName = firstName;
		contact.email = acc1;
		contact.AddressBook = folder;

		page.zABCompose.createItem(ActionMethod.DEFAULT, contact);

		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Address Book");
		obj.zFolder.zRtClick(newAddressBook);
		obj.zMenuItem.zClick(localize(locator.del));

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Address Book");
		obj.zFolder.zRtClick(newAddressBook);
		obj.zMenuItem.zClick(localize(locator.del));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.warningMsg));

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete from contacts sub and subsub folder. Steps, 1.Create
	 * 2 account 2.Create subaddressbook and subsubaddressbook folder 3.Add 1
	 * contact each to both addressbook 4.Verify autocomplete exists 5.Move
	 * subaddressbook to Trash folder and verify autocomplete not exists
	 * 6.Refresh UI and again verify autocomplete not exists
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteFromContactsSubAndSubSubFolders_Bug47044(
			String from, String to, String cc, String bcc, String subject,
			String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
				"zimbraPrefGalAutoCompleteEnabled", "TRUE");
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");

		String subAddressBook, subSubAddressBook, sublastName, subSublastName;
		subAddressBook = getLocalizedData_NoSpecialChar();
		subSubAddressBook = getLocalizedData_NoSpecialChar();
		sublastName = "1" + getLocalizedData_NoSpecialChar();
		subSublastName = "2" + getLocalizedData_NoSpecialChar();
		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "subf1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "subf2@testdomain.com";
		Stafzmprov.createAccount(acc1);
		Stafzmprov.createAccount(acc2);
		zGoToApplication("Address Book");
		page.zABCompose.zCreateNewAddBook(subAddressBook,
				localize(locator.contacts));
		page.zABCompose.zCreateNewAddBook(subSubAddressBook, subAddressBook);
		obj.zFolder.zClick(subAddressBook);

		ContactItem contact = new ContactItem();
		contact.lastName = sublastName;
		contact.middleName = "";
		contact.firstName = "";
		contact.email = acc1;

		ContactItem contact2 = new ContactItem();
		contact2.lastName = subSublastName;
		contact2.middleName = "";
		contact2.firstName = "";
		contact2.email = acc2;

		page.zABCompose.createItem(ActionMethod.DEFAULT, contact);
		obj.zFolder.zClick(subSubAddressBook);
		page.zABCompose.createItem(ActionMethod.DEFAULT, contact2);

		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteExists(acc2, 2, 0);

		zGoToApplication("Address Book");
		obj.zFolder.zRtClick(subAddressBook);
		obj.zMenuItem.zClick(localize(locator.del));

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Address Book");
		obj.zFolder.zRtClick(subAddressBook);
		obj.zMenuItem.zClick(localize(locator.del));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.warningMsg));

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete from shared sub and subsub folder - Bug 45550. Steps,
	 * 1.Create 2 account 2.Create subaddressbook and subsubaddressbook folder
	 * 3.Add 1 contact each to both addressbook 4.Share parent addressbook to
	 * user2 5.Login to user2 > accept share 6.mark ON/OFF to
	 * "Include addresses in shared address books" and verify autocomplete
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteFromSharedSubAndSubSubFolders_Bug45550(
			String from, String to, String cc, String bcc, String subject,
			String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		FolderItem subAddressBook = new FolderItem();
		subAddressBook.setName(getLocalizedData_NoSpecialChar());

		FolderItem subSubAddressBook = new FolderItem();
		subSubAddressBook.setName(getLocalizedData_NoSpecialChar());

		String lastName, sublastName, subSublastName, user2, mountingfoldername;
		lastName = "1" + getLocalizedData_NoSpecialChar();
		sublastName = "2" + getLocalizedData_NoSpecialChar();
		subSublastName = "3" + getLocalizedData_NoSpecialChar();
		mountingfoldername = getLocalizedData_NoSpecialChar();
		acc1 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "shar1@testdomain.com";
		acc2 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "shar2@testdomain.com";
		acc3 = ZimbraSeleniumProperties.getStringProperty("locale").replace(
				"_", "")
				+ "shar3@testdomain.com";
		user2 = Stafzmprov.getRandomAccount();

		Stafzmprov.createAccount(acc1);
		Stafzmprov.createAccount(acc2);
		Stafzmprov.createAccount(acc3);
		zGoToApplication("Address Book");
		page.zABCompose.zCreateNewAddBook(subAddressBook.getName(),
				localize(locator.contacts));
		page.zABCompose.zCreateNewAddBook(subSubAddressBook.getName(),
				subAddressBook.getName());

		ContactItem contact1 = new ContactItem();
		contact1.lastName = lastName;
		contact1.middleName = "";
		contact1.firstName = "";
		contact1.email = acc1;

		obj.zFolder.zClick(page.zABCompose.zContactsFolder);
		page.zABCompose.createItem(ActionMethod.DEFAULT, contact1);

		ContactItem subContact = new ContactItem();
		subContact.lastName = sublastName;
		subContact.middleName = "";
		subContact.firstName = "";
		subContact.email = acc2;

		obj.zFolder.zClick(subAddressBook.getName());
		page.zABCompose.createItem(ActionMethod.DEFAULT, subContact);

		ContactItem subSubContact = new ContactItem();
		subSubContact.lastName = subSublastName;
		subSubContact.middleName = "";
		subSubContact.firstName = "";
		subSubContact.email = acc3;

		obj.zFolder.zClick(subSubAddressBook.getName());
		page.zABCompose.createItem(ActionMethod.DEFAULT, subSubContact);

		page.zSharing.zShareFolder("Address Book",
				page.zABCompose.zContactsFolder, "", user2, "", "", "", "");
		Stafzmprov.modifyAccount(acc2, "zimbraPrefGalAutoCompleteEnabled",
						"FALSE");
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(user2);
		page.zSharing.zAcceptShare(mountingfoldername);

		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);

		Stafzmprov.modifyAccount(user2,
				"zimbraPrefSharedAddrBookAutoCompleteEnabled", "TRUE");
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteExists(acc3, 3, 0);

		zGoToApplication("Address Book");
		obj.zFolder.zRtClick(mountingfoldername);
		obj.zMenuItem.zClick(localize(locator.del));

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		zGoToApplication("Address Book");
		obj.zFolder.zRtClick(mountingfoldername);
		obj.zMenuItem.zClick(localize(locator.del));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.warningMsg));

		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		typeKeyboardKeys();
		page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		page.zMailApp.zVerifyAutocompleteNotExists(acc2, 2, 0);
		page.zMailApp.zVerifyAutocompleteNotExists(acc3, 3, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * verify autocomplete with large addressbook - 1KContacts. Steps, 1.Import
	 * 1K contacts CSV 2.Verify few autocompletes 3.Send few mails and recheck
	 * updated ranked autocomplete
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutoCompleteWithLargeAddressBook_1KContacts(String from,
			String to, String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zABCompose.zNavigateToPrefImportExport();
		obj.zRadioBtn.zClick(localize(locator.contacts));
		File f = new File(ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/csv/1000contacts.csv");
		String path = f.getAbsolutePath();
		obj.zBrowseField.zTypeWithKeyboard(localize(locator.fileLabel), path);
		obj.zButton.zClick(localize(locator._import));

		zWaitTillObjectExist("dialog", localize(locator.infoMsg));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.infoMsg));

		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zCcField);
		pressKeys("1");
		SleepUtil.sleep(2000);
		page.zMailApp.zVerifyAutocompleteExists("1@testdomain.com", 1, 1);
		page.zMailApp.zVerifyAutocompleteExists("19@testdomain.com", 20, 0);
		pressKeys("0");
		SleepUtil.sleep(2000);
		page.zMailApp.zVerifyAutocompleteExists("10@testdomain.com", 1, 1);
		page.zMailApp.zVerifyAutocompleteExists("100@testdomain.com", 2, 0);
		page.zMailApp.zVerifyAutocompleteExists("1000@testdomain.com", 3, 0);
		page.zMailApp.zVerifyAutocompleteExists("101@testdomain.com", 4, 0);
		page.zMailApp.zVerifyAutocompleteExists("102@testdomain.com", 5, 0);
		page.zMailApp.zVerifyAutocompleteExists("103@testdomain.com", 6, 0);
		page.zMailApp.zVerifyAutocompleteExists("104@testdomain.com", 7, 0);
		page.zMailApp.zVerifyAutocompleteExists("105@testdomain.com", 8, 0);
		page.zMailApp.zVerifyAutocompleteExists("106@testdomain.com", 9, 0);
		page.zMailApp.zVerifyAutocompleteExists("107@testdomain.com", 10, 0);
		page.zMailApp.zVerifyAutocompleteExists("108@testdomain.com", 11, 0);
		page.zMailApp.zVerifyAutocompleteExists("109@testdomain.com", 12, 0);
		pressKeys("backspace, backspace, 2");
		SleepUtil.sleep(2000);
		page.zMailApp.zVerifyAutocompleteExists("2@testdomain.com", 1, 1);
		page.zMailApp.zVerifyAutocompleteExists("29@testdomain.com", 20, 0);
		pressKeys("backspace, 7");
		SleepUtil.sleep(2000);
		page.zMailApp.zVerifyAutocompleteExists("7@testdomain.com", 1, 1);
		page.zMailApp.zVerifyAutocompleteExists("79@testdomain.com", 20, 0);
		pressKeys("backspace, 9");
		SleepUtil.sleep(2000);
		page.zMailApp.zVerifyAutocompleteExists("9@testdomain.com", 1, 1);
		page.zMailApp.zVerifyAutocompleteExists("90@testdomain.com", 2, 0);
		page.zMailApp.zVerifyAutocompleteExists("900@testdomain.com", 3, 0);
		page.zMailApp.zVerifyAutocompleteExists("901@testdomain.com", 4, 0);
		page.zMailApp.zVerifyAutocompleteExists("902@testdomain.com", 5, 0);
		page.zMailApp.zVerifyAutocompleteExists("903@testdomain.com", 6, 0);
		page.zMailApp.zVerifyAutocompleteExists("904@testdomain.com", 7, 0);
		page.zMailApp.zVerifyAutocompleteExists("905@testdomain.com", 8, 0);
		page.zMailApp.zVerifyAutocompleteExists("906@testdomain.com", 9, 0);
		page.zMailApp.zVerifyAutocompleteExists("907@testdomain.com", 10, 0);
		page.zMailApp.zVerifyAutocompleteExists("908@testdomain.com", 11, 0);
		page.zMailApp.zVerifyAutocompleteExists("91@testdomain.com", 12, 0);
		page.zMailApp.zVerifyAutocompleteExists("92@testdomain.com", 13, 0);
		page.zMailApp.zVerifyAutocompleteExists("93@testdomain.com", 14, 0);
		page.zMailApp.zVerifyAutocompleteExists("94@testdomain.com", 15, 0);
		page.zMailApp.zVerifyAutocompleteExists("95@testdomain.com", 16, 0);
		page.zMailApp.zVerifyAutocompleteExists("96@testdomain.com", 17, 0);
		page.zMailApp.zVerifyAutocompleteExists("97@testdomain.com", 18, 0);
		page.zMailApp.zVerifyAutocompleteExists("98@testdomain.com", 19, 0);
		page.zMailApp.zVerifyAutocompleteExists("99@testdomain.com", 20, 0);
		pressKeys("9");
		SleepUtil.sleep(2000);
		page.zMailApp.zVerifyAutocompleteExists("99@testdomain.com", 1, 1);
		page.zMailApp.zVerifyAutocompleteExists("990@testdomain.com", 2, 0);
		page.zMailApp.zVerifyAutocompleteExists("991@testdomain.com", 3, 0);
		page.zMailApp.zVerifyAutocompleteExists("992@testdomain.com", 4, 0);
		page.zMailApp.zVerifyAutocompleteExists("993@testdomain.com", 5, 0);
		page.zMailApp.zVerifyAutocompleteExists("994@testdomain.com", 6, 0);
		page.zMailApp.zVerifyAutocompleteExists("995@testdomain.com", 7, 0);
		page.zMailApp.zVerifyAutocompleteExists("996@testdomain.com", 8, 0);
		page.zMailApp.zVerifyAutocompleteExists("997@testdomain.com", 9, 0);
		page.zMailApp.zVerifyAutocompleteExists("998@testdomain.com", 10, 0);
		page.zMailApp.zVerifyAutocompleteExists("999@testdomain.com", 11, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		Stafzmprov.createAccount("999@testdomain.com");
		page.zComposeView.zComposeAndSendMail("999@testdomain.com", "", "",
				"testSubject", "testBody", "");

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		pressKeys("9, 9");
		SleepUtil.sleep(2000);
		page.zMailApp.zVerifyAutocompleteExists("999@testdomain.com", 1, 1);
		page.zMailApp.zVerifyAutocompleteExists("99@testdomain.com", 2, 0);
		page.zMailApp.zVerifyAutocompleteExists("990@testdomain.com", 3, 0);
		page.zMailApp.zVerifyAutocompleteExists("991@testdomain.com", 4, 0);
		page.zMailApp.zVerifyAutocompleteExists("992@testdomain.com", 5, 0);
		page.zMailApp.zVerifyAutocompleteExists("993@testdomain.com", 6, 0);
		page.zMailApp.zVerifyAutocompleteExists("994@testdomain.com", 7, 0);
		page.zMailApp.zVerifyAutocompleteExists("995@testdomain.com", 8, 0);
		page.zMailApp.zVerifyAutocompleteExists("996@testdomain.com", 9, 0);
		page.zMailApp.zVerifyAutocompleteExists("997@testdomain.com", 10, 0);
		page.zMailApp.zVerifyAutocompleteExists("998@testdomain.com", 11, 0);
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify forget functionality for autocomplete. Steps, 1.Create 2 accounts
	 * 2.Send 1 mail to each account, 3.Verify autocomplete 4.Forget one of the
	 * account 5.Recompose mail and check forgotten contact not exist in
	 * autocomplete. Run testcases for 4 different options for e.g.
	 * GALOFFAndEmailedContactsOFF, GALOFFAndEmailedContactsON,
	 * GALONAndEmailedContactsOFF & GALONAndEmailedContactsON
	 */
	@Test(dataProvider = "composeDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void forgetAutocompleteWithGALAndEmailedAddressOnOff(
			String settings, String from, String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		if (settings.equals("GALOFFAndEmailedContactsOFF")) {
			acc1 = ZimbraSeleniumProperties.getStringProperty("locale")
					.replace("_", "")
					+ "forg1@testdomain.com";
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefGalAutoCompleteEnabled", "FALSE");
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefAutoAddAddressEnabled", "FALSE");
		} else if (settings.equals("GALOFFAndEmailedContactsON")) {
			acc1 = ZimbraSeleniumProperties.getStringProperty("locale")
					.replace("_", "")
					+ "gorg1@testdomain.com";
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefGalAutoCompleteEnabled", "FALSE");
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefAutoAddAddressEnabled", "TRUE");
		} else if (settings.equals("GALONAndEmailedContactsOFF")) {
			acc1 = ZimbraSeleniumProperties.getStringProperty("locale")
					.replace("_", "")
					+ "horg1@testdomain.com";
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefGalAutoCompleteEnabled", "TRUE");
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefAutoAddAddressEnabled", "FALSE");
		} else if (settings.equals("GALONAndEmailedContactsON")) {
			acc1 = ZimbraSeleniumProperties.getStringProperty("locale")
					.replace("_", "")
					+ "jorg1@testdomain.com";
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefGalAutoCompleteEnabled", "TRUE");
			Stafzmprov.modifyAccount(ClientSessionFactory.session().currentUserName(),
					"zimbraPrefAutoAddAddressEnabled", "TRUE");
		}

		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");

		Stafzmprov.createAccount(acc1);
		page.zComposeView.zComposeAndSendMail(acc1, "", "", "testSubject",
				"testBody", "");
		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		getKeyboardKeys(acc1);
		typeKeyboardKeys();
		zVerifyAutocompleteExists(acc1.toLowerCase(), 1, 1);
		zForgetAutocomplete(1);
		assertReport(localize(locator.forgetSummary, acc1.toLowerCase(), ""),
				obj.zToastAlertMessage.zGetMsg(),
				"Verifying autocomplete forget summary toast message");
		zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);

		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		typeKeyboardKeys();
		if (settings.equals("GALONAndEmailedContactsOFF")) {
			zVerifyAutocompleteExists(acc1.toLowerCase(), 1, 1);
		} else {
			zVerifyAutocompleteNotExists(acc1.toLowerCase(), 1, 1);
		}
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	//------------------------------autocomplete_functions----------------------
	private static void createContacts(String[] contacts, Boolean withName)
			throws Exception {
		zGoToApplication("Address Book");
		if (ZimbraSeleniumProperties.getStringProperty("browser")
				.contains("IE")) {
			SleepUtil.sleep(2500);
		} else {
			SleepUtil.sleep(2000);
		}
		int lenAccounts = contacts.length;
		for (int i = 0; i <= lenAccounts - 1; i++) {
			obj.zButton.zClick(page.zABCompose.zNewContactMenuIconBtn);
			zWaitTillObjectExist("editfield", page.zABCompose.zLastEditField);
			if (withName.equals(true)) {
				obj.zEditField.zActivateAndType(
						page.zABCompose.zFirstEditField,
						contacts[i].split("@")[0]);
				obj.zEditField.zActivateAndType(page.zABCompose.zLastEditField,
						ZimbraSeleniumProperties.getStringProperty("locale")
								.replace("_", ""));
			}
			obj.zEditField.zActivateAndType(page.zABCompose.zEmail1EditField,
					contacts[i]);
			obj.zButton.zClick(page.zABCompose.zSaveContactMenuIconBtn);
		}
		SleepUtil.sleep(1500);
	}

	private static void sendMails() throws Exception {
		page.zComposeView.zComposeAndSendMail(acc2 + "; " + acc4 + "; " + acc5,
				"", "", "testSubject", "testBody", "");

		page.zComposeView.zComposeAndSendMail(acc4 + "; " + acc5, "", "",
				"testSubject", "testBody", "");

		page.zComposeView.zComposeAndSendMail(acc5 + "; " + acc1, "", "",
				"testSubject", "testBody", "");

		page.zComposeView.zComposeAndSendMail(acc3, "", "", "testSubject",
				"testBody", "");

		page.zComposeView.zComposeAndSendMail(acc3 + "; " + acc1, "", "",
				"testSubject", "testBody", "");

		page.zComposeView.zComposeAndSendMail(acc3, "", "", "testSubject",
				"testBody", "");
	}

	private static void sendMailsUpdated() throws Exception {
		page.zComposeView.zComposeAndSendMail(acc1 + "; " + acc2 + "; " + acc4
				+ "; " + acc5, "", "", "testSubject", "testBody", "");
		page.zComposeView.zComposeAndSendMail(acc1 + "; " + acc2 + "; " + acc5,
				"", "", "testSubject", "testBody", "");
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3500);
		zWaitTillObjectExist("id", "ztih__main_Mail__ZIMLET_textCell");
	}

	private static void verifyAutocomplete(Boolean rankMatters)
			throws Exception {
		SleepUtil.sleep(2000);
		if (rankMatters.equals(true)) {
			Assert
					.assertTrue(
							"Verifying first autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_0')]//td[contains(text(), '"
													+ acc3.toLowerCase()
													+ "')]"));

			Assert
					.assertTrue(
							"Verifying second autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_1')]//td[contains(text(), '"
													+ acc5.toLowerCase()
													+ "')]"));

			Assert
					.assertTrue(
							"Verifying third autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_2')]//td[contains(text(), '"
													+ acc1.toLowerCase()
													+ "')]"));

			Assert
					.assertTrue(
							"Verifying fourth autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_3')]//td[contains(text(), '"
													+ acc4.toLowerCase()
													+ "')]"));

			Assert
					.assertTrue(
							"Verifying fifth autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_4')]//td[contains(text(), '"
													+ acc2.toLowerCase()
													+ "')]"));
			obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		} else {
			Assert
					.assertTrue(
							"Verifying first autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_0')]//td[contains(text(), '"
													+ acc1 + "')]"));

			Assert
					.assertTrue(
							"Verifying second autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_1')]//td[contains(text(), '"
													+ acc2 + "')]"));

			Assert
					.assertTrue(
							"Verifying third autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_2')]//td[contains(text(), '"
													+ acc3 + "')]"));

			Assert
					.assertTrue(
							"Verifying fourth autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_3')]//td[contains(text(), '"
													+ acc4 + "')]"));

			Assert
					.assertTrue(
							"Verifying fifth autocomplete list rank",
							ClientSessionFactory.session().selenium()
									.isElementPresent(
											"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_4')]//td[contains(text(), '"
													+ acc5 + "')]"));
		}
	}

	private static void verifyAutocompleteUpdated() throws Exception {
		SleepUtil.sleep(2000);
		Assert
				.assertTrue(
						"Verifying first autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_0')]//td[contains(text(), '"
												+ acc5.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying second autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_1')]//td[contains(text(), '"
												+ acc1.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying third autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_2')]//td[contains(text(), '"
												+ acc2.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying fourth autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_3')]//td[contains(text(), '"
												+ acc3.toLowerCase() + "')]"));

		Assert
				.assertTrue(
						"Verifying fifth autocomplete list rank",
						ClientSessionFactory.session().selenium()
								.isElementPresent(
										"//div[contains(@class, 'ZmAutocompleteListView')]//tr[contains(@id, 'acRow_4')]//td[contains(text(), '"
												+ acc4.toLowerCase() + "')]"));
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
	}

	private static void getKeyboardKeys(String accont) throws Exception {
		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_GB")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_AU")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"pt_BR")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_CN")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_HK")) {
			first = accont.substring(0, 1);
			second = accont.substring(1, 2);
			third = accont.substring(2, 3);
			fourth = accont.substring(3, 4);
			fifth = accont.substring(4, 5);
			sixth = accont.substring(5, 6);
			seventh = accont.substring(6, 7);
			eighth = accont.substring(7, 8);
		} else {
			first = accont.substring(0, 1);
			second = accont.substring(1, 2);
			third = accont.substring(2, 3);
			fourth = accont.substring(3, 4);
			fifth = accont.substring(4, 5);
			sixth = accont.substring(5, 6);
		}
	}

	private static void typeKeyboardKeys() throws Exception {
		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zActivate(page.zComposeView.zToField);
		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_GB")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"en_AU")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"pt_BR")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_CN")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals(
						"zh_HK")) {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth + "," + seventh + "," + eighth);
		} else {
			pressKeys(first + "," + second + "," + third + "," + fourth + ","
					+ fifth + "," + sixth);
		}
	}

	private static void verifySpecialCharAutoComplete(String verifyAcc)
			throws Exception {
		if (verifyAcc.equals("acc1")) {
			page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
			pressKeys("1");
			page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
			pressKeys("space");
			page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
			pressKeys(ZimbraSeleniumProperties.getStringProperty("locale")
					.substring(0, 1));
			page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
			pressKeys(ZimbraSeleniumProperties.getStringProperty("locale")
					.substring(1, 2));
			page.zMailApp.zVerifyAutocompleteExists(acc1, 1, 1);
			pressKeys("y");
			page.zMailApp.zVerifyAutocompleteNotExists(acc1, 1, 1);
		} else if (verifyAcc.equals("acc2")) {
			page.zMailApp.zVerifyAutocompleteExists(acc2, 1, 1);
			pressKeys("2");
			page.zMailApp.zVerifyAutocompleteExists(acc2, 1, 1);
			pressKeys("space");
			page.zMailApp.zVerifyAutocompleteExists(acc2, 1, 1);
			pressKeys(ZimbraSeleniumProperties.getStringProperty("locale")
					.substring(0, 1));
			page.zMailApp.zVerifyAutocompleteExists(acc2, 1, 1);
			pressKeys(ZimbraSeleniumProperties.getStringProperty("locale")
					.substring(1, 2));
			page.zMailApp.zVerifyAutocompleteExists(acc2, 1, 1);
			pressKeys("z");
			page.zMailApp.zVerifyAutocompleteNotExists(acc2, 1, 1);
		}
	}

	public static void zVerifyIsColonAutocompleteExists(String value, int rank)
			throws Exception {
		Assert.assertTrue("Verifying is: autocomplete list rank " + rank
				+ " for " + value, ClientSessionFactory.session().selenium().isElementPresent(
				"//div[contains(@id, 'acRow_" + (rank - 1)
						+ "') and contains(text(), '" + value + "')]"));
	}
}