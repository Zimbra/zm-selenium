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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.recurring.modify;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar.Locators;

public class DeclinedAppointmentInstances extends AjaxCore {

	public DeclinedAppointmentInstances() {
		logger.info("New "+ DeclinedAppointmentInstances.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
		super.startingAccountPreferences.put("zimbraPrefCalendarInitialView", "week");
	}


	@Test (description = "Verify start date of the recurring appointment instances declined by recipients",
			groups = { "functional" })

	public void DeclinedAppointmentInstances_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = Calendar.getInstance();
		String tz = ZTimeZone.getLocalTimeZone().getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();


		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" 
						+	"<m>"
						+		"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
						+			"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" 
						+			"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" 
						+			"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" 
						+			"<at role='REQ' ptst='NE' rsvp='1' a='" + account1.EmailAddress + "'/>" 
						+			"<at role='REQ' ptst='NE' rsvp='1' a='" + account2.EmailAddress + "'/>" 
						+			"<recur>" 
						+				"<add>" 
						+					"<rule freq='DAI'>" 
						+						"<interval ival='2'/>" 
						+						"<count num='15'/>" 
						+					"</rule>" 
						+				"</add>" 
						+			"</recur>" 
						+		"</inv>" 
						+		"<e a='"+ account1.EmailAddress +"' t='t'/>" 
						+		"<e a='"+ account2.EmailAddress +"' t='t'/>" 
						+		"<mp content-type='text/plain'>" 
						+			"<content>"+ apptBody +"</content>" 
						+		"</mp>" 
						+		"<su>"+ apptSubject +"</su>" 
						+	"</m>" 
						+	"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		
		// Search the appointment to get invite id
		account1.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(20).toMillis() +"'>"
						+		"<query>"+ apptSubject +"</query>"
						+	"</SearchRequest>");
		String attendeeInvId = account1.soapSelectValue("//mail:appt", "invId");
		
		// Get the appointment
		account1.soapSend(
				"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		String apptid = account1.soapSelectValue("//mail:appt//mail:inv", "id");
		
		// account1 accepts the appointment 
		account1.soapSend(
				"<SendInviteReplyRequest xmlns='urn:zimbraMail' id='" + apptid +"' compNum='0' verb='ACCEPT' updateOrganizer='TRUE'>" 
						+ "</SendInviteReplyRequest>"
				);
		// account2 accepts the appointment
		account2.soapSend(
				"<SendInviteReplyRequest xmlns='urn:zimbraMail' id='" + apptid +"' compNum='0' verb='ACCEPT' updateOrganizer='TRUE'>" 
						+ "</SendInviteReplyRequest>");

		// Decline the second instance of the invite
		account1.soapSend(
				"<SendInviteReplyRequest xmlns='urn:zimbraMail' id='" + apptid +"' compNum='0' verb='DECLINE' updateOrganizer='TRUE'>" 
						+    "<exceptId d='" + startUTC.addDays(2).toYYYYMMDDTHHMMSS() + "'/>" 
						+ "</SendInviteReplyRequest>");

		// Refresh the view
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Check that the appointment declined by account1 is present in the displayed week
		if (!app.zPageCalendar.sIsElementPresent(Locators.zDeclinedAppointments + "[1]")) {
			app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_WEEK);
		}

		// Open the declined appointment and check the start date
		app.zPageCalendar.sDoubleClick(Locators.zDeclinedAppointments + "[1]");
		FormApptNew apptForm = new FormApptNew(app);
		ZAssert.assertEquals(apptForm.zGetStartDate(), startUTC.addDays(2).toM_D_YYYY() , "Verify the startDate of the  declined appointment instance");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

		// Decline the third instance of the invite
		account2.soapSend(
				"<SendInviteReplyRequest xmlns='urn:zimbraMail' id='" + apptid +"' compNum='0' verb='DECLINE' updateOrganizer='TRUE'>" 
						+    "<exceptId d='" + startUTC.addDays(4).toYYYYMMDDTHHMMSS() + "'/>" 
						+ "</SendInviteReplyRequest>");

		// Refresh the view
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Check that the appointment declined by account2 is present in the displayed week
		if (!app.zPageCalendar.sIsElementPresent(Locators.zDeclinedAppointments + "[2]")) {
			app.zPageCalendar.zToolbarPressButton(Button.B_NEXT_WEEK);
		}

		// Open the declined appointment and check the start date
		app.zPageCalendar.sDoubleClick(Locators.zDeclinedAppointments + "[2]");
		apptForm = new FormApptNew(app);
		ZAssert.assertEquals(apptForm.zGetStartDate(), startUTC.addDays(4).toM_D_YYYY() , "Verify the startDate of the  declined appointment instance");
		apptForm.zToolbarPressButton(Button.B_CLOSE);
	}
}
