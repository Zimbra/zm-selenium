/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.ResourceItem;

public class GetResource extends AdminCore {

	public GetResource() {
		logger.info("New "+ GetResource.class.getCanonicalName());
		super.startingPage = app.zPageManageResources;
	}


	@Test (description = "Verify get resource operation -- Manage resource View -- Location",
			groups = { "smoke" })

	public void GetResource_01() throws HarnessException {
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
		ZAssert.assertNotNull(found, "Verify the account is found");
	}


	@Test (description = "Search resource",
			groups = { "bhr" })

	public void GetResource_02() throws HarnessException {
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

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(resource.getEmailAddress());

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the resource list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for account "+ resource.getEmailAddress() + " found: "+ a.getGEmailAddress());
			if ( resource.getEmailAddress().equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the account is found");
	}
}