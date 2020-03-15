/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.cos;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditCos;

public class EditFeatures extends AdminCore {

	public EditFeatures() {
		logger.info("New "+ EditFeatures.class.getCanonicalName());
		super.startingPage = app.zPageManageCOS;
	}


	@Test (description = "Edit COS - Edit features",
			groups = { "bhr" })

	public void EditFeatures_01() throws HarnessException {
		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be edited
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);

		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on Features
		app.zPageEditCOS.zToolbarPressButton(Button.B_FEATURES);

		// Uncheck Mail
		form.zFeatureCheckboxSet(Button.B_MAIL,false);

		// Uncheck Calendar
		form.zFeatureCheckboxSet(Button.B_CALENDAR,false);

		// Submit the form
		form.zSubmit();

		// Verify mail and calendar features are disabled
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
						"<cos by='name'>"+cosName+"</cos>"+
				"</GetCosRequest>");

		// Verify calendar feature is disabled
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraFeatureCalendarEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"FALSE", "Verify calendar feature is disabled");

		// Verify mail feature is disabled
		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraFeatureMailEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"FALSE", "Verify mail feature is disabled");
	}
}