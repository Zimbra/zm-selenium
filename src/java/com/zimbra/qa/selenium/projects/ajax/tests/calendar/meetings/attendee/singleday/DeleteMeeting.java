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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmDeleteAttendee;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmationDeclineAppointment;

@SuppressWarnings("unused")
public class DeleteMeeting extends AjaxCommonTest {

	public DeleteMeeting() {
		logger.info("New "+ DeleteMeeting.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Delete appointment in the week view - Don't Notify Organizer",
			groups = { "smoke", "L1" })

	public void DeleteMeeting_01() throws HarnessException {

		// Create the appointment on the server
		String apptSubject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		// Create an appointment
		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Click to appointment and delete it
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        DialogConfirmDeleteAttendee dlgConfirm = (DialogConfirmDeleteAttendee)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);

        // Select "Don't notify organizer"
 		DialogConfirmationDeclineAppointment declineAppt = (DialogConfirmationDeclineAppointment) new DialogConfirmationDeclineAppointment(app, app.zPageCalendar);
 		declineAppt.zPressButton(Button.B_DONT_NOTIFY_ORGANIZER);
 		declineAppt.zPressButton(Button.B_YES);


 		// ---------------- Verification at organizer & invitee side both -------------------------------------

 		// Check that the organizer shows the attendee as "DECLINE" ---

 		// Organizer: Search for the appointment (InvId)
 		ZimbraAccount.AccountA().soapSend(
 					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
 				+		"<query>"+ apptSubject +"</query>"
 				+	"</SearchRequest>");

 		String organizerInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

 		// Get the appointment details
 		ZimbraAccount.AccountA().soapSend(
 					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

 		String attendeeStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

 		// Verify attendee status shows as ptst=NE (because "Don't notify organizer)
 		ZAssert.assertEquals(attendeeStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION' instead of 'DECLINED'");


 		// Attendee: Search for the appointment (InvId)
 		app.zGetActiveAccount().soapSend(
 					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
 				+		"<query>"+ apptSubject +"</query>"
 				+	"</SearchRequest>");

 		String attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

 		// Verify attendee status shows as ptst=DE
 		ZAssert.assertNull(attendeeInvId, "Verify appointment is not found");

 		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is deleted");
	}


	@Bugs (ids = "104604")
	@Test (description = "Delete appointment in the week view - Notify Organizer",
			groups = { "smoke", "L1" })

	public void DeleteMeeting_02() throws HarnessException {

		// Create the appointment on the server
		String apptSubject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		// Create an appointment
		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		// Click to appointment and delete it
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
		DialogConfirmDeleteAttendee dlgConfirm = (DialogConfirmDeleteAttendee)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);

        // Select "Don't notify organizer"
 		DialogConfirmationDeclineAppointment declineAppt = (DialogConfirmationDeclineAppointment) new DialogConfirmationDeclineAppointment(app, app.zPageCalendar);
 		declineAppt.zPressButton(Button.B_NOTIFY_ORGANIZER);
 		declineAppt.zPressButton(Button.B_YES);

 		// ---------------- Verification at organizer & invitee side both -------------------------------------

 		// Check that the organizer shows the attendee as "DECLINE" ---

 		// Organizer: Search for the appointment (InvId)
 		ZimbraAccount.AccountA().soapSend(
 					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
 				+		"<query>"+ apptSubject +"</query>"
 				+	"</SearchRequest>");

 		String organizerInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

 		// Get the appointment details
 		ZimbraAccount.AccountA().soapSend(
 					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

 		String attendeeStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

 		// Verify attendee status shows as ptst=DE
 		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee status shows as 'DECLINED' instead of 'NEEDS ACTION'");


 		// Attendee: Search for the appointment (InvId)
 		app.zGetActiveAccount().soapSend(
 					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
 				+		"<query>"+ apptSubject +"</query>"
 				+	"</SearchRequest>");

 		String attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

 		// Verify attendee status shows as ptst=DE
 		ZAssert.assertNull(attendeeInvId, "Verify appointment is not found");

 		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is deleted");
	}
}