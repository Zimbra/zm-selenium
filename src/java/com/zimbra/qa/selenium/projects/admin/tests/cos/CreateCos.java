/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.CosItem;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateCos;
import com.zimbra.qa.selenium.framework.core.Bugs;

public class CreateCos extends AdminCore {

	public CreateCos() {
		logger.info("New "+ CreateCos.class.getCanonicalName());
		super.startingPage = app.zPageManageCOS;
	}


	/**
	 * Testcase : Create a basic COS
	 * Steps :
	 * 1. Create a COS from GUI.
	 * 2. Verify cos is created using SOAP.
	 * @throws HarnessException
	 */

	@Bugs (ids = "100779")
	@Test (description = "Create a basic COS",
			groups = { "sanity", "L0" })

	public void CreateCos_01() throws HarnessException {

		// Create a new cos in the Admin Console
		CosItem cos = new CosItem();

		// Click "New"
		WizardCreateCos cosDialog = (WizardCreateCos) app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the necessary input fields and submit
		cosDialog.zCompleteWizard(cos);

		// Verify the cos exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCosRequest xmlns='urn:zimbraAdmin'>" +
                     "<cos by='name'>"+cos.getName()+"</cos>"+
                   "</GetCosRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCosResponse/admin:cos", 1);
		ZAssert.assertNotNull(response, "Verify the cos is created successfully");
	}
}