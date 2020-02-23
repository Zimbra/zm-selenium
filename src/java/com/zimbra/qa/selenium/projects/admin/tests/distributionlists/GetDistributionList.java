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
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class GetDistributionList extends AdminCore {

	public GetDistributionList() {
		logger.info("New "+ GetDistributionList.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	/**
	 * Testcase : Verify created dl is displayed in UI  -- Manage dl View
	 * Steps :
	 * 1. Create a dl using SOAP.
	 * 2. Go to Manage dl View.
	 * 3. Verify dl is present in the list
	 * @throws HarnessException
	 */

	@Test (description = "Verify created dl is present in the list view",
			groups = { "bhr" })

	public void GetDistributionList_01() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

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


	/**
	 * Testcase : Verify created admin dl is displayed in UI  -- Manage dl View
	 * Steps :
	 * 1. Create a admin dl using SOAP.
	 * 2. Go to Manage dl View.
	 * 3. Verify dl is present in the list
	 * @throws HarnessException
	 */

	@Test (description = "Verify created admin dl is present in the list view",
			groups = { "sanity" })

	public void GetDistributionList_02() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a n='zimbraIsAdminGroup'>TRUE</a>"
				+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

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


	/**
	 * Testcase : Verify created dynamic admin dl is displayed in UI  -- Manage dl View
	 * Steps :
	 * 1. Create a dynamic admin dl using SOAP.
	 * 2. Go to Manage dl View.
	 * 3. Verify dl is present in the list
	 * @throws HarnessException
	 */

	@Test (description = "Verify created dynamic admin dl is present in the list view",
			groups = { "sanity" })

	public void GetDistributionList_03() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin' dynamic='1'>"
				+			"<name>" + dlEmailAddress + "</name>"
				+			"<a n='zimbraIsAdminGroup'>TRUE</a>"
				+			"<a n='zimbraIsACLGroup'>TRUE</a>"
				+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

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


	/**
	 * Testcase : Verify created dl is displayed in UI - Search view.
	 * Steps :
	 * 1. Create an dl using SOAP.
	 * 2. Search list
	 * 2. Verify dl is present in the search list.
	 * @throws HarnessException
	 */

	@Test (description = "Verify created dl is present in the list view  - Search view",
			groups = { "functional" })

	public void GetDistributionList_04() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
						"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + dlEmailAddress + "</name>"
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