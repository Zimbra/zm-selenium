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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.week.singleday;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;

public class CreateAppointment extends AjaxCore {

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L; {
				put("zimbraPrefCalendarInitialView", "week");
			}
		};
	}


	@Test (description = "Create a basic appointment without an attendee in week view",
			groups = { "smoke", "L3" } )

	public void CreateAppointment_01() throws HarnessException {

		// Create appointment
		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();
		appt.setSubject(ConfigProperties.getUniqueString());
		appt.setContent("content" + ConfigProperties.getUniqueString());
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));

		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");

		// Fill out the form with the data
		apptForm.zFill(appt);

		// Send invite
		apptForm.zSubmit();

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
	}


	@Test (description = "Create appointment with all the fields and verify it",
			groups = { "functional", "L3" } )

	public void CreateAppointment_02() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);

		String apptSubject, apptAttendee1, apptOptional1, apptLocation1, apptEquipment1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptOptional1 = ZimbraAccount.AccountB().EmailAddress;
		apptLocation1 = location.EmailAddress;
		apptEquipment1 = equipment.EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setOptional(apptOptional1);
		appt.setLocation(apptLocation1);
		appt.setEquipment(apptEquipment1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setContent(apptContent);

		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		List<AutocompleteEntry> entries = apptForm.zAutocompleteFillField(Field.Equipment, apptEquipment1);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(apptEquipment1) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		apptForm.zAutocompleteSelectItem(found);
        ZAssert.assertTrue(apptForm.zVerifyEquipment(apptEquipment1), "Verify appointment equipment");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), appt.getAttendees(), "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getOptional(), appt.getOptional(), "Optional: Verify the appointment data");
		ZAssert.assertStringContains(actual.getLocation(), appt.getLocation(), "Location: Verify the appointment data");
		ZAssert.assertStringContains(actual.getEquipment(), appt.getEquipment(), "Equipment: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
	}


	@Test (description = "Create private appointment in week view",
			groups = { "functional", "L3" } )

	public void CreatePrivateAppointment_03() throws HarnessException {

		// Create appointment
		String apptSubject;
		apptSubject = ConfigProperties.getUniqueString();
		AppointmentItem appt = new AppointmentItem();

		appt.setSubject(apptSubject);
		appt.setContent("content" + ConfigProperties.getUniqueString());
		appt.setAttendees(ZimbraAccount.AccountA().EmailAddress);
		appt.setIsPrivate(true);

		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");

		// Fill the data and submit it
		apptForm.zFill(appt);
		apptForm.zSubmit();

		// Verify private appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(app.zGetActiveAccount().soapMatch("//mail:GetAppointmentResponse//mail:comp", "class", "PRI"), true, "");
	}
}