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
package com.zimbra.qa.selenium.projects.ajax.tests.search.search;

import java.util.Calendar;
import java.util.List;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;


public class SearchAppointment extends CalendarWorkWeekTest {

	int pollIntervalSeconds = 60;
	
	public SearchAppointment() {
		logger.info("New "+ SearchAppointment.class.getCanonicalName());
		
	}
	
	@Test(	description = "Search for an appointment by subject",
			groups = { "functional" })
	public void SearchAppointment_01() throws HarnessException {
		
		// Create a meeting
		String subject = "appointment" + ZimbraSeleniumProperties.getUniqueString();
		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				Calendar.getInstance(),
				120,
				null,
				subject,
				"content" + ZimbraSeleniumProperties.getUniqueString(),
				"location" + ZimbraSeleniumProperties.getUniqueString(),
				null);

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		
		// Verify appointment exists on the server 
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject + ")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		
		// Search for the appointment
		app.zPageSearch.zAddSearchQuery("subject:("+ subject +")");
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);
		
		// Get all the appointment in the default calendar
		List<AppointmentItem> items = app.zPageCalendar.zListGetAppointments();
		ZAssert.assertNotNull(items, "Get the list of appointments");
		
		// Verify the appointment is in the search result on UI
		AppointmentItem found = null;
		for(AppointmentItem item : items) {
			if ( item.getSubject().contains(subject) ) {
				found = item;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the appt list exists");


	}


}
