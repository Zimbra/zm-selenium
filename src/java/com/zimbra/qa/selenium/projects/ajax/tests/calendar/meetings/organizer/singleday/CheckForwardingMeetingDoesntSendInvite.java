/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar.Locators;

public class CheckForwardingMeetingDoesntSendInvite extends AjaxCore {

	public CheckForwardingMeetingDoesntSendInvite() {
		logger.info("New "+ CheckForwardingMeetingDoesntSendInvite.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "56465")
	@Test (description = "Bug 56465 - Forwarding of Calendar events sending invites to all attendees",
			groups = { "functional", "L2" })

	public void CheckForwardingMeetingDoesntSendInvite_01() throws HarnessException {

		// Creating a meeting
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);

		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String attendee1 = ZimbraAccount.AccountA().EmailAddress;
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;
		String apptLocation = location.EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + attendee1 + "' d='2'/>" +
                     		"<at cutype='RES' a='" + apptLocation + "' rsvp='1' role='NON' url='" + apptLocation + "' ptst='AC'/>" +
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // Delete the invite message from the attendee's mailbox
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountA().soapSend(
				"<ItemActionRequest  xmlns='urn:zimbraMail'>"
			+		"<action id='"+ id +"' op='delete'/>"
			+	"</ItemActionRequest>");

        // Forward appointment to different attendee
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_FORWARD_MENU, apptSubject);
        app.zPageCalendar.zType(Locators.ForwardToTextArea, attendee2);
        app.zPageCalendar.zToolbarPressButton(Button.B_SEND);

		// Verify meeting invite appears to attendee (appt forwarding)
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the attendee's inbox");

		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");
		id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the attendee's inbox");

		// Verify meeting invite appears for new attendee
		AppointmentItem a = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ apptSubject + ")");
		ZAssert.assertNotNull(a, "Verify that appointment matches in the calendar");

		// Verify meeting invite is not present for first attendee
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNull(id, "Verify meeting invite is not present to first attendee");

	}
}