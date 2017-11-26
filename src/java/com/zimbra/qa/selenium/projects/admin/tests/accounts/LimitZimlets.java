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

public class LimitZimlets extends AdminCore {

	public LimitZimlets() {
		logger.info("New "+ LimitZimlets.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}

	@Test (description = "Modify account to verify limited Zimlets available",
			groups = { "smoke", "L1" })

	public void LimitZimlets_01() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

		String unavailableZimlet = "com_zimbra_date";

		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT, account.getEmailAddress());

		// Click on Features
		app.zPageEditAccount.zToolbarPressButton(Button.B_ZIMLETS);

		// Limit zimlet
		form.zLimitZimlets(unavailableZimlet);

		// Save the changes
		form.zSave();

		// Verify zimlet is unavailable for an account
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<account by='name'>"+ account.getEmailAddress() +"</account>"
						+		"</GetAccountRequest>");

		Element[] response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNodes("//admin:a[@n='zimbraZimletAvailableZimlets']");

		boolean actualModifiedZimlets = false;
		for(Element e : response ) {
			if (e.getText().contains(unavailableZimlet)) {
				actualModifiedZimlets = true;
				break;
			}
		}
		ZAssert.assertFalse(actualModifiedZimlets, "Verify modified zimlet unavailable for an account");
	}
}