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
package com.zimbra.qa.selenium.projects.admin.tests.globalacl;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.DialogForDeleteOperationACL;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class DeleteGlobalACL extends AdminCommonTest {

	public DeleteGlobalACL() {
		logger.info("New " + DeleteGlobalACL.class.getCanonicalName());

		super.startingPage = app.zPageManageGlobalACL;
	}

	/**
	 * Testcase : Delete ACL
	 * 1. Go to Global settings > ACL
	 * 2. Click on Delete
	 * 3. Verify ACL is deleted 
	 * @throws HarnessException
	 */
	@Test(	description = "Delete ACL",
			groups = { "smoke", "L1" })
	public void DeleteACL_01() throws HarnessException {

		
		// Create a new account in the Admin Console using SOAP
		AccountItem del_admin_account = new AccountItem("delegated_admin" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
						+			"<name>" + del_admin_account.getEmailAddress() + "</name>"
						+			"<password>test123</password>"
						+			"<a xmlns='' n='zimbraIsDelegatedAdminAccount'>TRUE</a>"
						+		"</CreateAccountRequest>");

		String rightName="getServer";
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>" 
						+ "<target type='global'>globalacltarget</target>"
						+	"<grantee  by='name' type='usr'>" + del_admin_account.getEmailAddress() +  "</grantee>" 
						+	"<right>"+ rightName + "</right>" 
						+ "</GrantRightRequest>");
		
		// Refresh the service list
		app.zPageManageGlobalACL.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");
		
		// Click on service to be deleted
		app.zPageManageGlobalACL.zListItem(Action.A_LEFTCLICK, del_admin_account.getEmailAddress());
		
		// Click on Add 
		DialogForDeleteOperationACL dialog = (DialogForDeleteOperationACL) app.zPageManageACL.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_DELETE);
		
		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);
		
		// Verify Added right is displayed on UI
		ZAssert.assertFalse(app.zPageManageGlobalACL.zVerifyGranteeName(del_admin_account.getEmailAddress()), "Verify Added right is displayed on UI");

		
		


	}
}
