/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.general.login;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ClientTypeAdvanced extends AjaxCore {

	public ClientTypeAdvanced() {
		logger.info("New "+ ClientTypeAdvanced.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -1303088148746653112L; {
				put("zimbraPrefClientType", "advanced");
			}
		};
	}


	@Test (description = "Verify the 'Sign in using' option can be toggled",
			groups = { "functional" })

	public void ClientTypeAdvanced_01() throws HarnessException {
		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);

		String clientType = "standard";
		if (ConfigProperties.isZimbra9XEnvironment()) {
			clientType = "modern";
		}

		String locator = "css=input[id$='_input'][value='" + clientType + "']";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(locator), "Verify the 'Sign in using: advanced' radio is present");

		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.zWaitForBusyOverlay();
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefClientType'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefClientType']", null);
		ZAssert.assertEquals(value, clientType, "Verify the zimbraPrefClientType preference was changed");
	}
}