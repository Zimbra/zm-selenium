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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.month.allday;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class ModifyMultipleDayAppointment extends AjaxCommonTest {

	public ModifyMultipleDayAppointment() {
		logger.info("New " + ModifyMultipleDayAppointment.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L; {
				put("zimbraPrefCalendarInitialView", "month");
			}
		};
	}


	@Test( description = "Modify multiple-day all-day appointment (Date) and verify its display in month view.",
			groups = { "functional", "L3" })

	public void ModifyAllDayAppointment_01() throws HarnessException {

		// Appointment subject
		String subject = ConfigProperties.getUniqueString();
		int noOfDays = 5;

		// Start Date is 2 days ahead if current date is less than or equal to 21 else 8 days behind
		Calendar now = Calendar.getInstance();
		if(now.get(Calendar.DAY_OF_MONTH) <= 21) {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 2 );
		} else {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 8 );
		}

		//Attendees
		List<ZimbraAccount> attendees = Arrays.asList(ZimbraAccount.Account5());

		// Create multiple day spanning all-day appointments for 5 days
		AppointmentItem.createAppointmentAllDay(
				app.zGetActiveAccount(),
				now,
				noOfDays,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				attendees);

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Verify appointment exists in current view
		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(subject), "Verify appointment displayed in current view");

		// Open appointment & modify start date, end date and subject, and send it.
		FormApptNew form = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, subject);
		ZAssert.assertNotNull(form, "Verify the appointment form opens correctly");

		// Increasing the start date by 1 day
		now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1 );
		form.zSelectStartDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

		// Decreasing the duration of appointment by 1 day
		form.zSelectEndDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH) + noOfDays - 2));

		form.zFillField(Field.Subject, subject+"_new");
		form.zToolbarPressButton(Button.B_SEND);

		// Verify that multi-day appointments are displayed correctly in month view
		boolean displayed = app.zPageCalendar.zVerifyMultidayAllDayAppointmentInMonthView(now, 4, subject+"_new");
		ZAssert.assertTrue(displayed, "Multi-day all-day appointments are not created and displayed correctly in month view");
	}


	@Test( description = "Modify multiple-day all-day appointment (Add attendee) and verify its display in month view.",
			groups = { "functional", "L3" })

	public void ModifyAllDayAppointment_02() throws HarnessException {

		// Appointment subject
		String subject = ConfigProperties.getUniqueString();
		int noOfDays = 5;

		// Start Date is 2 days ahead if current date is less than or equal to 21 else 8 days behind
		Calendar now = Calendar.getInstance();
		if(now.get(Calendar.DAY_OF_MONTH) <= 21) {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 2 );
		} else {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 8 );
		}

		// Create multiple day spanning all-day appointments for 5 days
		AppointmentItem.createAppointmentAllDay(
				app.zGetActiveAccount(),
				now,
				noOfDays,
				subject,
				"content" + ConfigProperties.getUniqueString(),
				"location" + ConfigProperties.getUniqueString(),
				null);

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Verify appointment exists in current view
		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(subject), "Verify appointment displayed in current view");

		// Open appointment & modify start date, end date and subject, and send it.
		FormApptNew form = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, subject);
		ZAssert.assertNotNull(form, "Verify the appointment form opens correctly");

		form.zToolbarPressButton(Button.B_TO);
        DialogFindAttendees dialogFindAttendees = (DialogFindAttendees) new DialogFindAttendees(app, app.zPageCalendar);

        // Type attendee email address in search box & perform search
        dialogFindAttendees.zType(Locators.ContactPickerSerachField, ZimbraAccount.AccountA().EmailAddress);
        dialogFindAttendees.zClickButton(Button.B_SEARCH);
        dialogFindAttendees.zWaitForBusyOverlay();

        // Add the attendee from the search result
        dialogFindAttendees.zClick(Locators.ContactPickerFirstContact);
        dialogFindAttendees.zClickButton(Button.B_CHOOSE_CONTACT_FROM_PICKER);
        dialogFindAttendees.zWaitForBusyOverlay();
        dialogFindAttendees.zClickButton(Button.B_OK);

        // Save and send the appointment
		form.zToolbarPressButton(Button.B_SAVE);
		form.zToolbarPressButton(Button.B_SEND);

		// Login to attendee's account and go to calendar
		app.zPageLogin.zLogin(ZimbraAccount.AccountA());
		startingPage.zNavigateTo();

		// Go to month view
		app.zPageCalendar.zToolbarPressButton(Button.B_MONTH_VIEW);

		// Verify that multi-day appointments are displayed correctly in month view
		boolean displayed = app.zPageCalendar.zVerifyMultidayAllDayAppointmentInMonthView(now, noOfDays, subject);
		ZAssert.assertTrue(displayed, "Multi-day all-day appointments are not created and displayed correctly in month view");
		ZimbraAccount.ResetAccountZCS();
	}
}