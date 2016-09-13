/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.delegatedadmin;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditResource;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardCreateResource;


public class ResourceLocationManage extends AdminCommonTest {

	public ResourceLocationManage() throws HarnessException {
		logger.info("New "+ ResourceLocationManage.class.getCanonicalName());

		// All tests start at the "resource" page
		super.startingPage = app.zPageManageResources;


	}

	/**
	 * Testcase : Login as Delegated admin and Create a basic resource location
	 * Steps :
	 * 1. Create an resource location from GUI i.e. Gear Box -> New.
	 * 2. Verify  resource location is created using SOAP.
	 * @throws HarnessException
	 */


	@Test( description = "Delegated Admin's Create a basic resource location using New -> resource",
			groups = { "sanity" })
	public void CreateResource_01() throws HarnessException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();
		// Create a new resource in the Admin Console
		ResourceItem resource = new ResourceItem();


		// Click "New"
		WizardCreateResource wizard = 
			(WizardCreateResource)app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);

		// Fill out the wizard and click Finish
		wizard.setResourceType(WizardCreateResource.Locators.LOCATION);
		wizard.zCompleteWizard(resource);
		
		// Wait for resource creation
		SleepUtil.sleepMedium();

		// Verify the resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  resource.getEmailAddress() + "</calresource>"  
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1); 
		ZAssert.assertNotNull(response, "Verify the RESOURCE is created successfully");

	}

	/**
	 * Testcase : Delete a basic resource location-- Manage resource View
	 * Steps :
	 * 1. Create an resource using SOAP.
	 * 2. Go to Delegated Admin's Manage resource View.
	 * 3. Select an resource.
	 * 4. Delete an resource using delete button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * 
	 * @throws HarnessException
	 */
	@Test( description = "Delegated Admin's: Delete a basic  resource location -- Manage  resource  View",
			groups = { "sanity" })
	public void DeleteResource_01() throws HarnessException {

		// Create and authenticate delegated admin
		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();
		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<a n=\"displayName\">" + resource.getName() + "</a>"
						+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
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
		
		// Wait for resource deletion
		SleepUtil.sleepMedium();

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
	 * Testcase : Edit account name  - Manage Account View
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Go to Delegated Admin's Manage Account View
	 * 3. Select an Account.
	 * 4. Edit an account using edit button in Gear box menu.
	 * 5. Verify account is edited using SOAP.
	 * @throws HarnessException
	 * @throws ServiceException 
	 */
	@Test( description = "Delegated Admin's : Edit Account name  - Manage Account View",
			groups = { "sanity" })
	public void EditResource_01() throws HarnessException, ServiceException {

		app.provisionAuthenticateDA();
		this.startingPage.zNavigateTo();

		// Create a new Resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<a n=\"displayName\">" + resource.getName() + "</a>"
						+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
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
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
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
