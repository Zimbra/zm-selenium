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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.resources;

import java.util.Calendar;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class CreateMeetingWithEquipment extends AjaxCommonTest {

	public CreateMeetingWithEquipment() {
		logger.info("New "+ CreateMeetingWithEquipment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs(ids = "69132")
	@Test( description = "Create simple meeting with equipment",
			groups = { "smoke", "L1" })

	public void CreateMeetingWithEquipment_01() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		Calendar now = Calendar.getInstance();
		ZimbraResource equipment1 = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);

		String apptSubject, apptAttendee1, apptEquipment1, apptContent;
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptEquipment1 = equipment1.EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
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
        apptForm.zSubmitWithResources();

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), appt.getAttendees(), "Attendees: Verify the appointment data");
		ZAssert.assertStringContains(actual.getEquipment(), appt.getEquipment(), "Equipment: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify equipment free/busy status shows as psts=AC
		String equipmentStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment1 +"']", "ptst");
		ZAssert.assertEquals(equipmentStatus, "AC", "Verify that the equipment status shows as 'ACCEPTED'");

	}

}
