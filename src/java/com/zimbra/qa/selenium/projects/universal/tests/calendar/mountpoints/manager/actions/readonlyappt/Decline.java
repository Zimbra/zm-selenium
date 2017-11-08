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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.mountpoints.manager.actions.readonlyappt;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.DialogConfirmationDeclineAppointment;

public class Decline extends CalendarWorkWeekTest {

	public Decline() {
		logger.info("New "+ Decline.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "81647")
	@Test( description = "Assistant right clicks to calendar invite from shared calendar and declines the invite OBO boss (Don't notify organizer)",
			groups = { "smoke", "L1" })
			
	public void Decline_01() throws HarnessException {
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
	
		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account5(), FolderItem.SystemFolder.Calendar);
		
		// Share it
		ZimbraAccount.Account5().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account5().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");
		
		// Create invite
		ZimbraAccount.Account6().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account6().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account5().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account5().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
				
		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountPointName);
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_DECLINE_MENU, apptSubject);
		
		DialogConfirmationDeclineAppointment declineAppt = (DialogConfirmationDeclineAppointment) new DialogConfirmationDeclineAppointment(app, app.zPageCalendar);
		declineAppt.zPressButton(Button.B_DONT_NOTIFY_ORGANIZER);
		declineAppt.zPressButton(Button.B_YES);
		
		
		// -------------- Verification at organizer side --------------
		
		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account6(), FolderItem.SystemFolder.Inbox).getId();
		
		ZimbraAccount.Account6().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = ZimbraAccount.Account6().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(messageId, "Verify organizer doesn't get email notification");
		
		ZimbraAccount.Account6().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String organizerInvId = ZimbraAccount.Account6().soapSelectValue("//mail:appt", "invId");
	
		// Get the appointment details
		ZimbraAccount.Account6().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");
		
		String attendeeStatus = ZimbraAccount.Account6().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account5().EmailAddress +"']", "ptst");
	
		// Verify attendee status shows as ptst=NE
		ZAssert.assertEquals(attendeeStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION' instead of 'DECLINED'");

		
		// -------------- Verification at attendee side --------------

		ZimbraAccount.Account5().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		
		String attendeeInvId = ZimbraAccount.Account5().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.Account5().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		
		String myStatus = ZimbraAccount.Account5().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account5().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(myStatus, "DE", "Verify that the attendee shows as 'DECLINED'");
		
		// Verify sent mail not present for declined appointment notification (action performed by assistant)
		ZimbraAccount.Account5().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>" + "in:sent subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		messageId = ZimbraAccount.Account5().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(messageId, "Verify sent mail not present for declined appointment notification (action performed by assistant)");
		
	}
	
	@Test( description = "Assistant right clicks to calendar invite from shared calendar and declines the invite OBO boss (Notify organizer)",
			groups = { "smoke", "L1" })
			
	public void Decline_02() throws HarnessException {
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
	
		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account5(), FolderItem.SystemFolder.Calendar);
		
		// Share it
		ZimbraAccount.Account5().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account5().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");
		
		// Create invite
		ZimbraAccount.Account6().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account6().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account5().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account5().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
		
		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountPointName);
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_DECLINE_MENU, apptSubject);
		
		DialogConfirmationDeclineAppointment declineAppt = (DialogConfirmationDeclineAppointment) new DialogConfirmationDeclineAppointment(app, app.zPageCalendar);
		declineAppt.zPressButton(Button.B_NOTIFY_ORGANIZER);
		declineAppt.zPressButton(Button.B_YES);
		
		
		// -------------- Verification at organizer side --------------
		
		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account6(), FolderItem.SystemFolder.Inbox).getId();
		
		ZimbraAccount.Account6().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = ZimbraAccount.Account6().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify organizer gets email notification");
		
		String attendeeStatus = ZimbraAccount.Account6().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account5().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee status shows as 'DECLINED'");
		
		// Verify from and sender address in accept invitation message		
		ZimbraAccount.Account6().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");
		
		ZAssert.assertEquals(ZimbraAccount.Account6().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account5().EmailAddress, "Verify From address in decline invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account6().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in decline invitation message");
		
		ZimbraAccount.Account6().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String organizerInvId = ZimbraAccount.Account6().soapSelectValue("//mail:appt", "invId");
	
		// Get the appointment details
		ZimbraAccount.Account6().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");
		
		attendeeStatus = ZimbraAccount.Account6().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account5().EmailAddress +"']", "ptst");
	
		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee status shows as 'DECLINED'");

		
		// -------------- Verification at attendee side --------------

		ZimbraAccount.Account5().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		
		String attendeeInvId = ZimbraAccount.Account5().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.Account5().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		
		String myStatus = ZimbraAccount.Account5().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account5().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(myStatus, "DE", "Verify that the attendee shows as 'DECLINED'");
		
		// Verify sent mail for declined appointment notification (action performed by assistant)
		ZimbraAccount.Account5().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>" + "in:sent is:unread subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		messageId = ZimbraAccount.Account5().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify sent mail for declined appointment notification (action performed by assistant)");
		
		// Verify from and sender address in decline invitation message		
		ZimbraAccount.Account5().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");
		
		ZAssert.assertEquals(ZimbraAccount.Account5().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account5().EmailAddress, "Verify From address in decline invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account5().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in decline invitation message");
		
	}
}