/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditAccount;

public class EditPreferences extends AdminCore {

	public EditPreferences() {
		logger.info("New "+ EditPreferences.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	@Test (description = "Edit account - Edit preferences at account level",
			groups = { "bhr" })

	public void EditPreferences_01() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(),
				ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be edited
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT,
				account.getEmailAddress());

		// Click on preferences
		app.zPageEditAccount.zToolbarPressButton(Button.B_PREFERENCES);

		// Check show seach strings preference
		form.zPreferencesCheckboxSet(Button.B_SHOW_SEARCH_STRINGS, true);

		// Uncheck show imap search folders preference
		form.zPreferencesCheckboxSet(Button.B_SHOW_IMAP_SEARCH_FOLDERS, false);

		// Save the changes
		form.zSave();

		// Verify preferences saved correctly
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+ "<account by='name'>" + account.getEmailAddress() + "</account>" + "</GetAccountRequest>");

		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraPrefShowSearchString']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(), "TRUE", "Verify calendar feature is disabled");

		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode(
				"//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraPrefImapSearchFoldersEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"FALSE", "Verify mail feature is disabled");
	}
}