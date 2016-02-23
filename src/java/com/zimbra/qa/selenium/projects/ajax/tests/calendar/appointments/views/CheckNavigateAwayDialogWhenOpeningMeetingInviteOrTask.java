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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views;

import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;

public class CheckNavigateAwayDialogWhenOpeningMeetingInviteOrTask extends CalendarWorkWeekTest {

	public CheckNavigateAwayDialogWhenOpeningMeetingInviteOrTask() {
		logger.info("New " + CheckNavigateAwayDialogWhenOpeningMeetingInviteOrTask.class.getCanonicalName());
		
		//this.startingAccountPreferences.put("zimbraPrefWarnOnExit", "TRUE");

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "workWeek");
		}};
	}
	
	@Bugs(ids="50432")
	@Test( description = "Bug 50432 - Unwanted navigate away dialog after opening appointment/task", groups = { "functional" })
	
	public void CheckNavigateAwayDialogWhenOpeningMeetingInviteOrTask_01() throws HarnessException {
/*		
		// Modify the test account
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
			+		"<a n='zimbraPrefWarnOnExit'>TRUE</a>"
			+	"</ModifyAccountRequest>");
*/
		// Logout and login to pick up the changes
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();

		// Creating object for appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.TimeZoneEST.getID();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		
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
                   "</inv>" +
                   "<mp content-type='text/plain'>" +
                   "<content>"+ apptBody +"</content>" +
                   "</mp>" +
                   "<su>"+ apptSubject +"</su>" +
                   "</m>" +
             "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");

        // Open appointment & close it
        app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		// if logout stucks then assume that browser dialog appeared
		app.zPageMain.zLogout();
	}
}