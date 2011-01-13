package com.zimbra.qa.selenium.projects.zcs.tests.preferences.sharing;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.LmtpUtil;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;




/**
 * @author Jitesh Sojitra
 */
@SuppressWarnings( { "static-access" })
public class SharingPreferences extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "SharingDataProvider")
	protected Object[][] createData(Method method) throws Exception {
		String test = method.getName();
		if (test.equals("shareMailFolderAndAccept")) {
			return new Object[][] { { "Mail Folder", localize(locator.inbox),
					"", Stafzmprov.getRandomAccount().toLowerCase(),
					localize(locator.shareRoleViewer), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		} else if (test.equals("editABShareAndModifyRights")) {
			return new Object[][] { { "Address Book",
					localize(locator.contacts), "",
					Stafzmprov.getRandomAccount().toLowerCase(),
					localize(locator.shareRoleViewer), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		} else if (test.equals("declineCalendarFolderShare")) {
			return new Object[][] { { "Calendar", localize(locator.calendar),
					"", Stafzmprov.getRandomAccount().toLowerCase(),
					localize(locator.shareRoleManager), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		} else if (test.equals("revokeCalFolderShareFromPref")) {
			return new Object[][] { { "Calendar", localize(locator.calendar),
					"", Stafzmprov.getRandomAccount().toLowerCase(),
					localize(locator.shareRoleManager), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		} else if (test.equals("resendMailFolderShareFromPref")) {
			return new Object[][] { { "Mail Folder", localize(locator.inbox),
					"", Stafzmprov.getRandomAccount().toLowerCase(),
					localize(locator.shareRoleViewer), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		} else if (test.equals("shareFldrsFromAppTabAndVerifyinPref")) {
			return new Object[][] { { "", "", "",
					Stafzmprov.getRandomAccount().toLowerCase(),
					localize(locator.shareRoleViewer), "", "", "", "" } };
		} else {
			return new Object[][] { { "Mail Folder", localize(locator.inbox),
					"", Stafzmprov.getRandomAccount().toLowerCase(),
					localize(locator.shareRoleViewer), "", "", "",
					getLocalizedData_NoSpecialChar() } };
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
	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shareMailFolderAndAccept(String application,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		// if we are retrying the test, run cleanup and re-login etc
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		
		checkForSkipException("na", "IE", "42591", "Accepted share entry not showing immediately after accepting share in Preferences > Sharing");
		
		// injecting mail
		zGoToApplication("Mail");
		String currentLoggedinUser = ClientSessionFactory.session().currentUserName().toLowerCase();
		String subject = "subject_shareMailFolderAndAccept";
		String body = "body_shareMailFolderAndAccept";
		String recipients[] = { currentLoggedinUser };
		LmtpUtil.injectMessage(currentLoggedinUser, recipients,
				"ccuser@testdomain.com", subject, body);
		MailApp.ClickCheckMailUntilMailShowsUp(subject);

		// sharing folder via preferences and verifying in 1st list box
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		shareFolder(application, sharingfoldername, sharetype, invitedusers,
				role, message, sharingnoteifany, allowtoseeprivateappt,
				mountingfoldername);
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "1", "3");
		obj.zListItem.zClickItemInSpecificList(invitedusers, "3");

		// verifying share related mail in preferences > sharing
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zRadioBtn.zClick("id=*_user");
		obj.zEditField.zType("id=*_owner", currentLoggedinUser);
		obj.zButton.zClick(localize(locator.findShares));
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "1", "1");
		obj.zListItem.zClickLinkWithInListItemInSpecificList(
				currentLoggedinUser, "1", localize(locator.accept));
		obj.zEditField.zTypeInDlgByName(localize(locator.name),
				mountingfoldername, localize(locator.acceptShare));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.acceptShare));

		// verify accepted share in 2nd list box
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "", "2");
		obj.zListItem.zClickItemInSpecificList(currentLoggedinUser, "2");
		zGoToApplication("Mail");
		obj.zFolder.zClick(mountingfoldername);
		obj.zMessageItem.zClick(subject);
		obj.zFolder.zClick(mountingfoldername);
		obj.zMessageItem.zVerifyIsUnRead(subject);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void editABShareAndModifyRights(String application,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		// if we are retrying the test, run cleanup and re-login etc
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		
		checkForSkipException("all", "IE", "na", "Contact can't be saved via java script in new AB UI");

		// address book folder share
		String currentLoggedinUser = ClientSessionFactory.session().currentUserName().toLowerCase();
		String aSubject = getLocalizedData_NoSpecialChar();
		String aBody = getLocalizedData_NoSpecialChar();
		zGoToApplication("Address Book");
		obj.zButton.zClick(page.zABCompose.zNewMenuDropdownIconBtn);
		obj.zEditField.zType("id=*_FIRST", aSubject);
		obj.zEditField.zType("id=*_LAST", aBody);
		obj.zButton.zClick(page.zABCompose.zSaveContactMenuIconBtn);
		page.zSharing.zShareFolder(application, sharingfoldername, sharetype,
				invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);

		// go to sharing and modify the share from viewer to manager
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "1", "3");
		obj.zListItem
				.zClickItemInSpecificList(localize(locator.edit), "1", "3");
		SelNGBase.labelStartsWith = true;
		obj.zRadioBtn.zClickInDlgByName(localize(locator.shareRoleManager),
				localize(locator.shareProperties));
		SelNGBase.labelStartsWith = false;
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.shareProperties));

		// Decline share and verify in preferences > sharing
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		page.zSharing.zDeclineShare();
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zRadioBtn.zClick("id=*_user");
		obj.zEditField.zType("id=*_owner", currentLoggedinUser);
		obj.zButton.zClick(localize(locator.findShares));
		obj.zListItem.zVerifyItemInSpecificListNotExist(currentLoggedinUser,
				"1", "1"); // here bug 41633

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void declineCalendarFolderShare(String application,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		// if we are retrying the test, run cleanup and re-login etc
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		// calendar
		String currentLoggedinUser = ClientSessionFactory.session().currentUserName().toLowerCase();
		String cSubject = getLocalizedData_NoSpecialChar();
		String cBody = getLocalizedData_NoSpecialChar();
		zGoToApplication("Calendar");
		page.zCalCompose.zCreateSimpleAppt(cSubject, "", "", cBody);
		page.zSharing.zShareFolder("Calendar", page.zCalApp.zCalendarFolder,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);

		// sharing calendar folder via preferences
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		shareFolder("Calendar",
				"id=zti__ZmChooseFolderDialog_Calendar__10_textCell",
				sharetype, invitedusers, localize(locator.shareRoleAdmin),
				message, sharingnoteifany, allowtoseeprivateappt, cSubject);
		obj.zListItem.zClickItemInSpecificList(invitedusers, "1", "3");

		// Decline share and verify in preferences > sharing
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		MailApp
				.ClickCheckMailUntilMailShowsUp(localize(locator.shareCreatedSubject));
		obj.zMessageItem.zClick(localize(locator.shareCreatedSubject));
		obj.zButton.zClick(localize(locator.declineShare));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.declineShare));
		SleepUtil.sleep(2000);

		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zRadioBtn.zClick("id=*_user");
		obj.zEditField.zType("id=*_owner", currentLoggedinUser);
		obj.zButton.zClick(localize(locator.findShares));
		obj.zListItem.zVerifyItemInSpecificListNotExist(currentLoggedinUser,
				"1", "1"); // here bug 41633

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void revokeCalFolderShareFromPref(String application,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		// if we are retrying the test, run cleanup and re-login etc
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		// calendar
		String currentLoggedinUser = ClientSessionFactory.session().currentUserName().toLowerCase();
		String cSubject = getLocalizedData_NoSpecialChar();
		String cBody = getLocalizedData_NoSpecialChar();
		zGoToApplication("Calendar");
		page.zCalCompose.zCreateSimpleAppt(cSubject, "", "", cBody);
		page.zSharing.zShareFolder(application, sharingfoldername, sharetype,
				invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);

		// Decline share and verify in preferences > sharing
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		page.zSharing.zAcceptShare(mountingfoldername);

		// sharing calendar folder via preferences
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(currentLoggedinUser);
		
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zListItem.zClickLinkWithInListItemInSpecificList(invitedusers, "3",
				localize(locator.revoke));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.revokeShare));
		SleepUtil.sleep(1000);

		// shared folder should not contain invitation
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		page.zMailApp
				.ClickCheckMailUntilMailShowsUp(localize(locator.shareRevokedSubject));
		zGoToApplication("Calendar");
		obj.zAppointment.zNotExists(cSubject);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void resendMailFolderShareFromPref(String application,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		// if we are retrying the test, run cleanup and re-login etc
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		// injecting mail
		zGoToApplication("Mail");
		String currentLoggedinUser = ClientSessionFactory.session().currentUserName().toLowerCase();
		String subject = "subject_resendMailFolderShareFromPref";
		String body = "body_resendMailFolderShareFromPref";
		String recipients[] = { currentLoggedinUser };
		LmtpUtil.injectMessage(currentLoggedinUser, recipients,
				"ccuser@testdomain.com", subject, body);
		MailApp.ClickCheckMailUntilMailShowsUp(subject);

		// sharing folder via preferences and verifying in 1st list box
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		shareFolder(application, sharingfoldername, sharetype, invitedusers,
				role, message, sharingnoteifany, allowtoseeprivateappt,
				mountingfoldername);
		obj.zListItem.zClickLinkWithInListItemInSpecificList(invitedusers, "3",
				localize(locator.resend));

		// verify in preferences > sharing
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.byMessage));
		page.zMailApp
				.ClickCheckMailUntilMailShowsUp(localize(locator.shareCreatedSubject));
		obj.zMessageItem.zClick(localize(locator.shareCreatedSubject), "1");
		obj.zMessageItem.zClick(localize(locator.shareCreatedSubject), "2");
		obj.zButton.zClick(page.zMailApp.zViewIconBtn);
		obj.zMenuItem.zClick(localize(locator.byConversation));

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shareTaskFldrFromPrefAndAcceptVerify(String application,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		// if we are retrying the test, run cleanup and re-login etc
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		checkForSkipException("all", "na", "na", "zTaskItem fails to find item from list item");

		String currentLoggedinUser = ClientSessionFactory.session().currentUserName().toLowerCase();

		// tasks
		String tSubject = getLocalizedData_NoSpecialChar();
		String tBody = getLocalizedData_NoSpecialChar();
		zGoToApplication("Tasks");
		page.zTaskApp.zTaskCreateSimple(tSubject, "", "", tBody);

		// sharing all the folders
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		shareFolder("Task Folder",
				"id=zti__ZmChooseFolderDialog_Tasks__15_textCell", sharetype,
				invitedusers, localize(locator.shareRoleManager), message,
				sharingnoteifany, allowtoseeprivateappt, tSubject);

		SleepUtil.sleep(3000);
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "1", "3");

		// verifying share related mail in preferences > sharing
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zRadioBtn.zClick("id=*_user");
		obj.zEditField.zType("id=*_owner", currentLoggedinUser);
		obj.zButton.zClick(localize(locator.findShares));

		// tasks
		obj.zListItem.zClickLinkWithInListItemInSpecificList(
				currentLoggedinUser, "1", localize(locator.accept));
		obj.zEditField.zTypeInDlgByName(localize(locator.name), tSubject,
				localize(locator.acceptShare));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.acceptShare));
		SleepUtil.sleep(2000);
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "1", "2");

		// verification
		zGoToApplication("Tasks");
		obj.zFolder.zClick(tSubject);
		SleepUtil.sleep(1000);
		obj.zTaskItem.zExists(tSubject);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shareFldrsFromAppTabAndVerifyInPref(String application,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		// if we are retrying the test, run cleanup and re-login etc
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String currentLoggedinUser = ClientSessionFactory.session().currentUserName().toLowerCase();

		// mail
		zGoToApplication("Mail");
		page.zSharing.zShareFolder("Mail", page.zMailApp.zInboxFldr, sharetype,
				invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);
		// address book
		zGoToApplication("Address Book");
		page.zSharing.zShareFolder("Address Book", localize(locator.contacts),
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);
		// calendar
		zGoToApplication("Calendar");
		page.zSharing.zShareFolder("Calendar", page.zCalApp.zCalendarFolder,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);
		// tasks
		zGoToApplication("Tasks");
		page.zSharing.zShareFolder("Tasks", page.zTaskApp.zTasksFolder,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);
		// documents
		zGoToApplication("Documents");
		page.zSharing.zShareFolder("Documents",
				page.zDocumentApp.zNotebookFolder, sharetype, invitedusers,
				role, message, sharingnoteifany, allowtoseeprivateappt);
		// briefcase
		zGoToApplication("Briefcase");
		page.zSharing.zShareFolder("Briefcase",
				page.zBriefcaseApp.zBriefcaseFolder, sharetype, invitedusers,
				role, message, sharingnoteifany, allowtoseeprivateappt);

		// sharing all the folders
		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "1", "3");
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "2", "3");
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "3", "3");
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "4", "3");
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "5", "3");
		obj.zListItem.zVerifyItemInSpecificList(invitedusers, "6", "3");

		// verifying share related mail in preferences > sharing
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		
		for (int i = 0; i <= 5; i++) {
			page.zMailApp
					.ClickCheckMailUntilMailShowsUp(localize(locator.shareCreatedSubject));
			obj.zMessageItem.zClick(localize(locator.shareCreatedSubject));
			SleepUtil.sleep(1500);
			obj.zButton.zClick(page.zSharing.zAcceptShareIconBtn);
			obj.zButton.zClickInDlgByName(localize(locator.yes),
					localize(locator.acceptShare));
			SleepUtil.sleep(1500);
		}

		zGoToApplication("Preferences");
		zGoToPreferences("Sharing");
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "1", "2");
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "2", "2");
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "3", "2");
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "4", "2");
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "5", "2");
		obj.zListItem.zVerifyItemInSpecificList(currentLoggedinUser, "6", "2");

		SelNGBase.needReset.set(false);
	}

	private void shareFolder(String application, String sharingfoldername,
			String sharetype, String invitedusers, String role, String message,
			String sharingnoteifany, String allowtoseeprivateappt,
			String mountingfoldername) throws Exception {
		if (application.equalsIgnoreCase("Mail Folder")) {
			obj.zButton.zClick(localize(locator.mailFolder));
			obj.zMenuItem.zClickInDlgByName(localize(locator.mailFolder),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Address Book")) {
			obj.zButton.zClick(localize(locator.mailFolder));
			obj.zMenuItem.zClickInDlgByName(localize(locator.addressBook),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Calendar")) {
			obj.zButton.zClick(localize(locator.mailFolder));
			obj.zMenuItem.zClickInDlgByName(localize(locator.calendar),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Task Folder")) {
			obj.zButton.zClick(localize(locator.mailFolder));
			obj.zMenuItem.zClickInDlgByName(localize(locator.tasksFolder),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Notebook")) {
			obj.zButton.zClick(localize(locator.mailFolder));
			obj.zMenuItem.zClickInDlgByName(localize(locator.notebook),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Briefcase")) {
			obj.zButton.zClick(localize(locator.mailFolder));
			obj.zMenuItem.zClickInDlgByName(localize(locator.briefcase),
					localize(locator.chooseFolder));
		}
		obj.zButton.zClick(localize(locator.share));
		obj.zFolder.zClickInDlgByName(sharingfoldername,
				localize(locator.chooseFolder));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.chooseFolder));

		page.zSharing.zEnterValuesInShareDialog(sharetype, invitedusers, role,
				message, sharingnoteifany, allowtoseeprivateappt);

		if (application.equalsIgnoreCase("Address Book")) {
			obj.zButton.zClick(localize(locator.addressBook), "2");
			obj.zMenuItem.zClickInDlgByName(localize(locator.mailFolder),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Calendar")) {
			obj.zButton.zClick(localize(locator.calendar), "2");
			obj.zMenuItem.zClickInDlgByName(localize(locator.mailFolder),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Task Folder")) {
			obj.zButton.zClick(localize(locator.tasksFolder));
			obj.zMenuItem.zClickInDlgByName(localize(locator.mailFolder),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Notebook")) {
			obj.zButton.zClick(localize(locator.notebook));
			obj.zMenuItem.zClickInDlgByName(localize(locator.mailFolder),
					localize(locator.chooseFolder));
		} else if (application.equalsIgnoreCase("Briefcase")) {
			if (ZimbraSeleniumProperties.getStringProperty("locale").equals("de")) {
				obj.zButton.zClick(localize(locator.briefcase), "3");
			} else {
				obj.zButton.zClick(localize(locator.briefcase), "2");
			}
			obj.zMenuItem.zClickInDlgByName(localize(locator.mailFolder),
					localize(locator.chooseFolder));
		}
	}
}