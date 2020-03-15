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
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDistributionList;

public class AddAlias extends AdminCore {

	public AddAlias() {
		logger.info("New "+ AddAlias.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	@Test (description = "Edit DL - Add Alias",
			groups = { "bhr" })

	public void AddAlias_01() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+	"<name>" + dlEmailAddress + "</name>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+	"</CreateDistributionListRequest>");

		String domain = ConfigProperties.getStringProperty("testdomain");
		String aliasLocalName = "alias" + ConfigProperties.getUniqueString();
		String aliasEmail = aliasLocalName + "@" + domain;

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on distribution list to be edited.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Click on Aliases
		app.zPageEditDistributionList.zToolbarPressButton(Button.B_ALIASES);

		// Click "Add"
		app.zPageEditDistributionList.zToolbarPressButton(Button.B_ADD);

		// Add alias
		form.zAddDLAliases(aliasLocalName, domain);

		// Submit the form
		form.zSubmit();

		// Verify Alias is added correctly
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+dlEmailAddress+"</dl>"+
				"</GetDistributionListRequest>");

		Element email = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl/admin:a[@n='mail'][2]", 1);
		ZAssert.assertNotNull(email, "Verify the distribution list is edited successfully");
		ZAssert.assertEquals(email.getText(), aliasEmail, "Verify the alias is associated with the correct account");
	}
}