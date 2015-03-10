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

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditResource;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateResource;


public class DA_ResourceEquipmentAMD extends AdminCommonTest {

	public DA_ResourceEquipmentAMD() throws HarnessException {
		logger.info("New "+ DA_ResourceEquipmentAMD.class.getCanonicalName());

		// All tests start at the "resource" page
		super.startingPage = app.zPageManageResources;


	}

	/**
	 * Testcase : Login as Delegated admin and Create a basic resource Equipment
	 * Steps :
	 * 1. Create an resource Equipment from GUI i.e. Gear Box -> New.
	 * 2. Verify  resource Equipment is created using SOAP.
	 * @throws HarnessException
	 */


	@Test(	description = "Delegated Admin's Create a basic resource Equipment using New -> resource",
			groups = { "sanity" })
	public void DA_CreateResourceEquipment_01() throws HarnessException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();
		// Create a new resource in the Admin Console
		ResourceItem resource = new ResourceItem();


		// Click "New"
		WizardCreateResource wizard = 
			(WizardCreateResource)app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.setResourceType(WizardCreateResource.Locators.EQUIPMENT);
		wizard.zCompleteWizard(resource);


		// Verify the resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  resource.getEmailAddress() + "</calresource>"  
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1); 
		ZAssert.assertNotNull(response, "Verify the RESOURCE is created successfully");

	}

	/**
	 * Testcase : Delete a basic resource Equipment-- Manage resource View
	 * Steps :
	 * 1. Create an resource using SOAP.
	 * 2. Go to Delegated Admin's Manage resource View.
	 * 3. Select an resource.
	 * 4. Delete an resource using delete button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * 
	 * @throws HarnessException
	 */
	@Test(	description = "Delegated Admin's: Delete a basic  resource Equipment -- Manage  resource  View",
			groups = { "sanity" })
	public void DA_DeleteResourceEquipment_01() throws HarnessException {

		// Create and authenticate delegated admin
		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();
		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<a n=\"displayName\">" + resource.getName() + "</a>"
						+ "<a n=\"zimbraCalResType\">" + "Equipment" + "</a>"
						+ "<password>test123</password>"
						+ "</CreateCalendarResourceRequest>");

		// Refresh list to populate account.
		app.zPageManageResources.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be deleted.
		app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zClickButton(Button.B_OK);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageManageResources.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the resource list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for resource "+ resource.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( resource.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the resource is deleted successfully at DA");


	}


	/**
	 * Testcase : Edit equipment name  - Manage resource View
	 * Steps :
	 * 1. Create a equipment using SOAP.
	 * 2. Go to Delegated Admin's Manage Resource View
	 * 3. Select an equipment.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify equipment is edited using SOAP.
	 * @throws HarnessException
	 * @throws ServiceException 
	 */
	@Test(	description = "Delegated Admin's : Edit equipment name  - Manage resource View",
			groups = { "sanity" })
	public void DA_EditResourceEquipment_01() throws HarnessException, ServiceException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();

		// Create a new Resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<a n=\"displayName\">" + resource.getName() + "</a>"
						+ "<a n=\"zimbraCalResType\">" + "Equipment" + "</a>"
						+ "<password>test123</password>"
						+ "</CreateCalendarResourceRequest>");

		// Refresh the Resource list
		app.zPageManageResources.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on Resource to be Edited.
		app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Edit button
		FormEditResource form = (FormEditResource) app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on General Information tab.
		form.zClickTreeItem(FormEditResource.TreeItem.PROPERTIES);

		//Edit the name.
		String editedName = "editedResource_" + ZimbraSeleniumProperties.getUniqueString();
		form.setNameAsDA(editedName);

		//Submit the form.
		form.zSubmit();

		// Refresh the Resource list
		app.zPageManageResources.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ 		"<calresource by='name'>" + resource.getEmailAddress()  + "</calresource>"  
						+		"</GetCalendarResourceRequest>");

		
		 Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource/admin:a[@n='displayName']", 1);
		 
		 ZAssert.assertNotNull(response, "Verify the Resource is edited successfully at DA");
		 ZAssert.assertStringContains(response.toString(), editedName, "Verify the Resource Displayname is edited successfully at DA");
	}
}
