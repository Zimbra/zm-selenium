/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.attributes;

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.*;

public class ZimbraFeatureMailForwardingEnabled extends AjaxCommonTest {

	public ZimbraFeatureMailForwardingEnabled() {
		logger.info("New "+ ZimbraFeatureMailForwardingEnabled.class.getCanonicalName());

		super.startingPage = app.zPageLogin;
		super.startingAccountPreferences = new HashMap<String , String>() {
			private static final long serialVersionUID = 2672327300661475816L; {
				put("zimbraFeatureMailForwardingEnabled", "FALSE");
			}
		};
	}


	@Bugs (ids = "98850")
	@Test (description = "Verify preferences does not show 'Forward a copy to', if zimbraFeatureMailForwardingEnabled=FALSE",
			groups = { "functional", "L2" })

	public void zimbraFeatureMailForwardingEnabled_01() throws HarnessException {

		// Login
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

		// Go to preferences - mail
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		// Verify the field is not present
		String locator = "css=input[id$='MAIL_FORWARDING_ADDRESS']";
		boolean exists = app.zPagePreferences.sIsElementPresent(locator);
		ZAssert.assertFalse(exists, "Verify the forwarding address field is not present");

		locator = "css=td.ZOptionsField:contains('Forward a copy to:')";
		exists = app.zPagePreferences.sIsElementPresent(locator);
		ZAssert.assertFalse(exists, "Verify the 'Forward a copy to:' label is not present");
	}


	@Bugs (ids = "71403")
	@Test (description = "Bug 71403: Verify duplicate message lables do not exist in preferences (When a message arrives)",
			groups = { "functional", "L2" })

	public void zimbraFeatureMailForwardingEnabled_02() throws HarnessException {

		// Login
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

		// Go to preferences - mail
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);

		// Verify the field is not present
		String locator = "//td[@class='ZOptionsLabel'][contains(text(),'Message Arrival')]";

		boolean exists = app.zPagePreferences.sIsElementPresent(locator);
		ZAssert.assertTrue(exists, "Verify the 'Message Arrival' label exists");

		int count = app.zPagePreferences.sGetXpathCount(locator);
		ZAssert.assertEquals(count, 1, "Verify only 1 'Message Arrival' label exists");
	}
}