/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.FormEditAccount;
import com.zimbra.qa.selenium.projects.admin.ui.PageLogin.Locators;
import com.zimbra.qa.selenium.projects.admin.ui.PageMain;

public class ResetPassword extends AdminCommonTest {
	public ResetPassword() {
		logger.info("New "+ ResetPassword.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccounts;
	}

	/**
	 * Testcase : Bug 72655 Cannot change admin password from Admin Console login
	 * Steps :
	 *1. Create admin account, set a password.
	 *2. zmprov ma testadmin@foo.com zimbraPasswordMustChange TRUE
	 *3. Log in to Admin Console with testadmin account, enter password.
	 * @throws HarnessException
	 */
	   @Bugs( ids = "72655")
	   @Test( description = "Edit password  -- manage account > Select account > Options > Edit > change password",
			groups = { "functional", "L2" })
			public void ResetPassword_01() throws HarnessException {
			
			// Create admin account
			String adminaccount = "admin"+ ConfigProperties.getUniqueString() + "@" + ConfigProperties.getStringProperty("testdomain");
			ZimbraAdminAccount account = new ZimbraAdminAccount(adminaccount);
			account.provision();			

			// Refresh the account list
			app.zPageManageAccounts.sClickAt(PageMain.Locators.REFRESH_BUTTON, "");

			// Click on account to be Edited.
			app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, adminaccount);

			// Click on Edit button
			FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_EDIT);
			
			// Check must change password
			app.zPageManageAccounts.zPreferenceCheckboxSet(Button.B_MUST_CHANGE_PASSWORD,true);
			
			// Submit the form
			form.zSubmit();
			
			// Logout from global admin account
			app.zPageMain.logout();			
						
			// Enter new username password for created admin account
			app.zPageLogin.fillLoginFormFields(account);

			// Click the Login button
			app.zPageLogin.sClick(Locators.zLoginButtonContainer);
			
			// Reset the password
			app.zPageLogin.fillResetLoginPasswordFormFields("test1234", "test1234");
			
			// Verify admin is able to login with new password
			ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify admin cosole is opened up");
			
	}

}
