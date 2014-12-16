/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.tests.distributionlists;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class DeleteAdminDistributionList extends AdminCommonTest {

	public DeleteAdminDistributionList() {
		logger.info("New "+ DeleteAdminDistributionList.class.getCanonicalName());

		// All tests start at the "Distribution Lists" page
		super.startingPage = app.zPageManageDistributionList;
	}

	/**
	 * Testcase : Verify delete operation for admin DL - Manage distribution list view.
	 * Steps :
	 * 1. Create a admin DL using SOAP.
	 * 2. Go to Manage dl View.
	 * 3. Select an dl.
	 * 4. Delete an dl using delete button in Gear box menu.
	 * 5. Verify dl is deleted using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Verify delete operation for admin distribution list - Manage distribution list view",
			groups = { "test111" })
			public void DeleteAdminDistributionList_01() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a xmlns='' n='zimbraIsAdminGroup'>TRUE</a>"
				+		"</CreateDistributionListRequest>");

		// Refresh list to populate account.
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);


		// Verify the dl does not exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
				"<dl by='name'>"+dlEmailAddress+"</dl>"+
		"</GetDistributionListRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNull(response, "Verify the distribution list is deleted successfully");

	}
}
