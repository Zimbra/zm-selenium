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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.suggestions;

import java.util.Calendar;
import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class SuggestALocation extends CalendarWorkWeekTest {	
	
	public SuggestALocation() {
		logger.info("New "+ SuggestALocation.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "73966,88287,96567")
	@Test( description = "Suggest a free location while creating appointment",
			groups = { "smoke" })
	public void SuggestALocation_01() throws HarnessException {
		
		// Create a meeting
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);

		String tz = ZTimeZone.TimeZoneEST.getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		String apptLocation = location.EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 7, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee + "' d='2'/>" +
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
        
        // Select the location from suggested location pane
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm.zToolbarPressButton(Button.B_SUGGESTALOCATION);
        SleepUtil.sleepLong(); //specific sleep for suggested location pane because it searches free resources dynamically
        apptForm.zPressButton(Button.B_SUGGESTEDLOCATION, apptLocation);
        
        // Verify 'free' location appears in suggested locations pane
        ZAssert.assertEquals(apptForm.zVerifyLocation(apptLocation), true, "Verify 'free' location appear in suggested locations pane");
        apptForm.zSubmit();
        
        // Verify location in the appointment
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getLocation(), apptLocation, "Location: Verify the appointment data");
		
		// Verify location status shows as ACCEPTED
		String locationStatus = zWaitTillSoapResponse(app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst"), "AC");
		ZAssert.assertEquals(locationStatus, "AC", "Verify location status shows accepted");
		
		// Verify 'busy' location doesn't appear in suggested locations pane
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zToolbarPressButton(Button.B_SUGGESTALOCATION); 
        ZAssert.assertEquals(apptForm.zVerifyLocation(apptLocation), false, "Verify 'busy' location doesn't appear in suggested locations pane");
		
	}
	
}
