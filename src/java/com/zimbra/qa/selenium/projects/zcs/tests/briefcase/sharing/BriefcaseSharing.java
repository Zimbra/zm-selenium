package com.zimbra.qa.selenium.projects.zcs.tests.briefcase.sharing;

import java.lang.reflect.Method;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;



/**
 * @author Jitesh Sojitra
 * 
 *         Below parameter used to pass values from data provider
 * 
 * @param filename
 *            - specify filename - to be uploaded
 * @param applicationtab
 *            - Mail, Address Book or any other application tab from which you
 *            want to share folder - here it is Briefcase
 * @param sharingfoldername
 *            - Folder to be shared
 * @param sharetype
 *            - Either Internal, External or public
 * @param invitedusers
 *            - Email id to whom folder to be shared - as of now it is random
 *            account created by Stafzmprov.getRandomAccount() method
 * @param role
 *            - Either None, Viewer, Manager or Admin
 * @param message
 *            - Either Send message, No message, Add note or composing mail
 *            regarding shares
 * @param sharingnoteifany
 *            - Applicable only if Add note selected for previous message
 *            parameter
 * @param allowtoseeprivateappt
 *            - Applicable only for calendar folder sharing
 * @param mountingfoldername
 *            - While other user mount the share, he can specify his own name
 *            using this parameter
 * 
 */

