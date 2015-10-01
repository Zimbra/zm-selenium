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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.recurring.delete;

import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.*;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;

public class DeleteInstance extends CalendarWorkWeekTest {

	public DeleteInstance() {
		logger.info("New "+ DeleteInstance.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "day");
		}};
	}
	
	@Bugs(ids = "95735")
	@Test (	description = "Delete instance of recurring invite", 
			groups = { "functional" } )
	
	public void DeleteInstance_01() throws HarnessException {
		
		// Appointment data
		String tz, apptSubject, apptContent, apptAttendee1;
		tz = ZTimeZone.TimeZoneEST.getID();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 2, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 3, 0, 0);
		
		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='YEA'>" +
										"<interval ival='5'/>" +
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
		app.zPageCalendar.zRefresh();
        
		// Delete instance and verify corresponding UI
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.O_OPEN_INSTANCE_MENU, apptSubject);
        app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
        app.zPageMail.zClickButton(Button.B_YES);
        SleepUtil.sleepMedium();

		// Verify appointment is removed
        app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ apptSubject +"</query>"
			+	"</SearchRequest>");
        
        Element[] appts = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist ");
		
		// Verify appointment is removed
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ apptSubject +"</query>"
			+	"</SearchRequest>");
        
        appts = ZimbraAccount.AccountA().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist ");
        
        // Verify appointment is moved to Trash folder
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is moved to Trash folder");
		
		// Go to Trash and verify appointment is there
		app.zPageCalendar.zSelectFolder("Trash");
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), true, "Verify appointment is moved to Trash folder");
		
		// Verify cancelled message received to attendee
        MailItem canceledApptMsg = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + (char)34 + "Cancelled " + apptSubject + (char)34 + ")");
		ZAssert.assertNotNull(canceledApptMsg, "Verify cancelled message received to attendee");
	}
	
}