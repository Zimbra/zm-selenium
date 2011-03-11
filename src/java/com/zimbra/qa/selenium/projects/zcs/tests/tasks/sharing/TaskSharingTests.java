package com.zimbra.qa.selenium.projects.zcs.tests.tasks.sharing;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;



/**
 * @author Jitesh Sojitra
 * 
 *         Below parameter used to pass values from data provider
 * 
 * @param applicationtab
 *            - Mail, Address Book or any other application tab from which you
 *            want to share folder
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

@SuppressWarnings( { "static-access" })
public class TaskSharingTests extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "SharingDataProvider")
	protected Object[][] createData(Method method) throws Exception {
		String test = method.getName();
		if (test.equals("sharingFolderToExternalGuest")) {
			return new Object[][] { { "Tasks", localize(locator.tasks),
					localize(locator.shareWithGuest),
					Stafzmprov.getRandomAccount(), "", "", "", "",
					getLocalizedData_NoSpecialChar() } };
		} else {
			return new Object[][] { { "Tasks", localize(locator.tasks),
					localize(locator.shareWithGuest),
					Stafzmprov.getRandomAccount(), "", "", "", "",
					getLocalizedData_NoSpecialChar() } };
		}
	}

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="tasks";
		super.zLogin();
	}


	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	/**
	 * In this test, user shares folder to external guest & external guest
	 * verifies shared folder and its content
	 * 
	 * 1.Login to user1 and share folder to external guest (for e.g user2)
	 * 2.Login external guest and verify its content
	 */
	@Test(dataProvider = "SharingDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void sharingFolderToExternalGuest(String applicationtab,
			String sharingfoldername, String sharetype, String invitedusers,
			String role, String message, String sharingnoteifany,
			String allowtoseeprivateappt, String mountingfoldername)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("hi", "na", "34080", "'Share Accepted' & 'Share Modified' mail body missing sharing details");

		String currentloggedinuser = ClientSessionFactory.session().currentUserName();
		String subject = getLocalizedData_NoSpecialChar();

		zGoToApplication(applicationtab);
		page.zTaskApp.zTaskCreateSimple(subject, "", "",
				getLocalizedData_NoSpecialChar());
		SleepUtil.sleep(1000);
		obj.zTaskItem.zExists(subject);
		page.zSharing.zShareFolder(applicationtab, sharingfoldername,
				sharetype, invitedusers, role, message, sharingnoteifany,
				allowtoseeprivateappt);
		page.zSharing.zVerifyShareCreatedMailInSentFolder(currentloggedinuser,
				sharingfoldername, sharetype, invitedusers, role,
				sharingnoteifany);

		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(invitedusers);
		page.zSharing.zVerifyShareCreatedMailInInboxFolder(currentloggedinuser,
				sharingfoldername, sharetype, invitedusers, role,
				sharingnoteifany);

		page.zSharing.zAcceptShare(mountingfoldername);
		zGoToApplication(applicationtab);
		obj.zFolder.zClick(mountingfoldername);
		obj.zTaskItem.zExists(subject);

		SelNGBase.needReset.set(false);
	}

}