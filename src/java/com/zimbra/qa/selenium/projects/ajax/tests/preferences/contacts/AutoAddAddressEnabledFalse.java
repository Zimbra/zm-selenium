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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class AutoAddAddressEnabledFalse extends AjaxCore {

	public AutoAddAddressEnabledFalse() {

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 1275472695659221683L; {
		 		put("zimbraPrefAutoAddAddressEnabled", "TRUE");
			}
		};
	}


	@Test (description= "Select the checkbox to set zimbraPrefAutoAddAddressEnabled=false ",
			groups = { "functional" })

	public void AutoAddAddressEnabledFalse_01() throws HarnessException {

		// Navigate to preferences -> addressbook
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.AddressBook);

		// Uncheck the box
		app.zPagePreferences.zCheckboxSet("css=input[id$=_AUTO_ADD_ADDRESS]",false);

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Verification
		app.zGetActiveAccount().soapSend(
                   "<GetPrefsRequest xmlns='urn:zimbraAccount'>"
                 +     "<pref name='zimbraPrefAutoAddAddressEnabled'/>"
                 + "</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefAutoAddAddressEnabled']", null);
		ZAssert.assertEquals(value, "FALSE", "Verify the zimbraPrefAutoAddAddressEnabled preference was changed to 'FALSE'");
	}
}