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

public class EditFeatures extends AdminCore {

	public EditFeatures() {
		logger.info("New "+ EditFeatures.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	@Test (description = "Edit account - Edit features",
			groups = { "bhr", "testcafe" })

	public void EditFeatures_01() throws HarnessException {
		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(),
				ConfigProperties.getStringProperty("testdomain"));

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateAccountRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT,
				account.getEmailAddress());

		// Click on Features
		app.zPageEditAccount.zToolbarPressButton(Button.B_FEATURES);

		// Uncheck Mail
		form.zFeatureCheckboxSet(Button.B_MAIL, false);

		// Uncheck Calendar
		form.zFeatureCheckboxSet(Button.B_CALENDAR, false);

		// Save the changes
		form.zSave();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetAccountRequest xmlns='urn:zimbraAdmin'>"
				+ "<account by='name'>" + account.getEmailAddress() + "</account>" + "</GetAccountRequest>");

		// Verify calendar feature is disabled
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode(
				"//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureCalendarEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(), "FALSE", "Verify calendar feature is disabled");

		// Verify mail feature is disabled
		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin()
				.soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMailEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"FALSE", "Verify mail feature is disabled");
	}
}