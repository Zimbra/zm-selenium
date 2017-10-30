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
package com.zimbra.qa.selenium.projects.universal.tests.search.search;

import java.util.Calendar;
import java.util.List;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.PageCalendar.Locators;

public class SearchAppointment extends UniversalCommonTest {

	int pollIntervalSeconds = 60;
	
	public SearchAppointment() {
		logger.info("New "+ SearchAppointment.class.getCanonicalName());
		
	}
	
	@Test( description = "Search for an appointment by subject",
			groups = { "functional","L2" })
	
	public void SearchAppointment_01() throws HarnessException {
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0, 0);
		
		// Create a meeting
		String subject = "appointment" + ConfigProperties.getUniqueString();
		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				startDate,
				120,
				null,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);

		// Refresh the pane
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Verify appointment exists on the server 
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject + ")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		
		// Search for the appointment
		app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_APPOINTMENTS);
		app.zPageSearch.zAddSearchQuery("subject:("+ subject +")");
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);
		
		// Get all the appointment in the default calendar
		List<AppointmentItem> items = app.zPageCalendar.zListGetAppointments();
		ZAssert.assertNotNull(items, "Get the list of appointments");
		
		// Verify the appointment is in the search result on UI
		AppointmentItem found = null;
		for(AppointmentItem item : items) {
			if ( item.getGSubject().contains(subject) ) {
				found = item;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the appt list exists");
	}

	@Bugs(ids = "49386")
	@Test( description = "Script error when searching in calendar list view", 
			groups = { "functional","L2" })
	
	public void SearchAppointment_02() throws HarnessException {
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0, 0);
		
		// Create a meeting
		String subject = "appointment" + ConfigProperties.getUniqueString();
		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				startDate,
				120,
				null,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);

		// Refresh the UI (work around due to active dialogs found when running as a second test and directly using app.zPageCalendar.zNavigateTo();)
		app.zPageMain.sRefresh();
		app.zPageCalendar.zNavigateTo();
		
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_VIEW_MENU, Button.O_VIEW_LIST_SUB_MENU, "List");
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent(Locators.CalendarViewListCSS), "Changed to list view");
        ZAssert.assertTrue(app.zPageCalendar.zIsAppointmentVisible(subject), "Verify the appt list exists");
		
		// Verify appointment exists on the server 
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject + ")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		
		// Search for the appointment
		app.zPageSearch.zAddSearchQuery("subject:("+ subject +")");
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);
		ZAssert.assertTrue(app.zPageCalendar.zIsAppointmentVisible(subject), "Verify the appt list exists");

	}

}
