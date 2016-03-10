/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class zimbraPrefCalendarFirstDayOfWeek extends AjaxCommonTest {

	public zimbraPrefCalendarFirstDayOfWeek() {
		logger.info("New " + zimbraPrefCalendarFirstDayOfWeek.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}

	// Need to skip this test completely till bug 77465 get fixed otherwise automation may stuck at browser navigate away dialog 
	@Bugs(ids = "101729,103862")
	@Test(
			description = "Set First day of the week and verify that first day is set correctly", 
			groups = { "functional" })
	
	public void zimbraPrefCalendarFirstDayOfWeek_01() throws HarnessException {
		
		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);
		SleepUtil.sleepMedium();

		// Set Start week on day to something other than Sunday
		app.zPagePreferences.zToolbarPressPulldown(Button.O_START_WEEK_ON, Button.O_START_WEEK_ON_TUESDAY);
		
		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.ReloadApplication, app, app.zPagePreferences);
		ZAssert.assertNotNull(dialog, "Dialog is present");
		dialog.zClickButton(Button.B_YES);

		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();
		
		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);
		SleepUtil.sleepMedium();

		// Change the default appointment duration
		app.zPagePreferences.zToolbarPressPulldown(Button.O_DEFAULT_APPOINTMENT_DURATION, Button.O_APPOINTMENT_DURATION_90);
		
		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		app.zPageCalendar.zNavigateTo();
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayOfWorkWeek(1), "Tue", "First day matched");
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayOfWorkWeek(2), "Wed", "Second day matched");
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayOfWorkWeek(3), "Thu", "Third day matched");
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayOfWorkWeek(4), "Fri", "Fourth day matched");
		ZAssert.assertStringContains(app.zPageCalendar.zReturnDayOfWorkWeek(6), "Mon", "Fifth day matched");
	}
	
	@AfterMethod(groups={"always"})
	public void afterMethod() throws HarnessException {
		zKillBrowserAndRelogin();
		logger.info(app.zGetActiveAccount().EmailAddress);
	}
	
	
}