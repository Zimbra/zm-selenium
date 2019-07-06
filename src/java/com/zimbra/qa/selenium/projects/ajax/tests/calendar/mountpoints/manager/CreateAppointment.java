/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.manager;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;

public class CreateAppointment extends AjaxCore {

	public CreateAppointment() {
		logger.info("New " + CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Create a basic meeting with attendee and location on shared mailbox",
			groups = { "bhr" })

	public void CreateAppointment_01() throws HarnessException {

		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();

		String apptSubject, apptLocation, apptAttendee1, apptContent, mountPointName;
		apptSubject = ConfigProperties.getUniqueString();
		apptLocation = location.EmailAddress;
		apptAttendee1 = ZimbraAccount.Account1().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();
		mountPointName = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		appt.setSubject(apptSubject);
		appt.setLocation(apptLocation);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		appt.setContent(apptContent);

		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account2(),	FolderItem.SystemFolder.Calendar);

		// Share it
		ZimbraAccount.Account2().soapSend("<FolderActionRequest xmlns='urn:zimbraMail'>" + "<action id='"	+ folder.getId() + "' op='grant'>" + "<grant d='"
						+ app.zGetActiveAccount().EmailAddress	+ "' gt='usr' perm='rwidx' view='appointment'/>"
						+ "</action>" + "</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend("<CreateMountpointRequest xmlns='urn:zimbraMail'>" + "<link l='1' name='" + mountPointName + "'  rid='"
						+ folder.getId() + "' zid='" + ZimbraAccount.Account2().ZimbraId + "' view='appointment' color='4'/>" + "</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountPointName);
		ZAssert.assertNotNull(mountpoint, "Verify mount point is created");

		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Compose appointment on shared mailbox
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zFillField(Field.CalendarFolder, mountPointName);
		apptForm.zSubmit();

		// Verify appointment is created on shared mailbox
		AppointmentItem actual = AppointmentItem.importFromSOAP(ZimbraAccount.Account2(), "subject:(" + apptSubject + ")");
		ZAssert.assertEquals(actual.getFolder(), folder.getId(), "Verify appointment is created on shared mailbox");

		// Verify sent invite not present in current account
		app.zGetActiveAccount().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>" + "<query>" + "in:sent " + "subject:(" + apptSubject + ")</query>" + "</SearchRequest>");
		String messageId = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(messageId, "Verify sent invite not present in current account");

		ZimbraAccount.Account1().soapSend("<SearchRequest xmlns='urn:zimbraMail' types='message'>" + "<query>" + "in:inbox " + "subject:(" + apptSubject + ")</query>" + "</SearchRequest>");
		messageId = ZimbraAccount.Account1().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee gets email notification");

		// Verify from and sender address in received invite
		ZimbraAccount.Account1().soapSend("<GetMsgRequest  xmlns='urn:zimbraMail'>" + "<m id='"	+ messageId + "'/>" + "</GetMsgRequest>");
		ZAssert.assertEquals(ZimbraAccount.Account1().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account2().EmailAddress,	"Verify From address in received invite");
		ZAssert.assertEquals(ZimbraAccount.Account1().soapSelectValue("//mail:e[@t='s']","a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in received invite");

		// Verify appointment exists on the server
		actual = AppointmentItem.importFromSOAP(ZimbraAccount.Account2(), "subject:(" + appt.getSubject() + ")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(),"Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getLocation(), appt.getLocation(),"Location: Verify the appointment data");
		ZAssert.assertStringContains(actual.getAttendees(), apptAttendee1,"Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(),"Content: Verify the appointment data");

		actual = AppointmentItem.importFromSOAP(location, "subject:(" + appt.getSubject() + ")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify location is booked");

		// Verify appointment exists in UI
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountPointName);
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), true,"Verify appointment is visible in UI");

	}

}