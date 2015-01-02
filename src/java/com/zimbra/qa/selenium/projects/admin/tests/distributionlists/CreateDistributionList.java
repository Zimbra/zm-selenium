/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.distributionlists;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateDL;

public class CreateDistributionList extends AdminCommonTest {
	public CreateDistributionList() {
		logger.info("New "+ CreateDistributionList.class.getCanonicalName());

		// All tests start at the "Distribution Lists" page
		super.startingPage = app.zPageManageDistributionList;
	}
	
	/**
	 * Testcase : Create a basic DL
	 * Steps :
	 * 1. Create a DL from GUI.
	 * 2. Verify DL is created using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Create a basic DL",
			groups = { "obsolete" })
			public void CreateDistributionList_01() throws HarnessException {

		// Create a new dl in the Admin Console
		DistributionListItem dl = new DistributionListItem();

		// Click "New"
		
		WizardCreateDL wizard =(WizardCreateDL) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);
	
		// Fill out the necessary input fields and submit
		wizard.zCompleteWizard(dl);
		
		// Verify the dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
		                     "<dl by='name'>"+dl.getEmailAddress()+"</dl>"+
		                   "</GetDistributionListRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "Verify the distribution list is created successfully");
	}
	
	
	/**
	 * 1. Create DL from UI.
	 * 2. Verify DL is created using soap.
	 * @throws HarnessException
	 */
	@Test(	description = "Create a basic DL.",
			groups = { "sanity" })
			public void CreateDistributionList_02() throws HarnessException {

		// Create a new dl in the Admin Console
		DistributionListItem dl = new DistributionListItem();

		WizardCreateDL wizard =(WizardCreateDL) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);
		
		// Fill out the necessary input fields and submit
		wizard.zCompleteWizard(dl);
		
		// Verify the dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
		"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
		                     "<dl by='name'>"+dl.getEmailAddress()+"</dl>"+
		                   "</GetDistributionListRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "Verify the distribution list is created successfully");
	}


}
