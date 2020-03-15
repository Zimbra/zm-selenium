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
package com.zimbra.qa.selenium.projects.admin.tests.resources;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;
import com.zimbra.qa.selenium.projects.admin.pages.DialogForDeleteOperation;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class DeleteResource extends AdminCore {

	public DeleteResource() {
		logger.info("New "+ DeleteResource.class.getCanonicalName());
		super.startingPage = app.zPageManageResources;
	}


	@Test (description = "Verify delete resource operation -- Manage resource View -- Location",
			groups = { "bhr" })

	public void DeleteResource_01() throws HarnessException {
		this.startingPage.zNavigateTo();

		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
						+ "<a n='displayName'>" + resource.getName() + "</a>"
						+ "<a n='zimbraCalResType'>" + "Location" + "</a>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+ "</CreateCalendarResourceRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
		this.startingPage.zNavigateTo();

		// Click on account to be deleted.
		app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");
	}


	@Test (description = "Verify delete resource operation -- Manage resource View -- Equipment",
			groups = { "sanity" })

	public void DeleteResource_02() throws HarnessException {
		this.startingPage.zNavigateTo();

		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
						+ "<a n='displayName'>" + resource.getName() + "</a>"
						+ "<a n='zimbraCalResType'>" + "Equipment" + "</a>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+ "</CreateCalendarResourceRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
		this.startingPage.zNavigateTo();

		// Click on account to be deleted.
		app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");
	}


	@Test (description = "Verify delete resource operation -- Manage resource View/Right Click Menu -- Location",
			groups = { "sanity" })

	public void DeleteResource_03() throws HarnessException {
		this.startingPage.zNavigateTo();

		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
						+ "<a n='displayName'>" + resource.getName() + "</a>"
						+ "<a n='zimbraCalResType'>" + "Location" + "</a>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+ "</CreateCalendarResourceRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
		this.startingPage.zNavigateTo();

		// Right Click on resource to be deleted.
		app.zPageManageResources.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageResources.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");
	}


	@Test (description = "Verify delete resource operation -- Manage resource View/Right Click Menu -- Equipment",
			groups = { "functional" })

	public void DeleteResource_04() throws HarnessException {
		// Create a new resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
						+ "<a n='displayName'>" + resource.getName() + "</a>"
						+ "<a n='zimbraCalResType'>" + "Equipment" + "</a>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+ "</CreateCalendarResourceRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
		this.startingPage.zNavigateTo();

		// Right Click on resource to be deleted.
		app.zPageManageResources.zListItem(Action.A_RIGHTCLICK, resource.getEmailAddress());

		// Click on Delete button
		DialogForDeleteOperation dialog = (DialogForDeleteOperation) app.zPageManageResources.zToolbarPressButton(Button.B_TREE_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zPressButton(Button.B_YES);

		// Click Ok on "Delete Items" dialog
		dialog.zPressButton(Button.B_OK);

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
		ZAssert.assertNull(found, "Verify the resource is deleted successfully");
	}
}