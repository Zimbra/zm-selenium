//Test cases related to address book preferences

package com.zimbra.qa.selenium.projects.html.tests.preferences;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.html.tests.CommonTest;


@SuppressWarnings( { "static-access", "unused" })
public class ABPreference extends CommonTest {

	@DataProvider(name = "ABDataProvider")
	public Object[][] createData(Method method) {
		String test = method.getName();

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
	 * Test case to verify next and previous page arrow button and the Contacts
	 * displayed per page functionality .Selects contacts displayed per page to
	 * 10 then add 11 contacts.Then verify the next and previous page buttons
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "ABDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyNextPrevButton() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		int noOfContacts = 11;
		String commaSeparatedContacts = page.zABComposeHTML
				.zCreateCommaSeparatedString(noOfContacts);
		String[] contactsArray = commaSeparatedContacts.split(",");
		Arrays.sort(contactsArray);

		page.zABComposeHTML.zNavigateToPreferenceAB();
		SleepUtil.sleepSmall();// wait because it takes some time to load page after
		// cliking on Pref-AB
		page.zABComposeHTML.zSelectContactPerPage("10");
		obj.zButton.zClick(page.zABComposeHTML.zPrefSaveButton);
		SleepUtil.sleepMedium();// to wait for preferences to be saved
		page.zABComposeHTML.zVerifyContactToasterMsgs(obj.zToastAlertMessage
				.zGetMsg(), localize(locator.optionsSaved));
		page.zABComposeHTML.zNavigateToCnctAndCreateMultipleCncts(noOfContacts,
				commaSeparatedContacts);
		page.zABComposeHTML.zClickArrowAndVerifyContactExist("NextArrow",
				contactsArray[noOfContacts - 1]);

		page.zABComposeHTML.zClickArrowAndVerifyContactExist("PreviousArrow",
				contactsArray[noOfContacts - 8]);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Test to verify enabled auto adding of contacts
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "ABDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyEnabledAutoAddingOfContacts() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String contactFoldername = localize(locator.emailedContacts);
		if (ZimbraSeleniumProperties.getStringProperty("locale").equals("fr")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("nl")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("es")) {
			String[] testArray = contactFoldername.split(" ");
			contactFoldername = testArray[0] + " ...";
		} else if (!(ZimbraSeleniumProperties.getStringProperty("locale").equals("nl") || ZimbraSeleniumProperties.getStringProperty("locale").equals("hi"))) {
			if (contactFoldername.length() > 16) {
				String[] testArray = contactFoldername.split(" ");
				if (testArray[2].length() > 3) {
					contactFoldername = testArray[0] + " " + testArray[1] + " "
							+ "...";
				} else {
					contactFoldername = testArray[0] + " " + testArray[1] + " "
							+ testArray[2] + " " + "...";
				}
			}
		}

		page.zABComposeHTML
				.zSendMailToSelfAndNavigateToSpecificContactsFolder(contactFoldername);
		obj.zCheckbox.zNotExists("link=" + ClientSessionFactory.session().currentUserName());

		// commented for time being because of bug 32738
		// to verify enabled auto add to contacts selected
		// page.zABComposeHTML.
		// zNavigateToPrefABAndClickEnabledAutoAddingOfContacts();
		//page.zABComposeHTML.zSendMailToSelfAndNavigateToSpecificContactsFolder
		// (localize(locator.emailedContacts));
		// obj.zCheckbox.zExists("link=" + SelNGBase.selfAccountName);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Test to verify Manage Address Book link in AB preference
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "ABDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyManageAddressbookLink() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zABComposeHTML.zNavigateToPreferenceAB();
		SleepUtil.sleepSmall();// wait because it takes some time to load page after
		// cliking on Pref-AB
		ClientSessionFactory.session().selenium().click("link="
				+ localize(locator.optionsManageAddressBooksLink));
		obj.zButton.zExists(page.zABComposeHTML.zNewABIconBtn);
		SelNGBase.needReset.set(false);
	}

	// since all the tests are independent, retry is simply kill and re-login
	private void handleRetry() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		zLogin();
	}

}
