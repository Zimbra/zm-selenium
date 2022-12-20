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
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.pages.FormEditAccount;
import com.zimbra.qa.selenium.projects.admin.pages.PageLogin.Locators;

public class ResetPassword extends AdminCore {

	public ResetPassword() {
		logger.info("New "+ ResetPassword.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	@Bugs (ids = "72655")
	@Test (description = "Edit password  -- manage account > Select account > Options > Edit > change password",
			groups = { "sanity", "testcafe" })

	public void ResetPassword_01() throws HarnessException {
		// Create admin account
		String adminaccount = "tc" + ConfigProperties.getUniqueString() + "@"
				+ ConfigProperties.getStringProperty("testdomain");
		ZimbraAdminAccount account = new ZimbraAdminAccount(adminaccount);
		account.provision();

		// Refresh the list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Click on account to be edited
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, adminaccount);

		// Click on Edit button
		FormEditAccount form = (FormEditAccount) app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT,
				adminaccount);

		// Check must change password
		app.zPageManageAccounts.zPreferenceCheckboxSet(Button.B_MUST_CHANGE_PASSWORD, true);

		// Save the changes
		form.zSave();

		// Logout from global admin account
		app.zPageMain.zLogout();

		// Enter new username password for created admin account
		app.zPageLogin.zFillLoginFormFields(account);

		// Click the Login button
		app.zPageLogin.sClick(Locators.zLoginButtonContainer);

		// Reset the password
		app.zPageLogin.zFillResetLoginPasswordFormFields("test1234", "test1234");

		// Verify admin is able to login with new password
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify admin cosole is opened up");

		// Logout the current user
		app.zPageMain.zLogout();
	}
}