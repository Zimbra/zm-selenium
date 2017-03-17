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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.month.recurring;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;

public class RecurringWeekly extends CalendarWorkWeekTest {

	public RecurringWeekly() {
		logger.info("New "+ RecurringWeekly.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
		// Make sure we are using an account with month view
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 3028486541122343959L;

			{
				put("zimbraPrefCalendarInitialView", "month");
			}};
	}
	
	@Bugs(ids = "103157")
	@Test( description = "Create Weekly recurring appointment and verify that start date and other details appear correct after opening",
			groups = { "functional", "L2" })
			
	public void RecurringMonthly_01() throws HarnessException, ParseException {
		
		// ------------------------ Test data ------------------------------------

		Calendar now = this.calendarWeekDayUTC;
		String tz = ZTimeZone.TimeZoneEST.getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		int noOfApptInSeries = 6;
		ZDate temp = startUTC;
		
		//Making sure that appointment is not created on the first day of month as the date cell has month number mentioned in it.
		for(int i=1; i <= noOfApptInSeries; i++) {
			if(temp.toDD().equals("01")) {
				startUTC.addDays(1);
				break;
			}
			temp = temp.addDays(7);  //Incrementing date by a week 
		}
		
		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account2().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='WEE'>" +
										"<interval ival='1'/>" +
										"<count num='"+ noOfApptInSeries +"'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.Account2().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		

		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		
		boolean b = app.zPageCalendar.zVerifyWeeklyAppointmentInMonthView(startUTC, noOfApptInSeries, apptSubject);
		ZAssert.assertTrue(b, "Weekly recurring appointments are not displayed correctly!");	
		
	}

}
