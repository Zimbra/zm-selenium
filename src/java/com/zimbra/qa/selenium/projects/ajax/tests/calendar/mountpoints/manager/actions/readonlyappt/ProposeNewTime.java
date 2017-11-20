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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class ProposeNewTime extends AjaxCore {

	public ProposeNewTime() {
		logger.info("New "+ ProposeNewTime.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "80559")
	@Test (description = "Assistant right clicks to calendar invite from shared calendar and proposes new time OBO boss",
			groups = { "functional-skip", "application-bug" })

	public void ProposeNewTime_01() throws HarnessException {

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();

		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		String modifiedSubject = ConfigProperties.getUniqueString();
		ZDate modifiedStartUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		ZDate modifiedEndUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);
		String modifiedBody = "body" + ConfigProperties.getUniqueString();

		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account2(), FolderItem.SystemFolder.Calendar);

		// Share it
		ZimbraAccount.Account2().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account2().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");

		// Create invite
		ZimbraAccount.Account3().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account3().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account2().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account2().EmailAddress +"' t='t'/>"
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

		FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_PROPOSE_NEW_TIME_MENU, apptSubject);
		apptForm.zVerifyDisabledControlInProposeNewTimeUI();
		appt.setStartTime(modifiedStartUTC);
		appt.setEndTime(modifiedEndUTC);
		appt.setContent(modifiedBody);
		apptForm.zFill(appt);
		apptForm.zSubmit();
		app.zPageCalendar.zToolbarPressButton(Button.B_CLOSE);


		// -------------- Verification at organizer side --------------

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account3(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.Account3().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = ZimbraAccount.Account3().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify organizer gets email notification for proposed new time");

		// Verify from and sender address in accept invitation message
		ZimbraAccount.Account3().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");

		ZAssert.assertEquals(ZimbraAccount.Account3().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account2().EmailAddress, "Verify From address in accept invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account3().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in accept invitation message");

		// -------------- Verification at attendee side --------------

		// Verify sent mail for accepted appointment notification (action performed by assistant)
		ZimbraAccount.Account2().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>" + "in:sent is:unread subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		messageId = ZimbraAccount.Account2().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify sent mail for proposed new time appointment (action performed by assistant)");

		// Verify from and sender address in accept invitation message
		ZimbraAccount.Account2().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");

		ZAssert.assertEquals(ZimbraAccount.Account2().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account2().EmailAddress, "Verify From address in accept invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account2().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in accept invitation message");

		// Logout to attendee and login as organizer to accept proposed new time
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(ZimbraAccount.Account3());
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		display.zPressButton(Button.B_ACCEPT_PROPOSE_NEW_TIME);
		SleepUtil.sleepMedium();

		appt.setSubject(modifiedSubject);
		appt.setContent(" " + modifiedBody);
		apptForm.zFill(appt);
		apptForm.zSubmit();

		// Logout to organizer and login as attendee to accept new time
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(ZimbraAccount.Account2());
		display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, modifiedSubject);
		display.zPressButton(Button.B_ACCEPT);

		// ------ Organizer ------

		// Verify that the attendee1 status showing as 'ACCEPTED' for organizer
		ZimbraAccount.Account3().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>" + "subject:(" + modifiedSubject + ")" + " " + "content:(" + modifiedBody +")" + "</query>"
				+	"</SearchRequest>");

		String organizerInvId = ZimbraAccount.Account3().soapSelectValue("//mail:appt", "invId");
		ZimbraAccount.Account3().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String attendeeStatus = ZimbraAccount.Account3().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account2().EmailAddress +"']", "ptst");
		ZAssert.assertEquals(ZimbraAccount.Account3().soapSelectValue("//mail:s", "d"), modifiedStartUTC.toyyyyMMddTHHmmss(), "Verify modified start time of the appointment");
		ZAssert.assertEquals(ZimbraAccount.Account3().soapSelectValue("//mail:e", "d"), modifiedEndUTC.toyyyyMMddTHHmmss(), "Verify modified end time of the appointment");
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED' for organizer");

		// ------ Attendee1 ------

		// Verify that the attendee1 status showing as 'ACCEPTED' for attendee
		ZimbraAccount.Account2().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>" + "subject:(" + modifiedSubject + ")" + " " + "content:(" + modifiedBody +")" + "</query>"
				+	"</SearchRequest>");

		String attendeeInvId = ZimbraAccount.Account2().soapSelectValue("//mail:appt", "invId");
		ZimbraAccount.Account2().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.Account2().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account2().EmailAddress +"']", "ptst");
		ZAssert.assertEquals(ZimbraAccount.Account2().soapSelectValue("//mail:s", "d"), modifiedStartUTC.toyyyyMMddTHHmmss(), "Verify modified start time of the appointment");
		ZAssert.assertEquals(ZimbraAccount.Account2().soapSelectValue("//mail:e", "d"), modifiedEndUTC.toyyyyMMddTHHmmss(), "Verify modified end time of the appointment");
		ZAssert.assertEquals(myStatus, "AC", "Verify that the attendee1 status showing as 'ACCEPTED'");

		ZimbraAccount.Account2().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>" + "subject:(" + "New Time Proposed.*" + ")" + " " + "content:(" + modifiedBody +")" + "</query>"
			+	"</SearchRequest>");
		messageId = ZimbraAccount.Account2().soapSelectValue("//mail:m", "id");

		ZimbraAccount.Account2().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");
		ZAssert.assertNotNull(messageId, "Attendee should get message from the organizer for accepting proposed new time");

	}
}