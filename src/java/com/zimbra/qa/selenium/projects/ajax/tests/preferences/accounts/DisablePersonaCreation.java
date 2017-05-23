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
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class DisablePersonaCreation extends AjaxCommonTest {

	public DisablePersonaCreation() {
		super.startingPage = app.zPagePreferences;
	}

	@Test(description = "Verify the display of 'Add Persona' button when Persona creation is disabled", 
			groups = { "functional", "L3" })

	public void DisablePersonaCreation_01() throws HarnessException {

		// Navigate to preferences -> Accounts
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		
		// Verify the display of Add Persona button when Persona creation is allowed.
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zAddPersonaButton), "Add persona button is not present!");

		//Disable the External IMAP and POP3 access for the user
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
						+		"<a n='zimbraFeatureIdentitiesEnabled'>FALSE</a>"
						+	"</ModifyAccountRequest>");

		// Refresh Web-client
		app.zPageMail.sRefresh();

		// Navigate to Preferences-->Accounts
		startingPage.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Verify the display of Add Persona button when Persona access is disabled.
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent(Locators.zAddPersonaButton), 
				"Add persona button is displayed even if the Persona access is disabled!");

		// Enable Persona for the user
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
						+		"<a n='zimbraFeatureIdentitiesEnabled'>TRUE</a>"
						+	"</ModifyAccountRequest>");

		// Refresh Web-client
		app.zPageMail.sRefresh();

		// Navigate to Preferences-->Accounts
		startingPage.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Add a Persona
		app.zPagePreferences.sClick(Locators.zAddPersonaButton);

		// Verify that a Persona row has been added
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zPersonaRow1), "Persona is not getting created!");
	}

}
