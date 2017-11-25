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
package com.zimbra.qa.selenium.projects.admin.tests.search.distributionlists;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDistributionList;
import com.zimbra.qa.selenium.projects.admin.pages.PageSearchResults;

public class EditDistributionList extends AdminCore {

	public EditDistributionList() {
		logger.info("New "+ EditDistributionList.class.getCanonicalName());
		super.startingPage = app.zPageManageSearch;
	}


	/**
	 * Testcase : Verify edit operation for DL - Search distribution list view.
	 * Steps :
	 * 1. Create a dl using SOAP.
	 * 2. Search dl.
	 * 3. Select a dl.
	 * 4. Edit a dl using delete button in Gear box menu.
	 * 5. Verify dl is edited using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Verify edit operation for distribution list - Search distribution list view",
			groups = { "functional", "L2" })

	public void EditDistributionList_01() throws HarnessException {

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+		"</CreateDistributionListRequest>");

		// Enter the search string to find the dl
		app.zPageSearchResults.zAddSearchQuery(dlEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Right Click on distribution list to be edited
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DISTRIBUTION_LIST);
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Edit the name
		String editedName = "editedDL_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+editedName+"@"+dl.getDomainName()+	"</dl>"+
				"</GetDistributionListRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "Verify the distribution list is edited successfully");

		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Verify edit operation for DL - Search distribution list view + Right click.
	 * Steps :
	 * 1. Create a dl using SOAP.
	 * 2. Search dl.
	 * 3. Right click on dl.
	 * 4. Edit a dl using edit button in Gear box menu.
	 * 5. Verify dl is edited using SOAP.
	 * @throws HarnessException
	 */

	@Test (description = "Verify edit operation for distribution list - Search distribution list view + right click",
			groups = { "functional", "L3" })

	public void EditDistributionList_02() throws HarnessException {

		super.startingPage.zNavigateTo();
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+		"</CreateDistributionListRequest>");

		// Enter the search string to find the dl
		app.zPageSearchResults.zAddSearchQuery(dlEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Right Click on distribution list to be edited
		app.zPageSearchResults.zListItem(Action.A_RIGHTCLICK, dl.getEmailAddress());

		// Click on Edit button
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DISTRIBUTION_LIST);
		FormEditDistributionList form = (FormEditDistributionList) app.zPageSearchResults.zToolbarPressButton(Button.B_TREE_EDIT);

		// Edit the name
		String editedName = "editedDL_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+editedName+"@"+dl.getDomainName()+	"</dl>"+
				"</GetDistributionListRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "Verify the distribution list is edited successfully");
		app.zPageMain.zLogout();
	}


	/**
	 * Testcase : Verify edit operation for dynamic DL - Search distribution list view.
	 * Steps :
	 * 1. Create a dl using SOAP.
	 * 2. Search dl.
	 * 3. Select a dl.
	 * 4. Edit a dl using edit button in Gear box menu.
	 * 5. Verify dl is edited using SOAP.
	 * @throws HarnessException
	 */

	@Bugs (ids = "97150")
	@Test (description = "Verify edit operation for dynamic distribution list - Search distribution list view",
			groups = { "functional", "L2" })

	public void EditDistributionList_03() throws HarnessException {

		// Create a new dynamic dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();
		String memberURL ="ldap:///??sub?";

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest dynamic='1' xmlns='urn:zimbraAdmin'>"
						+ "<name>" + dlEmailAddress + "</name>"
						+ "<a xmlns='' n='zimbraIsACLGroup'>FALSE</a>"
						+ "<a xmlns='' n='zimbraMailStatus'>enabled</a>"
						+ "<a xmlns='' n='memberURL'>" +memberURL+ "</a>"
						+ "</CreateDistributionListRequest>");

		// Enter the search string to find the dl
		app.zPageSearchResults.zSetType(PageSearchResults.TypeOfObject.DISTRIBUTION_LIST);
		app.zPageSearchResults.zAddSearchQuery(dlEmailAddress);

		// Click search
		app.zPageSearchResults.zToolbarPressPulldown(Button.B_SEARCH_TYPE, Button.O_DISTRIBUTION_LISTS);
		app.zPageSearchResults.zToolbarPressButton(Button.B_SEARCH);

		// Right Click on distribution list to be edited
		app.zPageSearchResults.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Edit the name
		String editedName = "editedDL_" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
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