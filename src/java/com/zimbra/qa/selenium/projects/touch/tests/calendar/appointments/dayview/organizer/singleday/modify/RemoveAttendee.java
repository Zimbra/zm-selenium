/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.singleday.modify;

import java.util.Calendar;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew;

public class RemoveAttendee extends CalendarWorkWeekTest {	
	
	public RemoveAttendee() {
		logger.info("New "+ RemoveAttendee.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "95600")
	@Test(description = "Modify meeting by removing attendee",
			groups = { "smoke" })
			
	public void RemoveAttendee_01() throws HarnessException {
		
		// Create a meeting			
		String tz = ZTimeZone.TimeZoneEST.getID();
		String apptSubject = ZimbraSeleniumProperties.getUniqueString();
		String apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
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
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        app.zPageCalendar.zRefresh();
        
        // Remove attendee and save the appointment
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.B_EDIT, apptSubject);
        apptForm.zRemoveAttendee(apptAttendee1);
        apptForm.zSubmit();
         
        // Verify that attendee doesn't present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertNull(actual.getAttendees(), "Attendees: Verify the appointment data");
		
		// Verify attendee free/busy status
		String attendeeStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptAttendee1 +"']", "ptst");
		ZAssert.assertNull(attendeeStatus, "Verify that attendee status shows null");
		
		// Verify attendee receives cancellation message
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:" + (char)34 + "Cancelled: " + apptSubject + (char)34 + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify attendee receives cancelled meeting message");
		
		// Verify appointment is deleted from attendee's calendar
		AppointmentItem removedAttendee = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")");
		ZAssert.assertNull(removedAttendee, "Verify meeting is deleted from attendee's calendar");
		
	}
}
