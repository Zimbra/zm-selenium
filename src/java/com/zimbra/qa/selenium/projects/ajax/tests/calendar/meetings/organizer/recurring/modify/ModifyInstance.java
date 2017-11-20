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

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.DialogOpenRecurringItem;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;

public class ModifyInstance extends AjaxCore {

	public ModifyInstance() {
		logger.info("New "+ ModifyInstance.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	}


	@Test (description = "Modify series instance by adding one more attendee & modified subject/body",
			groups = { "functional", "L2" })

	public void ModifyInstance_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = Calendar.getInstance();
		String tz = ZTimeZone.getLocalTimeZone().getID();
		AppointmentItem appt = new AppointmentItem();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		String modifiedApptSubject = ConfigProperties.getUniqueString();
		String modifiedApptBody = ConfigProperties.getUniqueString();
		String apptAttendee2 = ZimbraAccount.AccountB().EmailAddress;

		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.AccountA().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		// --------------- Login to attendee & modify first instance ----------------------------------------------------

		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THIS_INSTANCE);
		openRecurring.zPressButton(Button.B_OK);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zVerifyDisabledControlInOpenInstance();

		apptForm.zFillField(Field.Subject, modifiedApptSubject);
		apptForm.zFillField(Field.Attendees, apptAttendee2);
		apptForm.zFillField(Field.Body, modifiedApptBody);
		apptForm.zFill(appt);
        apptForm.zSubmit();

		// ---------------- Verification at organizer & invitee side both -------------------------------------

		// Organizer: Search for the appointment (InvId)
        app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ modifiedApptSubject +"</query>"
				+	"</SearchRequest>");

		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		String exceptionApptName = app.zGetActiveAccount().soapSelectValue("//mail:inst[@ex='1']", "name");
		String exceptionRecurring = app.zGetActiveAccount().soapSelectValue("//mail:inst[@ex='1']", "recur");

		ZAssert.assertEquals(exceptionApptName, modifiedApptSubject, "Verify exception appointment name");
		ZAssert.assertEquals(exceptionRecurring, "0", "Verify recurring count for exception");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String exceptId = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:exceptId", "d");
		ZAssert.assertEquals(exceptId, startUTC.toyyyyMMddTHHmmss(), "Verify modified instance is represented as exception");


		// Attendee1: Search for the appointment (InvId)
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ modifiedApptSubject +"</query>"
				+	"</SearchRequest>");
		String attendeeInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.AccountA().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		String myStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.AccountA().EmailAddress +"']", "ptst");
		exceptId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt//mail:exceptId", "d");

		// Verify attendee status shows as ptst=NE
		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION'");
		ZAssert.assertEquals(exceptId, startUTC.toyyyyMMddTHHmmss(), "Verify modified instance is represented as exception");

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ modifiedApptSubject +")</query>"
				+	"</SearchRequest>");

		String messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee1 gets new email notification");


		// Attendee2: Search for the appointment (InvId)
		ZimbraAccount.AccountB().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ modifiedApptSubject +"</query>"
				+	"</SearchRequest>");
		attendeeInvId = ZimbraAccount.AccountB().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.AccountB().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		myStatus = ZimbraAccount.AccountB().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.AccountB().EmailAddress +"']", "ptst");
		exceptId = ZimbraAccount.AccountB().soapSelectValue("//mail:appt//mail:exceptId", "d");

		// Verify attendee status shows as ptst=NE
		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION'");
		ZAssert.assertEquals(exceptId, startUTC.toyyyyMMddTHHmmss(), "Verify modified instance is represented as exception");

		inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountB(), FolderItem.SystemFolder.Inbox).getId();
		ZimbraAccount.AccountB().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ modifiedApptSubject +")</query>"
				+	"</SearchRequest>");

		messageId = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee2 gets new email notification");
	}
}
