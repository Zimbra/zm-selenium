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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.recurring.modify;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew.Field;

public class ModifyInstanceTime extends CalendarWorkWeekTest {

	public ModifyInstanceTime() {
		logger.info("New "+ ModifyInstanceTime.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}

	@Test(description = "Modify instance date of series and verify it",
			groups = { "functional" })

	public void ModifyInstanceTime_01() throws HarnessException, ParseException {

		// Create a meeting
		String tz = ZTimeZone.TimeZoneEST.getID();
		String apptSubject = ZimbraSeleniumProperties.getUniqueString();
		String apptContent = ZimbraSeleniumProperties.getUniqueString();
		String apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 01, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 02, 0, 0);

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptContent +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		String apptId = app.zGetActiveAccount().soapSelectValue("//mail:CreateAppointmentResponse", "apptId");
		app.zPageCalendar.zRefresh();

        // Edit the invite and modify series date
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.O_OPEN_INSTANCE_MENU, apptSubject);
        apptForm = (FormApptNew)app.zPageCalendar.zToolbarPressButton(Button.B_EDIT);
        
        SleepUtil.sleepSmall();
        apptForm.zFillField(Field.StartTime, "03:00 AM");
        apptForm.zSubmit();
        ZAssert.assertEquals(app.zPageCalendar.zVerifyAppointmentTimeInViewAppt("3:00 AM - 4:00 AM"), true, "Verify modified time shown in view appointment UI");

        String modifiedSeriesDate = "Every day; No end date; Effective " + getTomorrowsDate();
        apptForm = (FormApptNew)app.zPageCalendar.zToolbarPressButton(Button.B_EDIT);
        
        SleepUtil.sleepSmall();
        ZAssert.assertTrue(app.zPageCalendar.zVerifyRepeatString(modifiedSeriesDate), "Verify series pattern is updated according to modified date");
        app.zPageCalendar.zToolbarPressButton(Button.B_CANCEL);

        app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ apptSubject + ")" + " " + "content:(" + apptContent +")</query>"
			+	"</SearchRequest>");

        // Verify appointment modified date and time
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ apptId +"'/>");
		ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetAppointmentResponse//mail:inv[2]//mail:s", "u"), String.valueOf(startUTC.addHours(2).toTimeZone(tz).toMillis()), "Verify appointment modified start time");
	    ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetAppointmentResponse//mail:inv[2]//mail:e", "u"), String.valueOf(endUTC.addHours(2).toTimeZone(tz).toMillis()), "Verify appointment modified end time");
	    ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetAppointmentResponse//mail:inv//mail:exceptId", "d"), getExceptionDate(), "Verify appointment modified start time");

	    ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ apptSubject + ")" + " " + "content:(" + apptContent +")</query>"
			+	"</SearchRequest>");

        // Verify appointment modified date and time in attendee's calendar
	    ZimbraAccount.AccountA().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ apptId +"'/>");
		ZAssert.assertEquals(ZimbraAccount.AccountA().soapSelectValue("//mail:GetAppointmentResponse//mail:inv//mail:s", "u"), String.valueOf(startUTC.addHours(2).toTimeZone(tz).toMillis()), "Verify appointment modified start time");
	    ZAssert.assertEquals(ZimbraAccount.AccountA().soapSelectValue("//mail:GetAppointmentResponse//mail:inv//mail:e", "u"), String.valueOf(endUTC.addHours(2).toTimeZone(tz).toMillis()), "Verify appointment modified end time");
	    ZAssert.assertEquals(ZimbraAccount.AccountA().soapSelectValue("//mail:GetAppointmentResponse//mail:inv//mail:exceptId", "d"), getExceptionDate(), "Verify appointment modified start time");
	    
	    app.zPageCalendar.zToolbarPressButton(Button.B_BACK);
	    
	}

	public String getTomorrowsDate() {
		Date tomorrow = calendarWeekDayUTC.getTime();
		DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
	    return dateFormat.format(tomorrow);
	}

	public String getExceptionDate() {
		Date tomorrow = calendarWeekDayUTC.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'" + "010000");
	    return dateFormat.format(tomorrow);
	}

}
