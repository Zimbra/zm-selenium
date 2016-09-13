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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;

public class ViewAppointment extends CalendarWorkWeekTest {

	public ViewAppointment() {
		logger.info("New "+ ViewAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test( description = "View appointment and verify reading pane shows invite details correctly",
			groups = { "sanity" })
			
	public void ViewAppointment_01() throws HarnessException {
		
		// Creating objects for appointment data
		String tz, apptSubject, apptContent, apptAttendee1, apptLocation;
		tz = ZTimeZone.TimeZoneEST.getID();
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		
		apptSubject = ConfigProperties.getUniqueString();
		apptContent = ConfigProperties.getUniqueString();
		apptLocation = location.EmailAddress;
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 2, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 4, 0, 0);
		
        app.zGetActiveAccount().soapSend(
    			"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
    		+		"<m>"
    		+			"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
    		+				"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
    		+				"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>"
    		+				"<at cutype='RES' a='" + apptLocation + "' rsvp='1' role='REQ' url='" + apptLocation + "' ptst='AC'/>"
    		+			"</inv>"
    		+			"<e a='"+ apptAttendee1 +"' t='t'/>"
    		+			"<mp content-type='text/plain'>"
    		+				"<content>" + apptContent + "</content>" 
    		+			"</mp>"
    		+			"<su>" + apptSubject + "</su>" 
    		+		"</m>" 
    		+	"</CreateAppointmentRequest>");
        app.zPageCalendar.zRefresh();
        
        // Select appointment and delete it
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        
        // Verify reading pane contents
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentSubjectInViewAppt(apptSubject), "Verify Subject value in view appointment UI");
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentDateInViewAppt(getDay()), "Verify Date value in view appointment UI");
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentDateInViewAppt(getDate()), "Verify Date value in view appointment UI");
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentTimeInViewAppt("2:00 AM - 4:00 AM"), "Verify Time value in view appointment UI");
     	
     	// See bug 89760 (Location field should auto-complete in new appt UI and book the resource accordingly)
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentLocationInViewAppt(apptLocation), "Verify location value in view appointment UI");
     	
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentAttendeesLabelInViewAppt(), "Verify Attendees label in view appointment UI");
     	//ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentAttendeesInViewAppt(apptAttendee1), "Verify Attendees value in view appointment UI");
     	
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentCalendarLabelInViewAppt(), "Verify Calendar label in view appointment UI");
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentCalendarInViewAppt("Calendar"), "Verify Calendar value in view appointment UI");
     	
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentDisplayLabelInViewAppt(), "Verify Display label in view appointment UI");
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentDisplayInViewAppt("Busy"), "Verify Display value in view appointment UI");
     	     	
     	ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentBodyInViewAppt(apptContent), "Verify Body value in view appointment UI");
	}
	
	public String getDay() {
	    Date tomorrow = calendarWeekDayUTC.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("d");
	    return dateFormat.format(tomorrow);
	}
	
	public String getDate() {
	    Date tomorrow = calendarWeekDayUTC.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
	    return dateFormat.format(tomorrow);
	}
}
