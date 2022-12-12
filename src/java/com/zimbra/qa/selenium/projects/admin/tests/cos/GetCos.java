/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.cos;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;

public class GetCos extends AdminCore {

	public GetCos() {
		logger.info("New " + GetCos.class.getCanonicalName());
		super.startingPage=app.zPageManageCOS;
	}


	@Test (description = "Verify created cos is displayed in UI - Manage COS list view",
			groups = { "bhr", "testcafe" })

	public void GetCos_01() throws HarnessException {
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Verify the presence of the created COS in UI
		ZAssert.assertTrue(app.zPageManageCOS.zIsCOSPresent(cosName), "Verify the cos is returned correctly");
	}


	@Test (description = "Verify created cos is displayed in UI - Search list view",
			groups = { "sanity", "testcafe" })

	public void GetCos_02() throws HarnessException {
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		SleepUtil.sleepSmall();

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(cosName);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Verify the presence of the created COS in UI
		ZAssert.assertTrue(app.zPageManageCOS.zIsSearchCOSPresent(cosName), "Verify the cos is returned correctly");
	}
}