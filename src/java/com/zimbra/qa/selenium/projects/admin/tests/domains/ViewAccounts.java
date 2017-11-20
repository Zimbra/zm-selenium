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
package com.zimbra.qa.selenium.projects.admin.tests.domains;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DomainItem;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class ViewAccounts extends AdminCore {

	public ViewAccounts() {
		logger.info("New "+ ViewAccounts.class.getCanonicalName());
		super.startingPage = app.zPageManageDomains;
	}


	@Test (description = "Verify edit domain operation --  View Accounts",
			groups = { "smoke", "L1" })

	public void ViewAccounts_01() throws HarnessException {

		// Create a new domain in the Admin Console using SOAP
		DomainItem domain = new DomainItem();
		String domainName=domain.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDomainRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + domainName + "</name>"
				+		"</CreateDomainRequest>");

		// Create admin account
		String adminaccount = "admin"+ ConfigProperties.getUniqueString() +"@"+domainName;
		ZimbraAdminAccount account = new ZimbraAdminAccount(adminaccount);
		account.provision();

		// Refresh the domain list
		app.zPageManageDomains.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on Domain
		app.zPageManageDomains.zListItem(Action.A_LEFTCLICK, domain.getName());

		// Click on View accounts
		app.zPageManageDomains.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_VIEW_ACCOUNTS);

		// Verify domain account list is displyed on UI
		ZAssert.assertTrue(app.zPageManageDomains.zVerifySearchResult(adminaccount), "Verify domain account list");
	}
}