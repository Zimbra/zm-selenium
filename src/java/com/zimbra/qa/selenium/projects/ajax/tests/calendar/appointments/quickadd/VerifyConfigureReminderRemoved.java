/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.quickadd;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.QuickAddAppointment;

public class VerifyConfigureReminderRemoved extends AjaxCommonTest {

	public VerifyConfigureReminderRemoved() {
		logger.info("New "+ VerifyConfigureReminderRemoved.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs(ids = "97410")
	@Test (description = "Verify 'configure' link from quick add dialog for calendar",
			groups = { "smoke", "L1" } )

	public void VerifyConfigureReminderRemoved_01() throws HarnessException {

		// Create appointment
		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();
		appt.setSubject(ConfigProperties.getUniqueString());
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));

		// Quick add appointment dialog
		QuickAddAppointment quickAddAppt = new QuickAddAppointment(app) ;
		quickAddAppt.zNewAppointment();
		quickAddAppt.zVerifyConfigureReminderLink(false);
	}
}