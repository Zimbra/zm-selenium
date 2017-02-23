/*
 * ***** BEGIN LICENSE BLOCK *****
 *
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
 *
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.recurring.delete;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmationDeleteAppointment;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmDeleteOrganizer;

public class DeleteSeries extends CalendarWorkWeekTest {

	public DeleteSeries() {
		logger.info("New "+ DeleteSeries.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
		
	}
	
	@Bugs(ids = "69920,75559")	
	@Test( description = "Delete series from third instance and onwards", 
			groups = { "functional", "L5" })
	public void DeleteSeries_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = this.calendarWeekDayUTC;
		String tz = ZTimeZone.TimeZoneEST.getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		
		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account2().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='WEE'>" +
										"<interval ival='1'/>" +
										"<count num='5'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.Account2().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_SERIES_MENU, Button.O_DELETE_MENU, apptSubject);
		DialogConfirmationDeleteAppointment dialog = (DialogConfirmationDeleteAppointment) new DialogConfirmationDeleteAppointment(app, app.zPageCalendar);
		dialog.zClickButton(Button.B_DELETE_THIS_AND_FUTURE_OCCURRENCES);	
		SleepUtil.sleepMedium();
        dialog.zClickButton(Button.B_YES);
        DialogConfirmDeleteOrganizer dialog2 = (DialogConfirmDeleteOrganizer) new DialogConfirmDeleteOrganizer(app, app.zPageCalendar);
		dialog2.zClickButton(Button.B_SEND_CANCELLATION);

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(organizerInvId, "Verify that appointment is deleted");

        //Login as attendee and verify that cancellation mail does not have A/D/T buttons
        app.zPageMain.zLogout();
        
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ ZimbraAccount.Account2().ZimbraId +"</id>"
			+		"<a n='zimbraPrefGroupMailBy'>message</a>"
			+	"</ModifyAccountRequest>");
		
		//Login as attendee
		app.zPageLogin.zLogin(ZimbraAccount.Account2());

		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		// Select the invitation and verify Accept/decline/Tentative buttons are not present
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertFalse(display.zHasADTButtons(), "Verify A/D/T buttons");
		
	}
	
	// remove this test case and move above testcase to L2 from L5 once bug #75559 is resolved
	@Test( description = "Delete series from third instance and onwards", 
			groups = { "functional", "L2" })
	public void DeleteSeries_02() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = this.calendarWeekDayUTC;
		String tz = ZTimeZone.TimeZoneEST.getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		
		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account2().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='WEE'>" +
										"<interval ival='1'/>" +
										"<count num='5'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.Account2().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_PAGE);
		
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_SERIES_MENU, Button.O_DELETE_MENU, apptSubject);
		DialogConfirmationDeleteAppointment dialog = (DialogConfirmationDeleteAppointment) new DialogConfirmationDeleteAppointment(app, app.zPageCalendar);
		dialog.zClickButton(Button.B_DELETE_THIS_AND_FUTURE_OCCURRENCES);	
		SleepUtil.sleepMedium();
        dialog.zClickButton(Button.B_YES);
        DialogConfirmDeleteOrganizer dialog2 = (DialogConfirmDeleteOrganizer) new DialogConfirmDeleteOrganizer(app, app.zPageCalendar);
		dialog2.zClickButton(Button.B_SEND_CANCELLATION);

		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(organizerInvId, "Verify that appointment is deleted");

	}
	
}
