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
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.accounts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogDelegate;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class AddDelegate extends AjaxCore {

	public AddDelegate() {
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Add a 'Send As' delegate to the primary account",
			groups = { "sanity", "L0" })

	public void AddDelegate_01() throws HarnessException {

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		app.zPagePreferences.zPressButton(Button.B_ADD_DELEGATE);

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		dialog.zSetEmailAddress(delegate.EmailAddress);
		dialog.zCheckRight(DialogDelegate.Rights.SendAs);
		dialog.zPressButton(Button.B_OK);

		// Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		String gt = app.zGetActiveAccount().soapSelectValue("//acct:ace[@d='" + delegate.EmailAddress + "']", "gt");
		String right = app.zGetActiveAccount().soapSelectValue("//acct:ace[@d='" + delegate.EmailAddress + "']", "right");

		ZAssert.assertEquals(gt, "usr", "Verify the user (usr) right was set correctly");
		ZAssert.assertEquals(right, "sendAs", "Verify the sendAs (sendAs) right was set correctly");
	}


	@Test (description = "Add a 'Send On Behalf Of' delegate to the primary account",
			groups = { "sanity", "L0" })

	public void AddDelegate_02() throws HarnessException {

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.startingPage.zNavigateTo();

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		app.zPagePreferences.zPressButton(Button.B_ADD_DELEGATE);

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		dialog.zSetEmailAddress(delegate.EmailAddress);
		dialog.zCheckRight(DialogDelegate.Rights.SendOnBehalfOf);
		dialog.zPressButton(Button.B_OK);

		// Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		String gt = app.zGetActiveAccount().soapSelectValue("//acct:ace[@d='" + delegate.EmailAddress + "']", "gt");
		String right = app.zGetActiveAccount().soapSelectValue("//acct:ace[@d='" + delegate.EmailAddress + "']", "right");

		ZAssert.assertEquals(gt, "usr", "Verify the user (usr) right was set correctly");
		ZAssert.assertEquals(right, "sendOnBehalfOf", "Verify the sendAs (sendAs) right was set correctly");
	}
}