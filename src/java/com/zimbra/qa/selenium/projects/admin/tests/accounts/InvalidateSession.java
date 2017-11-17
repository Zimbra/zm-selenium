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
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageAccounts;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;

public class InvalidateSession extends AdminCommonTest {
	public InvalidateSession() {
		logger.info("New "+ InvalidateSession.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageAccounts;
	}

	
	/**
	 * Testcase : Invalidate Session  -- manage account >> Gearbox >> Invalidate Session
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Manage account >> Gearbox >> edit account >>  Invalidate Session
	 * 3. Verify toast message is displayed
	 * @throws HarnessException
	 */
	
	@Test (description = " Invalidate Session  -- manage account >> Gearbox >> edit account >>  Invalidate Session",
			groups = { "smoke", "L1" })
			public void InvalidateSession_01() throws HarnessException {

		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+		"</CreateAccountRequest>");

		// Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
	
		// Click on account
		app.zPageManageAccounts.zListItem(Action.A_LEFTCLICK, account.getEmailAddress());
		
		// Gearbox >> Invalidate Session
		app.zPageManageAccounts.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.B_INVALIDATE_SESSIONS);
		app.zPageManageAccounts.sClickAt(PageManageAccounts.Locators.INVALIDATE_SESSIONS_YES,"");
		
		// Verify toast message should be displayed regarding session is invalidated for an account
		String expectedMsg ="is invalid";
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage,expectedMsg, "Verify toast message '" + expectedMsg + "'");
	}

	/**
	 * Testcase : Invalidate Session  -- manage account >> right click >> Invalidate Session
	 * Steps :
	 * 1. Create an account using SOAP.
	 * 2. Edit the account name using UI Right Click.
	 * 3. Verify invalidate session functionality
	 * @throws HarnessException
	 */
	@Bugs (ids = "74482")
	@Test (description = "Invalidate Session -- manage account > right click > Invalidate Session",
			groups = { "functional", "L2" })
			public void InvalidateSession_02() throws HarnessException {

		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("email" + ConfigProperties.getUniqueString(),ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>test123</password>"
				+		"</CreateAccountRequest>");

		 // Refresh the account list
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Right click on account 
		app.zPageManageAccounts.zListItem(Action.A_RIGHTCLICK, Button.O_INVALIDATE_SESSIONS, account.getEmailAddress());
		app.zPageManageAccounts.sClickAt(PageManageAccounts.Locators.INVALIDATE_SESSIONS_YES,"");
	
		// Verify toast message should be displayed regarding session is invalidated for an account
		String expectedMsg ="is invalid";
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage,expectedMsg, "Verify toast message '" + expectedMsg + "'");
    
	}
}