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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.trustedaddresses;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class AddTrustedDomain extends AjaxCommonTest {


	public AddTrustedDomain() throws HarnessException {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;
		
	}

	@Test(
			description = "Add a trusted domain address",
			groups = { "smoke" }
			)
	public void AddTrustedDomain_01() throws HarnessException {

		/* test properties */
		final String domain = "@domain" + ZimbraSeleniumProperties.getUniqueString() + ".com";

	
		/* GUI steps */

		// Navigate to preferences -> mail -> Trusted Addresses
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailTrustedAddresses);
		
		// Add the email address
		app.zPagePreferences.sType("css=td[id$='_EMAIL_ADDRESS'] input", domain);
		
		// Click "Add"
		app.zPagePreferences.zClick("css=td[id$='_ADD_BUTTON'] td[id$='_title']");
		
		// Click "Save"
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		
		// Wait for the ModifyPrefsRequest to complete
		app.zPagePreferences.zWaitForBusyOverlay();
		
		
		/* Test verification */
		
		app.zGetActiveAccount().soapSend(
					"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+		"<pref name='zimbraPrefMailTrustedSenderList'/>"
				+	"</GetPrefsRequest>");

		String found = null;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:pref[@name='zimbraPrefMailTrustedSenderList']");
		for (Element e : nodes) {
			if ( e.getText().contains(domain) ) {
				found = e.getText();
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify that the domain is saved in the server prefs");
		
	}

}
