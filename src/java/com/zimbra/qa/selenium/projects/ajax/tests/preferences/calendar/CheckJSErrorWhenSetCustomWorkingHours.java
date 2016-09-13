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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class CheckJSErrorWhenSetCustomWorkingHours extends AjaxCommonTest {

	public CheckJSErrorWhenSetCustomWorkingHours() {
		logger.info("New " + CheckJSErrorWhenSetCustomWorkingHours.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}
	
	@Bugs(ids = "102406,50479")
	@Test( description = "When setting custom working hours, JS error", groups = { "functional" })
	
	public void CheckJSErrorWhenSetCustomWorkingHours_01() throws HarnessException {

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
		
		// if logout stucks then assume that browser dialog appeared
		app.zPageMain.zLogout();
	}
}