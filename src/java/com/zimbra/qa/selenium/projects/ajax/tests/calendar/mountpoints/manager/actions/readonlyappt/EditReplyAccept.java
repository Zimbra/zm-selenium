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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.manager.actions.readonlyappt;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class EditReplyAccept extends AjaxCore {

	public EditReplyAccept() {
		logger.info("New "+ EditReplyAccept.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "80559")
	@Test (description = "Assistant right clicks to calendar invite from shared calendar and accepts the invite OBO boss using Edit Reply -> Accept",
			groups = { "functional-skip", "application-bug" })

	public void EditReplyAccept_01() throws HarnessException {

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = "body" + ConfigProperties.getUniqueString();
		String mountPointName = "mountpoint" + ConfigProperties.getUniqueString();
		String modifiedBody = ConfigProperties.getUniqueString();

		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account8(), FolderItem.SystemFolder.Calendar);

		// Share it
		ZimbraAccount.Account8().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account8().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");

		// Create invite
		ZimbraAccount.Account9().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account9().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account8().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account8().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountPointName);

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		FormMailNew mailComposeForm = (FormMailNew)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT_REPLY_ACCEPT_SUB_MENU, apptSubject);
        mailComposeForm.zFillField(Field.Body, " " + modifiedBody);
		mailComposeForm.zSubmit();

		// -------------- Verification at organizer side --------------

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account9(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.Account9().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId + " " + "subject:(" + "Accept " + apptSubject + ")" + " " + "content:(" + modifiedBody + " " + "Yes, I will attend." + ")" + "</query>"
				+	"</SearchRequest>");
		String messageId = ZimbraAccount.Account9().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify organizer gets email notification");

		String attendeeStatus = ZimbraAccount.Account9().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account8().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");

		// Verify from and sender address in accept invitation message
		ZimbraAccount.Account9().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");

		ZAssert.assertEquals(ZimbraAccount.Account9().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account8().EmailAddress, "Verify From address in accept invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account9().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in accept invitation message");

		ZimbraAccount.Account9().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");

		String organizerInvId = ZimbraAccount.Account9().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		ZimbraAccount.Account9().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		attendeeStatus = ZimbraAccount.Account9().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account8().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");


		// -------------- Verification at attendee side --------------

		ZimbraAccount.Account8().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String attendeeInvId = ZimbraAccount.Account8().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.Account8().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.Account8().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account8().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(myStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");

		// Verify sent mail for accepted appointment notification (action performed by assistant)
		ZimbraAccount.Account8().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>" + "in:sent is:unread subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");
		messageId = ZimbraAccount.Account8().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify sent mail for accepted appointment notification (action performed by assistant)");

		attendeeStatus = ZimbraAccount.Account8().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account8().EmailAddress +"']", "ptst");

		// Verify attendee status shows as ptst=AC
		ZAssert.assertEquals(attendeeStatus, "AC", "Verify that the attendee shows as 'ACCEPTED'");

		// Verify from and sender address in accept invitation message
		ZimbraAccount.Account8().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ messageId +"'/>"
			+	"</GetMsgRequest>");

		ZAssert.assertEquals(ZimbraAccount.Account8().soapSelectValue("//mail:e[@t='f']", "a"), ZimbraAccount.Account8().EmailAddress, "Verify From address in accept invitation message");
		ZAssert.assertEquals(ZimbraAccount.Account8().soapSelectValue("//mail:e[@t='s']", "a"), app.zGetActiveAccount().EmailAddress, "Verify Sender address in accept invitation message");

	}
}