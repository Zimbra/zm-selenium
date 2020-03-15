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
import com.zimbra.qa.selenium.projects.admin.core.AdminCore;
import com.zimbra.qa.selenium.projects.admin.items.AccountItem;
import com.zimbra.qa.selenium.projects.admin.pages.PageManageAccounts;
import com.zimbra.qa.selenium.projects.ajax.pages.Toaster;

public class InvalidateSession extends AdminCore {

	public InvalidateSession() {
		logger.info("New "+ InvalidateSession.class.getCanonicalName());
		super.startingPage = app.zPageManageAccounts;
	}


	@Test (description = " Invalidate Session  -- manage account >> Gearbox >> edit account >>  Invalidate Session",
			groups = { "functional" })

	public void InvalidateSession_01() throws HarnessException {
		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		// Refresh the list
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


	@Bugs (ids = "74482")
	@Test (description = "Invalidate Session -- manage account > right click > Invalidate Session",
			groups = { "functional" })

	public void InvalidateSession_02() throws HarnessException {
		// Create a new account in the admin Console using SOAP
		AccountItem account = new AccountItem("tc" + ConfigProperties.getUniqueString(), ConfigProperties.getStringProperty("testdomain"));
		ZimbraAdminAccount.AdminConsoleAdmin().soapSend(
				"<CreateAccountRequest xmlns='urn:zimbraAdmin'>"
				+			"<name>" + account.getEmailAddress() + "</name>"
				+			"<password>" + ConfigProperties.getStringProperty("accountPassword") + "</password>"
				+			"<a n='description'>Created by Selenium automation</a>"
				+		"</CreateAccountRequest>");

		 // Refresh the list
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