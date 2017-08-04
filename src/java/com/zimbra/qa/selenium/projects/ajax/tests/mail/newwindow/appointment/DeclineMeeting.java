/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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

package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.appointment;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;

public class DeclineMeeting extends CalendarWorkWeekTest {

	public DeclineMeeting() {
		logger.info("New "+ DeclineMeeting.class.getCanonicalName());
		super.startingPage =  app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>()
				{
			private static final long serialVersionUID = 1L;
			{
				put("zimbraPrefGroupMailBy", "message");
			}
				};
	}

	/**
	 * ZimbraAccount.AccountA() sends a two-hour appointment to app.zGetActiveAccount()
	 * with subject and start time
	 * @param subject
	 * @param start
	 * @throws HarnessException 
	 */
	private void SendCreateAppointmentRequest(String subject, ZDate start) throws HarnessException {

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ subject +"'>"
						+				"<s d='"+ start.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
						+				"<e d='"+ start.addHours(2).toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
						+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
						+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
						+			"</inv>"
						+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
						+			"<su>"+ subject +"</su>"
						+			"<mp content-type='text/plain'>"
						+				"<content>content</content>"
						+			"</mp>"
						+		"</m>"
						+	"</CreateAppointmentRequest>");        

	}

	@Test(
			description = " From New Windoow Decline a meeting using Decline button from invitation message", 
			groups = { "functional", "L2" })
	public void DeclineMeeting_01() throws HarnessException {



		// ------------------------ Test data ------------------------------------

		String apptSubject = "appointment" + ConfigProperties.getUniqueString();

		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);



		// --------------- Creating invitation (organizer) ----------------------------

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
						+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
						+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
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



		// --------------- Login to attendee & decline invitation ----------------------------------------------------

		// Refresh the view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message displayed in current view");


		// Select the invitation

		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + apptSubject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Select window
			window.zSelectWindow(windowTitle);

			// Click Accept
			window.zPressButton(Button.B_DECLINE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}


		// ---------------- Verification at organizer & invitee side both -------------------------------------


		// --- Check that the organizer shows the attendee as "DECLINE" ---

		// Organizer: Search for the appointment (InvId)
		ZimbraAccount
				.AccountA()
				.soapSend(
						"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"
								+ startUTC.addDays(-10).toMillis()
								+ "' calExpandInstEnd='"
								+ endUTC.addDays(10).toMillis()
								+ "'>"
								+ "<query>"
								+ apptSubject
								+ "</query>"
								+ "</SearchRequest>");

		String organizerInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		ZimbraAccount.AccountA().soapSend(
				"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String attendeeStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee shows as 'DECLINED'");


		// --- Check that the attendee showing status as "DECLINE" ---

		// Attendee: Search for the appointment (InvId)
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
						+		"<query>"+ apptSubject +"</query>"
						+	"</SearchRequest>");

		String attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
				"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(myStatus, "DE", "Verify that the attendee shows as 'DECLINED'");

	}


	@Test(
			description = " From New Window >>Decline meeting - Verify organizer gets notification message", 
			groups = { "functional", "L2" })
	public void DeclineMeeting_02() throws HarnessException {



		// ------------------------ Test data ------------------------------------

		String apptSubject = "appointment" + ConfigProperties.getUniqueString();

		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);



		// --------------- Creating invitation (organizer) ----------------------------


		this.SendCreateAppointmentRequest(apptSubject, startUTC);


		// --------------- Login to attendee & decline invitation ----------------------------------------------------

		// Refresh the view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message displayed in current view");

		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + apptSubject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Select window
			window.zSelectWindow(windowTitle);

			// Click Decline
			window.zPressButton(Button.B_DECLINE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}


		// ---------------- Verification at organizer & invitee side both -------------------------------------       


		// --- Check that the organizer shows the attendee as "DECLINE" ---

		// Organizer: Search for the appointment response
		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
						+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
						+	"</SearchRequest>");

		String messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		// Get the appointment details
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
						+		"<m id='"+ messageId +"'/>"
						+	"</GetMsgRequest>");

	}


	@Test(
			description = "From New Window >>Decline meeting using 'Decline -> Notify Organizer'", 
			groups = { "functional", "L2" })
	public void DeclineMeeting_03() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		String apptSubject = "appointment" + ConfigProperties.getUniqueString();

		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);


		// --------------- Creating invitation (organizer) ----------------------------

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
						+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
						+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
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



		// --------------- Login to attendee & decline invitation ----------------------------------------------------

		// Refresh the view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message displayed in current view");

		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + apptSubject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Select window
			window.zSelectWindow(windowTitle);

			// Click Decline > Notify Organizer
			window.zPressButtonPulldown(Button.B_DECLINE, Button.O_DECLINE_NOTIFY_ORGANIZER);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}


		// ---------------- Verification at organizer & invitee side both -------------------------------------       


		// --- Check that the organizer shows the attendee as "DECLINE" ---

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
		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee shows as 'DECLINED'");


		// --- Check that the attendee showing status as "DECLINE" ---

		// Attendee: Search for the appointment (InvId)
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
						+		"<query>"+ apptSubject +"</query>"
						+	"</SearchRequest>");

		String attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
				"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(myStatus, "DE", "Verify that the attendee shows as 'DECLINED'");

		// Organizer: Search for the appointment response
		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
						+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
						+	"</SearchRequest>");

		String messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		// Get the appointment details
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
						+		"<m id='"+ messageId +"'/>"
						+	"</GetMsgRequest>");

	}


	@Test(
			description = "Decline meeting using 'Decline -> Don't Notify Organizer'", 
			groups = { "functional", "L2" })
	public void DeclineMeeting_04() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		String apptSubject = "appointment" + ConfigProperties.getUniqueString();

		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);


		// --------------- Creating invitation (organizer) ----------------------------

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
						+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
						+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
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



		// --------------- Login to attendee & decline invitation ----------------------------------------------------

		// Refresh the view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message displayed in current view");

		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + apptSubject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			
			// Select window
			window.zSelectWindow(windowTitle);

			// Click Decline > Don't Notify Organizer
			window.zPressButtonPulldown(Button.B_DECLINE, Button.O_DECLINE_DONT_NOTIFY_ORGANIZER);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}


		// ---------------- Verification at organizer & invitee side both -------------------------------------       


		// --- Check that the organizer shows the attendee as "DECLINE" ---

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

		// Verify attendee status shows as ptst=NE (bug 65356)
		ZAssert.assertEquals(attendeeStatus, "NE", "Verify that the attendee shows as 'DECLINED'");


		// --- Check that the attendee showing status as "DECLINE" ---

		// Attendee: Search for the appointment (InvId)
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
						+		"<query>"+ apptSubject +"</query>"
						+	"</SearchRequest>");

		String attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
				"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(myStatus, "DE", "Verify that the attendee shows as 'DECLINED'");

		// Organizer: Search for the appointment response
		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
						+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
						+	"</SearchRequest>");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
						+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
						+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify appointment notification message is not present");

	}

}

