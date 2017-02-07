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

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.WizardUpdateLicense;

public class UpdateLicense extends AdminCommonTest {

	public UpdateLicense() {
		logger.info("New "+ UpdateLicense.class.getCanonicalName());
		super.startingPage = app.zPageManageLicense;
	}

	/**
	 * Testcase : Update new license in admin console
	 * Steps :
	 * 1. Go to configure >> global settings >> License >> select update license option from gear icon
	 * 2. Verify that new license is updated successfully 
	 * @throws HarnessException
	 */
	
	@Bugs(ids = "106019")
	@Test(description = "Upload new license in admin console", groups = { "smoke", "L1" })
	
	public void UpdateLicense_01() throws HarnessException {

		// Create file item
		final String fileName = "regular.xml";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\private\\license\\" + fileName;
		
		// Click on install certificate option
		WizardUpdateLicense wizard= (WizardUpdateLicense)app.zPageManageLicense.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_UPDATE_LICENSE);
		SleepUtil.sleepMedium();

		// Click on Upload File button in the toolbar
		app.zPageManageLicense.zToolbarPressButton(Button.B_UPLOAD_LICENSE);

		// Upload license
		zUpload(filePath);
		
		// Update license
		wizard.zUpdateLicenseWizard();

		// Flush cache
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<FlushCacheRequest  xmlns='urn:zimbraAdmin'>" +
						"<cache type='galgroup'/>" +
				"</FlushCacheRequest>");

		// Get license information
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetLicenseRequest xmlns='urn:zimbraAdmin'>"
						+		"</GetLicenseRequest>");

		// Verify account limit
		String accountLimit = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetLicenseResponse/admin:license/admin:attr[@name='AccountsLimit']", null);
		ZAssert.assertEquals(accountLimit, "-1", "Verify account limit");

		// Verify license type
		String installType = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetLicenseResponse/admin:license/admin:attr[@name='InstallType']", null);
		ZAssert.assertEquals(installType, "regular", "Verify install type");

		// Verify "valid until" date
		String validUntil = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetLicenseResponse/admin:license/admin:attr[@name='ValidUntil']", null);
		ZAssert.assertEquals(validUntil, "20170320090000Z", "Verify valid until");

		// Activate license
		staf.execute("zmlicense -a");

		// Create a new account after activation of license
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+		"</CreateAccountRequest>");

	}

}

