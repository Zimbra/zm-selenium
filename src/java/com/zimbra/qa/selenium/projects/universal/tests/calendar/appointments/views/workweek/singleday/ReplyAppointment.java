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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.appointments.views.workweek.singleday;

import java.util.Calendar;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.PageCalendar.*;

public class ReplyAppointment extends CalendarWorkWeekTest {	
	
	public ReplyAppointment() {
		logger.info("New "+ ReplyAppointment.class.getCanonicalName());
		
	}
	
	@DataProvider(name = "DataProviderReply")
	public Object[][] DataProviderReply() {
		return new Object[][] {
				new Object[] { Locators.ReplyMenu },
				new Object[] { Locators.ReplyToAllMenu },
		};
	}
	
	@Test( description = "Verify Reply & ReplyAll context menu option for saved appt",
			groups = { "functional", "L2" },
			dataProvider = "DataProviderReply")
			
	public void ReplyAppointment_01(String menuName) throws HarnessException {
		
		// Creating object for meeting data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.TimeZoneEST.getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     "<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     "<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     "<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     "</inv>" +
                     "<mp content-type='text/plain'>" +
                     "<content>"+ apptBody +"</content>" +
                     "</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
        
        // Select the appointment
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        
        // Right Click to appt -> check Reply and ReplyAll context menu       
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, apptSubject);
        ZAssert.assertEquals(app.zPageCalendar.zIsElementDisabled(menuName), false, "Verify Reply and ReplyAll menu remains disabled");

	}
	
	
}
