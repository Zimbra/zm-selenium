/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.trustedaddresses;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class AddTrustedEmail extends AjaxCommonTest {

	public AddTrustedEmail() throws HarnessException {
		super.startingPage = app.zPagePreferences;
	}


	@Bugs ( ids = "101356")
	@Test (description = "Add a trusted email address",
			groups = { "smoke", "L1" } )

	public void AddTrustedEmail_01() throws HarnessException {

		final String email = "email" + ConfigProperties.getUniqueString() + "@zimbra.com";

		// Navigate to preferences -> mail -> Trusted Addresses
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailTrustedAddresses);

		// Add the email address
		app.zPagePreferences.sType("css=td[id$='_EMAIL_ADDRESS'] input", email);

		// Click "Add"
		app.zPagePreferences.sClick("css=td[id$='_ADD_BUTTON'] td[id$='_title']");

		// Click "Save"
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Wait for the ModifyPrefsRequest to complete
		app.zPagePreferences.zWaitForBusyOverlay();

		app.zGetActiveAccount().soapSend(
					"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+		"<pref name='zimbraPrefMailTrustedSenderList'/>"
				+	"</GetPrefsRequest>");

		String found = null;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:pref[@name='zimbraPrefMailTrustedSenderList']");
		for (Element e : nodes) {
			if ( e.getText().contains(email) ) {
				found = e.getText();
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify that the address is saved in the server prefs");
	}
}