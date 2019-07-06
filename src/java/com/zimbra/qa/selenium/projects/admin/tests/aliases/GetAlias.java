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
package com.zimbra.qa.selenium.projects.admin.tests.aliases;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.AliasItem;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class GetAlias extends AdminCore {

	public GetAlias() {
		logger.info("New " + GetAlias.class.getCanonicalName());
		super.startingPage=app.zPageManageAliases;
	}


	/**
	 * Testcase : Verify delete alias operation  -- Manage alias View
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Go to Manage alias View.
	 * 3. Select an alias.
	 * 4. Delete an alias using delete button in Gear box menu.
	 * 5. Verify alias is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Verify alias creation operation   -- Manage alias View",
			groups = { "bhr" })

	public void GetAlias_01() throws HarnessException {

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

		// Refresh the account list
		app.zPageManageAliases.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");


		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageManageAliases.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the alias list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for alias "+ aliasEmailAddress + " found: "+ a.getGEmailAddress());
			if ( aliasEmailAddress.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify alias is present in the list!");
	}


	/**
	 * Testcase : Verify created alias is displayed in UI.
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Verify alias is present in the list.
	 * @throws HarnessException
	 */

	@Test (description = "Verify created alias is present in the list view",
			groups = { "sanity" })

	public void GetAlias_02() throws HarnessException {

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

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(aliasEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the alias list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for alias "+ aliasEmailAddress + " found: "+ a.getGEmailAddress());
			if ( aliasEmailAddress.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "See http://bugzilla.zimbra.com/show_bug.cgi?id=4704");
	}
}