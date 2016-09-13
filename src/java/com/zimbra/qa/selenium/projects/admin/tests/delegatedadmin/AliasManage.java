/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.AliasItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.ui.PageEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateAlias;


public class AliasManage extends AdminCommonTest {

	public AliasManage() throws HarnessException {
		logger.info("New "+ AliasManage.class.getCanonicalName());

		// All tests start at the "Alias" page
		super.startingPage=app.zPageManageAliases;

	}

	/**
	 * Testcase : Delegated Admin: Create a basic alias
	 * 1. Create a alias with GUI.
	 * 2. Verify alias is created using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Delegated Admin: Create a basic alias",
			groups = { "smoke" })
	public void CreateAlias_01() throws HarnessException {

		app.provisionAuthenticateDA();

		AccountItem target = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);

		// Navigate to craete alias page
		this.startingPage.zNavigateTo();

		AliasItem alias = new AliasItem();		// Create a new account in the Admin Console using SOAP
		alias.setTargetAccountEmail(target.getEmailAddress());


		// Click "New"
		WizardCreateAlias wizard = 
				(WizardCreateAlias)app.zPageManageAliases.zToolbarPressButton(Button.B_NEW);

		// Fill out the wizard	
		wizard.zCompleteWizard(alias);
		
		// Wait for alias creation
		SleepUtil.sleepMedium();

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ alias.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");
		String email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "name");
		ZAssert.assertEquals(email, target.getEmailAddress(), "Verify the alias is associated with the correct account");
	}

	/**
	 * Testcase : Delegated Admin: Edit alias
	 * Steps :
	 * 1. Edit an alias from GUI 
	 * 2. Verify edit account page opened up
	 * @throws HarnessException
	 */
	@Test( description = "Delegated Admin: Edit alias",
			groups = { "functional" })
	public void EditAlias_01() throws HarnessException {

		app.provisionAuthenticateDA();

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

		// Navigate to alias page
		this.startingPage.zNavigateTo();

		// Click on alias to be edited.
		app.zPageManageAliases.zListItem(Action.A_LEFTCLICK, alias.getEmailAddress());

		// Click on Edit button
		app.zPageManageAliases.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);
		SleepUtil.sleepMedium();

		// Verify edit account page opened up 
		ZAssert.assertTrue(app.zPageEditAccount.sIsElementPresent(PageEditAccount.ztab_ACCOUNT_EDIT_GENERAL_INFORMATION), "Verify general tab is displayed");

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ alias.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");
		String email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:account", "name");
		ZAssert.assertEquals(email, target.getEmailAddress(), "Verify the alias is associated with the correct account");
		app.zPageMain.logout();
	}

	/**
	 * Testcase : Delegated Admin: Verify delete alias operation  -- Manage alias View
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Go to Manage alias View.
	 * 3. Select an alias.
	 * 4. Delete an alias using delete button in Gear box menu.
	 * 5. Verify alias is deleted using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Delegated Admin: Verify delete alias operation  -- Manage alias View",
			groups = { "functional" })
	public void DeleteAlias_01() throws HarnessException {

		app.provisionAuthenticateDA();

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

		// Navigate to alias page
		this.startingPage.zNavigateTo();

		// Click on alias to be deleted.
		app.zPageManageAliases.zListItem(Action.A_LEFTCLICK, alias.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageAliases.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);
		
		// Wait for alias deletion
		SleepUtil.sleepMedium();

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
	 * Testcase : Delegated Admin: Verify delete alias operation-- Manage alias View/Right Click Menu
	 * Steps :
	 * 1. Create an alias using SOAP.
	 * 2. Go to Manage alias View.
	 * 3. Right Click on an alias.
	 * 4. Delete an alias using delete button in right click menu.
	 * 5. Verify alias is deleted using SOAP.
	 * @throws HarnessException
	 */
	@Test( description = "Delegated Admin: Verify delete alias operation-- Manage alias View/Right Click Menu",
			groups = { "functional" })
	public void DeleteAlias_02() throws HarnessException {

		app.provisionAuthenticateDA();
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

		// Navigate to alias page
		this.startingPage.zNavigateTo();

		// Click on alias to be deleted.
		app.zPageManageAliases.zListItem(Action.A_RIGHTCLICK, alias.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageAliases.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);

		// Wait for alias deletion
		SleepUtil.sleepMedium();
				
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

}
