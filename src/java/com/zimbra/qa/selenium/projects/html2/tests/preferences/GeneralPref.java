/**
 *Test cases related to preferences general
 * 
 * @author Prashant Jaiswal
 * 
 */

package com.zimbra.qa.selenium.projects.html2.tests.preferences;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.projects.html2.tests.CommonTest;


/**
 * @author VICKY JAISWAL
 * 
 */
@SuppressWarnings( { "static-access", "unused" })
public class GeneralPref extends CommonTest {
	private String constantSubjectForJunk = getLocalizedData_NoSpecialChar();
	private String constantSubjectForTrash = getLocalizedData_NoSpecialChar();

	@DataProvider(name = "GeneralPrefDataProvider")
	private Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("verifyIncludeJunkFolderInSearch")
				|| test.equals("verifyIncludeTrashFolderInSearch")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar() } };
		} else {
			return new Object[][] { {} };
		}
	}

	@BeforeClass(groups = { "always" })
	private void zLogin() throws Exception {

		zLoginIfRequired();
		SelNGBase.isExecutionARetry.set(false);
	}

	@BeforeMethod(groups = { "always" })
	private void zResetIfRequired() throws Exception {
		if (SelNGBase.needReset.get() && !SelNGBase.isExecutionARetry.get()) {
			zLogin();
		}
		SelNGBase.needReset.set(true);
	}

	/**
	 * Test to verify the change password functionality
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "GeneralPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void ChangePwdRelogin() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zGeneralPrefUI.zNavigateToChangePasswordWindow();

		page.zGeneralPrefUI.zEnterChangePWData("test123", "test321", "test321");
		obj.zButton.zClick("class=zLoginButton");
		SleepUtil.sleepMedium();

		resetSession();
		page.zLoginpage
				.zLoginToZimbraHTML(ClientSessionFactory.session().currentUserName(), "test321");

		resetSession();

		String accountName = Stafzmprov.getRandomAccount();
		
		page.zLoginpage.zLoginToZimbraHTML(accountName);

		SelNGBase.needReset.set(false);

	}

	/**
	 * To verify the junk folder is included in search
	 * 
	 * @param body
	 * @throws Exception
	 */
	@Test(dataProvider = "GeneralPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyIncludeJunkFolderInSearch(String body) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zGeneralPrefUI.zNavigateToPrefGenralAndSelectSearchFolder("Junk");

		// to have a mail in Junk folder
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndSelectIt(ClientSessionFactory.session().currentUserName(),
				"", "", constantSubjectForJunk, body, "");
		obj.zCheckbox.zClick(constantSubjectForJunk);
		SleepUtil.sleepSmall();
		obj.zHtmlMenu.zClick("name=actionOp", localize(locator.actionSpam));

		// To verify the message moved in junk folder
		obj.zFolder.zClick(page.zMailApp.zJunkFldr);
		SleepUtil.sleepSmall();
		obj.zMessageItem.zExists(constantSubjectForJunk);

		page.zGeneralPrefUI
				.zSearchUsingMainSearchField(ClientSessionFactory.session().currentUserName());

		obj.zMessageItem.zExists(constantSubjectForJunk);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Negative test to include junk folder in search
	 */
	@Test(dataProvider = "GeneralPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void NegativeTestIncludeJunkFolderInSearch() throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		IncludeJunkFolderInSearch();
		// This test works on the message which is in junk folder from test
		// verifyIncludeJunkFolderInSearch
		page.zGeneralPrefUI.zNavigateToPrefGenralAndSelectSearchFolder("Junk");

		page.zGeneralPrefUI
				.zSearchUsingMainSearchField(ClientSessionFactory.session().currentUserName());
		obj.zMessageItem.zNotExists(constantSubjectForJunk);

		SelNGBase.needReset.set(false);
	}

	/**
	 * To verify trash folder is included in search
	 * 
	 * @param body
	 * @throws Exception
	 */
	@Test(dataProvider = "GeneralPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyIncludeTrashFolderInSearch(String body) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zGeneralPrefUI.zNavigateToPrefGenralAndSelectSearchFolder("Trash");

		// to have a mail in Junk folder
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndSelectIt(ClientSessionFactory.session().currentUserName(),
				"", "", constantSubjectForTrash, body, "");
		obj.zCheckbox.zClick(constantSubjectForTrash);
		obj.zButton.zClick(localize(locator.del));

		// To verify the message moved in junk folder
		obj.zFolder.zClick(page.zMailApp.zTrashFldr);
		SleepUtil.sleepSmall();
		obj.zMessageItem.zExists(constantSubjectForTrash);

		page.zGeneralPrefUI
				.zSearchUsingMainSearchField(ClientSessionFactory.session().currentUserName());

		obj.zMessageItem.zExists(constantSubjectForTrash);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Negative test to test include trash folder in search
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "GeneralPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void NegativeTestIncludeTrashFolderInSearch() throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		IncludeTrashFolderInSearch();
		// This test works on the message which is in junk folder from test
		// verifyIncludeJunkFolderInSearch
		page.zGeneralPrefUI.zNavigateToPrefGenralAndSelectSearchFolder("Trash");

		page.zGeneralPrefUI
				.zSearchUsingMainSearchField(ClientSessionFactory.session().currentUserName());
		obj.zMessageItem.zNotExists(constantSubjectForTrash);

		SelNGBase.needReset.set(false);
	}

	/**
	 * To verify search string is displayed in in the search bar
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "GeneralPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAlwaysShowSrchString() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zGeneralPrefUI
				.zNavigateToPrefGenralAndSelectAlwaysShowSrchString();
		obj.zTab.zClick(localize(locator.mail));
		SleepUtil.sleepSmall();
		obj.zFolder.zClick(page.zMailApp.zInboxFldr);
		SleepUtil.sleepSmall();
		String actualValueDisplayed = obj.zEditField
				.zGetInnerText(page.zGeneralPrefUI.zFindEditFiled);
		Assert.assertTrue(actualValueDisplayed.equals("in:\"Inbox\""),
				"The string in:\"Inbox\" is not displayed in find edit field");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Negative test to include search string in the search string
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "GeneralPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void NegativeTestAlwaysShowSrchString() throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		AlwaysShowSrchString();

		page.zGeneralPrefUI
				.zNavigateToPrefGenralAndSelectAlwaysShowSrchString();
		obj.zTab.zClick(localize(locator.mail));
		obj.zFolder.zClick(page.zMailApp.zInboxFldr);
		String actualValueDisplayed = obj.zEditField
				.zGetInnerText(page.zGeneralPrefUI.zFindEditFiled);
		Assert.assertTrue(actualValueDisplayed.equals("<blank>"),
				"The Find edit field is not blank ");
		SelNGBase.needReset.set(false);
	}

	// since all the tests are independent, retry is simply kill and re-login
	private void handleRetry() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		zLogin();
	}

	private void IncludeJunkFolderInSearch() throws Exception {
		handleRetry();
		verifyIncludeJunkFolderInSearch(getLocalizedData_NoSpecialChar());
	}

	private void IncludeTrashFolderInSearch() throws Exception {
		handleRetry();
		verifyIncludeTrashFolderInSearch(getLocalizedData_NoSpecialChar());
	}

	private void AlwaysShowSrchString() throws Exception {
		handleRetry();
		verifyAlwaysShowSrchString();
	}
}
