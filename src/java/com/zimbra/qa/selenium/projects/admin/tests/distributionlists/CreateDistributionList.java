/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.distributionlists;

import org.testng.annotations.Test;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.pages.WizardCreateDL;

public class CreateDistributionList extends AdminCore {

	public CreateDistributionList() {
		logger.info("New "+ CreateDistributionList.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	@Test (description = "Create a basic DL",
			groups = { "smoke", "testcafe" })

	public void CreateDistributionList_01() throws HarnessException {
		this.startingPage.zNavigateTo();

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


	@Test (description = "Create a basic dynamic DL",
			groups = { "bhr" })

	public void CreateDistributionList_02() throws HarnessException, ServiceException {
		this.startingPage.zNavigateTo();

		// Create a new dynamic dl in the Admin Console
		String memberURL ="ldap:///??sub?(&(objectClass=zimbraAccount)(zimbraIsDelegatedAdminAccount=TRUE))";

		DistributionListItem dl = new DistributionListItem();
		dl.setDynamicDL(true);
		dl.setRightManagement(false);
		dl.setMemberURL(memberURL);
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

		// check if the created DL is really Dynamic
		ZAssert.assertEquals(response.getAttribute("dynamic"), "1" , "Verify the distribution list created is a Dynamic DL");

		// check if the created DL has correct memberURL
		response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl/admin:a[@n='memberURL']", 1);
		ZAssert.assertNotNull(response , "Verify if the created DL has correct memberURL");
	}
	
	@Bugs(ids = "51011")
	@Test (description = "Create a DL and add it as a member of another DL",
			groups = { "sanity" })

	public void CreateDistributionList_03() throws HarnessException {
		this.startingPage.zNavigateTo();
		
		// Create a distribution list in the Admin Console using SOAP
		DistributionListItem dl1 = new DistributionListItem();
		DistributionListItem dl2 = new DistributionListItem();
		
		// Create a DL using SOAP
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+	"<name>" + dl1.getEmailAddress() + "</name>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+	"</CreateDistributionListRequest>");

		// Create a DL using GUI
		WizardCreateDL wizard =(WizardCreateDL) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);
		
		// Fill out the necessary input fields and submit
		wizard.zCompleteWizardWithMemberOfDetails(dl2, dl1);

		// Verify the dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" 
						+	"<dl by='name'>" + dl2.getEmailAddress() + "</dl>"
						+	"</GetDistributionListRequest>");
		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "Verify the distribution list is created successfully");
		
		// Verify that the dl2 is added as a member of dl1 
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+ 	"<dl by='name'>" + dl1.getEmailAddress() + "</dl>"
						+ 	"</GetDistributionListRequest>");
		String dlMember = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:GetDistributionListResponse/admin:dl","dlm");
		ZAssert.assertTrue(dlMember.contains(dl2.getEmailAddress()), "Verify the dl member is added in the previous DL.");
	}
}