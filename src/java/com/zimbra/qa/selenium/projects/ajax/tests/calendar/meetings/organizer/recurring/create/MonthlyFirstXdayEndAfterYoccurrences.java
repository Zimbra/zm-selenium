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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.recurring.create;

import java.time.LocalDate;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;

public class MonthlyFirstXdayEndAfterYoccurrences extends AjaxCore {

	public MonthlyFirstXdayEndAfterYoccurrences() {
		logger.info("New "+ MonthlyFirstXdayEndAfterYoccurrences.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Create monthly recurring invite with attendee and location with particular week & end date",
			groups = { "sanity", "L1" })

	public void MonthlyFirstXdayEndAfterYoccurrences_01() throws HarnessException {

		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		String apptSubject, apptAttendee, apptContent, apptLocation;
		String getShortTomorrowDayOfWeek = LocalDate.now().plusDays(1).getDayOfWeek().toString();
		String getTomorrowDayOfWeek = StringUtils.capitalize(getShortTomorrowDayOfWeek.toLowerCase());
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);

		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ConfigProperties.getUniqueString();
		apptLocation = location.EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
		appt.setLocation(apptLocation);
		appt.setContent(apptContent);

		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zRepeat(Button.O_EVERY_MONTH_MENU, Button.B_THE_X_Y_OF_EVERY_Z_MONTHS_RADIO_BUTTON, "1", Button.B_END_AFTER_X_OCCURRENCES_RADIO_BUTTON, "2");
		ZAssert.assertStringContains(app.zPageCalendar.zGetRecurringLink(), "The first " + getTomorrowDayOfWeek + " of every 1 month(s). End after 2 occurrence(s).", "Recurring link: Verify the appointment data");
		apptForm.zSubmitWithResources();

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-40).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(40).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");

		String attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "id");
		app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		String count = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:count", "num");
		String interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");
		String wkday = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:wkday", "day");

		// Verify appointment exists on server meeting with correct recurring details
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-40), appt.getEndTime().addDays(40));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee, "Attendees: Verify the appointment data");
		ZAssert.assertTrue(actual.getLocation().contains(apptLocation), "Location: Verify the appointment data");
		ZAssert.assertEquals(ruleFrequency, "MON", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(count, "2", "No. of occurrence(s): Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(wkday, getShortTomorrowDayOfWeek.subSequence(0, 2), "Week day: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify location free/busy status shows as ptst=AC
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify that the location status shows as 'ACCEPTED'");

		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-40).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(40).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");

		attendeeInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");
		ZimbraAccount.AccountA().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		count = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:count", "num");
		interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");
		wkday = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:wkday", "day");

		// Verify the attendee receives the meeting with correct recurring details
		AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-40), appt.getEndTime().addDays(40));
		ZAssert.assertNotNull(received, "Verify the new appointment is created");
		ZAssert.assertEquals(received.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(received.getAttendees(), apptAttendee, "Attendees: Verify the appointment data");
		ZAssert.assertTrue(received.getLocation().contains(apptLocation), "Location: Verify the appointment data");
		ZAssert.assertEquals(ruleFrequency, "MON", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(count, "2", "No. of occurrence(s): Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(wkday, getShortTomorrowDayOfWeek.subSequence(0, 2), "Week day: Verify the appointment data");
		ZAssert.assertEquals(received.getContent(), appt.getContent(), "Content: Verify the appointment data");

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");

		// Go to month view and verify correct number of recurring instances
		app.zPageCalendar.zToolbarPressButton(Button.B_MONTH_VIEW);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		SleepUtil.sleepMedium(); //Let UI draw first and important for calendar testcases reliability
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), true, "Verify meeting invite is present in current calendar view");
	}
}