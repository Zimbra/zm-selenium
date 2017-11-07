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
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.month.allday;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class SingleDayAppointment extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public SingleDayAppointment() {
		logger.info("New "+ SingleDayAppointment.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {{
		    put("zimbraPrefCalendarInitialView", "month");
		}};
	}


	@Bugs(ids = "69132")
	@Test (description = "View a basic all-day appointment in the month view",
			groups = { "smoke", "L1" })

	public void GetAppointment_01() throws HarnessException {

		// Create an appointment on the server
		String subject = ConfigProperties.getUniqueString();

		AppointmentItem.createAppointmentAllDay(
				app.zGetActiveAccount(),
				Calendar.getInstance(),
				1,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Get the list of appointments in the current view
		List<AppointmentItem> items = app.zPageCalendar.zListGetAppointments();
		ZAssert.assertNotNull(items, "Get the list of appointments");

		// Verify the appointment is in the view
		AppointmentItem found = null;
		for(AppointmentItem item : items) {
			if ( item.getSubject().equals(subject) ) {
				found = item;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the new appointment appears in the view");
	 }
}
