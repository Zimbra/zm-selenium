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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.invitations.message;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class DeclineProposeNewTime extends CalendarWorkWeekTest {

	public DeclineProposeNewTime() {
		logger.info("New "+ DeclineProposeNewTime.class.getCanonicalName());
		super.startingPage =  app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>()
		{
			private static final long serialVersionUID = 1L;
			{
				put("zimbraPrefGroupMailBy", "message");
			}
		};
	}
	
	@Bugs(ids = "69132,96556,96748")
	@Test( description = "Receive meeting invite -> Propose New Time to organizer and organizer declines the new time using message view", 
			groups = { "functional" })
	public void DeclineProposeNewTime_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		String organizerEmailAddress, apptAttendee1EmailAddress, apptAttendee2EmailAddress;
		ZimbraAccount organizer, apptAttendee1; 
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		String modifiedBody = ConfigProperties.getUniqueString();
		
		apptAttendee1 = app.zGetActiveAccount();
		apptAttendee1EmailAddress = app.zGetActiveAccount().EmailAddress;
		organizer = ZimbraAccount.AccountA();
		organizerEmailAddress = ZimbraAccount.AccountA().EmailAddress;
		apptAttendee2EmailAddress = ZimbraAccount.AccountB().EmailAddress;

		Calendar now = this.calendarWeekDayUTC;
		AppointmentItem appt = new AppointmentItem();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);
		ZDate modifiedStartUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);
		ZDate modifiedEndUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 19, 0, 0);
		
		// --------------- Creating invitation (apptAttendee1) ----------------------------
		organizer.soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<or a='" + organizerEmailAddress + "'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1EmailAddress + "'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee2EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ apptAttendee1EmailAddress +"' t='t'/>"
				+			"<e a='"+ apptAttendee2EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");        
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message displayed in current view");
		
		// --------------- Login to attendee & propose new time ----------------------------------------------------

		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		display.zPressButton(Button.B_PROPOSE_NEW_TIME);
		SleepUtil.sleepMedium();
		
		FormApptNew apptForm = new FormApptNew(app);
		appt.setStartTime(modifiedStartUTC);
		appt.setEndTime(modifiedEndUTC);
		appt.setContent(modifiedBody);
		apptForm.zFill(appt);
		apptForm.zSubmit();
		
		// ------ Organizer ------
		
		// Verify organizer gets email notification using modified date & content
		String inboxId = FolderItem.importFromSOAP(organizer, FolderItem.SystemFolder.Inbox).getId();
		organizer.soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+       "<query>inid:"+ inboxId +" subject:(" + apptSubject + ")" + " " + "content:(" + modifiedBody +")" + "</query>"
				+	"</SearchRequest>");
		String messageId = organizer.soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify organizer gets email notification using modified date & content");
		
		// ------ Attendee1 ------
		
		// Verify that the attendee status still shows as 'NEEDS ACTION'
		apptAttendee1.soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>" + "subject:(" + apptSubject + ")" + " " + "content:(" + apptBody +")" + "</query>"
				+	"</SearchRequest>");
		
		String  apptAttendee1InvId= apptAttendee1.soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(apptAttendee1InvId, "Original invite body shouldn't be changed for attendee");

		// Login as organizer to decline proposed new time
		app.zPageMain.zLogout();
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ organizer.ZimbraId +"</id>"
			+		"<a n='zimbraPrefGroupMailBy'>conversation</a>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(organizer);
		
		display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		display.zPressButton(Button.B_DECLINE_PROPOSE_NEW_TIME);
		FormMailNew mailComposeForm = new FormMailNew(app);
		mailComposeForm.zSubmit();
		SleepUtil.sleepLong();
		
		// ------ Attendee1 ------
		
		// Verify that the attendee1 status showing as 'NEEDS ACTION' for attendee and reveive declined propose new time message
		apptAttendee1.soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>" + "subject:(" + apptSubject + ")" + " " + "content:(" + apptBody +")" + "</query>"
				+	"</SearchRequest>");
		String attendeeInvId = apptAttendee1.soapSelectValue("//mail:appt", "invId");
		apptAttendee1.soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		String myStatus = apptAttendee1.soapSelectValue("//mail:at[@a='"+ apptAttendee1EmailAddress +"']", "ptst");
		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee shows as 'NEEDS ACTION' for organizer");

	}
	
}
