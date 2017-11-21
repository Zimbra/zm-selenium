/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateAccount;

public class CreateAccount extends AdminCore {

	public CreateAccount() {
		logger.info("New "+ CreateAccount.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	/**
	 * Testcase : Create a basic account
	 * Steps :
	 * 1. Create an account from GUI i.e. Gear Box -> New.
	 * 2. Verify account is created using SOAP.
	 * @throws HarnessException
	 */

	@Bugs (ids = "100779")
	@Test (description = "Create a basic account using New->Account",
			groups = { "sanity", "L0" })

	public void CreateAccount_01() throws HarnessException {

		String surName = "lastName" + ConfigProperties.getUniqueString();

		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(), surName);

		// Click "New" -> "Account"
		WizardCreateAccount wizard = (WizardCreateAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.zCompleteWizard(account);

		// Verify the account exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+	"<account by='name'>"+ account.getEmailAddress() +"</account>"
				+"</GetAccountRequest>");

		ZAssert.assertEquals(ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectValue("//admin:GetAccountResponse/admin:account", "name"), account.getEmailAddress(),
				"Verify account name set successfully");
		ZAssert.assertEquals(
				ZimbraAdminAccount.AdminConsoleAdmin()
						.soapSelectValue("//admin:GetAccountResponse/admin:account/admin:a[@n='sn']", null),
				surName, "Verify account lastname set successfully");
		ZAssert.assertEquals(
				ZimbraAdminAccount.AdminConsoleAdmin()
						.soapSelectValue("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraMailHost']", null),
				account.zGetAccountStoreHost(), "Verify store host set successfully");
	}
}