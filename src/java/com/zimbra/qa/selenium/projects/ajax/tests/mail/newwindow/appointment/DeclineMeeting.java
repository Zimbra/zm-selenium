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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.appointment;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowDisplayMail;

public class DeclineMeeting extends AjaxCore {

	public DeclineMeeting() {
		logger.info("New "+ DeclineMeeting.class.getCanonicalName());

		super.startingPage =  app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L; {
				put("zimbraPrefGroupMailBy", "message");
			}
		};
	}

	private void SendCreateAppointmentRequest(String subject, ZDate start) throws HarnessException {

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ subject +"'>"
						+				"<s d='"+ start.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
						+				"<e d='"+ start.addHours(2).toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
						+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
						+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
						+			"</inv>"
						+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
						+			"<su>"+ subject +"</su>"
						+			"<mp content-type='text/plain'>"
						+				"<content>content</content>"
						+			"</mp>"
						+		"</m>"
						+	"</CreateAppointmentRequest>");
	}


	@Test (description = "From New Windoow Decline a meeting using Decline button from invitation message",
			groups = { "sanity" })

	public void DeclineMeeting_01() throws HarnessException {

		String apptSubject = "appointment" + ConfigProperties.getUniqueString();

		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
						+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
						+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
						+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
						+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
						+			"</inv>"
						+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
						+			"<su>"+ apptSubject +"</su>"
						+			"<mp content-type='text/plain'>"
						+				"<content>content</content>"
						+			"</mp>"
						+		"</m>"
						+	"</CreateAppointmentRequest>");

		// Refresh the view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message present in current view");

		// Select the invitation
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + apptSubject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Click Accept
			window.zPressButton(Button.B_DECLINE);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

		// Organizer: Search for the appointment (InvId)
		ZimbraAccount
				.AccountA()
				.soapSend(
						"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"
								+ startUTC.addDays(-10).toMillis()
								+ "' calExpandInstEnd='"
								+ endUTC.addDays(10).toMillis()
								+ "'>"
								+ "<query>"
								+ apptSubject
								+ "</query>"
								+ "</SearchRequest>");

		String organizerInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		ZimbraAccount.AccountA().soapSend(
				"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String attendeeStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(attendeeStatus, "DE", "Verify that the attendee shows as 'DECLINED'");


		// Check that the attendee showing status as "DECLINE" ---

		// Attendee: Search for the appointment (InvId)
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
						+		"<query>"+ apptSubject +"</query>"
						+	"</SearchRequest>");

		String attendeeInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
				"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ app.zGetActiveAccount().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=DE
		ZAssert.assertEquals(myStatus, "DE", "Verify that the attendee shows as 'DECLINED'");
	}
}