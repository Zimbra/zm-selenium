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
package com.zimbra.qa.selenium.projects.admin.tests.accounts;

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
import com.zimbra.qa.selenium.projects.admin.ui.PageEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACL;

public class DeleteACL extends AdminCommonTest {

	public DeleteACL() {
		logger.info("New " + DeleteACL.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage=app.zPageManageAccounts;
	}

	/**
	 * Testcase : Delete ACL
	 * 1. Go to Manage Account View
	 * 2. Select an Account.
	 * 3. Edit an account using edit button in Gear box menu > Delete added ACL
	 * 4. Verify ACL is deleted 
	 * @throws HarnessException
	 */
	@Test(	description = "Delete ACL",
			groups = { "smoke", "L1" })
	public void DeleteACL_01() throws HarnessException {


		// Create grantee account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create target account
		AccountItem grantee = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(grantee);

		String rightName="sendAs";
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>" 
						+	"<target  by='name' type='account'>" + account.getEmailAddress() + "</target>"
						+	"<grantee  by='name' type='usr'>" + grantee.getEmailAddress() + "</grantee>" 
						+	"<right>"+ rightName + "</right>" 
						+ "</GrantRightRequest>");

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on edit
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageEditAccount.zClickAt(PageEditAccount.ztab_ACCOUNT_EDIT_ACL,"");
		app.zPageManageACL.zClickAt(PageManageACL.Locators.GRANTED_ACL,"");

		// Click on Delete button
		DialogForDeleteOperationACL dialog = (DialogForDeleteOperationACL) app.zPageManageACL.zToolbarPressButton(Button.B_DELETE);

		// Click Yes in Confirmation dialog
		dialog.zClickButton(Button.B_YES);

		// Verify ACL is not displayed on UI
		ZAssert.assertFalse(app.zPageEditAccount.zVerifyACL(rightName), "Verfiy email address is returned in search result");


	}
}
