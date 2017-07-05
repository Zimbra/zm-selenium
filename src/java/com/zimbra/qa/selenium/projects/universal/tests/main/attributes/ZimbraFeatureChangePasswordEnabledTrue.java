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
package com.zimbra.qa.selenium.projects.universal.tests.main.attributes;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;

public class ZimbraFeatureChangePasswordEnabledTrue extends AjaxCommonTest {

	public ZimbraFeatureChangePasswordEnabledTrue() {
		logger.info("New " + ZimbraFeatureChangePasswordEnabledTrue.class.getCanonicalName());

		super.startingPage = app.zPageMail;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 6992909181069185672L;
			{

				put("zimbraFeatureChangePasswordEnabled", "TRUE");

			}
		};

	}

	@Bugs(ids = "81522")
	@Test(description = "Verify the 'Change Password' option is present in main -> account -> Change Password", 
		groups = { "functional", "L2"})
	
	public void ZimbraFeatureChangePasswordEnabledFalse_01() throws HarnessException {

		// Click the account pulldown to see the menu
		String locator = "css=td[id='skin_dropMenu'] td[id$='_dropdown']";

		app.zPageMain.zClickAt(locator, "");

		// Look for the menu
		locator = "css=div[id^='POPUP'] div#documentation td[id$='_title']";
		boolean present = app.zPageMain.sIsElementPresent(locator);
		ZAssert.assertTrue(present, "Verify the pulldown is present");

		locator = "css=div[id^='POPUP'] div#changePassword td[id$='_title']";
		present = app.zPageMain.sIsElementPresent(locator);
		ZAssert.assertTrue(present, "Verify the change password option is not present");
	}
}