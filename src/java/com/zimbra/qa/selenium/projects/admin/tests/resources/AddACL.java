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
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACL;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageResources.Locators;
import com.zimbra.qa.selenium.projects.admin.ui.WizardAddACL;

public class AddACL extends AdminCommonTest {

	public AddACL() {
		logger.info("New " + AddACL.class.getCanonicalName());

		// All tests start at the "Resources" page
		super.startingPage = app.zPageManageResources;
	}

	/**
	 * Testcase : Add ACL to Resources
	 * 1. Go to Manage Resources View
	 * 2. Select an resource
	 * 3. Edit resource using edit button in Gear box menu > Add ACL
	 * 4. Verify ACL is added 
	 * @throws HarnessException
	 */
	@Test( description = "Add ACL to Resources",
			groups = { "smoke", "L1" })
	public void AddACL_01() throws HarnessException {

		// Create target account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create grantee account
		AccountItem grantee = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(grantee);

		String rightName="sendAs";
		AclItem acl = new AclItem();
		acl.setRightName(rightName);

		// Set grantee account email address
		acl.setGranteeAccountEmail(grantee.getEmailAddress());

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

		// Refresh the Resource list
		app.zPageManageResources.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on Resource to be Edited.
		app.zPageManageResources.zListItem(Action.A_LEFTCLICK, resource.getEmailAddress());

		// Click on Edit button
		app.zPageManageResources.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageManageResources.zClickAt(Locators.ACL,"");

		// Click "Add"
		WizardAddACL wizard = 
				(WizardAddACL)app.zPageManageResources.zToolbarPressButton(Button.B_ADD_ACL);
		SleepUtil.sleepSmall();

		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Verify ACL is displayed on UI
		ZAssert.assertTrue(app.zPageManageACL.sIsElementPresent(PageManageACL.Locators.GRANTED_ACL), "Verify ACL is displayed on UI");

		// Verify ACL using soap
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetGrantsRequest  xmlns='urn:zimbraAdmin'>"
						+ "<target type='calresource' by='id'>"+ Id +"</target>"
						+ "</GetGrantsRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetGrantsResponse/admin:grant/admin:right", 1); 
		ZAssert.assertStringContains(response.getText(), rightName, "Verify right is granted!");
		app.zPageMain.logout();

	}

}
