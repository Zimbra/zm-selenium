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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDistributionList;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class EditPreferences extends AdminCore {

	public EditPreferences() {
		logger.info("New "+ EditPreferences.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	@Bugs (ids = "104654")
	@Test (description = "Edit DL - Edit Preferences",
			groups = { "bhr" })

	public void EditPreferences_01() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();
		String displayName=dl.getLocalName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+			"<a n='description'>Created by Selenium automation</a>"
						+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on distribution list to be edited.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Click on Properties
		app.zPageEditDistributionList.zToolbarPressButton(Button.B_PREFERENCES);

		// check Set the "Reply-to" field of email messages
		form.zPreferencesCheckboxSet(Button.B_SET_REPLY_TO,true);

		// set Display name in "Reply-to" message:
		form.zSetDisplayNameInReplyToMessage(displayName);

		// set "Reply-to" address
		form.zSetReplyToAddress(dlEmailAddress);

		// Submit the form
		form.zSubmit();

		// Verify Properties
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+dlEmailAddress+"</dl>"+
				"</GetDistributionListRequest>");

		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl/admin:a[@n='zimbraPrefReplyToEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the distribution list is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"TRUE", "Verify Reply-to field of email messages preference is checked!");

		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl/admin:a[@n='zimbraPrefReplyToDisplay']", 1);
		ZAssert.assertNotNull(response2, "Verify the distribution list is edited successfully");
		ZAssert.assertStringContains(response2.toString(),displayName, "Verify display name");

		Element response3 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl/admin:a[@n='zimbraPrefReplyToAddress']", 1);
		ZAssert.assertNotNull(response3, "Verify the distribution list is edited successfully");
		ZAssert.assertStringContains(response3.toString(),dlEmailAddress, "Verify Reply-to address");
		app.zPageMain.zLogout();
	}
}