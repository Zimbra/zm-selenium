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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.meetings.organizer.singleday.actions;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;

public class Forward extends CalendarWorkWeekTest {	
	
	public Forward() {
		logger.info("New "+ Forward.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test( description = "Forward a meeting invite using context menu",
			groups = { "smoke", "L1" })
			
	public void ForwardMeeting_01() throws HarnessException {
				
		// Creating a meeting
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String attendee1 = ZimbraAccount.AccountA().EmailAddress;
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + attendee1 + "' d='2'/>" + 
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

        // Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
        
        // Forward appointment to different attendee
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_FORWARD_MENU, apptSubject);
        
		FormApptNew form = new FormApptNew(app);
        form.zFillField(Field.To, attendee2);
        form.zSubmit();
		
		// Verify the new invitation appears in the inbox
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		// Verify only one appointment is in the calendar
		AppointmentItem a = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject + ")");
		ZAssert.assertNotNull(a, "Verify only one appointment matches in the calendar");

	}
	
	@Test( description = "Forward a meeting invite by changing content",
			groups = { "smoke", "L1" })
			
	public void ForwardMeeting_02() throws HarnessException {
				
		// Creating a meeting
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		String ForwardContent = ConfigProperties.getUniqueString();
		String attendee1 = ZimbraAccount.AccountA().EmailAddress;
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + attendee1 + "' d='2'/>" + 
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ apptBody +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
        
        // Forward appointment to different attendee
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_FORWARD_MENU, apptSubject);
        
        FormApptNew form = new FormApptNew(app);
        form.zFillField(Field.To, attendee2);
        form.zFillField(Field.Body, ForwardContent);
        form.zSubmit();
        SleepUtil.sleepLong(); // New invitation doesn't appear in the attendee's inbox
		
		// Verify the new invitation appears in the inbox
        ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + (char)34 + "Fwd: " + apptSubject + (char)34 + ")" + " " + "content:(" + ForwardContent +")" + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		// Verify only one appointment is in the calendar
		AppointmentItem a = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ apptSubject +")"  + " " + "content:(" + ForwardContent +")");
		ZAssert.assertNotNull(a, "Verify only one appointment matches in the calendar");
		
		// Verify original invite content remains as it is
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ apptSubject +")" + " " + "content:(" + apptBody +")</query>"
			+	"</SearchRequest>");
		id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify original invite content remains as it is");
		
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ apptSubject +")" + " " + "content:(" + ForwardContent +")</query>"
			+	"</SearchRequest>");
		id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNull(id, "Verify forwarded content doesn't modify original invite body");

	}
	
	@Bugs(ids = "59724")
	@Test( description = "Forwarding if a conf-room is already scheduled for same invite",
			groups = { "functional", "L2" })
			
	public void ForwardMeeting_03() throws HarnessException {
		
		// Creating object for meeting data
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		
		String tz, apptSubject, apptAttendeeEmail;
		tz = ZTimeZone.getLocalTimeZone().getID();
		
		apptSubject = "app" + ConfigProperties.getUniqueString();
		apptAttendeeEmail = ZimbraAccount.AccountA().EmailAddress;
		String apptLocation = location.EmailAddress;
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"' loc='"+ apptLocation+"'>" +
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendeeEmail + "' d='2'/>" +
                     		"<at cutype='RES' a='" + apptLocation + "' rsvp='1' role='REQ' url='" + apptLocation + "' ptst='AC'/>" +
                     	"</inv>" +
                     	"<e a='"+ apptLocation +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		
        // Forward appointment to different attendee
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_FORWARD_MENU, apptSubject);
        
		FormApptNew form = new FormApptNew(app);
        form.zFillField(Field.To, attendee2);
        form.zSubmit();
		
		// Verify the new invitation appears in the inbox
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		// Verify only one appointment is in the calendar
		AppointmentItem a = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ apptSubject + ")");
		ZAssert.assertNotNull(a, "Verify only one appointment matches in the calendar");

	}


	@Bugs(ids="100340")
	@Test( description = "forwarding invite shows html source in meeting notes section",
			groups = { "functional", "L2" })
			
	public void ForwardMeeting_04() throws HarnessException {
		
		app.zPageMain.zLogout();
		ZimbraAccount.AccountZCS().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "html" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "TRUE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());
		app.zPageCalendar.zNavigateTo();
		String attendee2 = ZimbraAccount.Account2().EmailAddress;
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		
		String apptSubject, apptAttendee1, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.Account3().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 5, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 7, 0, 0));
		appt.setContent(apptContent);
	
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
        app.zPageMail.zNavigateTo();
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Go to sent		
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		
		// Forward the item
		FormMailNew mailForm = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailForm, "Mail exists");
		
		// Fill out the form with the data
		FormApptNew apptForm1 = new FormApptNew(app);
		ZAssert.assertStringDoesNotContain(apptForm1.zGetApptBodyHtml(), "<div>", "Verify populated appointment does not contain <div> tag");
		ZAssert.assertStringDoesNotContain(apptForm1.zGetApptBodyHtml(), "<br>", "Verify populated appointment does not contain <br> tag");
		ZAssert.assertStringDoesNotContain(apptForm1.zGetApptBodyHtml(), "<td>", "Verify populated appointment does not contain <td> tag");
		apptForm1.zFillField(Field.To, attendee2);
		apptForm1.zSubmit();
		
		//Logout and login as AccountB
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(ZimbraAccount.Account2());
		
		// Refresh the view
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Select the invitation and verify Accept/decline/Tentative buttons are present
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertNotNull(display, "Mail exists");
	}
	
}
