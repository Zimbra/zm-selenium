/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.search;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageSearch;

public class SearchFunctionality extends AdminCore {

	public SearchFunctionality() {
		logger.info("New "+ SearchFunctionality.class.getCanonicalName());
		super.startingPage = app.zPageManageSearch;
	}


	/**
	 * Testcase : Verify search functionality of all results.
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Verify the account is present in the all results search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of all results.",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_01() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+		"</CreateAccountRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Search --> All Results"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.ALL_RESULT);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of Accounts.
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Verify the account is present in the "Accounts" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of accounts",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_02() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+		"</CreateAccountRequest>");


		/*
		 * Go to navigation path -- "Home --> Search --> Search --> Accounts"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of DL.
	 * Steps :
	 * 1. Create a DL using SOAP.
	 * 2. Verify the DL is present in the "DL" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of DL",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_03() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+		"</CreateDistributionListRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Search --> DLs"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.DISTRIBUTION_LISTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ dl.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( dl.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of Domains.
	 * Steps :
	 * 1. Create a Domain using SOAP.
	 * 2. Verify the Domain is present in the "DL" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of Domain",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_04() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+		"</CreateDomainRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Search --> Domains"
		 */
		app.zPageManageSearch.zSelectTreeItemOfSearch(PageManageSearch.Locators.DOMAINS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ domain.getName() + " found: "+ a.getGEmailAddress());
			if ( domain.getName().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of locked out accounts.
	 * Steps :
	 * 1. Create a locked out account using SOAP.
	 * 2. Verify the account is present in the "locked out account" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of locked out accounts",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_05() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a xmlns='' n='zimbraAccountStatus'>lockout</a>"
				+		"</CreateAccountRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Saved Searches --> Locked Out"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.LOCKED_OUT_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of non-active accounts.
	 * Steps :
	 * 1. Create a pending account using SOAP.
	 * 2. Verify the account is present in the "non-active account" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of non-active accounts",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_06() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a xmlns='' n='zimbraAccountStatus'>pending</a>"
				+		"</CreateAccountRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Saved Searches --> Non-Active accounts"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.NON_ACTIVE_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of admin accounts.
	 * Steps :
	 * 1. Create a admin account using SOAP.
	 * 2. Verify the account is present in the "admin account" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of admin accounts",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_07() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("global_admin" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a xmlns='' n='zimbraIsAdminAccount'>TRUE</a>"
				+		"</CreateAccountRequest>");

		// Create a new account in the Admin Console using SOAP
		AccountItem del_admin_account = new AccountItem("delegated_admin" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + del_admin_account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a xmlns='' n='zimbraIsDelegatedAdminAccount'>TRUE</a>"
				+		"</CreateAccountRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Saved Searches --> admin"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.ADMIN_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the global admin account is found");

		found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ del_admin_account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( del_admin_account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the delegated admin account is found");
	}


	/**
	 * Testcase : Verify search functionality of closed accounts.
	 * Steps :
	 * 1. Create a closed account using SOAP.
	 * 2. Verify the account is present in the "closed account" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of closed accounts",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_08() throws HarnessException {

		// Create a new closed account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a xmlns='' n='zimbraAccountStatus'>closed</a>"
				+		"</CreateAccountRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Saved Searches --> Closed"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.CLOSED_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of maintenance accounts.
	 * Steps :
	 * 1. Create a maintenance account using SOAP.
	 * 2. Verify the account is present in the "maintenance account" search.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of maintenance accounts",
			groups = { "smoke", "L1" })

	public void SearchFunctionality_09() throws HarnessException {

		// Create a new maintenance account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+			"<a xmlns='' n='zimbraAccountStatus'>maintenance</a>"
				+		"</CreateAccountRequest>");

		/*
		 * Go to navigation path -- "Home --> Search --> Saved Searches --> maintenance"
		 */
		app.zPageManageSearch.zSelectTreeItem(PageManageSearch.TreeItem.MAINTENANCE_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}
}