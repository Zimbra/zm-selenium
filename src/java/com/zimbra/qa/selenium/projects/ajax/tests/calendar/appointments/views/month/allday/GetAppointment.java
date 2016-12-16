/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.month.allday;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;


public class GetAppointment extends AjaxCommonTest {

	
	@SuppressWarnings("serial")
	public GetAppointment() {
		logger.info("New "+ GetAppointment.class.getCanonicalName());
		
		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		// Make sure we are using an account with month view
		super.startingAccountPreferences = new HashMap<String, String>() {{
		    put("zimbraPrefCalendarInitialView", "month");
		}};


	}
	
	@Bugs(ids = "69132")
	@Test( description = "View a basic appointment in the month view",
			groups = { "functional", "L1" })
	public void GetAppointment_01() throws HarnessException {
		
		// Create an appointment on the server
		String subject = ConfigProperties.getUniqueString();

		AppointmentItem.createAppointmentAllDay(
				app.zGetActiveAccount(),
				Calendar.getInstance(),
				1,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);
		
		

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Get the list of appointments in the current view
		List<AppointmentItem> items = app.zPageCalendar.zListGetAppointments();
		ZAssert.assertNotNull(items, "Get the list of appointments");
		
		// Verify the appointment is in the view
		AppointmentItem found = null;
		for(AppointmentItem item : items) {
			if ( item.getSubject().equals(subject) ) {
				found = item;
				break;
			}
		}
		
		ZAssert.assertNotNull(found, "Verify the new appointment appears in the view");
	    
	}

	@Bugs(ids = "69132")
	@Test( description = "View a multi-day appointment in the month view",
			groups = { "functional", "L2" })
	public void GetAppointment_02() throws HarnessException {
		
		// Create an appointment on the server
		String subject = ConfigProperties.getUniqueString();

		AppointmentItem.createAppointmentAllDay(
				app.zGetActiveAccount(),
				Calendar.getInstance(),
				2,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);
		
		

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Verify appointment displayed in month view
		ZAssert.assertEquals(app.zPageCalendar.sIsElementPresent(app.zPageCalendar.zGetAllDayApptLocator(subject)), true, "Verify appointment present in month view");
		
		// Below code works fine for single-day all day but for multi-day all day appointment it fails because it finds empty cell
		
		//List<AppointmentItem> items = app.zPageCalendar.zListGetAppointments();
		//ZAssert.assertNotNull(items, "Get the list of appointments");
		
		// Verify the appointment is in the view
		//AppointmentItem found = null;
		//for(AppointmentItem item : items) {
		//	if ( item.getSubject().equals(subject) ) {
		//		found = item;
		//		break;
		//	}
		//}
		
		//ZAssert.assertNotNull(found, "Verify the new appointment appears in the view");
	    
	}


}
