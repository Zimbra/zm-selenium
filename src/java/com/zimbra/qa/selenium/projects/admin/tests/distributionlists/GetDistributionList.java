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

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;

public class GetDistributionList extends AdminCore {

	public GetDistributionList() {
		logger.info("New "+ GetDistributionList.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	@Test (description = "Verify created dl is present in the list view",
			groups = { "bhr", "testcafe" })

	public void GetDistributionList_01() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageManageDistributionList.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the dl list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for dl "+ dlEmailAddress + " found: "+ a.getGEmailAddress());
			if ( dlEmailAddress.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the dl is returned correctly");
	}


	@Test (description = "Verify created admin dl is present in the list view",
			groups = { "sanity", "testcafe" })

	public void GetDistributionList_02() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a n='zimbraIsAdminGroup'>TRUE</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageManageDistributionList.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the dl list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for dl "+ dlEmailAddress + " found: "+ a.getGEmailAddress());
			if ( dlEmailAddress.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the dl is returned correctly");
	}


	@Test (description = "Verify created dynamic admin dl is present in the list view",
			groups = { "sanity", "testcafe" })

	public void GetDistributionList_03() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin' dynamic='1'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a n='zimbraIsAdminGroup'>TRUE</a>"
				+			"<a n='zimbraIsACLGroup'>TRUE</a>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageManageDistributionList.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the dl list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for dl "+ dlEmailAddress + " found: "+ a.getGEmailAddress());
			if ( dlEmailAddress.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the dl is returned correctly");
	}


	@Test (description = "Verify created dl is present in the list view  - Search view",
			groups = { "functional", "testcafe" })

	public void GetDistributionList_04() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateDistributionListRequest>");

		// Enter the search string to find the account
		app.zPageSearchResults.zAddSearchQuery(dlEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Get the list of displayed accounts
		List<AccountItem> accounts = app.zPageSearchResults.zListGetAccounts();
		ZAssert.assertNotNull(accounts, "Verify the dl list is returned");

		AccountItem found = null;
		for (AccountItem a : accounts) {
			logger.info("Looking for dl "+ dlEmailAddress + " found: "+ a.getGEmailAddress());
			if ( dlEmailAddress.equals(a.getGEmailAddress()) ) {
				found = a;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the dl is returned correctly");
	}
}