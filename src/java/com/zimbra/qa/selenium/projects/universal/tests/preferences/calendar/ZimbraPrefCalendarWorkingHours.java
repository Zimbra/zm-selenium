/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class ZimbraPrefCalendarWorkingHours extends UniversalCommonTest {

	public ZimbraPrefCalendarWorkingHours() {
		logger.info("New " + ZimbraPrefCalendarWorkingHours.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}

	// Need to skip this test completely till bug 77465 get fixed otherwise automation may stuck at browser navigate away dialog 
	@Bugs(ids = "77465")
	@Test(
			description = "Set calendar custom working hours and verify accordingly", 
			groups = { "functional", "L2" })
	
	public void ZimbraPrefCalendarWorkingHours_01() throws HarnessException {

		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);
		SleepUtil.sleepMedium();

		// Select custom work hours for e.g. Tuesday to Friday
		app.zPagePreferences.zSelectRadioButton(Button.R_CUSTOM_WORK_HOURS);
		app.zPagePreferences.zPressButton(Button.B_CUSTOMIZE);
		app.zPagePreferences.zCheckboxSet(Button.C_FRIDAY_WORK_HOUR, false);
		app.zPagePreferences.zPressButton(Button.B_OK);
		
		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		//DialogWarning dlgWarning = (DialogWarning) new DialogWarning(null, app, null).zClickButton(Button.B_NO);
		app.zPagePreferences.zPressButton(Button.B_NO);
 
		// Verify the preference value
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefCalendarWorkingHours'/>"
				+		"</GetPrefsRequest>");
		
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefCalendarWorkingHours']", null);
		ZAssert.assertEquals(value, "1:N:0800:1700,2:Y:0800:1700,3:Y:0800:1700,4:Y:0800:1700,5:Y:0800:1700,6:N:0800:1700,7:N:0800:1700", "Verify zimbraPrefCalendarWorkingHours value (Sunday, Monday & Saturday as non-working days)'");
		
		// if logout stucks then assume that browser dialog appeared
		app.zPageMain.zLogout();
	}

	@Bugs(ids = "103862")
	@Test( description = "Verify that modifying 'Work Week:' preference prompts for UI refresh", 
			groups = { "functional", "L2" })
	
	public void ZimbraPrefCalendarWorkingHours_02() throws HarnessException {
		
		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);
		SleepUtil.sleepMedium();

		// Set Start week on day to something other than Sunday
		app.zPagePreferences.zCheckboxSet(Button.C_MONDAY_WORK_WEEK, false);
		
		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.ReloadApplication, app, app.zPagePreferences);
		ZAssert.assertNotNull(dialog, "Dialog is present");
		dialog.zClickButton(Button.B_YES);
		// Verify the preference value
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefCalendarWorkingHours'/>"
				+		"</GetPrefsRequest>");
		
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefCalendarWorkingHours']", null);
		ZAssert.assertEquals(value, "1:N:0800:1700,2:N:0800:1700,3:Y:0800:1700,4:Y:0800:1700,5:Y:0800:1700,6:N:0800:1700,7:N:0800:1700", "Verify zimbraPrefCalendarWorkingHours value (Sunday, Monday & Saturday as non-working days)'");
		app.zPageMain.zLogout();
	}
	
}