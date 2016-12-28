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
 * If not, see <http://www.gnu.org/licenses/>.
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
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACLAtDL;
import com.zimbra.qa.selenium.projects.admin.ui.WizardEditACL;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageResources.Locators;

public class EditACL extends AdminCommonTest {

	public EditACL() {
		logger.info("New " + EditACL.class.getCanonicalName());

		// All tests start at the "Resources" page
		super.startingPage = app.zPageManageResources;
	}

	/**
	 * Testcase : Edit ACL 
	 * 1. Go to Manage Resources View
	 * 2. Select resource
	 * 3. Edit an resource using edit button in Gear box menu > edit resource
	 * 4. Verify ACL is edited 
	 * @throws HarnessException
	 */
	@Test(	description = "Edit ACL",
			groups = { "smoke", "L1" })
	public void EditACL_01() throws HarnessException {

		// Create grantee account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create target account
		AccountItem grantee = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(grantee);

		AccountItem update_grantee = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(update_grantee);
		String rightName="sendAs";
		String editedRightName="sendOnBehalfOf";
		AclItem acl = new AclItem();
		acl.setRightName(editedRightName);

		// Set grantee account email address
		acl.setGranteeAccountEmail(account.getEmailAddress());

		// Create a new Resource in the Admin Console using SOAP
		ResourceItem resource = new ResourceItem();
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCalendarResourceRequest xmlns='urn:zimbraAdmin'>"
						+ "<name>" + resource.getEmailAddress() + "</name>"
						+ "<a n=\"displayName\">" + resource.getName() + "</a>"
						+ "<a n=\"zimbraCalResType\">" + "Location" + "</a>"
						+ "<password>test123</password>"
						+ "</CreateCalendarResourceRequest>");
		String Id = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CreateCalendarResourceResponse/admin:calresource", "id").toString();

		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>" 
						+	"<target  by='name' type='calresource'>" + resource.getEmailAddress() + "</target>"
						+	"<grantee  by='name' type='usr'>" + account.getEmailAddress() + "</grantee>" 
						+	"<right>"+ rightName + "</right>" 
						+ "</GrantRightRequest>");

		// Refresh the Resource list
		app.zPageManageResources.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on Resource to be Edited.
		app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Edit button
		app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageManageResources.zClickAt(Locators.ACL,"");

		app.zPageManageACLAtDL.zClickAt(PageManageACLAtDL.Locators.GRANTED_ACL,"");

		// Click "Edit"
		WizardEditACL wizard = (WizardEditACL)app.zPageManageResources.zToolbarPressButton(Button.B_EDIT_ACL);

		// Complete wizard
		wizard.zCompleteWizard(acl);
		app.zPageManageACL.zClickButton(Button.B_YES);

		// Verify ACL is edited and displayed on UI
		ZAssert.assertTrue(app.zPageManageACLAtDL.zVerifyACL(editedRightName), "Verfiy right name is returned in search result");

		// Verify ACL using soap
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetGrantsRequest  xmlns='urn:zimbraAdmin'>"
						+ "<target type='calresource' by='id'>"+ Id +"</target>"
						+ "</GetGrantsRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetGrantsResponse/admin:grant/admin:right", 1); 
		ZAssert.assertStringContains(response.getText(), editedRightName, "Verify right is edited!");
		app.zPageMain.logout();


	}

}
