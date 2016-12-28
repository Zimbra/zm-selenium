/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class ViewRights extends AdminCommonTest {

	public ViewRights() {
		logger.info("New "+ ViewRights.class.getCanonicalName());

		// All tests start at the "Distribution Lists" page
		super.startingPage = app.zPageManageDistributionList;
	}

	@Test( description = "View Rights of Admin DL",
			groups = { "test12", "L2" })
	public void ViewRights_01() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+			"<a xmlns='' n='zimbraIsAdminGroup'>TRUE</a>"
						+		"</CreateDistributionListRequest>");

		// Refresh list 
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
		this.startingPage.zNavigateTo();

		// Click on DL
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on View Rights
		app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_VIEW_RIGHTS);

		// Verify rights are displaying correctly
		ZAssert.assertTrue(app.zPageManageDistributionList.zVerifyTab("Resources"), "Verfiy view search result operation");
		ZAssert.assertTrue(app.zPageManageDistributionList.zVerifyTab("Class of Service"), "Verfiy view search result operation");
		ZAssert.assertTrue(app.zPageManageDistributionList.zVerifyTab("Accounts"), "Verfiy view search result operation");
		ZAssert.assertTrue(app.zPageManageDistributionList.zVerifyTab("Domains"), "Verfiy view search result operation");
		app.zPageMain.logout();
	}

}
