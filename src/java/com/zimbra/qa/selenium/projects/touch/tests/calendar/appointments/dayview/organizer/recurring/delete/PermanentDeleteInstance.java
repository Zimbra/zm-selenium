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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.recurring.delete;

import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.*;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;

public class PermanentDeleteInstance extends CalendarWorkWeekTest {

	public PermanentDeleteInstance() {
		logger.info("New "+ PermanentDeleteInstance.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "day");
		}};
	}
	
	@Bugs(ids = "95735")
	@Test( description = "Delete an instance of recurring invite", 
			groups = { "functional" } )
	
	public void PermanentDeleteInstance_01() throws HarnessException {
		
		// Appointment data
		String tz, apptSubject, apptBody, apptAttendee1;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = "subject" + ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 1, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 2, 0, 0);
		
		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		app.zPageCalendar.zRefresh();
        
		// Delete instance and verify corresponding UI
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.O_OPEN_INSTANCE_MENU, apptSubject);
        app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
        app.zPageMail.zClickButton(Button.B_YES);
        
        app.zPageCalendar.zSelectFolder("Trash");
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.O_OPEN_INSTANCE_MENU, apptSubject);
        app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
        app.zPageMail.zClickButton(Button.B_YES);

 		app.zGetActiveAccount().soapSend(
 				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(7).toMillis() +"'>"
 			+		"<query>is:anywhere "+ apptSubject +"</query>"
 			+	"</SearchRequest>");

 		Element[] appts = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
 		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist");
 		
 		ZimbraAccount.AccountA().soapSend(
 				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(7).toMillis() +"'>"
 			+		"<query>is:anywhere "+ apptSubject +"</query>"
 			+	"</SearchRequest>");

 		appts = ZimbraAccount.AccountA().soapSelectNodes("//mail:appt");
 		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist");

        app.zPageCalendar.zSelectFolder("Trash");
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is removed from Trash folder");
        
	}
	
}