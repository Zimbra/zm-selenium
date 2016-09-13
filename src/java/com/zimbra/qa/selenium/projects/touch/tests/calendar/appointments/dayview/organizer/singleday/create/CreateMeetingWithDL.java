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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.singleday.create;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew.Field;

public class CreateMeetingWithDL extends CalendarWorkWeekTest {	
	
	public CreateMeetingWithDL() {
		logger.info("New "+ CreateMeetingWithDL.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	    super.startingAccountPreferences = null;
	}

	@Test( description = "Create meeting invite using DL",
			groups = { "smoke" })
	
	public void CreateMeetingWithDL_01() throws HarnessException {
				
		// Create a meeting
		AppointmentItem appt = new AppointmentItem();
	
		String apptSubject, apptContent;
		apptSubject = ConfigProperties.getUniqueString();
		
		// Create a DL
		ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();
		ZimbraDistributionList DL = (new ZimbraDistributionList()).provision();
		DL.addMember(account1);
		DL.addMember(account2);
		
		// Create appointment data
		apptContent = ConfigProperties.getUniqueString();
		appt.setSubject(apptSubject);
		appt.setContent(apptContent);
	
		// Compose appointment with DL
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSelectAddressUsingAutoComplete(Field.Attendees, DL.EmailAddress);
		apptForm.zSubmit();
		
        // Verify attendee1 of DL receives meeting invitation message
		account1.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		
		String idA = account1.soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(idA, "Verify new invitation appears in the inbox for members of DL ");
		
		// Verify attendee2 of DL receives meeting invitation message
		account2.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		
		String idB = account2.soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(idB, "Verify new invitation appears in the inbox for members of DL ");
	
		// Verify that attendee1 is present in the appointment
        AppointmentItem actualA = AppointmentItem.importFromSOAP(account1, "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actualA.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actualA.getAttendees(), DL.EmailAddress, "Attendees: Verify the appointment data");
		
		// Verify that attendee2 is present in the appointment
        AppointmentItem actualB = AppointmentItem.importFromSOAP(account2, "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actualB.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actualB.getAttendees(), DL.EmailAddress, "Attendees: Verify the appointment data");
	
	}
}
