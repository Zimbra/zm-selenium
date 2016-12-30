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
package com.zimbra.qa.selenium.projects.admin.tests.cos;

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
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACL;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageCOS;
import com.zimbra.qa.selenium.projects.admin.ui.WizardAddACLAtCos;

public class AddACL extends AdminCommonTest {

	public AddACL() {
		logger.info("New " + AddACL.class.getCanonicalName());

		// All tests start at the "Cos" page
		super.startingPage = app.zPageManageCOS;
	}

	/**
	 * Testcase : Add ACL to COS
	 * 1. Go to COS View
	 * 2. Select COS > Add ACL
	 * 3. Edit an account using edit button in Gear box menu.
	 * 4. Verify ACL is added 
	 * @throws HarnessException
	 */
	@Test( description = "Add ACL to COS",
			groups = { "smoke", "L1" })
	public void AddACL_01() throws HarnessException {

		AccountItem del_admin_account = new AccountItem("delegated_admin" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + del_admin_account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a xmlns='' n='zimbraIsDelegatedAdminAccount'>TRUE</a>"
						+		"</CreateAccountRequest>");

		String rightName="setAdminConsoleCOSACLTab";
		AclItem acl = new AclItem();
		acl.setRightName(rightName);

		// Create a new cos in the Admin Console using SOAP
		CosItem cos = new CosItem();
		String cosName=cos.getName();

		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateCosRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + cosName + "</name>"
						+		"</CreateCosRequest>");

		// Set grantee account email address
		acl.setGranteeAccountEmail(del_admin_account.getEmailAddress());

		// Refresh the account list
		app.zPageManageCOS.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be edited
		app.zPageManageCOS.zListItem(Action.A_LEFTCLICK, cosName);

		// Click on Edit button
		app.zPageManageCOS.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageManageCOS.zClickAt(PageManageCOS.Locators.COS_EDIT_ACL_TAB,"");
		
		// Click on Add 
		WizardAddACLAtCos wizard = 
				(WizardAddACLAtCos)app.zPageManageCOS.zToolbarPressButton(Button.B_ADD_ACL);

		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Verify added ACL is displayed on UI 
		ZAssert.assertTrue(app.zPageManageACL.sIsElementPresent(PageManageACL.Locators.GRANTED_ACL), "Verify added ACL is displayed on UI");

		// Verify ACL using soap
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetGrantsRequest  xmlns='urn:zimbraAdmin'>"
						+ "<target type='cos' by='name'>"+ cosName +"</target>"
						+ "</GetGrantsRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetGrantsResponse/admin:grant/admin:right", 1); 
		ZAssert.assertStringContains(response.getText(), rightName, "Verify right is edited!");
		app.zPageMain.logout();

	}

}
