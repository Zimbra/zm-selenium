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
package com.zimbra.qa.selenium.projects.admin.tests.DelegatedAdmin;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditDistributionList;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateDL;


public class DA_DistributionListAMD extends AdminCommonTest {

	public DA_DistributionListAMD() throws HarnessException {
		logger.info("New "+ DA_DistributionListAMD.class.getCanonicalName());

		// All tests start at the "DL" page
		super.startingPage = app.zPageManageDistributionList;


	}

	/**
	 * Testcase : Login as Delegated admin and Create a basic DL
	 * Steps :
	 * 1. Create a DL from GUI i.e. Gear Box -> New.
	 * 2. Verify DL is created using SOAP.
	 * @throws HarnessException
	 */


	@Test(	description = "Delegated Admin's Create a basic DL using New->Distribution List",
			groups = { "obsolete" })
	public void DA_CreateDL_01() throws HarnessException {

		app.provisionAuthenticateDA();
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

	/**
	 * Testcase : Delete a basic distribution list -- Manage distribution list View
	 * Steps :
	 * 1. Create a distribution list using SOAP.
	 * 2. Go to Delegated Admin's Manage distribution list View.
	 * 3. Select a distribution list.
	 * 4. Delete a distribution list using delete button in Gear box menu.
	 * 5. Verify distribution list is deleted using SOAP.
	 * 
	 * @throws HarnessException
	 */
	@Test(	description = "Delegated Admin's: Delete a basic DL -- Manage distribution list View",
			groups = { "obsolete" })
	public void DA_DeleteDL_01() throws HarnessException {

		// Create and authenticate delelgated admin
		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+		"</CreateDistributionListRequest>");

		// Refresh list to populate DL.
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on DL to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);


		// Verify the dl does not exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+dlEmailAddress+"</dl>"+
				"</GetDistributionListRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNull(response, "Verify the distribution list is deleted successfully");
	}


	/**
	 * Testcase : Edit distribution list name  - Manage distribution list View
	 * Steps :
	 * 1. Create an distribution list using SOAP.
	 * 2. Go to Delegated Admin's Manage distribution list View
	 * 3. Select an distribution list.
	 * 4. Edit an distribution list using edit button in Gear box menu.
	 * 5. Verify distribution list is edited using SOAP.
	 * @throws HarnessException
	 */
	@Test(	description = "Delegated Admin's : Edit distribution list name  - Manage distribution list View",
			groups = { "obsolete" })
	public void DA_EditAccount_01() throws HarnessException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();

		// Create a new distribution list in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on distribution list to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);
		//FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on General Information tab.
		form.zClickTreeItem(FormEditDistributionList.TreeItem.MEMBERS);

		//Edit the name.
		String editedName = "editedDL_" + ZimbraSeleniumProperties.getUniqueString();
		form.setName(editedName);

		//Submit the form.
		form.zSubmit();

		// Verify the dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+editedName+"@"+dl.getDomainName()+	"</dl>"+
				"</GetDistributionListRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "Verify the distribution list is edited successfully");
	}
}
