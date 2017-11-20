/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.calendar;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.TreeItem;


public class ZimbraPrefCalendarAlwaysShowMiniCalTRUE extends UniversalCore {

	@SuppressWarnings("serial")
	public ZimbraPrefCalendarAlwaysShowMiniCalTRUE() {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefCalendarAlwaysShowMiniCal", "FALSE");
			}
		};
	}

	@Bugs (ids = "78547")
	@Test(
			description = "Set zimbraPrefCalendarAlwaysShowMiniCal to 'TRUE'",
			groups = { "functional", "L3" }
			)
	public void ZimbraPrefCalendarAlwaysShowMiniCalFALSE_01() throws HarnessException {
		
		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);

		
		// Click checkbox for zimbraPrefCalendarAlwaysShowMiniCal
		logger.info("Click checkbox for zimbraPrefCalendarAlwaysShowMiniCal");
		app.zPagePreferences.zCheckboxSet("css=input[id$='_CAL_ALWAYS_SHOW_MINI_CAL']", true);
		
		// Not sure why, but sleep is required here
		SleepUtil.sleepLong();

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);		
		

		// Verify the preference is set to false
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefCalendarAlwaysShowMiniCal'/>"
				+		"</GetPrefsRequest>");
		
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefCalendarAlwaysShowMiniCal']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify zimbraPrefCalendarAlwaysShowMiniCal was changed to 'TRUE'");
		
	}
}
