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
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class CreateMeetingWithLocation extends AjaxCommonTest {

	public CreateMeetingWithLocation() {
		logger.info("New "+ CreateMeetingWithLocation.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs(ids = "69132")
	@Test (description = "Create simple meeting with location resource", 
			groups = { "smoke", "L1" })

	public void CreateMeetingWithSingleLocation_01() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);

		String apptSubject, apptAttendee1, apptLocation1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptLocation1 = location.EmailAddress;
		apptContent = "content" + ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setLocation(apptLocation1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));
		appt.setContent(apptContent);

		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmitWithResources();

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), appt.getAttendees(), "Attendees: Verify the appointment data");
		ZAssert.assertStringContains(actual.getLocation(), appt.getLocation(), "Location: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify location free/busy status shows as psts=AC
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation1 +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify that the location status shows as 'ACCEPTED'");
	}


	@Bugs(ids = "69132")
	@Test (description = "Create simple meeting with two location resource", 
			groups = { "functional", "L2" })

	public void CreateMeetingWithMultiLocation_02() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource location1 = new ZimbraResource(ZimbraResource.Type.LOCATION);
		ZimbraResource location2 = new ZimbraResource(ZimbraResource.Type.LOCATION);

		String apptSubject, apptAttendee1, apptLocation, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptLocation = location1.EmailAddress + " " + location2.EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setLocation(apptLocation);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setContent(apptContent);

		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmitWithResources();

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertStringContains(appt.getLocation(), actual.getLocation().split(";")[0], "Location: Verify the appointment data");

		// Verify both location free/busy status
		String locationStatus1 = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ location1.EmailAddress +"']", "ptst");
		String locationStatus2 = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ location2.EmailAddress +"']", "ptst");
		ZAssert.assertEquals(locationStatus1, "AC", "Verify that the location1 status shows as 'ACCEPTED'");
		ZAssert.assertEquals(locationStatus2, "AC", "Verify that the location2 status shows as 'ACCEPTED'");
	}


	@Test (description = "Create simple meeting with floating location resource", 
			groups = { "functional", "L2" })

	public void CreateMeetingWithFloatingLocation_03() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();

		String apptSubject, apptAttendee1, apptLocation, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptLocation = ConfigProperties.getUniqueString() + " " + ConfigProperties.getUniqueString();
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setLocation(apptLocation);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0));
		appt.setContent(apptContent);

		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");

	}

}
