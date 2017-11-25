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
package com.zimbra.qa.selenium.projects.admin.tests.resources;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditResource;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;
import com.zimbra.qa.selenium.projects.admin.pages.PageSearchResults;

public class EditResource extends AdminCore {

	public EditResource() {
		logger.info("New "+ EditResource.class.getCanonicalName());
		super.startingPage = app.zPageManageResources;
	}


	/**
	 * Testcase : Edit Resource name  -- Manage resource View -- Location
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Manage resource View.
	 * 3. Select a resource.
	 * 4. Edit a resource using edit button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = " Edit Resource name  -- Manage resource View -- Location",
			groups = { "smoke", "L1" })

	public void EditResource_01() throws HarnessException {

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

		// Click on Resource to be edited
		app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Edit button
		FormEditResource form = (FormEditResource) app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "Verify the Resource is edited successfully");
	}


	/**
	 * Testcase : Edit Resource name  -- Manage resource View -- Equipment
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Manage resource View.
	 * 3. Select a resource.
	 * 4. Edit a resource using edit button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = " Edit Resource name  -- Manage resource View -- Location",
			groups = { "functional", "L2" })

	public void EditResource_02() throws HarnessException {

		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				 		+ "<name>" + resource.getEmailAddress() + "</name>"
				 		+ "<a n=\"displayName\">" + resource.getName() + "</a>"
				 		+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
				 		+ "<password>test123</password>"
				 		+ "</CreateCalendarResourceRequest>");

		// Verify the resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  resource.getEmailAddress() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);

		// Refresh the Resource list
		app.zPageManageResources.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on Resource to be edited
		 app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Edit button
		FormEditResource form = (FormEditResource) app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		//form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		 response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
	}


	/**
	 * Testcase : Edit Resource name -- Manage resource View/Right Click Menu -- Location
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Manage resource View.
	 * 3. Right Click on a resource.
	 * 4. Edit a resource using edit button in right click menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit Resource name -- Manage resource View/Right Click Menu -- Location",
			groups = { "functional", "L3" })

	public void EditResource_03() throws HarnessException {

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

		// Right Click on Resource to be edited
		app.zPageManageResources.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Edit button
		FormEditResource form = (FormEditResource) app.zPageManageResources.zToolbarPressButton(Button.B_TREE_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
	}


	/**
	 * Testcase : Edit Resource name -- Manage resource View/Right Click Menu -- Equipment
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Manage resource View.
	 * 3. Right Click on a resource.
	 * 4. Edit a resource using edit button in right click menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit Resource name -- Manage resource View/Right Click Menu -- Equipment",
			groups = { "functional", "L3" })

	public void EditResource_04() throws HarnessException {

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

		// Right Click on Resource to be edited
		app.zPageManageResources.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Edit button
		FormEditResource form = (FormEditResource) app.zPageManageResources.zToolbarPressButton(Button.B_TREE_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
	}


	/**
	 * Testcase : Edit Resource name  -- Search list resource View -- Location
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Search list View.
	 * 3. Select a resource.
	 * 4. Edit a resource using edit button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = " Edit Resource name  -- Manage resource View -- Location",
			groups = { "functional", "L2" })

	public void EditResource_05() throws HarnessException {

		// Create a new Resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
		 		+ "<name>" + resource.getEmailAddress() + "</name>"
		 		+ "<a n=\"displayName\">" + resource.getName() + "</a>"
		 		+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
		 		+ "<password>test123</password>"
		 		+ "</CreateCalendarResourceRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on Resource to be edited
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.RESOURCE);
		FormEditResource form = (FormEditResource) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "Verify the Resource is edited successfully");
	}


	/**
	 * Testcase : Edit Resource name  -- Search list resource View -- Equipment
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Search list View.
	 * 3. Select a resource.
	 * 4. Edit a resource using edit button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = " Edit Resource name  -- Manage resource View -- Equipment",
			groups = { "functional", "L2" })

	public void EditResource_06() throws HarnessException {

		// Create a new Resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
		 		+ "<name>" + resource.getEmailAddress() + "</name>"
		 		+ "<a n=\"displayName\">" + resource.getName() + "</a>"
		 		+ "<a n=\"zimbraCalResType\">" + "Equipment" + "</a>"
		 		+ "<password>test123</password>"
		 		+ "</CreateCalendarResourceRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on Resource to be edited
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.RESOURCE);
		FormEditResource form = (FormEditResource) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "Verify the Resource is edited successfully");
	}


	/**
	 * Testcase : Edit Resource name -- Search list View/Right Click Menu -- Location
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Search list View.
	 * 3. Right Click on a resource.
	 * 4. Edit a resource using edit button in right click menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit Resource name -- Manage resource View/Right Click Menu -- Location",
			groups = { "functional", "L3" })

	public void EditResource_07() throws HarnessException {

		// Create a new Resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
		 		+ "<name>" + resource.getEmailAddress() + "</name>"
		 		+ "<a n=\"displayName\">" + resource.getName() + "</a>"
		 		+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
		 		+ "<password>test123</password>"
		 		+ "</CreateCalendarResourceRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on Resource to be edited
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.RESOURCE);
		FormEditResource form = (FormEditResource) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
	}


	/**
	 * Testcase : Edit Resource name -- Search list View/Right Click Menu -- Equipment
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Go to Search list View.
	 * 3. Right Click on a resource.
	 * 4. Edit a resource using edit button in right click menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Edit Resource name -- Manage resource View/Right Click Menu -- Equipment",
			groups = { "functional", "L3" })

	public void EditResource_08() throws HarnessException {

		// Create a new Resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
		 		+ "<name>" + resource.getEmailAddress() + "</name>"
		 		+ "<a n=\"displayName\">" + resource.getName() + "</a>"
		 		+ "<a n=\"zimbraCalResType\">" + "Equipment" + "</a>"
		 		+ "<password>test123</password>"
		 		+ "</CreateCalendarResourceRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Click on Resource to be edited
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.RESOURCE);
		FormEditResource form = (FormEditResource) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditResource.TreeItem.PROPERTIES);

		// Edit the name
		String editedName = "editedResource_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the Resource exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
				+ 		"<calresource by='name'>" +  editedName+"@"+resource.getDomainName() + "</calresource>"
				+		"</GetCalendarResourceRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetCalendarResourceResponse/admin:calresource", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
	}
}