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
import com.zimbra.qa.selenium.projects.admin.ui.PageEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACL;
import com.zimbra.qa.selenium.projects.admin.ui.WizardEditACL;

public class EditACL extends AdminCommonTest {

	public EditACL() {
		logger.info("New " + EditACL.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage=app.zPageManageAccounts;
	}

	/**
	 * Testcase : Edit ACL  
	 * 1. Go to Manage Account View
	 * 2. Select an Account.
	 * 3. Edit an account using edit button in Gear box menu > edit ACL details
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
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<GrantRightRequest xmlns='urn:zimbraAdmin'>" 
						+	"<target  by='name' type='account'>" + account.getEmailAddress() + "</target>"
						+	"<grantee  by='name' type='usr'>" + grantee.getEmailAddress() + "</grantee>" 
						+	"<right>"+ rightName + "</right>" 
						+ "</GrantRightRequest>");

		String editedRightName="sendOnBehalfOf";
		AclItem acl = new AclItem();
		acl.setRightName(editedRightName);

		// Set grantee account email address
		acl.setGranteeAccountEmail(update_grantee.getEmailAddress());

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on edit
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageEditAccount.zClickAt(PageEditAccount.ztab_ACCOUNT_EDIT_ACL,"");
		app.zPageManageACL.zClickAt(PageManageACL.Locators.GRANTED_ACL,"");

		// Click on Edit button
		// Click "New"
		WizardEditACL wizard = (WizardEditACL) app.zPageManageACL.zToolbarPressButton(Button.B_EDIT);

		wizard.zCompleteWizard(acl);
		app.zPageManageACL.zClickButton(Button.B_YES);

		// Verify ACL is edited on UI
		ZAssert.assertTrue(app.zPageEditAccount.zVerifyACL(editedRightName), "Verfiy edited right is returned in search result");

		// Verify ACL is edited using soap
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CheckRightRequest xmlns='urn:zimbraAdmin'>"
						+ "<target type='account' by='name'>"+ account.getEmailAddress()+"</target>"
						+ "<grantee type='usr' by='name'>"+ update_grantee.getEmailAddress()+"</grantee>"
						+ "<right>"+editedRightName+"</right>"
						+ "</CheckRightRequest>");
		String response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CheckRightResponse", "allow").toString();
		ZAssert.assertEquals(response, "1", "Verify ACL is edited!");
	}

}
