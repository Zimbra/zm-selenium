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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.quickadd;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.QuickAddAppointment;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.QuickAddAppointment.Field;

public class CreateAppointment extends AjaxCore {

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "107050")
	@Test (description = "Create basic appointment using quick add dialog",
			groups = { "smoke", "L0" } )

	public void CreateAppointment_01() throws HarnessException {

		// Create appointment
		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();
		appt.setSubject(ConfigProperties.getUniqueString());
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0));

		// Quick add appointment dialog
		QuickAddAppointment quickAddAppt = new QuickAddAppointment(app) ;
		quickAddAppt.zNewAppointment();
		quickAddAppt.zFill(appt);
		quickAddAppt.zSubmit();

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
	}


	@Test (description = "Create basic appointment using quick add dialog in the past and verify the warning message",
			groups = { "smoke", "L1" } )

	public void CreateAppointment_02() throws HarnessException {

		// Appointment Data
		Calendar now = Calendar.getInstance();
		String apptSubject = ConfigProperties.getUniqueString();

		//if it is last day of month than set appointment for same day
		if (now.get(Calendar.DAY_OF_MONTH) == now.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 1);
		}

		// Start and end day and hour of the appointment
		String startDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH) - 1);
		String endDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH) - 1);
		String startDayOfHour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));

		String meridiem = null;
		if (now.get(Calendar.AM_PM) == 0) {
			meridiem = "AM";
		} else {
			meridiem = "PM";
		}

		// Quick add appointment dialog
		QuickAddAppointment quickAddAppt = new QuickAddAppointment(app) ;
		quickAddAppt.zNewAppointment();

		// Enter the appointment data
		quickAddAppt.zFillField(Field.Subject, apptSubject);
		quickAddAppt.zSelectStartDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH) - 1));
		quickAddAppt.zSelectEndDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH) - 1));
		quickAddAppt.zSelectTimeFromDropdown(Field.StartTime, String.valueOf(now.get(Calendar.HOUR)), "00", meridiem);
		quickAddAppt.zSelectTimeFromDropdown(Field.EndTime, String.valueOf(now.get(Calendar.HOUR)), "30", meridiem);

		// Verify the past time warning message 
		ZAssert.assertTrue(quickAddAppt.zVerifyMeetingInPastWarning(), "Verify meeting in past warning appears");

		// Submit
		quickAddAppt.zSubmit();

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertTrue(actual.getStartTime().toString().contains(startDay) && actual.getStartTime().toString().contains(startDayOfHour), "Start time: Verify the appointment start day and hour");
		ZAssert.assertTrue(actual.getEndTime().toString().contains(endDay) && actual.getEndTime().toString().contains("30"),"End time: Verify the appointment end day and hour");
	}


	@Test (description = "Create appointment using quick add dialog and add date from date picker",
			groups = { "sanity", "L1" } )

	public void CreateAppointment_03() throws HarnessException {

		// Create appointment
		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();
		String apptSubject = ConfigProperties.getUniqueString();

		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0));
		appt.setSubject(apptSubject);

		String startDate;
		if (now.get(Calendar.DAY_OF_MONTH) < 26) {
			startDate = String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 2);
		} else {
			startDate = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		}

		// Quick add appointment dialog
		QuickAddAppointment quickAddAppt = new QuickAddAppointment(app) ;
		quickAddAppt.zNewAppointment();
		quickAddAppt.zFillField(Field.Subject, apptSubject);
		quickAddAppt.zSelectStartDateFromDatePicker(startDate);
		quickAddAppt.zSelectEndDateFromDatePicker(startDate);
		quickAddAppt.zFillField(Field.StartTime, "13:00");
		quickAddAppt.zFillField(Field.EndTime, "14:00");
		quickAddAppt.zSubmit();

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getGStartDate(), appt.getGStartDate(), "Start date: Verify the appointment data");
		ZAssert.assertEquals(actual.getGEndDate(), appt.getGEndDate(), "End date: Verify the appointment data");
	}


	@Test (description = "Create basic appointment using quick add dialog and add date and time using date/time picker",
			groups = { "smoke", "L0", "non-msedge" } )

	public void CreateAppointment_04() throws HarnessException {

		// Appointment Data
		Calendar now = Calendar.getInstance();
		String apptSubject = ConfigProperties.getUniqueString();

		//if it is last day of month than set appointment for same day
		if (now.get(Calendar.DAY_OF_MONTH) == now.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 1);
		}

		// Start and end day and hour of the appointment
		String startDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 1);
		String endDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 1);
		String startDayOfHour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));

		String meridiem = null;
		if (now.get(Calendar.AM_PM) == 0) {
			meridiem = "AM";
		} else {
			meridiem = "PM";
		}

		// Quick add appointment dialog
		QuickAddAppointment quickAddAppt = new QuickAddAppointment(app) ;
		quickAddAppt.zNewAppointment();
		quickAddAppt.zFillField(Field.Subject, apptSubject);
		quickAddAppt.zSelectStartDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 1));
		quickAddAppt.zSelectEndDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 1));
		quickAddAppt.zSelectTimeFromDropdown(Field.StartTime, String.valueOf(now.get(Calendar.HOUR)), "00", meridiem);
		quickAddAppt.zSelectTimeFromDropdown(Field.EndTime, String.valueOf(now.get(Calendar.HOUR)), "30", meridiem);
		quickAddAppt.zSubmit();

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertTrue(actual.getStartTime().toString().contains(startDay) && actual.getStartTime().toString().contains(startDayOfHour), "Start time: Verify the appointment start day and hour");
		ZAssert.assertTrue(actual.getEndTime().toString().contains(endDay) && actual.getEndTime().toString().contains("30"),"End time: Verify the appointment end day and hour");
	}
}