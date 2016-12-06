package com.zimbra.qa.selenium.projects.ajax.tests.preferences;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraURI;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class AppPreferences extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public AppPreferences() {
		logger.info("New " + AppPreferences.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageMail;

		// Add a preference so the account is 'dirty' after changing password
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefGroupMailBy", "message");
			}
		};

	}

	@Test( description = "?app=options in url", groups = { "smoke", "L0"  })
	public void AppPreferences_01() throws HarnessException {

		// Reload the application, with app=options query parameter
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
		uri.addQuery("app", "options");
		app.zPageMail.sOpen(uri.toString());

		SleepUtil.sleepLong();

		// Go to "General"
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);
		
		//Verify Change Pwd button

		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zChangePwdButton), "Change password button does not exist");
		

	}

}
