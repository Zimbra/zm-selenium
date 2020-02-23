/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.accounts;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;

public class GetAccount extends AdminCore {

	public GetAccount() {
		logger.info("New "+ GetAccount.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	/**
	 * Testcase : Verify created account is displayed in UI -- Manage Account View.
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Verify account is present in the list.
	 * @throws HarnessException
	 */

	@Test (description = "Verify created account is displayed in UI -- Manage Account View.",
			groups = { "smoke" })

	public void GetAccount_01() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageManageAccounts.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account " + account.getEmailAddress() + " found: " + a.getGEmailAddress());
			if (account.getEmailAddress().equals(a.getGEmailAddress())) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify created account is displayed in UI -- Search list view.
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account
	 * 3. Verify account is present in the list.
	 * @throws HarnessException
	 */

	@Test (description = "Verify created account is displayed in UI -- Search list view",
			groups = { "functional" })

	public void GetAccount_02() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account " + account.getEmailAddress() + " found: " + a.getGEmailAddress());
			if (account.getEmailAddress().equals(a.getGEmailAddress())) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Bug 40300 : Verify system accounts i.e. spam/ham, wiki, galsync accounts are not displayed in Account List.
	 * Steps :
	 * 1. Login to Admin Console.
	 * 2. Click on Accounts option in the Left Navigation pane.
	 * 3. List of all the accounts is displayed.
	 * 4. Verify spam/ham, wiki, galsync accounts are not displayed in Account List.
	 * @throws HarnessException
	 */

	@Test (description = "Verify that system accounts such as spam/ham, wiki and galsync accounts are not displayed in the list.",
			groups = { "functional" })

	public void GetAccount_03() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");


		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
		SleepUtil.sleepMedium();

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageManageAccounts.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account " + "ham." + " found: " + a.getGEmailAddress());
			if ((a.getGEmailAddress().contains("ham."))) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify alias is deleted successfully");

		AccountItem found_spam = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account " + "spam." + " found: " + a.getGEmailAddress());
			if ((a.getGEmailAddress().contains("spam."))) {
				found_spam = a;
				break;
			}
		}
		ZAssert.assertNull(found_spam, "Verify alias is deleted successfully");

		AccountItem found_galsync = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account " + "galsync." + " found: " + a.getGEmailAddress());
			if ((a.getGEmailAddress().contains("galsync."))) {
				found_galsync = a;
				break;
			}
		}
		ZAssert.assertNull(found_galsync, "Verify alias is deleted successfully");

	}

	/**
	 * Testcase : Verify system accounts i.e. spam/ham, wiki, galsync account is displayed in Search list view
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Search account
	 * 3. Verify account is present in the list.
	 * @throws HarnessException
	 */

	@Test (description = "Verify system accounts i.e. spam/ham, wiki, galsync account is displayed in Search list view",
			groups = { "sanity" })

	public void GetAccount_04() throws HarnessException {

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery("ham.");

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account " + "ham." + " found: " + a.getGEmailAddress());
			if ((a.getGEmailAddress().contains("ham."))) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery("spam.");

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Get the list of displayed accounts
		List<AccountItem> accounts1 = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts1, "Verify the account list is returned");

		AccountItem found1 = null;
		for (AccountItem a : accounts1) {
			logger.info("Looking for account " + "spam." + " found: " + a.getGEmailAddress());
			if ((a.getGEmailAddress().contains("spam."))) {
				found1 = a;
				break;
			}
		}
		ZAssert.assertNotNull(found1, "Verify the account is found");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery("galsync.");

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Get the list of displayed accounts
		List<AccountItem> accounts2 = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts2, "Verify the account list is returned");

		AccountItem found2 = null;
		for (AccountItem a : accounts2) {
			logger.info("Looking for account " + "galsync." + " found: " + a.getGEmailAddress());
			if ((a.getGEmailAddress().contains("galsync."))) {
				found2 = a;
				break;

			}
		}
		ZAssert.assertNotNull(found2, "Verify the account is found");
	}
}