/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;
import com.zimbra.qa.selenium.projects.admin.ui.WizardConfigureGrants;
import com.zimbra.qa.selenium.projects.admin.ui.WizardConfigureGrants.Locators;

public class ConfigureGrants extends AdminCommonTest {

	public ConfigureGrants() {
		logger.info("New " + ConfigureGrants.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage=app.zPageManageAccounts;
	}

	/**
	 * Testcase : Add ACE to account 
	 * 1. Go to Manage Account View
	 * 2. Select an Account.
	 * 3. Right click on Account and select Configure grants option
	 * 4. Click on gear icon and select Add > Enter details > Click on 'Add and Finish'  
	 * 5. Verify ACE is added successfully
	 * @throws HarnessException
	 */
	@Test( description = "Verify Configure Grants option by selecting it from right click menu option",
			groups = { "sanity" })
	public void AddACE_01() throws HarnessException {

		// Create grantee account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create target account
		AccountItem target_account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target_account);

		String rightName="sendAs";
		AclItem acl = new AclItem();
		acl.setRightName(rightName);

		// Set target account email address
		acl.setTargetAccountEmail(target_account.getEmailAddress());

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Right click on account 
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, account.getEmailAddress());

		// Click on Configure grants
		app.zPageManageAccounts.zToolbarPressButton(Button.B_CONFIGURE_GRANTS);

		SleepUtil.sleepMedium();

		// Click on Add
		WizardConfigureGrants wizard= (WizardConfigureGrants) app.zPageManageConfigureGrants.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_ADD);

		// Select target type
		wizard.selectTargetType(Locators.ACCOUNT);

		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Click on "Add and Finish"
		wizard.zCloseWizard();

		SleepUtil.sleepMedium();

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CheckRightRequest xmlns='urn:zimbraAdmin'>"
						+ "<target type='account' by='name'>"+ target_account.getEmailAddress() +"</target>"
						+ "<grantee type='usr' by='name'>"+ account.getEmailAddress()+"</grantee>"
						+ "<right>"+rightName+"</right>"
						+ "</CheckRightRequest>");
		String response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CheckRightResponse", "allow").toString();
		ZAssert.assertEquals(response, "1", "Verify right is granted!");

		boolean isACEAdded = app.zPageManageConfigureGrants.zVerifyACE(account.getEmailAddress());
		ZAssert.assertTrue(isACEAdded, "Verify ACE is added successfully!!");

	}

	/**
	 * Testcase : Add ACE to account 
	 * 1. Go to Manage Account View
	 * 2. Select an Account.
	 * 3. Click on gear icon and select Configure Grants
	 * 4. Click on gear icon and select Add > Enter details > Click on 'Add and Finish'  
	 * 5. Verify ACE is added successfully
	 * @throws HarnessException
	 */
	@Test( description = "Verify Configure Grants option by selecting it from gear menu option",
			groups = { "sanity" })
	public void AddACE_02() throws HarnessException {

		// Create grantee account
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(account);

		// Create target account
		AccountItem target_account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		AccountItem.createUsingSOAP(target_account);
		String rightName="sendAs";

		AclItem acl = new AclItem();
		acl.setRightName(rightName);

		// Set target account email address
		acl.setTargetAccountEmail(target_account.getEmailAddress());

		// Refresh the account list
		app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

		// Click on account to be Edited.
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());

		// Click on configure grants 
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_CONFIGURE_GRANTS);

		SleepUtil.sleepMedium();

		// Click on Add
		WizardConfigureGrants wizard= (WizardConfigureGrants) app.zPageManageConfigureGrants.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_ADD);

		// Select target type
		wizard.selectTargetType(Locators.ACCOUNT);

		// Fill out the wizard	
		wizard.zCompleteWizard(acl);

		// Click on "Add and Finish"
		wizard.zCloseWizard();

		SleepUtil.sleepMedium();

		// Verify the alias exists in the ZCS
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CheckRightRequest xmlns='urn:zimbraAdmin'>"
						+ "<target type='account' by='name'>"+ target_account.getEmailAddress() +"</target>"
						+ "<grantee type='usr' by='name'>"+ account.getEmailAddress()+"</grantee>"
						+ "<right>"+rightName+"</right>"
						+ "</CheckRightRequest>");
		String response = ZimbraAdminAccount.AdminConsoleAdmin().soapSelectValue("//admin:CheckRightResponse", "allow").toString();
		ZAssert.assertEquals(response, "1", "Verify right is granted!");

		boolean isACEAdded = app.zPageManageConfigureGrants.zVerifyACE(account.getEmailAddress());
		ZAssert.assertTrue(isACEAdded, "Verify ACE is added successfully!!");

	}
}
