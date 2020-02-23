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
package com.zimbra.qa.selenium.projects.admin.tests.accounts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditAccount;
import com.zimbra.qa.selenium.projects.admin.pages.WizardChangePassword;

public class ChangePassword extends AdminCore {

	public ChangePassword() {
		logger.info("New "+ ChangePassword.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	/**
	 * Testcase : Edit password  -- manage account >> right click
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name using UI Right Click.
	 * 3. Verify password is changed using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit password  -- manage account > Gearbox > edit account > change password",
			groups = { "bhr" })

	public void ChangePassword_01() throws HarnessException {

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

		// Click on account to be edited
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Fill out the wizard
		form.zSetPassword("test1234");

		// Save the changes
		form.zSave();

		// Confirm that the new password is in use by getting a new token
		app.zGetActiveAccount().soapSend(
					"<AuthRequest xmlns='urn:zimbraAccount'>"
				+		"<account by='name'>"+ account.getEmailAddress() +"</account>"
				+		"<password>"+ "test1234" +"</password>"
				+	"</AuthRequest>");
		String token = app.zGetActiveAccount().soapSelectValue("//acct:AuthResponse//acct:authToken", null);
		ZAssert.assertGreaterThan(token.trim().length(), 0, "Verify the token is returned");

		app.zGetActiveAccount().authenticate();
	}


	/**
	 * Testcase : Edit password  -- manage account >> right click
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name using UI Right Click.
	 * 3. Verify password is changed using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit password  -- manage account > right click > change password",
			groups = { "bhr" })

	public void ChangePassword_02() throws HarnessException {

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

		// Right Click on account to be edited
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, Button.O_CHANGE_PASSWORD, account.getEmailAddress());

		// Right Click account >> "Change password"
		WizardChangePassword wizard = new WizardChangePassword(startingPage);

		// Fill out the wizard
		wizard.zCompleteWizard(account);

		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Confirm that the new password is in use by getting a new token
		app.zGetActiveAccount().soapSend(
					"<AuthRequest xmlns='urn:zimbraAccount'>"
				+		"<account by='name'>"+ account.getEmailAddress() +"</account>"
				+		"<password>"+ "test1234" +"</password>"
				+	"</AuthRequest>");
		String token = app.zGetActiveAccount().soapSelectValue("//acct:AuthResponse//acct:authToken", null);
		ZAssert.assertGreaterThan(token.trim().length(), 0, "Verify the token is returned");
	}
}