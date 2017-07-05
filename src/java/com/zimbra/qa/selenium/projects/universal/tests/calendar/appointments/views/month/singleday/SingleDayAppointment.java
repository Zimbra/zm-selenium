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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.appointments.views.month.singleday;

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
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;

public class SingleDayAppointment extends AjaxCommonTest {

	@SuppressWarnings("serial")
	public SingleDayAppointment() {
		logger.info("New "+ SingleDayAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		// Make sure we are using an account with month view
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefCalendarInitialView", "month");
		}};

	}
	@Bugs(ids = "69132")
	@Test( description = "Verify the display of a basic appointment in the month view",
			groups = { "sanity", "L1" })
	
	public void SingleDayAppointment_01() throws HarnessException {

		// Appointment data
		String subject = "Appointment"+ ConfigProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1, 10, 0, 0);

		//Create an appointment of duration 120 mins on next day
		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				startDate,
				120,
				null,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);


		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		//Verify that the appointments is displayed correctly in month view 
		boolean displayed = app.zPageCalendar.zVerifyAppointmentInMonthView(startDate,subject);
		ZAssert.assertTrue(displayed, "The appointment is not created and displayed correctlly in month view");

	}


}
