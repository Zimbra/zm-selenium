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

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;




public class RemoveTrustedEmail extends AjaxCommonTest {

	// For RemoveTrustedEmail_01
	public String email1 = "email"+ ZimbraSeleniumProperties.getUniqueString() + "@zimbra.com";
	
	
	
	public RemoveTrustedEmail() throws HarnessException {
		
		super.startingPage = app.zPagePreferences;
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -1977182849340781539L;
			{
				put("zimbraPrefMailTrustedSenderList", email1);
			}
		};
		
	}

	
	@Test(
			description = "Remove a trusted email address",
			groups = { "smoke" }
			)
	public void RemoveTrustedEmail_01() throws HarnessException {

		/* test properties */

		
		/* GUI steps */

		// Navigate to preferences -> mail -> Trusted Addresses
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailTrustedAddresses);
		
		// Select the email address
		String locator = "css=td[id$='_LISTVIEW'] td:contains("+ email1 +")";
		app.zPagePreferences.zClick(locator);
		
		// Click "Remove"
		app.zPagePreferences.zClick("css=td[id$='_REMOVE_BUTTON'] td[id$='_title']");
		
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
			if ( e.getText().contains(email1) ) {
				found = e.getText();
				break;
			}
		}
		ZAssert.assertNull(found, "Verify that the address is no longer included in the server prefs");
		
	}

}
