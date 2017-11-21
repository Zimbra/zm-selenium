/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.AliasItem;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;

public class GlobalHomeSearch extends AdminCore {

	public GlobalHomeSearch() {
		logger.info("New "+ GlobalHomeSearch.class.getCanonicalName());
		super.startingPage = app.zPageManageSearch;
	}


	/**
	 * Testcase : Verify search functionality of all objects.
	 * Steps :
	 * 1. Create a COS using SOAP.
	 * 2. Verify the global search return the cos.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of COS.",
			groups = { "functional", "L2" })

	public void HomeSearchFunctionalityCOS_01() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		app.zPageSearchResults.zSelectSearchObject(app.zPageSearchResults.S_COS);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(cosName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for  "+ cosName + " found: "+ a.getGEmailAddress());
			if (cosName.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the COS is found");
	}


	/**
	 * Testcase : Verify search functionality of all objects.
	 * Steps :
	 * 1. Create an Account using SOAP.
	 * 2. Verify the global search return the Account.
	 * @throws HarnessException
	 */

	@Bugs (ids = "96768")
	@Test (description = "Verify search functionality of Account Object.",
			groups = { "functional", "L2" })

	public void HomeSearchFunctionalityAccount_02() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+		"</CreateAccountRequest>");

		app.zPageSearchResults.zSelectSearchObject(app.zPageSearchResults.S_ACCOUNT);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(account.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the account list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ account.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if (account.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	/**
	 * Testcase : Verify search functionality of all objects.
	 * Steps :
	 * 1. Create an Account using SOAP.
	 * 2. Verify the global search return the resource.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of Resource Object.",
			groups = { "functional", "L2" })

	public void HomeSearchFunctionalityResource_03() throws HarnessException {

		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
		 		+ "<name>" + resource.getEmailAddress() + "</name>"
		 		+ "<a n=\"displayName\">" + resource.getName() + "</a>"
		 		+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
		 		+ "<password>test123</password>"
		 		+ "</CreateCalendarResourceRequest>");

		// Refresh the  list
		app.zPageSearchResults.zSelectSearchObject(app.zPageSearchResults.S_RESOURCE);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the resource list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for  "+ resource.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if (resource.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the resource is found");
	}


	/**
	 * Testcase : Verify search functionality of all objects.
	 * Steps :
	 * 1. Create DL using SOAP.
	 * 2. Verify the global search return the DL.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of DL Object.",
			groups = { "functional", "L2" })

	public void HomeSearchFunctionalityDL_04() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+		"</CreateDistributionListRequest>");

		app.zPageSearchResults.zSelectSearchObject(app.zPageSearchResults.S_DISTRIBUTION_LIST);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(dlEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the DL list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for "+ dl.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if (dl.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the DL is found");
	}


	/**
	 * Testcase : Verify search functionality of all objects.
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Verify the global search return the alias.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of Alias Object.",
			groups = { "functional", "L2" })

	public void HomeSearchFunctionalityAlias_05() throws HarnessException {

		AccountItem target = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);

		// Create a new account in the Admin Console using SOAP
		AliasItem alias = new AliasItem();
		String aliasEmailAddress=alias.getEmailAddress();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<AddAccountAliasRequest xmlns='urn:zimbraAdmin'>"
				+			"<id>" + target.getID() + "</id>"
				+			"<alias>" + aliasEmailAddress + "</alias>"
				+		"</AddAccountAliasRequest>");

		app.zPageSearchResults.zSelectSearchObject(app.zPageSearchResults.S_ALIAS);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(aliasEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the alias list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for  "+ aliasEmailAddress + " found: "+ a.getGEmailAddress());
			if (aliasEmailAddress.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the alias is found");
	}


	/**
	 * Testcase : Verify search functionality of all objects.
	 * Steps :
	 * 1. Create domain using SOAP.
	 * 2. Verify the global search return the Account.
	 * @throws HarnessException
	 */

	@Test (description = "Verify search functionality of domain Object.",
			groups = { "functional", "L2" })

	public void HomeSearchFunctionalityDomain_06() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domain.getName() + "</name>"
				+		"</CreateDomainRequest>");

		app.zPageSearchResults.zSelectSearchObject(app.zPageSearchResults.S_DOMAIN);

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(domain.getName());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the domain list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for  "+ domain.getName()+ " found: "+ a.getGEmailAddress());
			if (domain.getName().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the domain is found");
	}
}