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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.universal.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.universal.pages.mail.FormMailNew.Field;

public class ReplyToAll extends CalendarWorkWeekPreference {	
	
	public ReplyToAll() {
		logger.info("New "+ ReplyToAll.class.getCanonicalName());
	    super.startingPage =  app.zPageCalendar;
	    
	}
	
	@Test (description = "Check when attendees get the reply when organizer ReplyAll to a meeting",
			groups = { "functional", "L2" })
	public void ReplyToAll_01() throws HarnessException {
		

		//-- Data Setup

		// Creating object for meeting data
		String tz, apptSubject, apptBody, apptAttendee1;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = "appt" + ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
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
                     "<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>" + 
                     "</inv>" +
                     "<e a='"+ apptAttendee1 +"' t='t'/>" +
                     "<mp content-type='text/plain'>" +
                     "<content>"+ apptBody +"</content>" +
                     "</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // When the form opens, add some text and then Submit
        String replyText= "ReplyBodyByOrganizer";
        FormMailNew mailComposeForm = (FormMailNew)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK,Button.O_REPLY_TO_ALL_MENU, apptSubject);
        mailComposeForm.zFillField(Field.Body, replyText);		
		mailComposeForm.zSubmit();
        
		// Verify the new invitation appears in the inbox
        String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id"); 
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +") content:("+ replyText +")</query>"			
			+	"</SearchRequest>");
		
		id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the replyall to meeting appears in the attendee's inbox");	
	}
	
	
	@Bugs (ids = "57418")
	@Test (description = "Verify if optional attendees appear as CC contact in the mail when organizer replies all to meeting",
			groups = { "functional", "L2" })
	public void ReplyToAll_02() throws HarnessException {
		

		//-- Data Setup

		// Creating object for meeting data
		String tz, apptSubject, apptAttendee1,apptAttendee2;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = "appt" + ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptAttendee2 = ZimbraAccount.AccountB().EmailAddress;
		
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
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>" +
                     		"<at role='OPT' ptst='NE' rsvp='1' a='" + apptAttendee2 + "' d='2'/>" +
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
   
        // Verify if Optional attendee appears in the CC feild when organizer replies all to meeting  
        FormMailNew mailComposeForm = (FormMailNew)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK,Button.O_REPLY_TO_ALL_MENU, apptSubject);
        String userInCcField = mailComposeForm.zGetFieldValue(Field.Cc);
		ZAssert.assertStringContains(apptAttendee2, userInCcField, "Verify the optional attendee appears in the CC field");
		
	}
}
