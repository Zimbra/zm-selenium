/*
 	* ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.minicalendar;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.QuickAddAppointment;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class CreateMeeting extends CalendarWorkWeekTest {

	public CreateMeeting() {
		logger.info("New "+ CreateMeeting.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	
	@Bugs(ids = "107050,81945")
	@Test( description = "Create meeting invite from mini-calendar's date using quick add dialog",
			groups = { "smoke" } )
	
	public void CreateMeeting_01() throws HarnessException {
		
		// Create appointment
		AppointmentItem appt = new AppointmentItem();
		Calendar now = this.calendarWeekDayUTC;
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		
		String apptSubject, apptAttendee, apptLocation, apptContent;
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		apptLocation = location.EmailAddress;
		apptContent = ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		appt.setLocation(apptLocation);
	
		// Quick add appointment dialog
		QuickAddAppointment quickAddAppt = new QuickAddAppointment(app) ;
		quickAddAppt.zNewAppointmentUsingMiniCal();
		quickAddAppt.zFill(appt);
		quickAddAppt.zMoreDetails();
		
		// Add attendees and body from main form
		FormApptNew apptForm = new FormApptNew(app);
        apptForm.zFillField(Field.Attendees, apptAttendee);
        apptForm.zFillField(Field.Body, apptContent);
		apptForm.zSubmitWithResources();
		
		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		//ZAssert.assertEquals(actual.getAttendees(), apptAttendee, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getLocation(), apptLocation, "Loction: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), apptContent, "Content: Verify the appointment data");

		// Verify the attendee receives the meeting
		AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertEquals(received.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertEquals(received.getAttendees(), apptAttendee, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getLocation(), apptLocation, "Loction: Verify the appointment data");
		ZAssert.assertEquals(received.getContent(), apptContent, "Content: Verify the appointment data");

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, apptSubject, "Subject: Verify the appointment data");
		
		// Verify location free/busy status shows as ptst=AC	
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify that the location status shows as 'ACCEPTED'");
	}

}