package com.zimbra.qa.selenium.projects.zcs.tests.preferences.general;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.cs.service.account.GetAccountInfo;
import com.zimbra.cs.service.account.GetPrefs;
import com.zimbra.cs.service.admin.GetAccount;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.LmtpUtil;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.DocumentApp;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;



@SuppressWarnings( { "static-access", "unused" })
public class GeneralPreferencesSetFalse extends CommonTest {
	protected String currentloggedinuser;
	protected String aChar = new Character((char) 34).toString();
	public static final String zMailListItemChkBox = "id=zlhi__CLV__se";
	public static final String zTaskListItemChkBox = "id=zlhi__TKL__se";
	public static final String zBriefcaseListItemChkBox = "id=zlhi__BC__se";
	public static final String zPrefSaveIconBtn = "id=zb__PREF__SAVE_left_icon";

	// Before Class
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		zLoginIfRequired();
		currentloggedinuser = ClientSessionFactory.session().currentUserName();
		Stafzmprov.modifyAccount(currentloggedinuser,
				"zimbraPrefIncludeSpamInSearch", "FALSE");
		Stafzmprov.modifyAccount(currentloggedinuser,
				"zimbraPrefIncludeTrashInSearch", "FALSE");
		Stafzmprov.modifyAccount(currentloggedinuser,
				"zimbraPrefShowSearchString", "FALSE");
		Stafzmprov.modifyAccount(currentloggedinuser,
				"zimbraPrefShowSelectionCheckbox", "FALSE");
		ClientSessionFactory.session().selenium().refresh();
		SleepUtil.sleep(3000);/* without this we get permission denied error */
		zWaitTillObjectExist("button", page.zLoginpage.zSearchFldr);
		SleepUtil.sleep(2000);/*
							 * wait another 3 secs after we see the search
							 * button
							 */
		super.zLogin();
	}

	// Before method
	@BeforeMethod(groups = { "always" })
	public void zResetIfRequired() throws Exception {
		if (SelNGBase.needReset.get() && !SelNGBase.isExecutionARetry.get()) {
			zLogin();
		}
		SelNGBase.needReset.set(true);
	}

	// Tests
	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void GeneralPrefVerifyAllCheckBoxesFalse() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		zGoToApplication("Preferences");
		if (!ZimbraSeleniumProperties.getStringProperty("locale").equals("ru")) {
			obj.zCheckbox
					.zVerifyIsNotChecked(localize(locator.includeJunkFolder));
		}
		obj.zCheckbox.zVerifyIsNotChecked(localize(locator.includeTrashFolder));
		obj.zCheckbox.zVerifyIsNotChecked(localize(locator.showSearchString));
		obj.zCheckbox
				.zVerifyIsNotChecked(localize(locator.showSelectionString));

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void GeneralPrefIncludeJunkFolderInSearchFalse() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String[] recipients = { ClientSessionFactory.session().currentUserName() };
		String[] message = { "junksubject21", "junksubject22" };
		for (int i = 0; i <= 1; i++) {
			LmtpUtil.injectMessage(ClientSessionFactory.session().currentUserName(), recipients,
					"ccuser@testdomain.com", message[i], "generalbody");
			MailApp.ClickCheckMailUntilMailShowsUp(localize(locator.inbox),
					message[i]);
			if (i == 1) {
				obj.zMessageItem.zClick(message[0]);
				obj.zButton.zClick(page.zMailApp.zJunkIconBtn);
				obj.zFolder.zClick(localize(locator.inbox));
				obj.zMessageItem.zNotExists(message[0]);
				obj.zFolder.zClick(localize(locator.junk));
				obj.zMessageItem.zExists(message[0]);
				obj.zFolder.zClick(localize(locator.inbox));
				ClientSessionFactory.session().selenium().type("xpath=//input[@class='search_input']",
						ClientSessionFactory.session().currentUserName());
				obj.zButton.zClick(page.zMailApp.zSearchIconBtn);
				obj.zMessageItem.zNotExists(message[0]);
				obj.zMessageItem.zExists(message[1]);
			}
		}

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void GeneralPrefIncludeTrashFolderInSearchFalse() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String[] recipients = { ClientSessionFactory.session().currentUserName() };
		String[] message = { "trashsubject21", "trashsubject22" };
		for (int i = 0; i <= 1; i++) {
			LmtpUtil.injectMessage(ClientSessionFactory.session().currentUserName(), recipients,
					"ccuser@testdomain.com", message[i], "generalbody");
			MailApp.ClickCheckMailUntilMailShowsUp(localize(locator.inbox),
					message[i]);
			if (i == 1) {
				obj.zMessageItem.zClick(message[0]);
				obj.zButton.zClick(page.zMailApp.zDeleteIconBtn);
				obj.zFolder.zClick(localize(locator.trash));
				SleepUtil.sleep(1000);
				obj.zMessageItem.zExists(message[0]);
				ClientSessionFactory.session().selenium().type("xpath=//input[@class='search_input']",
						ClientSessionFactory.session().currentUserName());
				obj.zButton.zClick(page.zMailApp.zSearchIconBtn);
				obj.zMessageItem.zNotExists(message[0]);
				obj.zMessageItem.zExists(message[1]);
			}
		}

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void GeneralPrefShowAdvSearchLangFalse() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		// Inbox folder
		obj.zFolder.zClick(replaceUserNameInStaticId(page.zMailApp.zInboxFldr));
		String inboxSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), inboxSearchValue,
				"Advanced search string not showing blank while click on folder (Inbox)");

		// Sent folder
		obj.zFolder.zClick(replaceUserNameInStaticId(page.zMailApp.zSentFldr));
		String sentSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), sentSearchValue,
				"Advanced search string not showing blank while click on folder (Sent)");

		// Drafts folder
		obj.zFolder
				.zClick(replaceUserNameInStaticId(page.zMailApp.zDraftsFldr));
		String draftsSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), draftsSearchValue,
				"Advanced search string not showing blank while click on folder (Drafts)");

		// Junk folder
		obj.zFolder.zClick(replaceUserNameInStaticId(page.zMailApp.zJunkFldr));
		String junkSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), junkSearchValue,
				"Advanced search string not showing blank while click on folder (Junk)");

		// Trash folder
		obj.zFolder.zClick(replaceUserNameInStaticId(page.zMailApp.zTrashFldr));
		String trashSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), trashSearchValue,
				"Advanced search string not showing blank while click on folder (Trash)");

		// New Folder
		String newFolder = getLocalizedData_NoSpecialChar();
		page.zMailApp.zCreateFolder(newFolder);
		obj.zFolder.zClick(newFolder);
		String newFolderSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), newFolderSearchValue,
				"Advanced search string not showing blank while click on new folder ("
						+ newFolder + ")");

		// Address Book - Contacts folder
		zGoToApplication("Address Book");
		String contactsSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		obj.zFolder
				.zClick(replaceUserNameInStaticId(page.zABCompose.zContactsFolder));
		contactsSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), contactsSearchValue,
				"Advanced search string not showing blank while click on folder (Contacts)");

		// Address Book - 'Emailed Contacts' folder
		obj.zFolder
				.zClick(replaceUserNameInStaticId(page.zABCompose.zEmailedContactsFolder));
		String emailedContactsSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(
				localize(locator.search),
				emailedContactsSearchValue,
				"Advanced search string not showing blank while click on folder (Emailed Contacts)");

		// Tasks - Tasks folder
		zGoToApplication("Tasks");
		String tasksSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), tasksSearchValue,
				"Advanced search string not showing blank while go to 'Tasks' application tab");
		obj.zFolder
				.zClick(replaceUserNameInStaticId(page.zTaskApp.zTasksFolder));
		tasksSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), tasksSearchValue,
				"Advanced search string not showing blank while click on folder (Tasks)");

		// Documents - Notebook folder
		zGoToApplication("Documents");
		obj.zFolder.zClick(DocumentApp.zNotebookFolder);
		String notebookSearchValue = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(localize(locator.search), notebookSearchValue,
				"Advanced search string not showing blank while click on folder (Notebook)");

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void GeneralPrefDisplayChkboxInListItemFalse() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		// Verify mail list item check box
		String[] recipients = { ClientSessionFactory.session().currentUserName() };
		LmtpUtil.injectMessage(ClientSessionFactory.session().currentUserName(), recipients,
				"ccuser@testdomain.com", "checkboxinlistitem",
				"checkboxinlistitemmailbody");
		MailApp.ClickCheckMailUntilMailShowsUp(
				replaceUserNameInStaticId(page.zMailApp.zInboxFldr),
				"checkboxinlistitem");
		String mailListItemChkBoxExist = obj.zCheckbox
				.zNotExistsDontWait(zMailListItemChkBox);
		assertReport(
				"false",
				mailListItemChkBoxExist,
				"Mail list item check box still showing though 'Display checkboxes to quickly select items in lists' general preference is TRUE");

		// Verify task list item check box
		zGoToApplication("Tasks");
		zWaitTillObjectExist("folder",
				replaceUserNameInStaticId(page.zTaskApp.zTasksFolder));
		String taskListItemChkBoxExist = obj.zCheckbox
				.zNotExistsDontWait(zTaskListItemChkBox);
		assertReport(
				"false",
				taskListItemChkBoxExist,
				"Task list item check box still showing though 'Display checkboxes to quickly select items in lists' general preference is TRUE");

		// Verify briefcase list item check box
		zGoToApplication("Briefcase");
		zWaitTillObjectExist("folder",
				replaceUserNameInStaticId(page.zBriefcaseApp.zBriefcaseFolder));
		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.detailView));
		String briefcaseListItemChkBoxExist = obj.zCheckbox
				.zNotExistsDontWait(zBriefcaseListItemChkBox);
		assertReport(
				"false",
				briefcaseListItemChkBoxExist,
				"Briefcase list item check box still showing though 'Display checkboxes to quickly select items in lists' general preference is TRUE");

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void VerifyGeneralPreferenceDBValues() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		zGoToApplication("Preferences");

		// Verify database values by setting check box ON / OFF
		String getJunkStatus = Stafzmprov.getAccountPreferenceValue(
				currentloggedinuser, "zimbraPrefIncludeSpamInSearch");
		String getTrashStatus = Stafzmprov.getAccountPreferenceValue(
				currentloggedinuser, "zimbraPrefIncludeTrashInSearch");
		String getSearchyStringStatus = Stafzmprov.getAccountPreferenceValue(
				currentloggedinuser, "zimbraPrefShowSearchString");
		String getShowChkboxStatus = Stafzmprov.getAccountPreferenceValue(
				currentloggedinuser, "zimbraPrefShowSelectionCheckbox");

		assertReport(
				"FALSE",
				Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
						"zimbraPrefIncludeSpamInSearch"),
				"'Include Junk Folder in Searches' general preference not marked FALSE in database");
		assertReport(
				"FALSE",
				Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
						"zimbraPrefIncludeTrashInSearch"),
				"'Include Trash Folder in Searches' general preference not marked FALSE in database");
		assertReport(
				"FALSE",
				Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
						"zimbraPrefShowSearchString"),
				"'Show advanced search language in search toolbar' general preference not marked FALSE in database");
		assertReport(
				"FALSE",
				Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
						"zimbraPrefShowSelectionCheckbox"),
				"'Display checkboxes to quickly select items in lists' general preference not marked FALSE in database");

		if (!ZimbraSeleniumProperties.getStringProperty("locale").equals("ru")) {
			obj.zCheckbox.zClick(localize(locator.includeJunkFolder));
		}
		obj.zCheckbox.zClick(localize(locator.includeTrashFolder));
		obj.zCheckbox.zClick(localize(locator.showSearchString));
		obj.zCheckbox.zClick(localize(locator.showSelectionString));

		obj.zButton.zClick(zPrefSaveIconBtn);
		zWaitTillObjectExist(
				"button",
				replaceUserNameInStaticId(page.zMailApp.zNewFolderOverviewPaneIcon));
		;
		zGoToApplication("Preferences");

		if (!ZimbraSeleniumProperties.getStringProperty("locale").equals("ru")) {
			assertReport(
					"TRUE",
					Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
							"zimbraPrefIncludeSpamInSearch"),
					"'Include Junk Folder in Searches' general preference not marked TRUE in database");
		}
		assertReport(
				"TRUE",
				Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
						"zimbraPrefIncludeTrashInSearch"),
				"'Include Trash Folder in Searches' general preference not marked TRUE in database");
		assertReport(
				"TRUE",
				Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
						"zimbraPrefShowSearchString"),
				"'Show advanced search language in search toolbar' general preference not marked TRUE in database");
		assertReport(
				"TRUE",
				Stafzmprov.getAccountPreferenceValue(currentloggedinuser,
						"zimbraPrefShowSelectionCheckbox"),
				"'Display checkboxes to quickly select items in lists' general preference not marked TRUE in database");

		if (!ZimbraSeleniumProperties.getStringProperty("locale").equals("ru")) {
			obj.zCheckbox.zClick(localize(locator.includeJunkFolder));
		}
		obj.zCheckbox.zClick(localize(locator.includeTrashFolder));
		obj.zCheckbox.zClick(localize(locator.showSearchString));
		obj.zCheckbox.zClick(localize(locator.showSelectionString));

		obj.zButton.zClick(zPrefSaveIconBtn);

		SelNGBase.needReset.set(false);
	}
}
