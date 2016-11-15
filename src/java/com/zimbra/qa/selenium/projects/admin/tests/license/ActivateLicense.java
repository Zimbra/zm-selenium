/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.license;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForActivateLicense;

public class ActivateLicense extends AdminCommonTest {

	public ActivateLicense() {
		logger.info("New "+ ActivateLicense.class.getCanonicalName());
		super.startingPage = app.zPageManageLicense;
	}

	/**
	 * Testcase : Activate license in admin console
	 * Steps :
	 * 1. Go to configure >> global settings >> License >> select Activate License option from gear icon
	 * 2. Verify that license is activated successfully 
	 * @throws HarnessException
	 */

	@Test(description = "Activate license in admin console", groups = { "sanity" })

	public void ActivateLicense_01() throws HarnessException {

		// Click on Activate license
		DialogForActivateLicense dialog = (DialogForActivateLicense) app.zPageManageLicense.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_ACTIVATE_LICENSE);
		
		// Verify message - Your license is successfully activated.
		String checkMessage= app.zPageManageGlobalSettings.sGetText(DialogForActivateLicense.Locators.MESSAGE_DIALOG);	
		ZAssert.assertStringContains(checkMessage, "successfully activated.","Verify message - Your license is successfully activated.");

		// Click Ok on "Zimbra Administration" dialog
		dialog.zClickButton(Button.B_OK);
		
		// Get license information
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetLicenseRequest xmlns='urn:zimbraAdmin'>"
						+		"</GetLicenseRequest>");

		// Verify license is activated 
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetLicenseResponse/admin:activation", 1);
		ZAssert.assertNotNull(response, "Verify license is activated successfully");
	}
}

