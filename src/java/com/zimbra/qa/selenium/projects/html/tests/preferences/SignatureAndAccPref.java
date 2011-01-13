/**
 *Test cases related to preferences signature and preference account
 * 
 * @author Prashant Jaiswal
 * 
 */

package com.zimbra.qa.selenium.projects.html.tests.preferences;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.projects.html.tests.CommonTest;




/**
 * @author VICKY JAISWAL
 * 
 */
@SuppressWarnings( { "static-access", "unused" })
public class SignatureAndAccPref extends CommonTest {

	// following two private variables are needed as
	// verifySignatureBelowIncludedMsg test depends on
	// verifySignatureAboveIncludedMsg
	private String constantSubject = getLocalizedData_NoSpecialChar();
	private String constantSignatureBody = getLocalizedData_NoSpecialChar();

	@DataProvider(name = "AccPrefDataProvider")
	private Object[][] createData(Method method) throws ServiceException, HarnessException {
		String test = method.getName();
		if (test.equals("createSignatureAndVerifyInAccTab")
				|| test.equals("create2ndSignatureAndVerifyInAccTab")
				|| test.equals("deleteSignatureAndVerifyInAccTab")
				|| test.equals("verifySignatureInMailCompose")
				|| test.equals("verifySignatureAboveIncludedMsg")
				|| test.equals("verifySignatureBelowIncludedMsg")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData_NoSpecialChar() } };
		} else if (test.equals("verifyAccountsSettings")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData_NoSpecialChar(),
					getLocalizedData_NoSpecialChar(),
					getLocalizedData_NoSpecialChar(),
					Stafzmprov.getRandomAccount() } };
		}

		else
			return new Object[][] { {} };
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
	 * To create signature and verify it is displayed in account tab
	 * 
	 * @param signatureName
	 * @param signatureBody
	 * @throws Exception
	 */
	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full",
			"enabled" }, retryAnalyzer = RetryFailedTests.class)
	public void createSignatureAndVerifyInAccTab(String signatureName,
			String signatureBody) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Need to find out a way to write in html signatures : Still not working.");

		page.zAccPref.zNavigateToPrefSignatureAndCreateSignature(signatureName,
				signatureBody, "");
		SleepUtil.sleepSmall();
		page.zAccPref.zVerifyPrefToasterMsgs(obj.zToastAlertMessage.zGetMsg(),
				localize(locator.optionsSaved));

		page.zAccPref.zNavigateToAccPrefAndVerifySignature(signatureName);

		// page.zAccPref.zNavigateToPreferenceSignature();

		SelNGBase.needReset.set(false);
	}

	/**
	 * To create second signature and to verify it is displayed in acc tab
	 * .Depends on test createSignatureAndVerifyInAccTab
	 * 
	 * @param signatureName
	 * @param signatureBody
	 * @throws Exception
	 */
	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full", "enabled" }, retryAnalyzer = RetryFailedTests.class)
	public void create2ndSignatureAndVerifyInAccTab(String signatureName,
			String signatureBody) throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		createSignatureAndVerify();

		checkForSkipException("all", "na", "na", "Need to find out a way to write in html signatures : Still not working.");

		page.zAccPref.zNavigateToPrefSignatureAndCreateSignature(signatureName,
				signatureBody, "");
		page.zAccPref.zNavigateToAccPrefAndVerifySignature(signatureName);

		page.zAccPref.zNavigateToPreferenceSignature();
