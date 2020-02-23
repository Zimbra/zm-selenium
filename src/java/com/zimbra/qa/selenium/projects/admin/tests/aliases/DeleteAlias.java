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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.AliasItem;
import com.zimbra.qa.selenium.projects.admin.pages.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class DeleteAlias extends AdminCore {

	public DeleteAlias() {
		logger.info("New " + DeleteAlias.class.getCanonicalName());
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

	@Test (description = "Verify delete alias operation  -- Manage alias View",
			groups = { "bhr" })

	public void DeleteAlias_01() throws HarnessException {

		AccountItem target = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
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

		// Click on alias to be deleted.
		app.zPageManageAliases.zListItem(Action.A_LEFTCLICK, alias.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageAliases.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify alias is deleted successfully");
	}


	/**
	 *
	 * Testcase : Verify delete alias operation-- Manage alias View/Right Click Menu
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Go to Manage alias View.
	 * 3. Right Click on an alias.
	 * 4. Delete an alias using delete button in right click menu.
	 * 5. Verify alias is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Verify delete alias operation-- Manage alias View/Right Click Menu",
			groups = { "sanity" })

	public void DeleteAlias_02() throws HarnessException {

		AccountItem target = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
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

		// Click on alias to be deleted.
		app.zPageManageAliases.zListItem(Action.A_RIGHTCLICK, alias.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageAliases.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify alias is deleted successfully");
	}


	/**
	 * Testcase : Verify delete alias operation - Search list view.
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Search created alias.
	 * 3. Select the alias from gear box menu and select delete.
	 * 4. Verify account is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Verify delete alias operation - Search list view",
			groups = { "sanity" })

	public void DeleteAlias_03() throws HarnessException {

		AccountItem target = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);

		// Create a new account in the Admin Console using SOAP
		AliasItem alias = new AliasItem();
		String aliasEmailAddress=alias.getEmailAddress();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<AddAccountAliasRequest xmlns='urn:zimbraAdmin'>"
				+			"<id>" + target.getID() + "</id>"
				+			"<alias>" + aliasEmailAddress + "</alias>"
				+		"</AddAccountAliasRequest>");

		// Enter the search string to find the alias
		app.zPageSearchResults.zAddSearchQuery(aliasEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on alias to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, alias.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify alias is deleted successfully");
	}


	/**
	 *
	 * Testcase : Verify delete alias operation - Search list view/Right Click menu.
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Search created alias.
	 * 3. Select the alias from gear box menu and select delete.
	 * 4. Verify account is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Verify delete alias operation - Search list view/Right Click menu.",
			groups = { "functional" })

	public void DeleteAlias_04() throws HarnessException {

		AccountItem target = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);


		// Create a new account in the Admin Console using SOAP
		AliasItem alias = new AliasItem();
		String aliasEmailAddress=alias.getEmailAddress();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<AddAccountAliasRequest xmlns='urn:zimbraAdmin'>"
				+			"<id>" + target.getID() + "</id>"
				+			"<alias>" + aliasEmailAddress + "</alias>"
				+		"</AddAccountAliasRequest>");

		// Enter the search string to find the alias
		app.zPageSearchResults.zAddSearchQuery(aliasEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Right Click on alias to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, alias.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify alias is deleted successfully");
	}
}