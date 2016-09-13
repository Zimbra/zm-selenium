/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.folders;

import java.util.Calendar;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.PageCalendar.Locators;

public class CheckFreeBusyURLLink extends CalendarWorkWeekTest {

	public CheckFreeBusyURLLink() {
		logger.info("New "+ CheckFreeBusyURLLink.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}

	@DataProvider(name = "DataProviderFB")
	public Object[][] DataProviderFB() {
		return new Object[][] {
				new Object[] { Locators.DayViewOnFBLink , "view=day" , "Day"},
				new Object[] { Locators.WorkWeekViewOnFBLink , "view=workWeek" ,"Work Week"},
				new Object[] { Locators.WeekViewOnFBLink, "view=week" ,"Week"},
				new Object[] { Locators.MonthViewOnFBLink, "view=month" ,"Month"},
				new Object[] { Locators.TodayViewOnFBLink,"view=month&fmt=freebusy" ,"Today"},
		};
	}
	
	@Bugs(ids = "60277")
	@Test( description = "Verify free busy link is accessible and no error is thrown", groups = { "functional" }, dataProvider = "DataProviderFB")
			
	public void CheckFreeBusyURLLink_01(String locator , String viewName, String label) throws HarnessException {
		
		// Creating object for appointment data
		String tz, apptSubject, apptBody, apptAttendee;
		tz = ZTimeZone.TimeZoneEST.getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
        app.zGetActiveAccount().soapSend(
              "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                   "<m>"+
                   "<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                   "<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                   "<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                   "<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                   "<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee + "' d='2'/>" + 
                   "</inv>" +
                   "<mp content-type='text/plain'>" +
                   "<content>"+ apptBody +"</content>" +
                   "</mp>" +
                   "<su>"+ apptSubject +"</su>" +
                   "</m>" +
             "</CreateAppointmentRequest>");
        
        String Organizer =  app.zGetActiveAccount().EmailAddress;

        // Logout from the organizer to check if the FB view is accessible 
        app.zPageMain.zLogout();
        
        // Reload the application, with fmt=freebusy query parameter
        ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
        uri.setURI(uri.toString() + "/home/"+ Organizer);
        uri.addQuery("fmt", "freebusy");
        logger.info("URI is here"+ uri.toString());
        app.zPageCalendar.sOpen(uri.toString());
        SleepUtil.sleepMedium();
        
        // Verify if all views on Free busy view are clickable and visible 
        app.zPageCalendar.sClickAt(locator, "");
        SleepUtil.sleepMedium();
		ZAssert.assertStringContains(app.zPageCalendar.sGetLocation(), viewName,  "URL for "+ label +" view is open");
		
		if (label != "Day") {
			// Verify if all views show free busy status 
			String body = app.zPageCalendar.sGetBodyText();
			ZAssert.assertStringContains( body, "Busy" , "Verify free busy view is visible and no error is thrown");
		}

	}
	
}
