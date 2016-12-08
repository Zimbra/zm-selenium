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
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;


public class AddAlias extends AdminCommonTest {

	public AddAlias() {
		logger.info("New "+ AddAlias.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}

	/**
	 * Testcase : Add Alias at account level 
	 * Steps :
	 * 1. Login to Admin Console and go to Manage > Accounts.
	 * 2. Select any account from the list
	 * 3. Click on Add, create alias with details > Save
	 * 4. Verify alias should be created without any error
	 * @throws HarnessException
	 */
	@Test( description = "Edit account - Add Alias at account level ",
			groups = { "smoke", "L1", "network" })

	public void AddAlias_01() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

		AccountItem target = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target);

		String aliasLocalName = "alias" + ConfigProperties.getUniqueString();
		String aliasDomainName = ConfigProperties.getStringProperty("testdomain");
		String aliasEmail = aliasLocalName + "@" + aliasDomainName;

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());


		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// CLick on Aliases
		app.zPageEditAccount.zToolbarPressButton(Button.B_ALIASES);

		// Click "Add"
		app.zPageEditAccount.zToolbarPressButton(Button.B_ADD);

		// Add alias
		form.zAddAccountAliases(aliasLocalName, aliasDomainName);

		// Submit form
		form.zSubmit();

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress()+"</account>"
						+		"</GetAccountRequest>");

		Element email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraMailAlias']", 1);		
		ZAssert.assertEquals(email.getText(), aliasEmail, "Verify the alias is associated with the correct account");

	}

}
