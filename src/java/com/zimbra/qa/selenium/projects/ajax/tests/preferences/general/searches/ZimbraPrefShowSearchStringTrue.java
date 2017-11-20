/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.general.searches;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ZimbraPrefShowSearchStringTrue extends AjaxCore {

	public ZimbraPrefShowSearchStringTrue() {
		logger.info("New "+ ZimbraPrefShowSearchStringTrue.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 3912190366691055120L; {
				put("zimbraPrefShowSearchString", "FALSE");
			}
		};
	}


	@Test (description = "Change zimbraPrefShowSearchString setting TRUE",
			groups = { "functional", "L2" })

	public void PreferencesGeneralSearches_zimbraPrefShowSearchString_01() throws HarnessException {

		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);

		// Check the box
		String locator = "css=input[id$=_SHOW_SEARCH_STRING]";
		app.zPagePreferences.zCheckboxSet(locator, true);
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Verify the account preference has been modified
		app.zGetActiveAccount().soapSend(
					"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+		"<pref name='zimbraPrefShowSearchString'/>"
				+	"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefShowSearchString']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify the zimbraPrefShowSearchString preference was changed to 'TRUE'");
	}
}