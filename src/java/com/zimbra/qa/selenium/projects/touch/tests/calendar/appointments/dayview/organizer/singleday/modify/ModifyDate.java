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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.singleday.modify;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.touch.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.touch.pages.calendar.FormApptNew.Field;

public class ModifyDate extends CalendarWorkWeekPreference {	
	
	public ModifyDate() {
		logger.info("New "+ ModifyDate.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test (description = "Modify meeting date and verify it",
			groups = { "smoke" })
			
	public void ModifyDate_01() throws HarnessException, ParseException {
		
		// Create a meeting			
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();
		String apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 01, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 02, 0, 0);
		
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
                     		"<content>"+ apptContent +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        app.zPageCalendar.zRefresh();
        
        // Remove attendee and save the appointment
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.B_EDIT, apptSubject);
        apptForm.zFillField(Field.StartDate, getTomorrowsDay());
        apptForm.zSubmit();
        
        app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ apptSubject + ")" + " " + "content:(" + apptContent +")</query>"
			+	"</SearchRequest>");
		String apptId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		
        // Verify appointment modified date and time
		app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ apptId +"'/>");
		ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetAppointmentResponse//mail:comp//mail:s", "d"), getTomorrowsStartDate(), "Verify appointment modified start date");
	    ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetAppointmentResponse//mail:comp//mail:e", "d"), getTomorrowsEndDate(), "Verify appointment modified end date");
	    
	    // Verify appointment modified time in UI
	 	ZAssert.assertEquals(app.zPageCalendar.zVerifyAppointmentDateInViewAppt(getTomorrowsDay()), true, "Verify modified date shown in view appointment UI");
	}
	
	public String getTomorrowsDay() {	
	    Date tomorrow = calendarWeekDayUTC.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("d");
	    return dateFormat.format(tomorrow);
	}
	
	public String getTomorrowsDate() {
		calendarWeekDayUTC.add(Calendar.DAY_OF_YEAR, 1);
		
	    Date tomorrow = calendarWeekDayUTC.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
	    return dateFormat.format(tomorrow);
	}
	
	public String getTomorrowsStartDate() {
		
	    Date tomorrow = calendarWeekDayUTC.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'");
	    return dateFormat.format(tomorrow) + "010000";
	}
	
	public String getTomorrowsEndDate() {
		
		Date tomorrow = calendarWeekDayUTC.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'");
	    return dateFormat.format(tomorrow) + "020000";
	}

}
