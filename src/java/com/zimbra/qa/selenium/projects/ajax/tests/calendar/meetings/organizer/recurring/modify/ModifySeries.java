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

import java.time.LocalDate;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogOpenRecurringItem;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class ModifySeries extends AjaxCommonTest {

	public ModifySeries() {
		logger.info("New "+ ModifySeries.class.getCanonicalName());
		super.startingPage =  app.zPageCalendar;
	}


	@Test (description = "Modify series from every day to every week",
			groups = { "functional", "L2" })

	public void ModifySeries_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = Calendar.getInstance();
		String tz = ZTimeZone.getLocalTimeZone().getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
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
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// --------------- Login to attendee & modify series ----------------------------------------------------

		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THE_SERIES);
		openRecurring.zPressButton(Button.B_OK);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zRepeat(Button.O_EVERY_WEEK_MENU, Button.B_EVERY_X_RADIO_BUTTON, Button.B_NO_END_DATE_RADIO_BUTTON);
        apptForm.zSubmit();

		// ---------------- Verification at organizer & invitee side both -------------------------------------

        app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		String interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");

		ZAssert.assertEquals(ruleFrequency, "WEE", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");


		// Attendee1: Search for the appointment (InvId)
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		String attendeeInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.AccountA().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.AccountA().EmailAddress +"']", "ptst");
		ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");

		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION'");
		ZAssert.assertEquals(ruleFrequency, "WEE", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");

		String messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee1 gets new email notification");
	}

	@Test (description = "Modify summary of a weekly recurring series",
			groups = { "functional", "L2" })

	public void ModifySeries_02() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = Calendar.getInstance();
		String tz = ZTimeZone.getLocalTimeZone().getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		AppointmentItem appt = new AppointmentItem();
		String apptSubject = ConfigProperties.getUniqueString();
		String modifiedSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();

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
									"<rule freq='WEE'>" +
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
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// --------------- Login to attendee & modify series ----------------------------------------------------

		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THE_SERIES);
		openRecurring.zPressButton(Button.B_OK);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.Subject, modifiedSubject);
		apptForm.zFill(appt);
        apptForm.zSubmit();

		// ---------------- Verification at organizer & invitee side both -------------------------------------

        app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ modifiedSubject +"</query>"
				+	"</SearchRequest>");

		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		String interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");

		ZAssert.assertEquals(ruleFrequency, "WEE", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");


		// Attendee1: Search for the appointment (InvId)
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ modifiedSubject +"</query>"
				+	"</SearchRequest>");
		String attendeeInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.AccountA().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.AccountA().EmailAddress +"']", "ptst");
		ruleFrequency = ZimbraAccount.AccountA().soapSelectValue("//mail:appt//mail:rule", "freq");
		interval = ZimbraAccount.AccountA().soapSelectValue("//mail:appt//mail:interval", "ival");

		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION'");
		ZAssert.assertEquals(ruleFrequency, "WEE", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ modifiedSubject +")</query>"
				+	"</SearchRequest>");

		String messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee1 gets new email notification");
	}


	@Test (description = "Modify summary of a daily recurring series",
			groups = { "functional", "L2" })

	public void ModifySeries_03() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = Calendar.getInstance();
		String tz = ZTimeZone.getLocalTimeZone().getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

		AppointmentItem appt = new AppointmentItem();
		String apptSubject = ConfigProperties.getUniqueString();
		String modifiedSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();

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
										"<count num='3'/>" +
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

		// --------------- Login to attendee & modify series ----------------------------------------------------

		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THE_SERIES);
		openRecurring.zPressButton(Button.B_OK);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zFillField(Field.Subject, modifiedSubject);
		apptForm.zFill(appt);
        apptForm.zSubmit();

		// ---------------- Verification at organizer & invitee side both -------------------------------------

        app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ modifiedSubject +"</query>"
				+	"</SearchRequest>");

		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		String interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");
		String countNumber = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:count", "num");

		ZAssert.assertEquals(ruleFrequency, "DAI", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(countNumber, "3", "Count number: Verify the appointment data");


		// Attendee1: Search for the appointment (InvId)
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ modifiedSubject +"</query>"
				+	"</SearchRequest>");
		String attendeeInvId = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.AccountA().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.AccountA().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.AccountA().EmailAddress +"']", "ptst");
		ruleFrequency = ZimbraAccount.AccountA().soapSelectValue("//mail:appt//mail:rule", "freq");
		interval = ZimbraAccount.AccountA().soapSelectValue("//mail:appt//mail:interval", "ival");
		countNumber = ZimbraAccount.AccountA().soapSelectValue("//mail:appt//mail:count", "num");

		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION'");
		ZAssert.assertEquals(ruleFrequency, "DAI", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(countNumber, "3", "Count number: Verify the appointment data");

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ modifiedSubject +")</query>"
				+	"</SearchRequest>");

		String messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee1 gets new email notification");
	}


	@Bugs(ids = "100575")
	@Test (description = "Modify series by setting end date",
			groups = { "functional", "L2" })

	public void ModifySeries_04() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = Calendar.getInstance();
		String tz = ZTimeZone.getLocalTimeZone().getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);

		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		String getTomorrowDayOfWeek = StringUtils.capitalize(LocalDate.now().plusDays(1).getDayOfWeek().toString().toLowerCase());

		// --------------- Creating invitation (organizer) ----------------------------

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account1().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='WEE'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.Account1().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// --------------- Login to attendee & modify series ----------------------------------------------------

		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THE_SERIES);
		openRecurring.zPressButton(Button.B_OK);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zRepeat(Button.O_EVERY_WEEK_MENU, Button.B_EVERY_X_RADIO_BUTTON, getTomorrowDayOfWeek, Button.B_END_BY_DATE_RADIO_BUTTON, "01/02/2020");
		ZAssert.assertStringContains(app.zPageCalendar.zGetRecurringLink(), "Every " + getTomorrowDayOfWeek + ". End by Jan 2, 2020. Effective ", "Recurring link: Verify the appointment data");
		apptForm.zSubmit();

		// ---------------- Verification at organizer & invitee side both -------------------------------------

        app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='" + startUTC.addDays(-10).toMillis() + "' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		String interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");
		String untilDate = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:until", "d");

		ZAssert.assertEquals(ruleFrequency, "WEE", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(untilDate, "20200103T045959Z", "Until date: Verify the appointment data");


		// Attendee1: Search for the appointment (InvId)
		ZimbraAccount.Account1().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		String attendeeInvId = ZimbraAccount.Account1().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.Account1().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.Account1().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account1().EmailAddress +"']", "ptst");
		ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");
		untilDate = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:until", "d");

		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION'");
		ZAssert.assertEquals(ruleFrequency, "WEE", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(untilDate, "20200103T045959Z", "Until date: Verify the appointment data");

		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.Account1(), FolderItem.SystemFolder.Inbox).getId();
		ZimbraAccount.Account1().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>inid:"+ inboxId +" subject:("+ apptSubject +")</query>"
				+	"</SearchRequest>");

		String messageId = ZimbraAccount.Account1().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(messageId, "Verify attendee1 gets new email notification");
	}

	@Bugs(ids = "101610")
	@Test (description = "Modifying daily custom series doesn't update new selection in custom repeat dialog ",
			groups = { "functional-skip", "application-bug" })

	public void ModifySeries_05() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		Calendar now = Calendar.getInstance();
		String tz = ZTimeZone.getLocalTimeZone().getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

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
							"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account3().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
										"<count num='10'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ ZimbraAccount.Account3().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		DialogOpenRecurringItem openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THE_SERIES);
		openRecurring.zPressButton(Button.B_OK);

		FormApptNew apptForm = new FormApptNew(app);
		apptForm.zRepeat(Button.O_EVERY_DAY_MENU, Button.B_EVERY_X_DAYS_RADIO_BUTTON, "2", Button.B_END_AFTER_X_OCCURRENCES_RADIO_BUTTON, "10");
		ZAssert.assertStringContains(app.zPageCalendar.zGetRecurringLink(), "Every 2 days. End after 10 occurrence(s)", "Recurring link: Verify the appointment data");
        apptForm.zSubmit();

		openRecurring = (DialogOpenRecurringItem) app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		openRecurring.zPressButton(Button.B_OPEN_THE_SERIES);
		openRecurring.zPressButton(Button.B_OK);
		ZAssert.assertStringContains(app.zPageCalendar.zGetRecurringLink(), "Every 2 days. End after 10 occurrence(s)", "Recurring link: Verify the appointment data");


		// ---------------- Verification at organizer & invitee side both -------------------------------------

        app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");

		String organizerInvId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");

		// Get the appointment details
		app.zGetActiveAccount().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ organizerInvId +"'/>");

		String ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		String interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");
		String countNumber = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:count", "num");

		ZAssert.assertEquals(ruleFrequency, "DAI", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "2", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(countNumber, "10", "Count number: Verify the appointment data");


		// Attendee1: Search for the appointment (InvId)
		ZimbraAccount.Account3().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		String attendeeInvId = ZimbraAccount.Account3().soapSelectValue("//mail:appt", "invId");

		ZimbraAccount.Account3().soapSend(
					"<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");

		String myStatus = ZimbraAccount.Account3().soapSelectValue("//mail:at[@a='"+ ZimbraAccount.Account3().EmailAddress +"']", "ptst");
		ruleFrequency = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:rule", "freq");
		interval = app.zGetActiveAccount().soapSelectValue("//mail:appt//mail:interval", "ival");
		countNumber = ZimbraAccount.Account3().soapSelectValue("//mail:appt//mail:count", "num");

		ZAssert.assertEquals(myStatus, "NE", "Verify that the attendee status shows as 'NEEDS ACTION'");
		ZAssert.assertEquals(ruleFrequency, "DAI", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "2", "Repeat interval: Verify the appointment data");
		ZAssert.assertEquals(countNumber, "10", "Count number: Verify the appointment data");
	}
}