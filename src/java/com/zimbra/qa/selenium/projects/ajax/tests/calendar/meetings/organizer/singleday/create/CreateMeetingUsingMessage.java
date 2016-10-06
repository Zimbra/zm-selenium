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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.io.File;
import java.util.Calendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogAddAttendees;

public class CreateMeetingUsingMessage extends CalendarWorkWeekTest {

	public CreateMeetingUsingMessage() {
		logger.info("New "+ CreateMeetingUsingMessage.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}

	@Test( description = "Create a meeting invite by right clicking to HTML formatted message by setting zimbraPrefComposeFormat=text & zimbraPrefForwardReplyInOriginalFormat=TRUE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_01() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "text" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "TRUE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeHtmlOnly1.txt";
		final String subject = "1 html mail";
		final String content = "Bold and Italics";
		String apptAttendee1 = "foo@testdomain.com";
		String apptAttendee2 = "bar@testdomain.com";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 8, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_YES);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		ZAssert.assertTrue(apptForm.zVerifyRequiredAttendee(apptAttendee1), "Verify populated email address bubble 1 from message");
		ZAssert.assertTrue(apptForm.zVerifyRequiredAttendee(apptAttendee2), "Verify populated email address bubble 2 from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), "Bold and Italics", "Verify populated appointment body from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("1 html mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "1 html mail");
		ZAssert.assertTrue(apptForm.zVerifyRequiredAttendee(apptAttendee1), "Open created appointment again and verify attendee1");
		ZAssert.assertTrue(apptForm.zVerifyRequiredAttendee(apptAttendee2), "Open created appointment again and verify attendee2");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), content, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Bugs(ids = "80922")
	@Test( description = "Create a meeting invite by right clicking to HTML formatted message by setting zimbraPrefComposeFormat=text & zimbraPrefForwardReplyInOriginalFormat=FALSE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_02() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "text" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "FALSE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeHtmlOnly2.txt";
		final String subject = "2 html mail";
		final String content = "Bold and Italics";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 9, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_YES);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), "Bold and Italics", "Verify populated appointment body from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("2 html mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "2 html mail");
		ZAssert.assertStringContains(apptForm.zGetApptBodyValue(), content, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Test( description = "Create a meeting invite by right clicking to plain text formatted message by setting zimbraPrefComposeFormat=text & zimbraPrefForwardReplyInOriginalFormat=FALSE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_03() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "text" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "FALSE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeTextOnly1.txt";
		final String subject = "1 plain mail";
		final String content = "The Ming Dynasty";
		final String fullContent = "The Ming Dynasty, also Empire of the Great Ming, was the ruling dynasty of China from 1368 to 1644.";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 10, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_NO);
		SleepUtil.sleepMedium();

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyValue(), fullContent, "Verify populated appointment body from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("1 plain mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "1 plain mail");
		ZAssert.assertStringContains(apptForm.zGetApptBodyValue(), fullContent, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Test( description = "Create a meeting invite by right clicking to plain text formatted message by setting zimbraPrefComposeFormat=text & zimbraPrefForwardReplyInOriginalFormat=TRUE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_04() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "text" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "TRUE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeTextOnly2.txt";
		final String subject = "2 plain mail";
		final String content = "The Ming Dynasty";
		final String fullContent = "The Ming Dynasty, also Empire of the Great Ming, was the ruling dynasty of China from 1368 to 1644.";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 11, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_NO);
		SleepUtil.sleepMedium();

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyValue(), fullContent, "Verify populated appointment body from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("2 plain mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "2 plain mail");
		ZAssert.assertStringContains(apptForm.zGetApptBodyValue(), fullContent, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Test( description = "Create a meeting invite by right clicking to HTML formatted message by setting zimbraPrefComposeFormat=html & zimbraPrefForwardReplyInOriginalFormat=TRUE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_05() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "html" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "FALSE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeHtmlOnly3.txt";
		final String subject = "3 html mail";
		final String content = "Bold and Italics";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 12, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_YES);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), "Bold and Italics", "Verify populated appointment body from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("3 html mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "3 html mail");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), content, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Test( description = "Create a meeting invite by right clicking to HTML formatted message by setting zimbraPrefComposeFormat=html & zimbraPrefForwardReplyInOriginalFormat=FALSE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_06() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "html" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "FALSE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeHtmlOnly4.txt";
		final String subject = "4 html mail";
		final String content = "Bold and Italics";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 13, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_YES);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), "Bold and Italics", "Verify populated appointment body from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("4 html mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "4 html mail");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), content, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Test( description = "Create a meeting invite by right clicking to plain text formatted message by setting zimbraPrefComposeFormat=html & zimbraPrefForwardReplyInOriginalFormat=TRUE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_07() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "html" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "TRUE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeTextOnly3.txt";
		final String subject = "3 plain mail";
		final String content = "The Ming Dynasty";
		final String fullContent = "The Ming Dynasty, also Empire of the Great Ming, was the ruling dynasty of China from 1368 to 1644.";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 14, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_YES);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("3 plain mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "3 plain mail");
		ZAssert.assertStringContains(apptForm.zGetApptBodyValue(), fullContent, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Bugs(ids = "80922")
	@Test( description = "Create a meeting invite by right clicking to plain text formatted message by setting zimbraPrefComposeFormat=html & zimbraPrefForwardReplyInOriginalFormat=FALSE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_08() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "html" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "FALSE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email10/mimeTextOnly4.txt";
		final String subject = "4 plain mail";
		final String content = "The Ming Dynasty";
		final String fullContent = "The Ming Dynasty, also Empire of the Great Ming, was the ruling dynasty of China from 1368 to 1644.";

		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 15, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_NO);
		SleepUtil.sleepMedium();

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), "Bold and Italics", "Verify populated appointment body from message");
		ZAssert.assertStringContains(apptForm.zGetApptBodyValue(), fullContent, "Verify populated appointment body from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("4 plain mail") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "4 plain mail");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), fullContent, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

	@Bugs(ids = "76043")
	@Test( description = "Create a meeting invite by right clicking to plain text formatted message by setting zimbraPrefComposeFormat=html & zimbraPrefForwardReplyInOriginalFormat=TRUE",
			groups = { "functional" })

	public void CreateMeetingUsingMessage_09() throws HarnessException {

		app.zPageMain.zLogout();
		ZimbraAccount.AccountZWC().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
			+		"<pref name='zimbraPrefComposeFormat'>"+ "html" +"</pref>"
			+		"<pref name='zimbraPrefForwardReplyInOriginalFormat'>"+ "TRUE" +"</pref>"
			+	"</ModifyPrefsRequest>");
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC());

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email16/mime.txt";
		final String subject = "ZCS 8 triage";
		final String content = "Dev is aggressively";
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 1, 14, 30, 0);

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Verify mail exists
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Rt-click to message and hit 'Create Appointment'
		DialogAddAttendees dlgAddAttendees = (DialogAddAttendees) app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_APPOINTMENT, subject);
		dlgAddAttendees.zClickButton(Button.B_YES);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.StartDate, startUTC);
		apptForm.zFillField(Field.EndDate, endUTC);
		apptForm.zFillField(Field.StartTime, startUTC);
		apptForm.zFillField(Field.EndTime, endUTC);
		ZAssert.assertEquals(apptForm.zGetApptSubject(), subject, "Verify populated appointment subject from message");
		apptForm.zSubmit();

		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ subject +")" + " " + "content:(" + content +")</query>"
			+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");

		// Open appointment again and check from the UI side
		app.zPageCalendar.zNavigateTo();
		if (app.zPageCalendar.zClickToRefreshOnceIfApptDoesntExists("ZCS 8 triage") == false) {
			app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		}
		app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, "ZCS 8 triage");
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), content, "Open created appointment again and verify body text");
		apptForm.zToolbarPressButton(Button.B_CLOSE);

	}

}
