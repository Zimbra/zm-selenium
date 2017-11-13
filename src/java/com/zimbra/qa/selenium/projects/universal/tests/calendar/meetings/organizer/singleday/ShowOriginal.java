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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.meetings.organizer.singleday;

import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.SeparateWindow;

public class ShowOriginal extends CalendarWorkWeekTest {

	public ShowOriginal() {
		logger.info("New "+ ShowOriginal.class.getCanonicalName());

	    super.startingPage =  app.zPageCalendar;
	    
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
			{
			    put("zimbraPrefCalendarInitialView", "workWeek");
			}
		};
	}
	
	@Test (description = "check context menu for Show Original option and check of the its displayed", 
			groups = { "smoke", "L1"})
	
	public void ShowOriginal_01() throws HarnessException {

		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		String apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
	                     "<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
		                     "<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
		                     "<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
		                     "<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
		                     "<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>" + 
	                     "</inv>" +
	                     "<e a='"+ apptAttendee1 +"' t='t'/>" +
	                     "<mp content-type='text/plain'>" +
	                     	"<content>"+ apptBody +"</content>" +
	                     "</mp>" +
	                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // Open appointment & click context menu 'Show Original' Option
        SeparateWindow window = (SeparateWindow)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK,Button.O_SHOW_ORIGINAL_MENU, apptSubject);
        try {
        	window.zSetWindowName();
			window.zWaitForActive();
			SleepUtil.sleepMedium();
						
			// Verify show original window content
			String body = window.sGetBodyText();
			String attendeeHeader = "ATTENDEE;CN=2;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:mailto:";
			ZAssert.assertStringContains(body, apptSubject, "Verify subject in Show original view");
			ZAssert.assertStringContains(body, apptBody, "Verify content in Show original view");
			ZAssert.assertStringContains(body, "BEGIN:VCALENDAR", "Verify Begin Header in Show original view");
			ZAssert.assertStringContains(body, "END:VCALENDAR", "Verify Begin Header in Show original view");
			ZAssert.assertStringContains(body, attendeeHeader,"Verify Attendee is present in Show original view");
			
        } finally {
			app.zPageMain.zCloseWindow(window, app);
		}
	}
}