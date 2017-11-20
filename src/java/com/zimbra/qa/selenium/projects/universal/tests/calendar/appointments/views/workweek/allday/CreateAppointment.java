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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.appointments.views.workweek.allday;

import java.util.Calendar;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew;

public class CreateAppointment extends CalendarWorkWeekPreference {
	
	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}

	@Bugs (ids = "69132,95899,95900")
	@Test (description = "Create simple all day appointment",
			groups = { "smoke", "L0" }
	)
	public void CreateAllDayAppointment_01() throws HarnessException {
		   
		// Create appointment
		String apptSubject;
		apptSubject = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		
		appt.setSubject(apptSubject);
		appt.setStartTime(new ZDate(this.calendarWeekDayUTC));
		appt.setEndTime(new ZDate(this.calendarWeekDayUTC));
		appt.setContent("content" + ConfigProperties.getUniqueString());
		appt.setAttendees(ZimbraAccount.AccountA().EmailAddress);
		appt.setIsAllDay(true);
	
		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");
		
		// Fill the data and submit it
		apptForm.zFill(appt);
		apptForm.zSubmit();
			
		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(app.zGetActiveAccount().soapMatch("//mail:GetAppointmentResponse//mail:comp", "allDay", "1"), true, "");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

	}

	@Bugs (ids = "104312")
	@Test (description = "Create multi-day all day appointment in week view",
			groups = { "smoke", "L1" }
	)
	public void CreateAllDayAppointment_02() throws HarnessException {
		
		// Create appointment
		String apptSubject;
		apptSubject = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		
		appt.setSubject(apptSubject);
		appt.setContent("content" + ConfigProperties.getUniqueString());
		appt.setAttendees(ZimbraAccount.AccountA().EmailAddress);
		appt.setIsAllDay(true);
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 15, 0, 0, 0);
		appt.setStartTime(startUTC);;
		appt.setEndTime(endUTC);
		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();
			
		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(app.zGetActiveAccount().soapMatch("//mail:GetAppointmentResponse//mail:comp", "allDay", "1"), true, "");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
        
	}

	@Test (description = "Create simple all day appointment which occurs in past",
			groups = { "smoke", "L0" }
	)
	public void CreateAllDayAppointment_03() throws HarnessException {
		   
		// Create appointment
		String apptSubject;
		apptSubject = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();
		Calendar now = this.calendarWeekDayUTC;
		
		appt.setSubject(apptSubject);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) - 4, 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) - 4, 14, 0, 0));
		appt.setContent("content" + ConfigProperties.getUniqueString());
		appt.setAttendees(ZimbraAccount.AccountA().EmailAddress);
		appt.setIsAllDay(true);
	
		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");
		
		// Fill the data and submit it
		apptForm.zFill(appt);
        ZAssert.assertTrue(apptForm.zVerifyMeetingInPastWarning(), "Verify meeting in past warning appears");
		apptForm.zSubmit();
			
		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(app.zGetActiveAccount().soapMatch("//mail:GetAppointmentResponse//mail:comp", "allDay", "1"), true, "");
		
	}

}
