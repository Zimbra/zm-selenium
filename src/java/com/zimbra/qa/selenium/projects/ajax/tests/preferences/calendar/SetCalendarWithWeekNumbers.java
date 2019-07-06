/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.calendar;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class SetCalendarWithWeekNumbers extends AjaxCore {

	public SetCalendarWithWeekNumbers() {
		logger.info("New " + SetCalendarWithWeekNumbers.class.getCanonicalName());

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 3028486541122343959L; {
				put("zimbraPrefCalendarInitialView", "month");
			}
		};
	}


	@Test (description = "Set 'Show Calendars with week numbers' and verify accordingly",
			groups = { "sanity" })

	public void SetCalendarWithWeekNumbers_01() throws HarnessException {

		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);

		// Select custom work hours for e.g. Tuesday to Friday
		app.zPagePreferences.zCheckboxSet(Button.C_SHOW_CALENDAR_WEEK_NUMBERS, true);

		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Go to Calendar
		app.zPageCalendar.zNavigateTo();
		ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=td[class='calendar_month_weekno_td']"),"Verify that week number column appears");

		// Verify the preference value
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefShowCalendarWeek'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefShowCalendarWeek']", null);
		ZAssert.assertEquals(value, "TRUE", "Verify zimbraPrefShowCalendarWeek value'");

		// If logout stucks then assume that browser dialog appeared
		app.zPageMain.zLogout();
	}
}