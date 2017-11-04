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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.month.multiday;

import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class NonAllDayMultiDayAppointment extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public NonAllDayMultiDayAppointment() {
		logger.info("New "+ NonAllDayMultiDayAppointment.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefCalendarInitialView", "month");
		}};
	}


	@Bugs(ids = "69132")
	@Test( description = "Verify the display of a non-all-day-multiday appointment in the month view",
			groups = { "functional", "L2" })

	public void NonAllDayMultiDayAppointment_01() throws HarnessException {

		// Appointment data
		String subject = "Appointment"+ ConfigProperties.getUniqueString();
		int noOfDays = 5;

		// Start Date is 2 days ahead if current date is less than or equal to 21 else 8 days behind
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.DAY_OF_MONTH) <= 21) {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 2 );
		} else {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 8 );
		}

		ZDate startDate = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		//Create a non-all-day-multiday appointment of duration 5 days
		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				startDate,
				(noOfDays * 24 * 60),    //number of days in minutes.
				null,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		//Verify that the appointments is displayed correctly in month view
		boolean displayed = app.zPageCalendar.zVerifyNonAllDayMultiDayAppointmentInMonthView(startDate,noOfDays, subject);
		ZAssert.assertTrue(displayed, "The appointment is not created and displayed correctlly in month view");
	}
}