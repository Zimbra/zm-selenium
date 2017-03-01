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

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;


public class MultipleDayAppointment extends AjaxCommonTest {


	@SuppressWarnings("serial")
	public MultipleDayAppointment() {
		logger.info("New "+ MultipleDayAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		// Make sure we are using an account with month view
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefCalendarInitialView", "month");
		}};
	}

	@Bugs(ids = "107583, 69132, ZCS-725")
	@Test( description = "Verify the display of a multi-day all-day appointment in the month view",
	groups = { "functional", "L2" })
	public void DisplayMultipleDayAppointment_01() throws HarnessException {

		// Appointment subject
		String subject = ConfigProperties.getUniqueString();

		// Start Date is 2 days ahead if current date is greater than or equal to 21 else 8 days behind
		Calendar now = Calendar.getInstance();
		if(now.get(Calendar.DAY_OF_MONTH) <= 21) {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 2 );
		} else {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 8 );
		}

		//Attendees
		List<ZimbraAccount> attendees = Arrays.asList(ZimbraAccount.Account1(), ZimbraAccount.Account2(), ZimbraAccount.Account3());

		//Create multiple day spanning all-day appointments for 5 days
		AppointmentItem.createAppointmentAllDay(
				app.zGetActiveAccount(),
				now,
				5,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				attendees);

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		//Verify that multi-day appointments are displayed correctly in month view 
		boolean b =app.zPageCalendar.zVerifyMultidayAllDayAppointmentInMonthView(now,5,subject);
		ZAssert.assertTrue(b, "Multi-day all-day appointments are not created and displayed correctlly in month view");
	}
}
