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
package com.zimbra.qa.selenium.projects.admin.tests.distributionlists;

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

public class EditDistributionList extends AdminCore {

	public EditDistributionList() {
		logger.info("New "+ EditDistributionList.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	@Test (description = "Edit Distribution List name - Manage Distribution List view",
			groups = { "bhr" })

	public void EditDistributionList_01() throws HarnessException {
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

		// Click on distribution list to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);
		//FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditDistributionList.TreeItem.MEMBERS);

		// Edit the name
		String editedName = "tcediteddl" + ConfigProperties.getUniqueString();
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


	@Test (description = "Edit Distribution List name - Manage Distribution List view + Right Click Menu",
			groups = { "sanity" })

	public void EditDistributionList_02() throws HarnessException {
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

		// Right Click on distribution list to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_RIGHTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressButton(Button.B_TREE_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditDistributionList.TreeItem.MEMBERS);

		// Edit the name
		String editedName = "tcediteddl" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();

		// Verify the dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+editedName+"@"+dl.getDomainName()+	"</dl>"+
				"</GetDistributionListRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
	}


	@Test (description = "Edit Admin Distribution List name - Manage Distribution List view",
			groups = { "sanity" })

	public void EditDistributionList_03() throws HarnessException {
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

		// Click on distribution list to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);
		//FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditDistributionList.TreeItem.MEMBERS);

		// Edit the name
		String editedName = "tcediteddl" + ConfigProperties.getUniqueString();
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


	@Test (description = "Edit Dynamic Admin Distribution List name - Manage Distribution List view",
			groups = { "sanity" })

	public void EditDistributionList_04() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		String memberURL ="ldap:///??sub?";
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest dynamic='1' xmlns='urn:zimbraAdmin'>"
						+ "<name>" + dlEmailAddress + "</name>"
						+ "<a n='zimbraIsACLGroup'>FALSE</a>"
						+ "<a n='zimbraMailStatus'>enabled</a>"
						+ "<a n='memberURL'>" + memberURL + "</a>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+ "</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on distribution list to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);
		//FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditDistributionList.TreeItem.MEMBERS);

		// Edit the name
		String editedName = "tcediteddl" + ConfigProperties.getUniqueString();
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


	@Bugs (ids = "97150")
	@Test (description = "Edit dynamic Distribution List name - Manage Distribution List view",
			groups = { "functional" })

	public void EditDistributionList_05() throws HarnessException {
		// Create a new dynamic dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();
		String memberURL ="ldap:///??sub?";

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest dynamic='1' xmlns='urn:zimbraAdmin'>"
						+ "<name>" + dlEmailAddress + "</name>"
						+ "<a n='zimbraIsACLGroup'>FALSE</a>"
						+ "<a n='zimbraMailStatus'>enabled</a>"
						+ "<a n='memberURL'>" + memberURL+ "</a>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+ "</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on distribution list to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditDistributionList.TreeItem.MEMBERS);

		// Edit the name
		String editedName = "tcediteddl" + ConfigProperties.getUniqueString();
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


	@Bugs (ids = "97150")
	@Test (description = "Edit Dynamic Distribution List name - Manage Distribution List view + Right Click Menu",
			groups = { "functional" })

	public void EditDistributionList_06() throws HarnessException {
		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();
		String memberURL ="ldap:///??sub?";

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest dynamic='1' xmlns='urn:zimbraAdmin'>"
						+ "<name>" + dlEmailAddress + "</name>"
						+ "<a n='zimbraIsACLGroup'>FALSE</a>"
						+ "<a n='zimbraMailStatus'>enabled</a>"
						+ "<a n='memberURL'>" + memberURL+ "</a>"
						+ "<a n='description'>Created by Selenium automation</a>"
						+ "</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Right Click on distribution list to be deleted.
		app.zPageManageDistributionList.zListItem(Action.A_RIGHTCLICK, dl.getEmailAddress());

		// Click on Edit button
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressButton(Button.B_TREE_EDIT);

		// Click on General Information tab
		form.zSelectTreeItem(FormEditDistributionList.TreeItem.MEMBERS);

		// Edit the name
		String editedName = "tcediteddl" + ConfigProperties.getUniqueString();
		form.zSetName(editedName);

		// Submit
		form.zSubmit();
		// Verify the edited dynamic dl exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
						"<dl by='name'>"+editedName+"@"+dl.getDomainName()+	"</dl>"+
				"</GetDistributionListRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl", 1);
		ZAssert.assertNotNull(response, "https://bugzilla.zimbra.com/show_bug.cgi?id=74487");
	}
}