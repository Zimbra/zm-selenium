/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.globalacl;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.WizardGlobalACL;

public class AddGlobalACL extends AdminCommonTest {

	public AddGlobalACL() {

		logger.info("New "+ NavigateGlobalACL.class.getCanonicalName());

		// All tests start at the "Cos" page
		super.startingPage = app.zPageManageGlobalACL;
	}

	/**
	 * Testcase : Add ACL 
	 * 1. Go to Global settings > ACL
	 * 2. Click on Add
	 * 3. Enter all the details 
	 * 4. Verify ACL is added 
	 * @throws HarnessException
	 */
	@Test( description = "Add ACL to account",
			groups = { "smoke" })
	public void AddGlobalACL_01() throws HarnessException {

		// Create a new account in the Admin Console using SOAP
		AccountItem del_admin_account = new AccountItem("delegated_admin" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + del_admin_account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a xmlns='' n='zimbraIsDelegatedAdminAccount'>TRUE</a>"
						+		"</CreateAccountRequest>");

		String rightName="getServer";
		AclItem acl = new AclItem();
		acl.setRightName(rightName);

		// Set grantee account email address
		acl.setGranteeAccountEmail(del_admin_account.getEmailAddress());

		// Click on Add 
		WizardGlobalACL wizard = 
				(WizardGlobalACL)app.zPageManageACL.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_ADD_GLOBAL_ACL);

		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Verify Added right is displayed on UI
		ZAssert.assertTrue(app.zPageManageGlobalACL.zVerifyGranteeName(del_admin_account.getEmailAddress()), "Verify Added right is displayed on UI");

		// Verify Right is granted successfully
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<GetGrantsRequest xmlns='urn:zimbraAdmin'>"
						+ "<target type='global'></target>"
						+ "<grantee all='1' type='usr' by='name'>"+ del_admin_account.getEmailAddress()+"</grantee>"
						+ "<right>"+rightName+"</right>"
						+ "</GetGrantsRequest>");

		Element response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectNode("//admin:GetGrantsResponse/admin:grant/admin:right", 1); 
		ZAssert.assertStringContains(response.getText(), rightName, "Verify right is granted!");

	}

}
