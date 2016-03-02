/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.delegatedadmin;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateAccount;

public class AccountManage extends AdminCommonTest {

	public AccountManage() throws HarnessException {
		logger.info("New "+ AccountManage.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccounts;


	}

	/**
	 * Testcase : Login as Delegated admin and Create a basic account
	 * Steps :
	 * 1. Create an account from GUI i.e. Gear Box -> New.
	 * 2. Verify account is created using SOAP.
	 * @throws HarnessException
	 */


	@Test(	description = "Delegated Admin's Create a basic account using New->Account",
			groups = { "sanity" })
	public void CreateAccount_01() throws HarnessException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();
		AccountItem account = new AccountItem("email" + ZimbraSeleniumProperties.getUniqueString(),ZimbraSeleniumProperties.getStringProperty("testdomain"));

		// Click "New" -> "Account" at Delelgated Admin manage account page
		WizardCreateAccount wizard = 
				(WizardCreateAccount)app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(account);

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1); 
		ZAssert.assertNotNull(response, "Verify the account is created successfully at DA");
	}

	/**
	 * Testcase : Delete a basic account -- Manage Account View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Go to Delegated Admin's Manage Account View.
	 * 3. Select an Account.
	 * 4. Delete an account using delete button in Gear box menu.
	 * 5. Verify account is deleted using SOAP.
	 * 
	 * @throws HarnessException
	 */
	@Test(	description = "Delegated Admin's: Delete a basic account -- Manage Account View",
			groups = { "sanity" })
	public void DeleteAccount_01() throws HarnessException {

		// Create and authenticate delelgated admin
		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();


		// Create a new account in the Delegated Admin 
		String account = ZimbraAccount.AccountZWC().EmailAddress;

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be deleted.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account);


		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);


		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account +"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1); 
		ZAssert.assertNull(response, "Verify the account is deleted successfully at DA");


	}


	/**
	 * Testcase : Edit account name  - Manage Account View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Go to Delegated Admin's Manage Account View
	 * 3. Select an Account.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify account is edited using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Delegated Admin's : Edit Account name  - Manage Account View",
			groups = { "sanity" })
	public void EditAccount_01() throws HarnessException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();

		// Create a new account in the Delegated Admin 
		String account = ZimbraAccount.AccountZWC().EmailAddress;

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account);

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on General Information tab.
		form.zClickTreeItem(FormEditAccount.TreeItem.GENERAL_INFORMATION);

		//Edit the name.
		String editedName = "editedAccount_" + ZimbraSeleniumProperties.getUniqueString();
		form.setNameAsDA(editedName);

		//Submit the form.
		form.zSubmit();

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ editedName+"@" + ZimbraSeleniumProperties.getStringProperty("testdomain", "testdomain.com")+"</account>"
						+		"</GetAccountRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account", 1);
		ZAssert.assertNotNull(response, "Verify the account is edited successfully at DA");
	}
}
