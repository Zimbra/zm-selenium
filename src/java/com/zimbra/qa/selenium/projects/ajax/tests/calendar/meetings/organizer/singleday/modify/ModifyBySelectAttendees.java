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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.modify;

import java.util.Calendar;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees.Locators;;

public class ModifyBySelectAttendees extends AjaxCommonTest {

	public ModifyBySelectAttendees() {
		logger.info("New "+ ModifyBySelectAttendees.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "modify appt and Add attendee to existing appointment from contact picker",
			groups = { "functional", "L2" })

	public void ModifyBySelectAttendees_01() throws HarnessException {

		// Create a meeting
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		String apptAttendee2 = ZimbraAccount.AccountB().EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>" +
                     	"</inv>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // Delete the invite message from the attendee1's mailbox
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

        // Add attendee2 to existing appt from 'Select Address' dialog and resend the appointment
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm.zToolbarPressButton(Button.B_TO);

        DialogFindAttendees dialogFindAttendees = (DialogFindAttendees) new DialogFindAttendees(app, app.zPageCalendar);
        dialogFindAttendees.zType(Locators.ContactPickerSerachField, ZimbraAccount.AccountB().EmailAddress);
        dialogFindAttendees.zPressButton(Button.B_SEARCH);
        dialogFindAttendees.zWaitForBusyOverlay();
        dialogFindAttendees.sClick(Locators.ContactPickerFirstContact);
        dialogFindAttendees.zPressButton(Button.B_CHOOSE_CONTACT_FROM_PICKER);
        dialogFindAttendees.zWaitForBusyOverlay();
        dialogFindAttendees.zPressButton(Button.B_OK);
        apptForm.zSubmit();

        // Verify attendee1 receives meeting invitation message
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the attendee1's inbox");

		// Verify that attendee2 present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getAttendees(), apptAttendee2, "Attendees: Verify the appointment data");

		// Verify appointment is present in attendee2's calendar
		AppointmentItem addeddAttendee = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(addeddAttendee, "Verify meeting invite is present in attendee2's calendar");

		// Verify attendee2 free/busy status
		String attendee2Status = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptAttendee2 +"']", "ptst");
		ZAssert.assertEquals(attendee2Status, "NE", "Verify attendee2 free/busy status");

	}
}
