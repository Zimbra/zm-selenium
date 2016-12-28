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
package com.zimbra.qa.selenium.projects.admin.tests.distributionlists;

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
import com.zimbra.qa.selenium.projects.admin.ui.WizardAddACLAtDL;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageDistributionLists.Locators;

public class AddACL extends AdminCommonTest {

	public AddACL() {
		logger.info("New " + AddACL.class.getCanonicalName());

		// All tests start at the "Distribution List" page
		super.startingPage = app.zPageManageDistributionList;
	}

	/**
	 * Testcase : Add ACL to DL
	 * 1. Go to Manage DL View
	 * 2. Select DL
	 * 3. Edit DL using edit button in Gear box menu > Add ACL
	 * 4. Verify ACL is added 
	 * @throws HarnessException
	 */
	@Test( description = "Add ACL to DL",
			groups = { "smoke", "L1" })
	public void AddACL_01() throws HarnessException {

		// Create target account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create grantee account
		AccountItem grantee = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(grantee);

		String rightName="sendAsDistList";
		String granteeType="usr";
		AclItem acl = new AclItem();
		acl.setRightName(rightName);
		acl.setGranteeType(granteeType);

		// Set grantee account email address
		acl.setGranteeAccountEmail(account.getEmailAddress());

		// Create a new dl in the Admin Console using SOAP
		DistributionListItem dl = new DistributionListItem();
		String dlEmailAddress=dl.getEmailAddress();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateDistributionListRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + dlEmailAddress + "</name>"
						+		"</CreateDistributionListRequest>");
		String Id = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CreateDistributionListResponse/admin:dl", "id").toString();

		// Refresh the list
		app.zPageManageDistributionList.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on distribution list 
		app.zPageManageDistributionList.zListItem(Action.A_LEFTCLICK, dl.getEmailAddress());

		// Click on Edit button
		app.zPageManageDistributionList.zToolbarPressPulldown(Button.B_GEAR_BOX,Button.O_EDIT);

		//Click on ACL tab
		app.zPageManageDistributionList.zClickAt(Locators.ACL_TAB,"");

		// Click "Add"
		WizardAddACLAtDL wizard = 
				(WizardAddACLAtDL)app.zPageManageACLAtDL.zToolbarPressButton(Button.B_ADD);
		SleepUtil.sleepSmall();

		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Verify ACL is displayed on UI
		ZAssert.assertTrue(app.zPageManageACLAtDL.zVerifyACL(rightName), "Verify ACL is displayed on UI");

		// Verify ACL using soap
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetGrantsRequest  xmlns='urn:zimbraAdmin'>"
						+ "<target type='dl' by='id'>"+ Id +"</target>"
						+ "</GetGrantsRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetGrantsResponse/admin:grant/admin:right", 1); 
		ZAssert.assertStringContains(response.getText(), rightName, "Verify right is granted!");
		app.zPageMain.logout();

	}

}
