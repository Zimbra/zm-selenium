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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday;

import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.SeparateWindow;

public class ShowOriginal extends CalendarWorkWeekTest {

	public ShowOriginal() {
		logger.info("New "+ ShowOriginal.class.getCanonicalName());

	    super.startingPage =  app.zPageCalendar;
	    

		// Make sure we are using an account with work week view
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "workWeek");
		}};
	}
	
	
	@Test( description = "check context menu for Show Original option and check of the its displayed", 
			groups = { "functional" })
	
	public void ShowOriginal_01() throws HarnessException {
		
		organizerTest = false;
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();

		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");     
		
		// Switch to work week view
        app.zPageCalendar.zToolbarPressPulldown(Button.B_LISTVIEW, Button.O_LISTVIEW_WORKWEEK);
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");

        // Open appointment & click context menu 'Show Original' Option
        SeparateWindow window = (SeparateWindow)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK,Button.O_SHOW_ORIGINAL_MENU, apptSubject);
		try {
			window.zWaitForActive();
			SleepUtil.sleepMedium();
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			//Verify content in Print view.
			String body = window.sGetBodyText();
			ZAssert.assertStringContains(body, apptSubject, "Verify subject in Show original view");
			ZAssert.assertStringContains(body, apptBody, "Verify content in Show original view");
			ZAssert.assertStringContains(body, "BEGIN:VCALENDAR", "Verify Begin Header in Show original view");
			ZAssert.assertStringContains(body, "END:VCALENDAR", "Verify Begin Header in Show original view");
			ZAssert.assertStringContains(body, "ORGANIZER:mailto:"+ZimbraAccount.AccountA().EmailAddress,"Verify organizer is present in Show original view");
	    
		} finally {
			app.zPageMain.zCloseWindow(window, app);
		}
        
	}
	
}
