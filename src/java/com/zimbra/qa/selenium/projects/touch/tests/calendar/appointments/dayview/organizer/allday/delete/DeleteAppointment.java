/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.allday.delete;

import java.util.Calendar;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekPreference;

public class DeleteAppointment extends CalendarWorkWeekPreference {

	public DeleteAppointment() {
		logger.info("New "+ DeleteAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;
	}
	
	@Test (description = "Delete an appointment using Delete toolbar button in day view",
			groups = { "bhr" })
	
	public void DeleteAppointment_01() throws HarnessException {
		
		// Creating objects for appointment data
		String tz, apptSubject, apptContent, apptAttendee1;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptContent = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 2, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 4, 0, 0);
		
        app.zGetActiveAccount().soapSend(
    			"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
    		+		"<m>"
    		+			"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='1' name='"+ apptSubject +"'>"
    		+				"<s d='"+ startUTC.toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<e d='"+ endUTC.toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
    		+				"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>"
    		+			"</inv>" 
    		+			"<mp content-type='text/plain'>" 
    		+				"<content>" + apptContent + "</content>" 
    		+			"</mp>"
    		+			"<su>" + apptSubject + "</su>" 
    		+		"</m>" 
    		+	"</CreateAppointmentRequest>");
        app.zPageCalendar.zRefresh();
        String apptId = app.zGetActiveAccount().soapSelectValue("//mail:CreateAppointmentResponse", "apptId");
        
        // Select appointment and delete it
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
        app.zPageMail.zPressButton(Button.B_YES);
        
        ZAssert.assertEquals(zVerifyToastMessage("Appointment moved to trash"), true, "Verify toast message after creating invite");
        
        // Verify appointment is moved to Trash
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is deleted");
		
		// Verify appointment is in Trash folder
		app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ apptId +"'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetAppointmentResponse//mail:comp", "ciFolder"), "3", "Verify appointment is in Trash folder");
        
        ZimbraAccount.AccountA().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ apptId +"'/>");
        ZAssert.assertNull(ZimbraAccount.AccountA().soapSelectValue("//mail:GetAppointmentResponse//mail:comp", "id"), "Verify invite is removed from attendee's calendar");
        
        // Verify cancelled message received to attendee
        MailItem canceledApptMsg = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + (char)34 + "Cancelled " + apptSubject + (char)34 + ")");
		ZAssert.assertNotNull(canceledApptMsg, "Verify cancelled message received to attendee");
	}
	
}
