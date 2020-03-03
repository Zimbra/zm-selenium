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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.calendar;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class SetCalendarWorkingHours extends AjaxCore {

	public SetCalendarWorkingHours() {
		logger.info("New " + SetCalendarWorkingHours.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}


	@Bugs (ids = "77465")
	@Test (description = "Set calendar custom working hours and verify accordingly",
			groups = { "sanity" })

	public void SetCalendarWorkingHours_01() throws HarnessException {

		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);

		// Select custom work hours for e.g. Tuesday to Friday
		app.zPagePreferences.zSelectRadioButton(Button.R_CUSTOM_WORK_HOURS);
		app.zPagePreferences.zPressButton(Button.B_CUSTOMIZE);
		app.zPagePreferences.zCheckboxSet(Button.C_FRIDAY_WORK_HOUR, false);
		app.zPagePreferences.zPressButton(Button.B_OK);

		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		app.zPagePreferences.zPressButton(Button.B_NO);

		// Verify the preference value
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefCalendarWorkingHours'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefCalendarWorkingHours']", null);
		ZAssert.assertEquals(value, "1:N:0800:1700,2:Y:0800:1700,3:Y:0800:1700,4:Y:0800:1700,5:Y:0800:1700,6:N:0800:1700,7:N:0800:1700", "Verify zimbraPrefCalendarWorkingHours value (Sunday, Monday & Saturday as non-working days)'");

		// If logout stucks then assume that browser dialog appeared
		app.zPageMain.zLogout();
	}


	@Bugs (ids = "103862")
	@Test (description = "Verify that modifying 'Work Week:' preference prompts for UI refresh",
			groups = { "functional" })

	public void SetCalendarWorkingHours_02() throws HarnessException {

		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);

		// Set Start week on day to something other than Sunday
		app.zPagePreferences.zCheckboxSet(Button.C_MONDAY_WORK_WEEK, false);

		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.ReloadApplication, app, app.zPagePreferences);
		ZAssert.assertNotNull(dialog, "Dialog is present");
		dialog.zPressButton(Button.B_YES);

		// Verify the preference value
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefCalendarWorkingHours'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefCalendarWorkingHours']", null);
		ZAssert.assertEquals(value, "1:N:0800:1700,2:N:0800:1700,3:Y:0800:1700,4:Y:0800:1700,5:Y:0800:1700,6:N:0800:1700,7:N:0800:1700", "Verify zimbraPrefCalendarWorkingHours value (Sunday, Monday & Saturday as non-working days)'");
		app.zPageMain.zLogout();
	}

	@Bugs (ids = "102406,50479")
	@Test (description = "When setting custom working hours, JS error",
			groups = { "functional" })

	public void SetCalendarWorkingHours_03() throws HarnessException {

		// Navigate to preferences -> calendar
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);

		// Select custom work hours
		app.zPagePreferences.zSelectRadioButton(Button.R_CUSTOM_WORK_HOURS);
		app.zPagePreferences.zPressButton(Button.B_CUSTOMIZE);
		app.zPagePreferences.zSelectCheckBox(Button.B_MONDAY_CHECK_BOX);
		app.zPagePreferences.zPressButton(Button.B_OK);

		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		app.zPagePreferences.zPressButton(Button.B_YES);
		SleepUtil.sleepVeryLong();
		SleepUtil.sleepMedium();

		// Verify the preference value
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefCalendarWorkingHours'/>"
				+		"</GetPrefsRequest>");

		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefCalendarWorkingHours']", null);
		ZAssert.assertEquals(value, "1:N:0800:1700,2:N:0800:1700,3:Y:0800:1700,4:Y:0800:1700,5:Y:0800:1700,6:Y:0800:1700,7:N:0800:1700", "Verify zimbraPrefCalendarWorkingHours value (Sunday & Saturday as non-working days)'");

		// If logout stucks then assume that browser dialog appeared
		app.zPageMain.zLogout();
	}

}