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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.singleday.create;

import java.util.Calendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew;

public class CreateMeeting extends CalendarWorkWeekTest {

	public CreateMeeting() {
		logger.info("New "+ CreateMeeting.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test(description = "Create a basic meeting with attendee and location",
			groups = { "sanity" })
			
	public void CreateMeeting_01() throws HarnessException {
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		
		String apptSubject, apptLocation, apptAttendee1, apptContent;
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptLocation = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 01, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 02, 0, 0);
				
		appt.setSubject(apptSubject);
		appt.setLocation(apptLocation);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		appt.setContent(apptContent);
		
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();
		
		ZAssert.assertEquals(zVerifyToastMessage("Invitation Sent"), true, "Verify toast message after creating invite");
		
		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getLocation(), appt.getLocation(), "Location: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
		
		// Verify appointment exists in UI
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), true, "Verify appointment is visible in UI");

		// Verify the attendee receives the meeting
		AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");
		ZAssert.assertEquals(received.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(received.getLocation(), appt.getLocation(), "Location: Verify the appointment data");
		ZAssert.assertEquals(received.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(received.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");

	}
	
	@Test(description = "Create invite with multiple attendees and location",
			groups = { "sanity" })
			
	public void CreateMeeting_02() throws HarnessException {
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 01, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 02, 0, 0);
		
		String apptSubject, apptLocation, apptAttendee1, apptAttendee2, apptContent;
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptLocation = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptAttendee2 = ZimbraAccount.AccountB().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		appt.setLocation(apptLocation);
		appt.setAttendees(apptAttendee1);
		appt.setContent(apptContent);
		
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zAddAttendees(apptAttendee2);
		apptForm.zSubmit();
		
		ZAssert.assertEquals(zVerifyToastMessage("Invitations Sent"), true, "Verify toast message after creating invite");
		
		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getLocation(), appt.getLocation(), "Location: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee1 + "," + apptAttendee2, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
		
		// Verify appointment exists in UI
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), true, "Verify appointment is visible in UI");

		// Verify the attendee1 receives the meeting
		AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");
		ZAssert.assertEquals(received.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(received.getLocation(), appt.getLocation(), "Location: Verify the appointment data");
		ZAssert.assertEquals(received.getAttendees(), apptAttendee1 + "," + apptAttendee2, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(received.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee1 receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");
		
		// Verify the attendee2 receives the meeting
		received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");
		ZAssert.assertEquals(received.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(received.getLocation(), appt.getLocation(), "Location: Verify the appointment data");
		ZAssert.assertEquals(received.getAttendees(), apptAttendee1 + "," + apptAttendee2, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(received.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee2 receives the invitation
		invite = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");
		
	}

}
