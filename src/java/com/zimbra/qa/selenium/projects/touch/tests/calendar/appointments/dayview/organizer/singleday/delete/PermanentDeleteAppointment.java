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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.singleday.delete;

import java.util.Calendar;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekPreference;

public class PermanentDeleteAppointment extends CalendarWorkWeekPreference {

	public PermanentDeleteAppointment() {
		logger.info("New "+ PermanentDeleteAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;
	}
	
	@Test (description = "Permanently delete an appointment",
			groups = { "functional" })
	
	public void PermanentDeleteAppointment_01() throws HarnessException {
		
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
    		+			"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
    		+				"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" 
    		+				"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>"
    		+			"</inv>" 
    		+			"<mp content-type='text/plain'>" 
    		+				"<content>" + apptContent + "</content>" 
    		+			"</mp>"
    		+			"<su>" + apptSubject + "</su>" 
    		+		"</m>" 
    		+	"</CreateAppointmentRequest>");
        String apptId = app.zGetActiveAccount().soapSelectValue("//mail:CreateAppointmentResponse", "apptId");
        app.zPageCalendar.zRefresh();
        
        // Select appointment and delete it
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.B_DELETE, apptSubject);
        app.zPageMail.zPressButton(Button.B_YES);


        // Permanently delete an appointment
        SleepUtil.sleepMedium(); // invite doesn't remove from calendar immidiately so testcase fails here
		app.zPageCalendar.zSelectFolder("Trash");
		SleepUtil.sleepSmall();
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
		SleepUtil.sleepSmall();
        app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
        SleepUtil.sleepSmall();
        app.zPageMail.zPressButton(Button.B_YES);

        
        // Verify appointment is removed from Trash folder
        AppointmentItem deleteAppt = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
        ZAssert.assertNull(deleteAppt, "Verify appointment is removed from Trash folder");
        
        deleteAppt = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")");
        ZAssert.assertNull(deleteAppt, "Verify appointment is removed from Trash folder");
        
        ZimbraAccount.AccountA().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ apptId +"'/>");
        ZAssert.assertNull(ZimbraAccount.AccountA().soapSelectValue("//mail:GetAppointmentResponse//mail:comp", "id"), "Verify invite is removed from attendee's calendar");

        app.zPageCalendar.zSelectFolder("Trash");
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is removed from Trash folder");
	}
}
