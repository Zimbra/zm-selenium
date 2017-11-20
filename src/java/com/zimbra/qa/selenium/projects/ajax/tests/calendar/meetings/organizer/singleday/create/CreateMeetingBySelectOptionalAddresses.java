/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogFindAttendees;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;

public class CreateMeetingBySelectOptionalAddresses extends AjaxCore {

	public CreateMeetingBySelectOptionalAddresses() {
		logger.info("New "+ CreateMeetingBySelectOptionalAddresses.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	}


	@Test (description = "Compose appointment by selecting optional attendees using 'Select Addresses' dialog and send the appointment",
			groups = { "functional", "L2" })

	public void CreateMeetingBySelectOptionalAttendees_01() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();

		String apptSubject, apptOptionalAttendee, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptOptionalAttendee = ZimbraAccount.AccountA().EmailAddress;

		apptContent = ConfigProperties.getUniqueString();
		appt.setSubject(apptSubject);
		appt.setOptional(apptOptionalAttendee);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setContent(apptContent);

		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
        apptForm.zToolbarPressButton(Button.B_OPTIONAL);
        DialogFindAttendees dialogFindAttendees = (DialogFindAttendees) new DialogFindAttendees(app, app.zPageCalendar);

        // Type optional attendee name in search box & perform search
        AppointmentItem apptSearchForm1 = new AppointmentItem();
        apptSearchForm1.setAttendeeName(apptOptionalAttendee);
        dialogFindAttendees.zFill(apptSearchForm1);

        // Choose the contact and select it
        dialogFindAttendees.zPressButton(Button.B_SEARCH);
        SleepUtil.sleepSmall();
        dialogFindAttendees.zPressButton(Button.B_SELECT_FIRST_CONTACT);
        dialogFindAttendees.zPressButton(Button.B_CHOOSE_CONTACT_FROM_PICKER);
        dialogFindAttendees.zPressButton(Button.B_OK);
        apptForm.zSubmit();

        // Verify new invitation appears in the optional attendee's inbox
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the optional attendee's inbox");

		// Verify that optional attendee present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getOptional(), apptOptionalAttendee, "optional Attendees: Verify the appointment data");

		// Verify appointment is present in optional attendee's calendar
		AppointmentItem addeddAttendee = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(addeddAttendee, "Verify meeting invite is present in optional attendee's calendar");

	}
}
