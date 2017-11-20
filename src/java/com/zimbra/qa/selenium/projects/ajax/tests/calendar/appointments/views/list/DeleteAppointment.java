/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.list;

import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.*;

public class DeleteAppointment extends AjaxCore {

	public DeleteAppointment() {
		logger.info("New "+ DeleteAppointment.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L; {
				put("zimbraPrefCalendarInitialView", "list");
				put("zimbraPrefShowSelectionCheckbox", "TRUE");
			}
		};
	}


	@Bugs (ids = "69132")
	@Test (description = "Delete an appointment in the list view - Toolbar Delete",
			groups = { "functional", "L2" })

	public void DeleteAppointment_01() throws HarnessException {

		// Create the appointment on the server
		String subject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 3, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 4, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, subject);

		// Click delete
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");

		// Click Yes on the confirmation
		dialog.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject +"</query>"
			+	"</SearchRequest>");

		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// On the GUI app, verify the appointment is gone from the list
		AppointmentItem found = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject.contains(item.getGSubject()) ) {
				found = item;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify the appointment is no longer in the list");
	}


	@Bugs (ids = "69132")
	@Test (description = "Delete an appt using checkbox and toolbar delete button",
			groups = { "functional", "L2" })

	public void DeleteAppointment_02() throws HarnessException {

		// Create the appointment on the server
		String subject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 4, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 5, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageCalendar.zListItem(Action.A_CHECKBOX, subject);

		// Click delete
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");

		// Click Yes on the confirmation
		dialog.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject +"</query>"
			+	"</SearchRequest>");


		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// Verify the appointment is gone
		AppointmentItem found = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject.contains(item.getGSubject()) ) {
				found = item;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify the appointment is no longer in the list");
	}


	@DataProvider(name = "DataProviderDeleteKeys")
	public Object[][] DataProviderDeleteKeys() {
		return new Object[][] {
				new Object[] { "VK_DELETE", KeyEvent.VK_DELETE },
				new Object[] { "VK_BACK_SPACE", KeyEvent.VK_BACK_SPACE },
		};
	}

	@Bugs (ids = "69132")
	@Test (description = "Delete a appt by selecting and typing 'delete' keyboard",
			groups = { "functional", "L2" },
			dataProvider = "DataProviderDeleteKeys")

	public void DeleteAppointment_03(String name, int keyEvent) throws HarnessException {

		// Create the appointment on the server
		String subject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 5, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 6, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, subject);

		// Click delete
		logger.info("Typing shortcut key "+ name + " KeyEvent: "+ keyEvent);
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zKeyboardKeyEvent(keyEvent);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");

		// Click Yes on the confirmation
		dialog.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject +"</query>"
			+	"</SearchRequest>");

		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// Verify the appointment is gone
		AppointmentItem found = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject.contains(item.getGSubject()) ) {
				found = item;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify the appointment is no longer in the list");
	}


	@Bugs (ids = "72444")
	@Test (description = "Delete a appt by selecting and typing '.t' shortcut",
			groups = { "functional-skip", "application-bug" } )

	public void DeleteAppointment_04() throws HarnessException {

		// Create the appointment on the server
		String subject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 7, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, subject);

		// Click delete
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zKeyboardShortcut(Shortcut.S_MAIL_MOVETOTRASH);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");

		// Click Yes on the confirmation
		dialog.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject +"</query>"
			+	"</SearchRequest>");

		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// Verify the appointment is gone
		AppointmentItem found = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject.contains(item.getGSubject()) ) {
				found = item;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify the appointment is no longer in the list");
	}


	@Bugs (ids = "69132,79524")
	@Test (description = "Delete multiple appts (3) by select and toolbar delete",
			groups = { "functional", "L2" } )

	public void DeleteAppointment_05() throws HarnessException {

		// Create three appointments on the server
		String subject1 = ConfigProperties.getUniqueString();
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 7, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject1 +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject1 + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		String subject2 = ConfigProperties.getUniqueString();
		startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject2 +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject2 + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		String subject3 = ConfigProperties.getUniqueString();
		startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject3 +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject3 + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");



		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageCalendar.zListItem(Action.A_CHECKBOX, subject1);
		app.zPageCalendar.zListItem(Action.A_CHECKBOX, subject2);
		app.zPageCalendar.zListItem(Action.A_CHECKBOX, subject3);

		// Click delete
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");

		// Click Yes on the confirmation
		dialog.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject1 +"</query>"
			+	"</SearchRequest>");

		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject2 +"</query>"
			+	"</SearchRequest>");

		folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject3 +"</query>"
			+	"</SearchRequest>");

		folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// Verify the appointment is gone
		AppointmentItem found1 = null;
		AppointmentItem found2 = null;
		AppointmentItem found3 = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject1.contains(item.getGSubject()) ) {
				found1 = item;
			}
			if ( subject2.contains(item.getGSubject()) ) {
				found2 = item;
			}
			if ( subject3.contains(item.getGSubject()) ) {
				found3 = item;
			}
		}

		ZAssert.assertNull(found1, "Verify the appointment "+ subject1 +" no longer exists");
		ZAssert.assertNull(found2, "Verify the appointment "+ subject2 +" no longer exists");
		ZAssert.assertNull(found3, "Verify the appointment "+ subject3 +" no longer exists");
	}


	@Bugs (ids = "69132")
	@Test (description = "Delete a appt using context menu delete button",
			groups = { "functional", "L2" })

	public void DeleteAppointment_06() throws HarnessException {

		// Create the appointment on the server
		String subject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Work around
		app.zPageMain.zRefreshMainUI();
		app.zPageCalendar.zNavigateTo();

		// Right click the item, select delete
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_DELETE, subject);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");
		dialog.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ subject +"</query>"
			+	"</SearchRequest>");

		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		ZAssert.assertEquals(
				folderID,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
				"Verify appointment is in the trash folder");

		// Verify the appointment is gone
		AppointmentItem found = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject.contains(item.getGSubject()) ) {
				found = item;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify the appointment is no longer in the list");
	}


	@Bugs (ids = "102051")
	@Test (description = "Hard-delete a appt by selecting and typing 'shift-del' shortcut",
			groups = { "functional-skip", "application-bug" } )

	public void HardDeleteAppointment_01() throws HarnessException {

		// Create the appointment on the server
		String subject = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageCalendar.zListItem(Action.A_LEFTCLICK, subject);

		// Type shift-delete
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");
		dialog.zPressButton(Button.B_YES);

		// Verify the appointment is gone
		AppointmentItem found = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject.contains(item.getGSubject()) ) {
				found = item;
				break;
			}
		}

		ZAssert.assertNull(found, "Verify the appointment is no longer in the list");


		// Verify the appointment is not in the calendar or trash
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
				+		"<query>is:anywhere "+ subject +"</query>"
				+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(nodes.length, 0, "Verify the appointment is not in the calendar or trash (trash folder l='3')");
	}


	@Bugs (ids = "102051")
	@Test (description = "Hard-delete multiple appts (3) by selecting and typing 'shift-del' shortcut",
			groups = { "functional-skip", "application-bug" })

	public void HardDeleteAppointment_02() throws HarnessException {

		// Create three appointments on the server
		String subject1 = ConfigProperties.getUniqueString();
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject1 +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject1 + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		String subject2 = ConfigProperties.getUniqueString();
		startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject2 +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject2 + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		String subject3 = ConfigProperties.getUniqueString();
		startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);

		// Create an appointment
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ subject3 +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ subject3 + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Refresh the calendar
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Check the item
		app.zPageCalendar.zListItem(Action.A_CHECKBOX, subject1);
		app.zPageCalendar.zListItem(Action.A_CHECKBOX, subject2);
		app.zPageCalendar.zListItem(Action.A_CHECKBOX, subject3);

		// Click delete
		DialogConfirmDeleteAppointment dialog = (DialogConfirmDeleteAppointment)app.zPageCalendar.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
		ZAssert.assertNotNull(dialog, "Verify the dialog appears correctly");
		dialog.zPressButton(Button.B_YES);

		// Verify the appointment is gone
		AppointmentItem found1 = null;
		AppointmentItem found2 = null;
		AppointmentItem found3 = null;
		List<AppointmentItem> appts = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : appts) {
			if ( subject1.contains(item.getGSubject()) ) {
				found1 = item;
			}
			if ( subject2.contains(item.getGSubject()) ) {
				found2 = item;
			}
			if ( subject3.contains(item.getGSubject()) ) {
				found3 = item;
			}
		}

		ZAssert.assertNull(found1, "Verify the appointment "+ subject1 +" no longer exists");
		ZAssert.assertNull(found2, "Verify the appointment "+ subject2 +" no longer exists");
		ZAssert.assertNull(found3, "Verify the appointment "+ subject3 +" no longer exists");


		// Verify the appointment is not in the calendar or trash
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
				+		"<query>is:anywhere "+ subject1 +"</query>"
				+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(nodes.length, 0, "Verify the appointment is not in the calendar or trash (trash folder l='3')");

		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
				+		"<query>is:anywhere "+ subject2 +"</query>"
				+	"</SearchRequest>");

		nodes = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(nodes.length, 0, "Verify the appointment is not in the calendar or trash (trash folder l='3')");

		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
				+		"<query>is:anywhere "+ subject3 +"</query>"
				+	"</SearchRequest>");

		nodes = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(nodes.length, 0, "Verify the appointment is not in the calendar or trash (trash folder l='3')");

	}
}