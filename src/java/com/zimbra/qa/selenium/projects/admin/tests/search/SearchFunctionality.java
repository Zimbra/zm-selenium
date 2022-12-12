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


	@Test (description = "Verify search functionality of all results",
			groups = { "bhr", "testcafe" })

	public void SearchFunctionality_01() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Go to navigation path -- Home --> Search --> Search --> All Results
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


	@Test (description = "Verify search functionality of accounts",
			groups = { "bhr", "testcafe" })

	public void SearchFunctionality_02() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");


		// Go to navigation path -- Home --> Search --> Search --> Accounts
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


	@Test (description = "Verify search functionality of DL",
			groups = { "bhr", "testcafe" })

	public void SearchFunctionality_03() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDistributionListRequest>");

		// Go to navigation path -- Home --> Search --> Search --> DLs
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


	@Test (description = "Verify search functionality of Domain",
			groups = { "bhr", "testcafe" })

	public void SearchFunctionality_04() throws HarnessException {
		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDomainRequest>");

		// Go to navigation path -- Home --> Search --> Search --> Domains
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


	@Test (description = "Verify search functionality of locked out accounts",
			groups = { "functional", "testcafe" })

	public void SearchFunctionality_05() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='zimbraAccountStatus'>lockout</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Go to navigation path -- Home --> Search --> Saved Searches --> Locked Out
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


	@Test (description = "Verify search functionality of non-active accounts",
			groups = { "functional", "testcafe" })

	public void SearchFunctionality_06() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='zimbraAccountStatus'>pending</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Go to navigation path -- Home --> Search --> Saved Searches --> Non-Active accounts
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


	@Test (description = "Verify search functionality of admin accounts",
			groups = { "functional", "testcafe" })

	public void SearchFunctionality_07() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tcadmin" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='zimbraIsAdminAccount'>TRUE</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Create a new account in the Admin Console using SOAP
		AccountItem del_admin_account = new AccountItem("tcda" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + del_admin_account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='zimbraIsDelegatedAdminAccount'>TRUE</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Go to navigation path -- Home --> Search --> Saved Searches --> admin
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


	@Test (description = "Verify search functionality of closed accounts",
			groups = { "functional", "testcafe" })

	public void SearchFunctionality_08() throws HarnessException {
		// Create a new closed account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='zimbraAccountStatus'>closed</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Go to navigation path -- Home --> Search --> Saved Searches --> Closed
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


	@Test (description = "Verify search functionality of maintenance accounts",
			groups = { "functional", "testcafe" })

	public void SearchFunctionality_09() throws HarnessException {
		// Create a new maintenance account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='zimbraAccountStatus'>maintenance</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Go to navigation path -- Home --> Search --> Saved Searches --> maintenance
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