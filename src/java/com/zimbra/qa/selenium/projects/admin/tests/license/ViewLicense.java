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
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;

public class ViewLicense extends AdminCommonTest {

	public ViewLicense() {
		logger.info("New "+ ViewLicense.class.getCanonicalName());
		super.startingPage = app.zPageManageLicense;
	}

	/**
	 * Testcase : Verify License Information
	 * Steps :
	 * 1. Go to configure >> global settings >> License >> Click on License tab
	 * 2. Verify that license imformation 
	 * @throws HarnessException
	 */

	@Test(description = "Verify License Information", groups = { "smoke", "L1" })

	public void ViewLicense_01() throws HarnessException {

		String companyName="zimbra";
		String licenseType = "regular";
		
		// Get license information
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetLicenseRequest xmlns='urn:zimbraAdmin'>"
						+		"</GetLicenseRequest>");
		String Id = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetLicenseResponse/admin:license/admin:attr[@name='LicenseId']",null).toString();
		
		boolean verifyCompanyName = app.zPageManageLicense.zVerifyCompanyName(companyName);
		ZAssert.assertTrue(verifyCompanyName, "Verify company name is displaying correctly!");
		
		boolean verifyLicenseType = app.zPageManageLicense.zVerifyLicenseType(licenseType);
		ZAssert.assertTrue(verifyLicenseType, "Verify license type is displaying correctly!");
		
		boolean verifyLicenseId = app.zPageManageLicense.zVerifyLicenseID(Id);
		ZAssert.assertTrue(verifyLicenseId, "Verify license id is displaying correctly!");
		
	}
}

