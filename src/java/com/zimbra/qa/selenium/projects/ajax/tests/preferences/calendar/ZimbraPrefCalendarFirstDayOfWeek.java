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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.calendar;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ZimbraPrefCalendarFirstDayOfWeek extends AjaxCommonTest {

	public ZimbraPrefCalendarFirstDayOfWeek() {
		logger.info("New " + ZimbraPrefCalendarFirstDayOfWeek.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}

	
	@Bugs(ids = "101729,103862")
	@Test(description = "Set First day of the week and verify that first day is set correctly", 
			groups = { "functional", "L2" })
	
	public void ZimbraPrefCalendarFirstDayOfWeek_01() throws HarnessException {
		
		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);

		// Set Start week on day to something other than Sunday
		app.zPagePreferences.zToolbarPressPulldown(Button.O_START_WEEK_ON, Button.O_START_WEEK_ON_TUESDAY);
		
		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();
		
		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);

		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.ReloadApplication, app, app.zPagePreferences);
		ZAssert.assertNotNull(dialog, "Dialog is present");
		dialog.zClickButton(Button.B_YES);
		
		app.zPageCalendar.zNavigateTo();
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(1), "Tue", "First day matched");
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(2), "Wed", "Second day matched");
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(3), "Thu", "Third day matched");
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(4), "Fri", "Fourth day matched");
		if (app.zPageCalendar.zIsWeekend()) {
			ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(5), "Sat", "Fifth day matched");
			ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(6), "Sun", "Sixth day matched");
			ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(7), "Mon", "Seventh day matched");
        } else {
			ZAssert.assertStringContains(app.zPageCalendar.zReturnDayWeek(5), "Mon", "Fifth day matched");
        }
	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		zFreshLogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}
}