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
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditCos;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class ManageMobileAccess extends AdminCommonTest {

	public ManageMobileAccess() {
		logger.info("New "+ ManageMobileAccess.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}

	/**
	 * Testcase : Edit COS - Enable mobile access
	 * Steps :
	 * 1. Create an COS using SOAP.
	 * 2. Edit COS > navigate to mobile access
	 * 3. Enable mobile access
	 * @throws HarnessException
	 */
	@Test( description = "Edit COS - Enable mobile access",
			groups = { "smoke", "L1"})

	public void EnableMobileAccess_01() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Refresh the list
		app.zPageManageCOS.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be edited.
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);

		// Click on Edit button
		FormEditCos form = (FormEditCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// CLick on mobile access
		app.zPageManageCOS.zToolbarPressButton(Button.B_MOBILE_ACCESS);
		SleepUtil.sleepMedium();

		// Enable mobile sync
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_SYNC,true);

		// Enable mobile policy
		form.zSetMobileAccess(Button.B_ENABLE_MOBILE_POLICY,true);

		// Submit the form
		form.zSubmit();

		// Verify mobile sync is enabled
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
						"<cos by='name'>"+cosName+"</cos>"+
				"</GetCosRequest>");

		// Verify mobile sync is enabled
		Element response1 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos/admin:a[@n='zimbraFeatureMobileSyncEnabled']", 1);
		ZAssert.assertNotNull(response1, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response1.toString(),"TRUE", "Verify Enable mobile sync is checked");

		Boolean status = app.zPageManageCOS.sIsChecked(FormEditCos.Locators.ENABLE_MOBILE_SYNC);	
		ZAssert.assertTrue(status, "Verify Enable mobile sync is checked!!");

		Element response2 = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetAccountResponse/admin:account/admin:a[@n='zimbraFeatureMobilePolicyEnabled']", 1);
		ZAssert.assertNotNull(response2, "Verify the account is edited successfully");
		ZAssert.assertStringContains(response2.toString(),"TRUE", "Verify Enable mobile policy is checked");

	}
}
