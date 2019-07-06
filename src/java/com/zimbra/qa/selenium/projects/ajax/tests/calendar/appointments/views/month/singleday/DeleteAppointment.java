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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.month.singleday;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.*;

public class DeleteAppointment extends AjaxCore {

	public DeleteAppointment() {
		logger.info("New "+ DeleteAppointment.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L; {
				put("zimbraPrefCalendarInitialView", "month");
			}
		};
	}


	@Bugs (ids = "69132")
	@Test (description = "Delete a basic appointment in the month view",
			groups = { "sanity" })

	public void DeleteAppointment_01() throws HarnessException {

		// Appointment data
		String subject = ConfigProperties.getUniqueString();
		ZDate startDate = new ZDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0, 0);

		AppointmentItem.createAppointmentSingleDay(
				app.zGetActiveAccount(),
				startDate,
				120,
				null,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Select the appointment
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, subject);

		// Click the "delete" button
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);

		// Send the notification immediately
		dialog.zPressButton(Button.B_YES);

		// Verify that the appointment is in the trash now
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash);
		AppointmentItem deleted = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +") is:anywhere");
		ZAssert.assertNotNull(deleted, "Verify the deleted appointment exists");
		ZAssert.assertEquals(deleted.getFolder(), trash.getId(), "Verify the deleted appointment exists");
	}
}