//		obj.zButton.zClick(localize(locator.del));
		SelNGBase.needReset.set(false);
	}

	/* to write delete signature tests .there is problem in clicking on delete
	 button at the moment
		
	 /** Test to delete signature and verify the deletion
	 * 
	 * @param signatureName
	 * @param signatureBody
	 *@throws Exception

	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full", "enabled" }, retryAnalyzer = RetryFailedTests.class)
	public void deleteSignatureAndVerifyInAccTab(String signatureName,
			String signatureBody) throws Exception {
		//if (isExecutionARetry)
		create2ndSignatureAndVerify();
		obj.zButton.zClick(localize(locator.del));
		needReset = false;
	}*/

	/**
	 * Test to verify navigation of Accounts Options link in signature tab
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAccountsOptionsLink() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Need to find out a way to write in html signatures : Still not working.");

		page.zAccPref.zNavigateToPreferenceSignature();
		SleepUtil.sleepSmall();//this wait is req for some locales
		ClientSessionFactory.session().selenium().click("link=" + localize(locator.optionsManageAccountsLink));
		obj.zEditField.zExists(page.zAccPref.zAccNameEditField);
		SelNGBase.needReset.set(false);
	}

	/**
	 * Test to create signature and to verify signature display in mail compose
	 * 
	 * @param signatureName
	 * @param signatureBody
	 * @throws Exception
	 */
	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifySignatureInMailCompose(String signatureName,
			String signatureBody) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Need to find out a way to write in html signatures : Still not working.");

		page.zAccPref.zNavigateToPrefSignatureAndCreateSignature(signatureName,
				signatureBody, "");
		SleepUtil.sleepMedium();
		page.zComposeView.zNavigateToMailCompose();
		SleepUtil.sleepMedium();
		String displayedSignature = obj.zTextAreaField
				.zGetInnerText(page.zComposeView.zBodyTextAreaField);
		Assert.assertTrue(displayedSignature.contains(signatureBody),
				" The signature " + signatureName
						+ "  is not displayed in mail compose");
		SelNGBase.needReset.set(false);
	}

	/**
	 * To verify signature is displayed above the composed message as per the
	 * settings in signature preference
	 * 
	 * @param signatureName
	 * @param signatureBody
	 * @throws Exception
	 */
	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifySignatureAboveIncludedMsg(String signatureName,
			String signatureBody) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Need to find out a way to write in html signatures : Still not working.");

		page.zAccPref.zNavigateToPrefSignatureAndCreateSignature(signatureName,
				constantSignatureBody, "Above");
		SleepUtil.sleepSmall();
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndSelectIt(ClientSessionFactory.session().currentUserName(),
				"", "", constantSubject, "", "");

		page.zAccPref.zClickReplyToAMailAndVerifySignaturePlace("Above",
				signatureBody);

		SelNGBase.needReset.set(false);
	}

	/**
	 * To verify the signature display in mail compose is below the mail body
	 * 
	 * @param signatureName
	 * @param signatureBody
	 * @throws Exception
	 */
	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifySignatureBelowIncludedMsg(String signatureName,
			String signatureBody) throws Exception {
		//if (SelNGBase.isExecutionARetry.get())
		verifyAboveBelowSignature();

		checkForSkipException("all", "na", "na", "Need to find out a way to write in html signatures : Still not working.");

		page.zAccPref.zNavigateToPreferenceSignature();
		page.zAccPref.zChangeSignaturePlacment("Below");
		obj.zTab.zClick(localize(locator.mail));

		SleepUtil.sleepSmall();

		obj.zMessageItem.zClick(constantSubject);
		page.zAccPref.zClickReplyToAMailAndVerifySignaturePlace("Below",
				constantSignatureBody);

		SelNGBase.needReset.set(false);
	}

	/**
	 * To enter account details and save it.Then to verify the account
	 * details(i.e.fromField, replyToName, replyToAcc) in the mail compose by
	 * sending mail to self.
	 * 
	 * @param signatureName
	 * @param signatureBody
	 * @param fromField
	 * @param replyToName
	 * @param replyToAcc
	 * @throws Exception
	 */
	@Test(dataProvider = "AccPrefDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAccountsSettings(String signatureName,
			String signatureBody, String fromField, String replyToName,
			String replyToAcc) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Need to find out a way to write in html signatures : Still not working.");

		page.zAccPref.zNavigateToPrefSignatureAndCreateSignature(signatureName,
				signatureBody, "");
		page.zAccPref.zNavigateToPreferenceAccounts();
		page.zAccPref.zMakeAccSettings("", fromField, "setReplyTo",
				replyToName, replyToAcc, signatureName);
		SleepUtil.sleepMedium();
		page.zComposeView.zNavigateToMailCompose();
		page.zAccPref.zVerifySignatureInMailCompose(signatureBody);

		page.zAccPref.zSendMailToSelfAndVerifyAccSettings(fromField,
				replyToName, replyToAcc);

		SelNGBase.needReset.set(false);
	}

	// since all the tests are independent, retry is simply kill and re-login
	private void handleRetry() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		zLogin();
	}

	private void createSignatureAndVerify() throws Exception {
		handleRetry();
		createSignatureAndVerifyInAccTab(getLocalizedData_NoSpecialChar(),
				getLocalizedData_NoSpecialChar());
	}

	private void create2ndSignatureAndVerify() throws Exception {// this retry
		// method
		// will be
		// used by
		// delete
		// signature
		// test.
		handleRetry();
		create2ndSignatureAndVerifyInAccTab(getLocalizedData_NoSpecialChar(),
				getLocalizedData_NoSpecialChar());
	}

	private void verifyAboveBelowSignature() throws Exception {// this retry
		handleRetry();
		verifySignatureAboveIncludedMsg(getLocalizedData_NoSpecialChar(),
				getLocalizedData_NoSpecialChar());
	}

}
