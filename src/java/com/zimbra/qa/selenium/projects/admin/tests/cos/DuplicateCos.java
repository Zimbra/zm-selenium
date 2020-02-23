/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateCos;

public class DuplicateCos extends AdminCore {

	public DuplicateCos() {
		logger.info("New " + DuplicateCos.class.getCanonicalName());
		super.startingPage=app.zPageManageCOS;
	}


	/**
	 * Testcase : Verify delete cos operation -- Manage Cos view.
	 * Steps :
	 * 1. Create a cos using SOAP.
	 * 2. Search cos created in Step-1.
	 * 3. Select Duplicate from gear box menu
	 * 4. Verify cos is Duplicated using SOAP
	 * @throws HarnessException
	 */

	@Test (description = "Verify Duplicate cos operation -- Manage cos view",
			groups = { "sanity" })

	public void DuplicateCos_01() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName = cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosName + "</name>"
				+		"</CreateCosRequest>");

		// Refresh the account list
		app.zPageManageCOS.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be duplicated.
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);

		// Click "New"
		WizardCreateCos cosDialog = (WizardCreateCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DUPLICATE_COS);

		CosItem dupCos = new CosItem();
		dupCos.setCosName(cosName + "duplicate");

		// Fill out the necessary input fields and submit
		cosDialog.zCompleteWizard(dupCos);

		// Verify the cos exists in the ZCS
				ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
				                     "<cos by='name'>"+dupCos.getName()+"</cos>"+
				                   "</GetCosRequest>");
				Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
				ZAssert.assertNotNull(response, "Verify the duplicate cos is created successfully");
	}


	/**
	 * Testcase : Verify delete cos operation -- Manage COS list view/Right click menu.
	 * Steps :
	 * 1. Create a cos using SOAP.
	 * 2. Select Duplicate from Manage COS list view/Right click menu
	 * 3. Verify Duplicate cos is created using SOAP
	 * @throws HarnessException
	 */

	@Test (description = "Verify Duplicate cos operation -- Search COS list view/Right click menu",
			groups = { "sanity" })

	public void DuplicateCos_02() throws HarnessException {

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosname = cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + cosname + "</name>"
				+		"</CreateCosRequest>");

		// Refresh the account list
		app.zPageManageCOS.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Duplicated.
		app.zPageManageCOS.zListItem(Action.A_RIGHTCLICK, cosname);

		// Click on Duplicate  button
		WizardCreateCos form = (WizardCreateCos) app.zPageManageCOS.zToolbarPressButton(Button.O_DUPLICATE_COS);

		CosItem dupCos = new CosItem();
		dupCos.setCosName(cosname + "duplicate");

		// Fill out the necessary input fields and submit
		form.zCompleteWizard(dupCos);

		// Verify the duplicate cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
		                     "<cos by='name'>"+dupCos.getName()+"</cos>"+
		                   "</GetCosRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the duplicate cos is created successfully");
	}
}