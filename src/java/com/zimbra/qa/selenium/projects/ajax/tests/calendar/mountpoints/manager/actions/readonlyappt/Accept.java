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
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;

public class Accept extends CalendarWorkWeekTest {

	public Accept() {
		logger.info("New "+ Accept.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "81647")
	@Test( description = "Assistant right clicks to calendar invite from shared calendar and accepts the invite OBO boss",
			groups = { "smoke", "L1" })
			
	public void Accept_01() throws HarnessException {
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 7, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
	
		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account1(), FolderItem.SystemFolder.Calendar);
		
		// Share it
		ZimbraAccount.Account1().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account1().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");
		
		// Create invite
		ZimbraAccount.Account2().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account2().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account1().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account1().EmailAddress +"' t='t'/>"
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
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_ACCEPT_MENU, apptSubject);
		
		
		// -------------- Verification at organizer side --------------
		
		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account2(), FolderItem.SystemFolder.Inbox).getId();
		
		ZimbraAccount.Account2().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		String messageId = ZimbraAccount.Account2().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify organizer gets email notification");
		
		String attendeeStatus = ZimbraAccount.Account2().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account1().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");
		
		// Verify from and sender address in accept invitation message		
		ZimbraAccount.Account2().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");
		
		ZAssert.assertEquals(ZimbraAccount.Account2().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account1().EmailAddress, "Verify From address in accept invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account2().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in accept invitation message");
		
		ZimbraAccount.Account2().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String organizerInvId = ZimbraAccount.Account2().soapSelectValue("//mail:appt", "invId");
	
		// Get the appointment details
		ZimbraAccount.Account2().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");
		
		attendeeStatus = ZimbraAccount.Account2().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account1().EmailAddress +"']", "ptst");
	
		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");

		
		// -------------- Verification at attendee side --------------

		ZimbraAccount.Account1().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		
		String attendeeInvId = ZimbraAccount.Account1().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.Account1().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		
		String myStatus = ZimbraAccount.Account1().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account1().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(myStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");
		
		// Verify sent mail for accepted appointment notification (action performed by assistant)
		ZimbraAccount.Account1().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>" + "in:sent is:unread subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		messageId = ZimbraAccount.Account1().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify sent mail for accepted appointment notification (action performed by assistant)");
		
		attendeeStatus = ZimbraAccount.Account1().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account1().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");
		
		// Verify from and sender address in accept invitation message		
		ZimbraAccount.Account1().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");
		
		ZAssert.assertEquals(ZimbraAccount.Account1().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account1().EmailAddress, "Verify From address in accept invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account1().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in accept invitation message");
		
		// Mark ON/OFF to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		
	}
}