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
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.DistributionListItem;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDistributionList;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDistributionList.Locators;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditDistributionList.TreeItem;
import com.zimbra.qa.selenium.projects.admin.pages.PageMain;

public class AddDirectMemberOfDistributionList extends AdminCore {

	public AddDirectMemberOfDistributionList() {
		logger.info("New "+ AddDirectMemberOfDistributionList.class.getCanonicalName());
		super.startingPage = app.zPageManageDistributionList;
	}


	@Bugs (ids = "99081, ZCS-1638")
	@Test (description = "Edit DL - Add 'Direct Member of' to DL",
		   	groups = { "smoke", "L1" })

	public void AddDirectMemberOfDistributionList_01() throws HarnessException {

		// Create two distribution lists in the Admin Console using SOAP
		DistributionListItem dl1 = new DistributionListItem();
		DistributionListItem dl2 = new DistributionListItem();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dl1.getEmailAddress() + "</name>"
						+		"</CreateDistributionListRequest>");

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dl2.getEmailAddress() + "</name>"
						+		"</CreateDistributionListRequest>");

		// Refresh the list
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on distribution list to be edited.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl1.getEmailAddress());

		// Open the Dl for editing
		FormEditDistributionList form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Select Member of of tree element
		form.zSelectTreeItem(TreeItem.MEMBER_OF);

		// Check the message displayed inside the Direct member of box
		ZAssert.assertEquals(form.sGetText(Locators.DirectMemberOf),"Not a direct member of any distribution list","Verify that Loading... text is not present");

		// Check the message displayed inside the Indirect members of box
		ZAssert.assertEquals(form.sGetText(Locators.IndirectMemberOf),"Not an indirect member of any distribution list","Verify that Loading... text is not present");

		// Add DL1 as direct member of DL2
		form.zAddDistributionList(dl2);

		// Save the changes in DL
		form.zSubmit();

		// Check the content of inside the Direct member of box
		ZAssert.assertEquals(form.sGetText(Locators.DirectMemberOf),dl2.getEmailAddress(),"Verify that " + dl2.getEmailAddress() + "is added to the direct member of list");

		// Check the message displayed inside the Indirect members of box
		ZAssert.assertEquals(form.sGetText(Locators.IndirectMemberOf),"Not an indirect member of any distribution list","Verify that Loading... text is not present");

		// Verify using SOAP that DL1 ia a member of DL2
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetDistributionListRequest xmlns='urn:zimbraAdmin'>" +
				"<dl by='name'>"+ dl2.getEmailAddress() +"</dl>"+
		"</GetDistributionListRequest>");

		Element dlMember = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetDistributionListResponse/admin:dl/admin:dlm", 1);

		ZAssert.assertEquals(dlMember.getText(), dl1.getEmailAddress(), "Verify that " + dl1.getEmailAddress() + "is a member of " + dl2.getEmailAddress());

		app.zPageManageDistributionList.zRefreshMainUI();

		startingPage.zNavigateTo();

		// Click on distribution list to be edited.
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl1.getEmailAddress());

		// Open the Dl for editing
		form = (FormEditDistributionList) app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		// Select Member of of tree element
		form.zSelectTreeItem(TreeItem.MEMBER_OF);

		// Check the content of inside the Direct member of box
		ZAssert.assertEquals(form.sGetText(Locators.DirectMemberOf),dl2.getEmailAddress(),"Verify that" + dl2.getEmailAddress() + "is added to the direct member of list");

		// Check the message displayed inside the Indirect members of box
		ZAssert.assertEquals(form.sGetText(Locators.IndirectMemberOf),"Not an indirect member of any distribution list","Verify that Loading... text is not present");
	}
}