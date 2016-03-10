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

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardChangePassword;

public class ChangePassword extends AdminCommonTest {

	public ChangePassword() throws HarnessException {
		logger.info("New "+ ChangePassword.class.getCanonicalName());

		//All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccounts;
	}

	/**
	 * Testcase : Delegated Admin - Edit password  -- manage account > Gearbox > edit account > change password 
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name - manage account > Gearbox > edit account > change password
	 * 3. Verify password is changed using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Delegated Admin - Edit password  -- manage account > Gearbox > edit account > change password ",
			groups = { "functional" })
	public void ChangePassword_01() throws HarnessException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();

		// Create a new account in the Delegated Admin 
		String account = ZimbraAccount.AccountZWC().EmailAddress;
		
		//Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
		
		//Navigate to manage accounts page
		app.zPageManageAccounts.zNavigateTo();

		//Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account);

		//Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on General Information tab.
		form.zClickTreeItem(FormEditAccount.TreeItem.GENERAL_INFORMATION);

		//Edit the password
		String editedPassword = "password_" + ZimbraSeleniumProperties.getUniqueString();
		form.setPassword(editedPassword);

		//Submit the form.
		form.zSubmit();

		//Confirm that the new password is in use
		//by getting a new token
		app.zGetActiveAccount().soapSend(
				"<AuthRequest xmlns='urn:zimbraAccount'>"
						+		"<account by='name'>"+ account +"</account>"
						+		"<password>"+ editedPassword +"</password>"
						+	"</AuthRequest>");
		String token = app.zGetActiveAccount().soapSelectValue("//acct:AuthResponse//acct:authToken", null);
		ZAssert.assertGreaterThan(token.trim().length(), 0, "Verify the token is returned");
		app.zPageMain.logout();
	}
	
	/**
	 * Testcase : Edit password  -- manage account >> right click 
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name using UI Right Click.
	 * 3. Verify password is changed using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Edit password  -- manage account >> right click",
			groups = { "functional" })
	public void ChangePassword_02() throws HarnessException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();

		// Create a new account in the Delegated Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ZimbraSeleniumProperties.getUniqueString(),ZimbraSeleniumProperties.getStringProperty("testdomain"));
		
		// Create a new account in the Delegated Admin 
		String new_account = ZimbraAccount.AccountZWC().EmailAddress;
		
		//Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
		
		//Navigate to manage accounts page
		app.zPageManageAccounts.zNavigateTo();
		
		//Right Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, new_account);
		
		//Right Click account >>  "Change password"
		WizardChangePassword wizard = 
			(WizardChangePassword)app.zPageManageAccounts.zToolbarPressButton(Button.B_CHANGE_PASSWORD);

		//Fill out the wizard	
		wizard.zCompleteWizard(account);
				
		//Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
		//Confirm that the new password is in use
		//by getting a new token
		app.zGetActiveAccount().soapSend(
					"<AuthRequest xmlns='urn:zimbraAccount'>"
				+		"<account by='name'>"+ new_account +"</account>"
				+		"<password>"+ "test1234" +"</password>"
				+	"</AuthRequest>");
		String token = app.zGetActiveAccount().soapSelectValue("//acct:AuthResponse//acct:authToken", null);
		ZAssert.assertGreaterThan(token.trim().length(), 0, "Verify the token is returned");
		app.zPageMain.logout();
	}
}
