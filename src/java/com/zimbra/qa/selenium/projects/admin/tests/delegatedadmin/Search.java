/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.delegatedadmin;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageSearch;

public class Search extends AdminCommonTest {

	public Search() {
		logger.info("New "+ Search.class.getCanonicalName());		
	}

	/**
	 * Testcase : Verify search functionality of all results.
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Verify the account is present in the all results search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of all results.",
			groups = { "smoke", "L1" })
	public void SearchAllResults_01() throws HarnessException {		

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
		app.zPageManageSearch.zClickTreeItemOfSearch(PageManageSearch.Locators.ALL_RESULT);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify that the account is displayed in the search result
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(account.getEmailAddress()), "Verify the account is found");
	}

	/**
	 * Testcase : Verify search functionality of Accounts.
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Verify the account is present in the "Accounts" search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of Accounts.",
			groups = { "smoke", "L1" })
	public void SearchAccounts_02() throws HarnessException {		

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
		app.zPageManageSearch.zClickTreeItemOfSearch(PageManageSearch.Locators.ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify that the account is displayed in the search result
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(account.getEmailAddress()), "Verify the account is found");
	}

	/**
	 * Testcase : Verify search functionality of DL.
	 * Steps :
	 * 1. Create a DL using SOAP.
	 * 2. Verify the DL is present in the "DL" search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of DL",
			groups = { "smoke", "L1" })
	public void SearchDistributionList_03() throws HarnessException {
		
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
		app.zPageManageSearch.zClickTreeItemOfSearch(PageManageSearch.Locators.DISTRIBUTION_LISTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify that the dl is displayed in the search result
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(dlEmailAddress), "Verify the dl is found");
	}



	/**
	 * Testcase : Verify search functionality of locked out accounts.
	 * Steps :
	 * 1. Create a locked out account using SOAP.
	 * 2. Verify the account is present in the "locked out account" search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of locked out accounts.",
			groups = { "smoke", "L1" })
	public void SearchLockedAccount_05() throws HarnessException {

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
		app.zPageManageSearch.zClickTreeItem(PageManageSearch.TreeItem.LOCKED_OUT_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify that the locked out account is displayed in the search result
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(account.getEmailAddress()), "Verify the locked out account is found");		
	}

	/**
	 * Testcase : Verify search functionality of non-active accounts.
	 * Steps :
	 * 1. Create a pending account using SOAP.
	 * 2. Verify the account is present in the "non-active account" search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of non-active accounts.",
			groups = { "smoke", "L1" })
	public void SearchNonactiveAccount_06() throws HarnessException {
		
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
		app.zPageManageSearch.zClickTreeItem(PageManageSearch.TreeItem.NON_ACTIVE_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify that the non-active out is displayed in the search result
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(account.getEmailAddress()), "Verify the non-active account is found");
	}

	/**
	 * Testcase : Verify search functionality of admin accounts.
	 * Steps :
	 * 1. Create a admin account using SOAP.
	 * 2. Verify the account is present in the "admin account" search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of admin accounts.",
			groups = { "smoke", "L1" })
	public void SearchAdmin_07() throws HarnessException {

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
		app.zPageManageSearch.zClickTreeItem(PageManageSearch.TreeItem.ADMIN_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify the global admin account is found
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(account.getEmailAddress()), "Verify the global admin account is found");

		//Verify the delegated admin account is found
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(del_admin_account.getEmailAddress()), "Verify the delegated admin account is found");
	}

	/**
	 * Testcase : Verify search functionality of closed accounts.
	 * Steps :
	 * 1. Create a closed account using SOAP.
	 * 2. Verify the account is present in the "closed account" search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of closed accounts.",
			groups = { "smoke", "L1" })
	public void SearchClosedAccount_08() throws HarnessException {	

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
		app.zPageManageSearch.zClickTreeItem(PageManageSearch.TreeItem.CLOSED_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify that the closed account is displayed in the search result
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(account.getEmailAddress()), "Verify the closed account is found");
	}


	/**
	 * Testcase : Verify search functionality of maintenance accounts.
	 * Steps :
	 * 1. Create a maintenance account using SOAP.
	 * 2. Verify the account is present in the "maintenance account" search.
	 * @throws HarnessException
	 */
	@Test( description = "Verify search functionality of maintenance accounts.",
			groups = { "smoke", "L1" })
	public void SearchMaintenanceAccount_10() throws HarnessException {	

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
		app.zPageManageSearch.zClickTreeItem(PageManageSearch.TreeItem.MAINTENANCE_ACCOUNTS);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		//Verify that the maintenance account is displayed in the search result
		ZAssert.assertTrue(app.zPageSearchResults.zIsPresentInSearchResult(account.getEmailAddress()), "Verify the closed maintenance is found");
	}

	@BeforeClass( groups = { "always" } )
	public void beforeClassMethod() throws HarnessException {		
		
		//Delegated admin authentication is required before all of these test 
		app.provisionAuthenticateDA();
		super.startingAccount = (ZimbraAdminAccount) app.zGetActiveAccount();
		app.zPageManageSearch.zNavigateTo();		
	}
}
