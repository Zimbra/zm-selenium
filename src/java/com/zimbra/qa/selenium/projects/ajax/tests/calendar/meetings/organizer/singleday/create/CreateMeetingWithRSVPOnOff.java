/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class CreateMeetingWithRSVPOnOff extends AjaxCore {

	public CreateMeetingWithRSVPOnOff() {
		logger.info("New "+ CreateMeetingWithRSVPOnOff.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	}


	@Bugs (ids = "101999")
	@Test (description = "Verify organizer does not recieve email notification when attendee responds to the meeting invite while 'Request Response' remains OFF",
			groups = { "smoke", "L1" })

	public void CreateMeetingWithRSVPOff_01() throws HarnessException {

		// Create appointment data
		ZimbraAccount apptAttendee1,organizer;
		String apptAttendee1EmailAddress;
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();

		organizer = app.zGetActiveAccount();
		apptAttendee1 = ZimbraAccount.AccountA();
		apptAttendee1EmailAddress = ZimbraAccount.AccountA().EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		AppointmentItem appt = new AppointmentItem();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);

		// Create appointment data
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1EmailAddress);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		appt.setContent(apptContent);

		// Create meeting with Request Response OFF
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zRequestResponseOFF();
		apptForm.zSubmit();

		// Logout from organizer and Login as attendee
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(apptAttendee1);

		// Accept the invite from attendee
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		display.zPressButton(Button.B_ACCEPT);

		// Search for the appointment (InvId)
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the attendee appointment details

		app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");
		String attendeeStatus = apptAttendee1.soapSelectValue("//mail:at[@a='"+ apptAttendee1EmailAddress +"']", "ptst");

		// Verify attendee status shows as ACCEPTED
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");

		// Organizer: Search for the appointment response
		String inboxId = FolderItem.importFromSOAP(organizer, FolderItem.SystemFolder.Inbox).getId();
		organizer.soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = organizer.soapSelectValue("//mail:m", "id");

		// Verify organizer does not receive email notification because request response was set OFF while creating meeting invite
		ZAssert.assertNull(messageId, "Verify organizer does not recieve email notification because request response was set OFF while creating meeting invite");
	}


	@Bugs (ids = "101999")
	@Test (description = "Verify organizer receives email notification when attendee responds to the meeting invite while 'Request Response' remains ON",
			groups = { "functional", "L2" })

	public void CreateMeetingWithRSVPOn_02() throws HarnessException {

		// Create appointment data
		ZimbraAccount apptAttendee1,organizer;
		String apptAttendee1EmailAddress;
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();

		organizer = app.zGetActiveAccount();
		apptAttendee1 = ZimbraAccount.AccountA();
		apptAttendee1EmailAddress = apptAttendee1.EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		AppointmentItem appt = new AppointmentItem();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		apptSubject = ConfigProperties.getUniqueString();

		// Create appointment data
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1EmailAddress);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		appt.setContent(apptContent);

		// Create meeting with Request Response OFF and then ON
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zRequestResponseON(); // toggle request response option to enable it
		apptForm.zSubmit();

		// Logout from organizer and Login as attendee
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(apptAttendee1);

		// Accept the invite
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		display.zPressButton(Button.B_ACCEPT);

		// Search for the appointment (InvId)
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String invId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the attendee appointment details
		app.zGetActiveAccount().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ invId +"'/>");
		String attendeeStatus = apptAttendee1.soapSelectValue("//mail:at[@a='"+ apptAttendee1EmailAddress +"']", "ptst");

		// Verify attendee status is shows as ACCEPTED
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");

		// Organizer: Search for the appointment response
		String inboxId = FolderItem.importFromSOAP(organizer, FolderItem.SystemFolder.Inbox).getId();
		organizer.soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = organizer.soapSelectValue("//mail:m", "id");

		// Verify organizer receives email notification when attendee responds to the meeting invite while 'Request Response' remains ON
		ZAssert.assertNotNull(messageId, "Verify organizer receives email notification when attendee responds to the meeting invite while 'Request Response' remains ON");

	}

}
