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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.singleday.actions;

import java.io.File;
import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

@SuppressWarnings("unused")
public class Forward extends AjaxCore {

	public Forward() {
		logger.info("New "+ Forward.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Forward a meeting invite by changing content",
			groups = { "bhr" })

	public void ForwardMeeting_01() throws HarnessException {

		// Creating a meeting
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();

		String ForwardContent = ConfigProperties.getUniqueString();
		String attendee1 = ZimbraAccount.AccountA().EmailAddress;
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ attendee1 +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptContent + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		// Forward appointment to different attendee
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_FORWARD_MENU, apptSubject);
		FormApptNew form = new FormApptNew(app);
		form.zFillField(Field.To, attendee2);
		form.zFillField(Field.Body, ForwardContent);
		form.zSubmit();

		// Verify the new invitation appears in the inbox
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + (char)34 + "Fwd: " + apptSubject + (char)34 + ")" + " " + "content:(" + ForwardContent +")" + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");

		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		// Verify only one appointment is in the calendar
		AppointmentItem a = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ apptSubject +")"  + " " + "content:(" + ForwardContent +")");
		ZAssert.assertNotNull(a, "Verify only one appointment matches in the calendar");

		// Verify meeting notification to organizer
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + (char)34 + "Meeting Forward Notification: " + apptSubject + (char)34 + ")" + " " + "content:(" + attendee2 +")" + "</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify meeting notification to organizer");
	}


	@Bugs (ids = "50663")
	@Test (description = "Forward a meeting invite after accepting it",
			groups = { "sanity" })

	public void ForwardMeeting_02() throws HarnessException {

		// Creating a meeting
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();

		String attendee1 = ZimbraAccount.AccountA().EmailAddress;
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

	    ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" +
					ZimbraAccount.AccountA().ZimbraId + "</id>" +
					"<a n='zimbraPrefGroupMailBy'>message</a>" +
				"</ModifyAccountRequest>");

	    ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" +
					ZimbraAccount.AccountB().ZimbraId + "</id>" +
					"<a n='zimbraPrefGroupMailBy'>message</a>" +
				"</ModifyAccountRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ attendee1 +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptContent + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
		ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		// --------------- Login to attendee & accept invitation ----------------------------------------------------

		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_ACCEPT_MENU, apptSubject);

		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(ZimbraAccount.AccountA());

		// Refresh the view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Go to sent folder and forward
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,sent);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);

		// Forward the item
		FormMailNew mailForm = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);

		// Fill out the form with the data
		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.To, attendee2);
		apptForm.zSubmit();

		// Logout and login as AccountB
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(ZimbraAccount.AccountB());

		// Refresh the view
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Select the invitation and verify Accept/decline/Tentative buttons are present
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertTrue(display.zHasADTButtons(), "Verify A/D/T buttons");
	}


	@Bugs (ids = "100340")
	@Test (description = "forwarding invite shows html source in meeting notes section",
			groups = { "sanity" })

	public void ForwardMeeting_03() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZCS().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "text" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "FALSE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

		String attendee2 = ZimbraAccount.Account2().EmailAddress;

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug100340/bug100340.txt";
		final String apptSubject = "Schedule for Generic Training";
		final String apptContent = "<div>";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message displayed in current view");

		// Forward invite
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,inbox);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		FormMailNew mailForm = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);

		// Fill out the form with the data
		FormApptNew apptForm = new FormApptNew(app);
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyHtml(), "<div>", "Verify populated appointment does not contain <div> tag");
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyHtml(), "<br>", "Verify populated appointment does not contain <br> tag");
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyHtml(), "<td>", "Verify populated appointment does not contain <td> tag");
		apptForm.zFillField(Field.To, attendee2);
		apptForm.zSubmit();

		// Logout and login as AccountB
		app.zPageMain.zLogout();
		app.zPageLogin.zLogin(ZimbraAccount.Account2());

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists("Fwd: " + apptSubject), "Verify message displayed in current view");
	}


	@Bugs (ids = "100340")
	@Test (description = "forwarding invite shows html source in meeting notes section",
			groups = { "sanity" })

	public void ForwardMeeting_04() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZCS().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "text" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "FALSE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZCS());

		String attendee2 = ZimbraAccount.Account3().EmailAddress;

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug106342/bug106342.txt";
		final String apptSubject = "Group photo with Steve";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(apptSubject), "Verify message displayed in current view");

		// Forward invite
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,inbox);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		FormMailNew mailForm = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);

		// Fill out the form with the data
		FormApptNew apptForm = new FormApptNew(app);
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyValue(), "<div>", "Verify populated appointment does not contain <div> tag");
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyValue(), "<br>", "Verify populated appointment does not contain <br> tag");
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyValue(), "<td>", "Verify populated appointment does not contain <td> tag");
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyValue(), "</div>", "Verify populated appointment does not contain </div> tag");
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyValue(), "</body>", "Verify populated appointment does not contain </body> tag");
		ZAssert.assertStringDoesNotContain(apptForm.zGetApptBodyValue(), "</html>", "Verify populated appointment does not contain </html> tag");
		apptForm.zFillField(Field.To, attendee2);
		apptForm.zSubmit();

		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.Account3(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, "Fwd: Group photo with Steve", "Subject: Verify the appointment data");
	}
}