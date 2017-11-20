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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.actions;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogInformational;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;

public class CreateACopy extends AjaxCore {

	public CreateACopy() {
		logger.info("New "+ CreateACopy.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	}


	@Test (description = "Copy meeting invite and verify content",
			groups = { "smoke", "L1" })

	public void CopyMeeting_01() throws HarnessException {

		// Create a meeting
		String organizer;
		organizerTest = true;
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();

		String newSubject = ConfigProperties.getUniqueString();
		String newContent = ConfigProperties.getUniqueString();
		String attendee1 = ZimbraAccount.AccountA().EmailAddress;
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;

		// Absolute dates in UTC zone
		String tz = ZTimeZone.getLocalTimeZone().getID();
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + attendee1 + "' d='2'/>" +
                     	"</inv>" +
                     	"<e a='"+ attendee1 +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ apptContent + "</content>" +
                     	"</mp>" +
                     "<su>"+ apptContent +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // Copy appointment
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_A_COPY_MENU, apptSubject);
        DialogInformational dialog = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, null, null);
		ZAssert.assertFalse(dialog.zIsActive(), "Change organizer information dialog appeared while copying appointment");

        FormApptNew form = new FormApptNew(app);
        form.zFillField(Field.Subject, newSubject);
        form.zFillField(Field.Body, newContent);
        form.zFillField(Field.Attendees, attendee2);
        form.zSubmit();

		// Verify the new invitation appears in the attendee1's inbox
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + newSubject + ")" + " " + "content:(" + newContent +")" + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");

		// Verify organizer for the copied appointment
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ newSubject +")" + " " + "content:(" + newContent +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");
		organizer = ZimbraAccount.AccountA().soapSelectValue("//mail:appt/mail:or", "a");
		ZAssert.assertEquals(organizer, app.zGetActiveAccount().EmailAddress, "Verify organizer for the copied appointment");

		// Verify the new invitation appears in the attendee2's inbox
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + newSubject + ")" + " " + "content:(" + newContent +")" + "</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");

		// Verify organizer for the copied appointment
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ newSubject +")" + " " + "content:(" + newContent +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountB().soapSelectValue("//mail:appt", "invId");
		organizer = ZimbraAccount.AccountB().soapSelectValue("//mail:appt/mail:or", "a");
		ZAssert.assertEquals(organizer, app.zGetActiveAccount().EmailAddress, "Verify organizer for the copied appointment");

	}
}