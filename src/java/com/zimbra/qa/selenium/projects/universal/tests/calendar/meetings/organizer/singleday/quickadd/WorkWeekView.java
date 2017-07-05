/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.meetings.organizer.singleday.quickadd;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.QuickAddAppointment;

public class WorkWeekView extends CalendarWorkWeekTest {

	public WorkWeekView() {
		logger.info("New "+ WorkWeekView.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		// Make sure we are using an account with work week view
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "workWeek");
		}};
	}
	
	@Test( description = "Verify quick add dialog opens after hitting new appointment in work week view",
			groups = { "functional", "L2" } )
	
	public void WorkWeekView_01() throws HarnessException {
			
		// Verify quick add dialog opened
		QuickAddAppointment quickAddAppt = new QuickAddAppointment(app) ;
		quickAddAppt.zNewAppointment();
		quickAddAppt.zVerifyQuickAddDialog(true);
		
		/* Meeting invite full verification is already covered by meetings.organizer.singleday.minicalendar testcases so
		 not validating over here and avoiding duplication */
		
		/* If we find out something else in the future then will add verification accordingly */
	}

}
