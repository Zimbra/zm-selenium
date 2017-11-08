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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.util.Calendar;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;;

public class CreateMeetingBySelectAddresses extends AjaxCommonTest {

	public CreateMeetingBySelectAddresses() {
		logger.info("New "+ CreateMeetingBySelectAddresses.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	}


	@Test (description = "Compose appointment by selecting attendees using 'Select Addresses' dialog and send the appointment",
			groups = { "functional", "L2" })

	public void CreateMeetingBySelectAttendees_01() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();

		String apptSubject, apptAttendee1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;

		apptContent = ConfigProperties.getUniqueString();
		appt.setSubject(apptSubject);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));
		appt.setContent(apptContent);

		// Compose appointment and select attendees using "Select Addresses" dialog
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
        apptForm.zToolbarPressButton(Button.B_TO);
        DialogFindAttendees dialogFindAttendees = (DialogFindAttendees) new DialogFindAttendees(app, app.zPageCalendar);

        // Search any attendee and select it
        AppointmentItem apptSearchForm = new AppointmentItem();
        apptSearchForm.setAttendeeName(apptAttendee1);
        dialogFindAttendees.zFill(apptSearchForm);
        dialogFindAttendees.zPressButton(Button.B_SEARCH);
        dialogFindAttendees.zPressButton(Button.B_SELECT_FIRST_CONTACT);
        dialogFindAttendees.zPressButton(Button.B_CHOOSE_CONTACT_FROM_PICKER);
        dialogFindAttendees.zPressButton(Button.B_OK);
        apptForm.zSubmit();

        // Verify attendee1 receives meeting invitation message
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the attendee1's inbox");

		// Verify attendee1 present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");

		// Verify appointment is present in attendee1's calendar
		AppointmentItem addeddAttendee = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(addeddAttendee, "Verify meeting invite is present in attendee1's calendar");

	}
}
