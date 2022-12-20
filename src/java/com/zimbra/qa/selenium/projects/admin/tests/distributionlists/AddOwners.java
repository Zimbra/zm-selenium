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
package com.zimbra.qa.selenium.projects.admin.tests.distributionlists;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDistributionList;

public class AddOwners extends AdminCore {

	public AddOwners() {
		logger.info("New "+ AddOwners.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	@Test (description = "Edit DL - Add Owner to DL",
			groups = { "bhr", "testcafe" })

	public void AddOwner_01() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateDistributionListRequest>");

		String aliasDomainName = ConfigProperties.getStringProperty("testdomain");

		// Create owner account
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateAccountRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on distribution list to be edited.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Click on owners
		app.zPageEditDistributionList.zToolbarPressButton(Button.B_OWNER);

		// Click "Add"
		app.zPageEditDistributionList.zToolbarPressButton(Button.B_ADD);

		// Add owner
		form.zAddDLOwner(account.getLocalName(), aliasDomainName);

		// Submit the form
		form.zSubmit();

		// Verify owner is added correctly
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+dlEmailAddress+"</dl>"+
				"</GetDistributionListRequest>");

		String email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetDistributionListResponse/admin:dl/admin:owners/admin:owner", "name");
		ZAssert.assertStringContains(email, account.getEmailAddress() , "Verify the alias is associated with the correct account");
	}
}