@SuppressWarnings("static-access")
public class BriefcaseSharing extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@SuppressWarnings("unused")
	@DataProvider(name = "BriefcaseSharing")
	private Object[][] createData(Method method) throws Exception {
		String test = method.getName();
		if (test.equals("shareAsViewerAndVerifyFileUpload"))
			return new Object[][] { { "data/public/other/testsoundfile.wav", "Briefcase",
					localize(locator.briefcase), "",
					Stafzmprov.getRandomAccount(),
					localize(locator.shareRoleViewer), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		else if (test.equals("shareAsManagerAndVerifyFileUpload"))
			return new Object[][] { { "data/public/other/testwordfile.doc", "Briefcase",
					localize(locator.briefcase), "",
					Stafzmprov.getRandomAccount(),
					localize(locator.shareRoleManager), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		else if (test.equals("verifyBriefcaseSendLinkFunctionality"))
			return new Object[][] { { "data/public/other/testtextfile.txt", "Briefcase",
					localize(locator.briefcase), "",
					Stafzmprov.getRandomAccount(),
					localize(locator.shareRoleManager), "", "", "",
					getLocalizedData_NoSpecialChar() } };
		else if (test.equals("publicBriefcaseSharing"))
			return new Object[][] { { "data/public/other/structure.jpg", "Briefcase",
					localize(locator.briefcase),
					localize(locator.shareWithPublicLong),
					Stafzmprov.getRandomAccount(), "", "", "", "",
					getLocalizedData_NoSpecialChar() } };
		else
			return new Object[][] { { "data/public/other/samlejpg.jpg", "Briefcase",
					localize(locator.briefcase), "",
					Stafzmprov.getRandomAccount(),
					localize(locator.shareRoleViewer), "", "", "",
					getLocalizedData_NoSpecialChar() } };
	}

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="briefcase";
		super.zLogin();
	}

	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	/**
	 * In this test user1 uploads files in his Briefcase folder, shares this
	 * folder as viewer rights, and user2 verifies file deletion (-ve case)
	 * 
	 * 1.Login to user1, go to briefcase application tab 2.Upload file in his
	 * briefcase folder, share this folder as viewer rights 3.Login to user2,
	 * accept share and verify mounted folder 4.Click to briefcase shared folder
	 * >> verify file exists 5.Click to file and verify Delete toolbar button
	 * remains disabled 6.Right click to mail and verify Delete menu item
	 * remains disabled 7.(-ve case) Try to upload file, it should give warning
	 * message for permission denied
	 */
	@Test(dataProvider = "BriefcaseSharing", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shareAsViewerAndVerifyFileUpload(String filename,
			String applicationtab, String sharingfoldername, String sharetype,
			String invitedusers, String role, String message,
			String sharingnoteifany, String allowtoseeprivateappt,
			String mountingfoldername) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zBriefcaseApp.zBriefcaseFileUpload(filename, "");
		page.zSharing.zShareFolder(applicationtab, sharingfoldername,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		page.zSharing.zAcceptShare(mountingfoldername);
		page.zBriefcaseApp.zGoToBriefcaseApp();
		obj.zFolder.zClick(mountingfoldername);
		obj.zBriefcaseItem.zExists(filename);
		obj.zBriefcaseItem.zClick(filename);
		obj.zButton.zIsDisabled(page.zBriefcaseApp.zDeleteBtn);
		obj.zBriefcaseItem.zRtClick(filename);
		obj.zMenuItem.zIsDisabled(localize(locator.del));

		SelNGBase.needReset.set(false);
	}

	/**
	 * In this test user1 uploads files in his Briefcase folder, shares this
	 * folder as manager rights, and user2 verifies file deletion (+ve case)
	 * 
	 * 1.Login to user1, go to briefcase application tab 2.Upload file in his
	 * briefcase folder, share this folder as manager rights 3.Login to user2,
	 * accept share and verify mounted folder 4.Click to briefcase shared folder
	 * >> verify file exists 5.Click to file and verify Delete toolbar button
	 * remains enabled 6.Right click to mail and verify Delete menu item remains
	 * enabled 7.Upload new file in shared folder, delete one of the file from 2
	 * file 8.Login to user1, go to Briefcase and verify deleted file not exists
	 * and other file exists
	 */
	@Test(dataProvider = "BriefcaseSharing", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shareAsManagerAndVerifyFileUpload(String filename,
			String applicationtab, String sharingfoldername, String sharetype,
			String invitedusers, String role, String message,
			String sharingnoteifany, String allowtoseeprivateappt,
			String mountingfoldername) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String currentloggedinuser = ClientSessionFactory.session().currentUserName();
		page.zBriefcaseApp.zBriefcaseFileUpload(filename, "");
		page.zSharing.zShareFolder(applicationtab, sharingfoldername,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		page.zSharing.zAcceptShare(mountingfoldername);
		page.zBriefcaseApp.zGoToBriefcaseApp();

		String newfilename = "data/public/other/putty.log";
		page.zBriefcaseApp
				.zBriefcaseFileUpload(newfilename, mountingfoldername);
		obj.zFolder.zClick(mountingfoldername);
		obj.zBriefcaseItem.zClick(filename);
		obj.zButton.zClick(page.zBriefcaseApp.zDeleteIconBtn);
		obj.zDialog.zExists(localize(locator.confirmTitle));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.confirmTitle));
		obj.zMenuItem.zNotExists(filename);

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(currentloggedinuser);
		page.zBriefcaseApp.zGoToBriefcaseApp();
		obj.zFolder.zClick(sharingfoldername);
		obj.zBriefcaseItem.zExists(newfilename);
		obj.zBriefcaseItem.zNotExists(filename);

		SelNGBase.needReset.set(false);
	}

	/**
	 * In this test user1 uploads files in his Briefcase folder, sends link to
	 * that user & shares this folder as manager rights, and user2 verifies
	 * corresponding functionality
	 * 
	 * 1.Login to user1, go to briefcase application tab 2.Upload file in his
	 * briefcase folder, share this folder as manager rights 3.Login to user2,
	 * accept share and verify mounted folder 4.verify both mail - share created
	 * and received link 5.Click to briefcase shared folder and verify
	 * corresponding functionality
	 */
	@Test(dataProvider = "BriefcaseSharing", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyBriefcaseSendLinkFunctionality(String filename,
			String applicationtab, String sharingfoldername, String sharetype,
			String invitedusers, String role, String message,
			String sharingnoteifany, String allowtoseeprivateappt,
			String mountingfoldername) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "34080", "not able to write password in password edit field in FF & SF - works fine only in IE (selenium bug)");

		page.zBriefcaseApp.zBriefcaseFileUpload(filename, "");
		obj.zBriefcaseItem.zClick(filename);
		obj.zButton.zClick(localize(locator.send));
		SleepUtil.sleep(1000);
		obj.zMenuItem.zClick(localize(locator.send));
		obj.zButton.zClickInDlgByName(localize(locator.yes),
				localize(locator.confirmTitle));
		String subjectValue = obj.zEditField
				.zGetInnerText(page.zComposeView.zSubjectField);
		Assert
				.assertNotSame(subjectValue, filename,
						"Subject edit field value mismatched while send link from briefcase");
		obj.zTextAreaField.zType(page.zComposeView.zToField, invitedusers);
		obj.zButton.zClick(page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(1500);

		page.zBriefcaseApp.zGoToBriefcaseApp();
		page.zSharing.zShareFolder(applicationtab, sharingfoldername,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);

		resetSession();
		
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		page.zSharing.zAcceptShare(mountingfoldername);
		page.zMailApp.ClickCheckMailUntilMailShowsUp(filename);
		obj.zMessageItem.zExists(filename);
		page.zBriefcaseApp.zGoToBriefcaseApp();
		obj.zFolder.zClick(mountingfoldername);
		obj.zBriefcaseItem.zExists(filename);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "BriefcaseSharing", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void publicBriefcaseSharing(String filename, String applicationtab,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zBriefcaseApp.zBriefcaseFileUpload(filename, "");
		page.zSharing.zShareFolder(applicationtab, sharingfoldername,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);
		page.zLoginpage.logoutOfZimbraAjax();
		SleepUtil.sleep(3000);
		ClientSessionFactory.session().selenium().open(
				ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
						+ ZimbraSeleniumProperties.getStringProperty("server.host")
						+ "/home/"
						+ ClientSessionFactory.session().currentUserName().toLowerCase() + "/"
						+ "Briefcase");
		zWaitTillObjectExist(
				"xpath",
				"//td[contains(@class, 'zmwiki-pageLink')]//a[contains(text(), 'structure.jpg')]");
		zWaitTillObjectExist("xpath",
				"//td[contains(@class, 'zmwiki-author') and contains(text(), '"
						+ ClientSessionFactory.session().currentUserName().toLowerCase() + "')]");
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(ClientSessionFactory.session().currentUserName());

		SelNGBase.needReset.set(false);
	}
}