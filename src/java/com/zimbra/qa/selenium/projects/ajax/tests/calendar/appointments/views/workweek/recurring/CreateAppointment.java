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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.workweek.recurring;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class CreateAppointment extends AjaxCommonTest {

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Create basic recurring appointment (every day)",
			groups = { "smoke", "L0" } )

	public void CreateRecurringAppointment_01() throws HarnessException {

		//-- Data Setup

		// Appointment data
		ZDate startTime, endTime;
		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();

		startTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		endTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0);

		appt.setSubject(ConfigProperties.getUniqueString());
		appt.setAttendees(ZimbraAccount.AccountA().EmailAddress);
		appt.setContent("content" + ConfigProperties.getUniqueString());
		appt.setStartTime(startTime);
		appt.setEndTime(endTime);
		appt.setRecurring("EVERYDAY", "");

		//-- GUI steps

		// Create series appointment
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();

		//-- Data Verification

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:s", "d"), startTime.toYYYYMMDDTHHMMSS(), "Verify recurring appointment start time and date");
		ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:e", "d"), endTime.toYYYYMMDDTHHMMSS(), "Verify recurring appointment end time and date");

//		Move this verification to GetAppointment or ViewAppointment
//
//		// Open instance and verify corresponding UI
//		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
//        app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, appt.getSubject());
//        DialogOpenRecurringItem dlgConfirm = new DialogOpenRecurringItem(DialogOpenRecurringItem.Confirmation.OPENRECURRINGITEM, app, ((AppAjaxClient) app).zPageCalendar);
//		dlgConfirm.zPressButton(Button.B_OK);
//		ZAssert.assertEquals(app.zPageCalendar.sIsElementPresent(Locators.RepeatDisabled), true, "Verify 'Every Week' menu item is disabled");
//		SleepUtil.sleepMedium();
//		app.zPageCalendar.zToolbarPressButton(Button.B_CLOSE);
//
//		// Open entire series and verify corresponding UI
//		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, appt.getSubject());
//        dlgConfirm = new DialogOpenRecurringItem(DialogOpenRecurringItem.Confirmation.OPENRECURRINGITEM, app, ((AppAjaxClient) app).zPageCalendar);
//        app.zPageCalendar.zCheckRadioButton(Button.B_OPEN_THE_SERIES);
//		dlgConfirm.zPressButton(Button.B_OK);
//		ZAssert.assertEquals(app.zPageCalendar.sIsElementPresent(Locators.RepeatEnabled), true, "Verify 'Every Week' menu item is enabled");
//		SleepUtil.sleepMedium();
//		app.zPageCalendar.zToolbarPressButton(Button.B_CLOSE);
	}
}