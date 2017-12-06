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
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class CreateMeeting extends AjaxCore {

	public CreateMeeting() {
		logger.info("New "+ CreateMeeting.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "95899,95900,46442")
	@Test (description = "Create basic meeting invite with one attendee",
			groups = { "sanity", "L0" })

	public void CreateMeeting_01() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();

		String apptSubject, apptAttendee1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setContent(apptContent);

		// Send invite
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertEquals(apptForm.zVerifyComposeFormatHTML(), "none", "Verify that the HTML formatting toolbar is visible on compose appointment page");
		apptForm.zFill(appt);
		apptForm.zSubmit();

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
        app.zPageMail.zNavigateTo();

		// Go to sent folder
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Sent);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, sent);

		// Select the invitation and verify Accept/decline/Tentative buttons are not present
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertFalse(display.zHasADTButtons(), "Verify A/D/T buttons");

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee receives the meeting
		AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");
		ZAssert.assertEquals(received.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(received.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(received.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");
	}


	@Test (description = "Verify new appointment creation is based on mail compose preference set to TEXT",
			groups = { "smoke", "L1" })

	public void CreateMeeting_02() throws HarnessException {

		// Set mail Compose preference to Text format
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		ZimbraAccount.AccountZCS().modifyAccountPreferences(startingAccountPreferences);

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		app.zPageCalendar.zNavigateTo();

		AppointmentItem appt = new AppointmentItem();
		String apptSubject, apptAttendee1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0));
		appt.setContent(apptContent);

		// Verify Compose appointment format obeys the mail compose format and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertEquals(apptForm.zVerifyComposeFormatHTML(), "", "Verify that the HTML formatting toolbar is NOT visible on compose appointment page");
		apptForm.zFill(appt);
		apptForm.zSubmit();

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");
	}


	@Test (description = "Create basic meeting invite with required and optional attendee",
			groups = { "functional", "L2" })

	public void CreateMeeting_03() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();

		String apptSubject, apptAttendee1, apptOptional1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptOptional1 = ZimbraAccount.AccountB().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setOptional(apptOptional1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		appt.setContent(apptContent);

		// Send invite
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();

		// Verify appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		ZAssert.assertEquals(actual.getOptional(), apptOptional1, "Optional: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee receives the meeting
		AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");

		// Verify the optional receives the meeting
		received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");

		// Verify the optional receives the invitation
		invite = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
	}


	@Test (description = "Create basic meeting invite by selecting date from date picker and time from dropdown",
			groups = { "sanity", "L0", "non-msedge" })

	public void CreateMeeting_04() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();

		String apptSubject, apptAttendee1, apptContent;
		Calendar now = Calendar.getInstance();
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFillField(Field.Subject,apptSubject);
		apptForm.zFillField(Field.Attendees, apptAttendee1);
		apptForm.zFillField(Field.Body, apptContent);

		//if it is last day of month than set appointment for same day
		if (now.get(Calendar.DAY_OF_MONTH) == now.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 1);
		}

		String meridiem = null;
		if (now.get(Calendar.AM_PM) == 0) {
			meridiem = "AM";
		} else {
			meridiem = "PM";
		}

		apptForm.zSelectStartDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 1));
		apptForm.zSelectStartTimeFromDropdown(String.valueOf(now.get(Calendar.HOUR)), "00", meridiem);
		apptForm.zSelectEndDateFromDatePicker(String.valueOf(now.get(Calendar.DAY_OF_MONTH) + 1));
		apptForm.zSelectEndTimeFromDropdown(String.valueOf(now.get(Calendar.HOUR)), "15", meridiem);
		apptForm.zSubmit();

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:(" + appt.getSubject() + ")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");

	}
}