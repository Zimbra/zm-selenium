/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ZimbraFeatureChangePasswordEnabledTrue extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public ZimbraFeatureChangePasswordEnabledTrue() {
		logger.info("New "+ ZimbraFeatureChangePasswordEnabledTrue.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraFeatureChangePasswordEnabled", "TRUE");
		}};
	}


	@Bugs(ids="63439")
	@Test(description = "Verify the 'Change Password' option is present in preferences",
			groups = { "functional", "L2" })

	public void ZimbraFeatureChangePasswordEnabledTrue_01() throws HarnessException {

		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);

		String labelLocator = "//div[@id='CHANGE_PASSWORD']/../../td[@class='ZOptionsLabel']";
		String fieldLocator = "//div[@id='CHANGE_PASSWORD']";

		ZAssert.assertTrue(app.zTreePreferences.sIsElementPresent(labelLocator), "Verify the 'change password' label is present");
		ZAssert.assertTrue(app.zTreePreferences.sIsElementPresent(fieldLocator), "Verify the 'change password' field is present");
	}
}