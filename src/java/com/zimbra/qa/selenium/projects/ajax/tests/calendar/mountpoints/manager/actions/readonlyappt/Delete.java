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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.manager.actions.readonlyappt;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmationDeclineAppointment;

public class Delete extends AjaxCommonTest {

	public Delete() {
		logger.info("New "+ Delete.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test( description = "Assistant right clicks to calendar invite from shared calendar and deletes the invite OBO boss (Don't notify organizer)",
			groups = { "functional", "L2" })

	public void Delete_01() throws HarnessException {

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();

		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);

		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account6(), FolderItem.SystemFolder.Calendar);

		// Share it
		ZimbraAccount.Account6().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account6().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");

		// Create invite
		ZimbraAccount.Account7().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account7().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account6().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account6().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountPointName);

		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_DELETE, apptSubject);

		DialogConfirmationDeclineAppointment declineAppt = (DialogConfirmationDeclineAppointment) new DialogConfirmationDeclineAppointment(app, app.zPageCalendar);
		declineAppt.zClickButton(Button.B_DONT_NOTIFY_ORGANIZER);
		declineAppt.zClickButton(Button.B_YES);


		// -------------- Verification at organizer side --------------

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account7(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.Account7().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = ZimbraAccount.Account7().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(messageId, "Verify organizer doesn't get email notification");

		// -------------- Verification at attendee side --------------

		ZimbraAccount.Account6().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String attendeeInvId = ZimbraAccount.Account6().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNull(attendeeInvId, "Verify appointment is deleted");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");

		attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNull(attendeeInvId, "Verify appointment is deleted");

	}


	@Test( description = "Assistant right clicks to calendar invite from shared calendar and deletes the invite OBO boss (Notify organizer)",
			groups = { "functional", "L2" })

	public void Delete_02() throws HarnessException {

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();

		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account6(), FolderItem.SystemFolder.Calendar);

		// Share it
		ZimbraAccount.Account6().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account6().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");

		// Create invite
		ZimbraAccount.Account7().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account7().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account6().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account6().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountPointName);

		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_DELETE, apptSubject);

		DialogConfirmationDeclineAppointment declineAppt = (DialogConfirmationDeclineAppointment) new DialogConfirmationDeclineAppointment(app, app.zPageCalendar);
		declineAppt.zClickButton(Button.B_NOTIFY_ORGANIZER);
		declineAppt.zClickButton(Button.B_YES);


		// -------------- Verification at organizer side --------------

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account7(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.Account7().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = ZimbraAccount.Account7().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify organizer gets email notification");

		String attendeeStatus = ZimbraAccount.Account7().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account6().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee status shows as 'DECLINED'");

		// Verify from and sender address in accept invitation message
		ZimbraAccount.Account7().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");

		ZAssert.assertEquals(ZimbraAccount.Account7().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account6().EmailAddress, "Verify From address in decline invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account7().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in decline invitation message");

		ZimbraAccount.Account7().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");

		String organizerInvId = ZimbraAccount.Account7().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		ZimbraAccount.Account7().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		attendeeStatus = ZimbraAccount.Account7().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account6().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee status shows as 'DECLINED'");


		// -------------- Verification at attendee side --------------

		ZimbraAccount.Account6().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String attendeeInvId = ZimbraAccount.Account6().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.Account6().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.Account6().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account6().EmailAddress +"']", "ptst");

		// Verify attendee status
		ZAssert.assertNull(myStatus, "Verify that attendee status is null");

		// Verify sent mail for declined appointment notification (action performed by assistant)
		ZimbraAccount.Account6().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>" + "in:sent is:unread subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		messageId = ZimbraAccount.Account6().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify sent mail for declined appointment notification (action performed by assistant)");

		// Verify from and sender address in decline invitation message
		ZimbraAccount.Account6().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");

		ZAssert.assertEquals(ZimbraAccount.Account6().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account6().EmailAddress, "Verify From address in decline invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account6().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in decline invitation message");

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");

		attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNull(attendeeInvId, "Verify appointment is deleted");

	}


	@Test( description = "Assistant clicks to calendar invite from shared calendar and deletes the invite OBO boss",
			groups = { "functional", "L2" })

	public void Delete_03() throws HarnessException {

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();

		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account6(), FolderItem.SystemFolder.Calendar);

		// Share it
		ZimbraAccount.Account6().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account6().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");

		// Create invite
		ZimbraAccount.Account7().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account7().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account6().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account6().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountPointName);

		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.B_DELETE, apptSubject);

		DialogConfirmationDeclineAppointment deleteAppt = (DialogConfirmationDeclineAppointment) new DialogConfirmationDeclineAppointment(app, app.zPageCalendar);
		deleteAppt.zClickButton(Button.B_NO);
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);

		// Don't notify organizer and notify organizer logic is covered in above tests

	}
}