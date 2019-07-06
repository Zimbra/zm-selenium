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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Checkbox;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.PageMain;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ReplyToSentMessage extends AjaxCore {

	public ReplyToSentMessage() {
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Verify that reply-to-sent-message details are set/unset properly",
			groups = { "functional" })

	public void ReplyToSentMessage_01() throws HarnessException {
		
		// Account to set the reply-to address
		ZimbraAccount activeAccount = app.zGetActiveAccount();
		ZimbraAccount account = ZimbraAccount.Account6();

		// Navigate to preferences -> Accounts
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		
		// Check the Reply-to Check box
		app.zPagePreferences.zCheckboxSet(Checkbox.C_REPLY_TO_SENT_MESSAGE, true);
		
		// Enter name and email address
		app.zPagePreferences.sType(Locators.zFromAccountName, account.DisplayName);
		app.zPagePreferences.sType(Locators.zReplyToSentMessageName, account.DisplayName);
		app.zPagePreferences.sType(Locators.zReplyToSentMessageEmail, account.EmailAddress);
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		
		// Refresh the web-client
		app.zPageMain.zRefreshMainUI();
		app.zPageMain.zNavigateToAppTab(PageMain.Locators.zPreferencesTab);
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		
		// Verify that all the entered details are saved
		ZAssert.assertEquals(app.zPagePreferences.sGetAttribute(Locators.zReplyToSentMessageName + "@value"), account.DisplayName, "Verify that entered reply-to name is displayed correctly");
		ZAssert.assertEquals(app.zPagePreferences.sGetAttribute(Locators.zReplyToSentMessageEmail + "@value"), account.EmailAddress, "Verify that entered reply-to email is displayed correctly");
		ZAssert.assertTrue(app.zPagePreferences.sIsChecked(Locators.zReplyToSentMessageCheckbox),"Verify that reply-to checkbox is checked");
			
		// Remove the entered name and email address
		app.zPagePreferences.sType(Locators.zReplyToSentMessageName, "");
		app.zPagePreferences.sType(Locators.zReplyToSentMessageEmail, "");
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		
		//Logout --> login --> Preferences --Account
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(activeAccount);
		startingPage.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		
		// Verify that entered values are not present
		ZAssert.assertEquals(app.zPagePreferences.sGetAttribute(Locators.zReplyToSentMessageName + "@value"), "", "Verify that entered reply-to name is not displayed");
		ZAssert.assertEquals(app.zPagePreferences.sGetAttribute(Locators.zReplyToSentMessageEmail + "@value"), "", "Verify that entered reply-to email is not displayed");
		ZAssert.assertTrue(app.zPagePreferences.sIsChecked(Locators.zReplyToSentMessageCheckbox),"Verify that reply-to checkbox is checked");
	}
}