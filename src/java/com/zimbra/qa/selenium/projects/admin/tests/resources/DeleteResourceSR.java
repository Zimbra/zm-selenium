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
package com.zimbra.qa.selenium.projects.admin.tests.resources;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperation;

public class DeleteResourceSR extends AdminCommonTest {

	public DeleteResourceSR() {
		logger.info("New "+ DeleteResourceSR.class.getCanonicalName());

		// All tests start at the "Resources" page
		super.startingPage = app.zPageManageResources;

	}

	/**
	 * Testcase : Verify delete resource operation -- Search List View -- Location
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Search resource.
	 * 3. Select a resource.
	 * 4. Delete a resource using delete button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */
	@Test (description = "Verify delete resource operation -- Search List View -- Location",
			groups = { "functional", "L2" })
	public void DeleteResourceSR_01() throws HarnessException {

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
		
		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());
		
		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);
		
		// Click on resource to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);
		
		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the resource list is returned");
		
		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for resource "+ resource.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( resource.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");

	}
	
	/**
	 * Testcase : Verify delete resource operation -- Search List View -- Equipment
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Search resource.
	 * 3. Select a resource.
	 * 4. Delete a resource using delete button in Gear box menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */
	@Test (description = "Verify delete resource operation -- Search List View -- Equipment",
			groups = { "functional", "L2" })
	public void DeleteResourceSR_02() throws HarnessException {

		// Create a new resource in the Admin Console using SOAP
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
		
		// Click on resource to be deleted.
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);
		
		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the resource list is returned");
		
		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for resource "+ resource.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( resource.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");

	}
	
	/**
	 * Testcase : Verify delete resource operation -- Search List View/Right Click Menu -- Location
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Search resource.
	 * 3. Right click on resource.
	 * 4. Delete a resource using delete button in right click menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */
	@Test (description = "Verify delete resource operation -- Search List View/Right Click Menu -- Location",
			groups = { "functional", "L3" })
	public void DeleteResourceSR_03() throws HarnessException {

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
		
		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());
		
		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);
		
		// Right Click on resource to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);
		
		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the resource list is returned");
		
		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for resource "+ resource.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( resource.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");

	}
	
	/**
	 * Testcase : Verify delete resource operation -- Search List View/Right Click Menu -- Equipment
	 * Steps :
	 * 1. Create a resource using SOAP.
	 * 2. Search resource.
	 * 3. Right click on resource.
	 * 4. Delete a resource using delete button in right click menu.
	 * 5. Verify resource is deleted using SOAP.
	 * @throws HarnessException
	 */
	@Test (description = "Verify delete resource operation -- Search List View/Right Click Menu -- Equipment",
			groups = { "functional", "L3" })
	public void DeleteResourceSR_04() throws HarnessException {

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
		
		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());
		
		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);
		
		// Click on resource to be deleted.
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);
		
		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the resource list is returned");
		
		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for resource "+ resource.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( resource.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");

	}

}