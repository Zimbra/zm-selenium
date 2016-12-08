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
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.*;
import com.zimbra.qa.selenium.projects.admin.ui.PageEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageACL;
import com.zimbra.qa.selenium.projects.admin.ui.WizardAddACL;

public class AddACL extends AdminCommonTest {

	public AddACL() {
		logger.info("New " + AddACL.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage=app.zPageManageAccounts;
	}

	/**
	 * Testcase : Add ACL to account
	 * 1. Go to Manage Account View
	 * 2. Select an Account.
	 * 3. Edit an account using edit button in Gear box menu.
	 * 4. Verify ACL is added 
	 * @throws HarnessException
	 */
	@Test( description = "Add ACL to account",
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

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on edit
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);

		//Click on ACL tab
		app.zPageEditAccount.zClickAt(PageEditAccount.ztab_ACCOUNT_EDIT_ACL,"");

		// Click "New"
		WizardAddACL wizard = 
				(WizardAddACL)app.zPageManageACL.zToolbarPressButton(Button.B_ADD);
		
		SleepUtil.sleepLong();
		
		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Verify edit account page opened up 
		ZAssert.assertTrue(app.zPageManageACL.sIsElementPresent(PageManageACL.Locators.GRANTED_ACL), "Verify general tab is displayed");

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CheckRightRequest xmlns='urn:zimbraAdmin'>"
						+ "<target type='account' by='name'>"+ account.getEmailAddress()+"</target>"
						+ "<grantee type='usr' by='name'>"+ grantee.getEmailAddress()+"</grantee>"
						+ "<right>"+rightName+"</right>"
						+ "</CheckRightRequest>");
		String response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CheckRightResponse", "allow").toString();
		ZAssert.assertEquals(response, "1", "Verify right is granted!");

	}

}
