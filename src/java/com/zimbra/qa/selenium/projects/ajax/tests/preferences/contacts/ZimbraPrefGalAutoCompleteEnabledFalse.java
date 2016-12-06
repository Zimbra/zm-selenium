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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.contacts;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ZimbraPrefGalAutoCompleteEnabledFalse extends AjaxCommonTest {

	public ZimbraPrefGalAutoCompleteEnabledFalse() {
		
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 9163639565562154710L;
			{				
				put("zimbraPrefGalAutoCompleteEnabled", "TRUE");
			}
		};
		
		
	}

	@Test(
			description = "Set zimbraPrefGalAutoCompleteEnabled to 'FALSE'",
			groups = { "functional", "L2" }
	)
	public void zimbraPrefGalAutoCompleteEnabledFalse_01() throws HarnessException {

		//-- DATA Setup
		
		
		
		//-- GUI Actions
		
		// Navigate to preferences -> mail -> composing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.AddressBook);

		
		// Click radio button for "	Initially search the Global Address List when using the contact picker"
		app.zPagePreferences.sClick("css=div[id$='GAL_AUTOCOMPLETE_control'] input[id$='GAL_AUTOCOMPLETE']");

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		
		
		//-- VERIFICATION
		
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefGalAutoCompleteEnabled'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefGalAutoCompleteEnabled']", null);
		ZAssert.assertEquals(value, "FALSE", "Verify the zimbraPrefGalAutoCompleteEnabled preference was changed to 'FALSE'");

	}
	
}
