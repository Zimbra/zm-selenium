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
package com.zimbra.qa.selenium.projects.admin.tests.cos;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditCos;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class EditPreferences extends AdminCore {

	public EditPreferences() {
		logger.info("New "+ EditPreferences.class.getCanonicalName());
		super.startingPage = app.zPageManageCOS;
	}


	/**
	 * Testcase : Edit COS - Edit preferences at COS level
	 * Steps :
	 * 1. Login to Admin Console and go to Configure > COS
	 * 2. Select created COS from the list
	 * 3. Select Edit, go to Preferences tab
	 * 4. Check/Uncheck some Preferences > Save
	 * 5. Edited details should be saved without any errors
	 * @throws HarnessException
	 */

	@Test (description = "Edit COS - Edit preferences at COS level",
			groups = { "bhr" })

	public void EditPreferences_01() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Refresh the list
		app.zPageManageCOS.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on COS to be edited.
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);

		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on preferences
		app.zPageEditCOS.zToolbarPressButton(Button.B_PREFERENCES);
		SleepUtil.sleepMedium();

		// Check show seach strings preference
		form.zPreferencesCheckboxSet(Button.B_SHOW_SEARCH_STRINGS,true);

		// Uncheck show imap search folders preference
		form.zPreferencesCheckboxSet(Button.B_SHOW_IMAP_SEARCH_FOLDERS,false);

		// Submit the form
		form.zSubmit();

		// Verify preferences are updated
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
						"<cos by='name'>"+cosName+"</cos>"+
				"</GetCosRequest>");

		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraPrefShowSearchString']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"TRUE", "Verify show seach strings preference is enabled");

		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraPrefImapSearchFoldersEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"FALSE", "Verify show imap search folders preference is disabled");
	}
}