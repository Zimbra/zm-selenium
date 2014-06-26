/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.general.login;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.SeparateWindowChangePassword;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class ChangePassword extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public ChangePassword() {
		logger.info("New "+ ChangePassword.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPagePreferences;

		// Add a preference so the account is 'dirty' after changing password
		super.startingAccountPreferences = new HashMap<String, String>() {{
					put("zimbraPrefGroupMailBy", "message");
				}};
			
		
	}
	
	@Test(	description = "Change the account password",
			groups = { "functional" })
	public void ChangePassword_01() throws HarnessException {
		
		String password = "password"+ ZimbraSeleniumProperties.getUniqueString();
		
		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);
		
		// Determine the status of the checkbox
		SeparateWindowChangePassword dialog = (SeparateWindowChangePassword)app.zPagePreferences.zToolbarPressButton(Button.B_CHANGE_PASSWORD);
		ZAssert.assertNotNull(dialog, "Verify the dialog was created");
		
		dialog.zSetOldPassword(app.zGetActiveAccount().Password);
		dialog.zSetNewPassword(password);
		dialog.zSetConfirmPassword(password);
		
		dialog.zClickButton(Button.B_SAVE);
		dialog.zCloseWindow();
				
		// Confirm that the new password is in use
		// by getting a new token
		app.zGetActiveAccount().Password = password;
		app.zGetActiveAccount().soapSend(
					"<AuthRequest xmlns='urn:zimbraAccount'>"
				+		"<account by='name'>"+ app.zGetActiveAccount().EmailAddress +"</account>"
				+		"<password>"+ app.zGetActiveAccount().Password +"</password>"
				+	"</AuthRequest>");
		String token = app.zGetActiveAccount().soapSelectValue("//acct:AuthResponse//acct:authToken", null);
		ZAssert.assertGreaterThan(token.trim().length(), 0, "Verify the token is returned");
		
		app.zGetActiveAccount().authenticate();
	}



}
