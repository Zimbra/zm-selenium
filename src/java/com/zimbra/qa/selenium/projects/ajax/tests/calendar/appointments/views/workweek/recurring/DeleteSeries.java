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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.workweek.recurring;

import java.awt.event.KeyEvent;
import java.util.Calendar;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.*;

public class DeleteSeries extends AjaxCore {

	public DeleteSeries() {
		logger.info("New "+ DeleteSeries.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "69132")
	@Test (description = "Delete entire series of recurring appointment (every day) using toolbar button",
			groups = { "smoke", "L1" } )

	public void DeleteSeries_01() throws HarnessException {

		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endTime   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);


        // If you select an instance and click delete button, you
        // Get two dialogs:
        // First: do you want to delete the instance or series?
        // Second: do you want to delete all occurrences or this instance and all future instances
        //
        DialogWarning deleteRecurringItems = (DialogWarning)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
        if (deleteRecurringItems == null) {
        	throw new HarnessException("The 'Delete Recurring Items' dialog never appeared.");
        }
        deleteRecurringItems.zPressButton(Button.B_DELETE_THE_SERIES);
        DialogWarning confirmDelete = (DialogWarning)deleteRecurringItems.zPressButton(Button.B_OK);
        if (confirmDelete == null) {
        	throw new HarnessException("The 'Confirm Delete' dialog never appeared.");
        }
        confirmDelete.zPressButton(Button.B_YES);

        // On the server, verify the appointment is in the trash
        app.zGetActiveAccount().soapSend(
        			"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
        		+		"<query>is:anywhere "+ apptSubject +"</query>"
        		+	"</SearchRequest>");

        String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
        ZAssert.assertEquals(
        		folderID,
        		FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash).getId(),
        		"Verify appointment is in the trash folder");


        // Verify the appointment is not in the GUI view
        ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");

        //boolean deleted = app.zPageCalendar.zWaitForElementDeleted(app.zPageCalendar.zGetApptLocator(apptSubject), "10000");
	    //ZAssert.assertEquals(deleted, true, "Verify instance is deleted from the calendar");

	}


	@Bugs (ids = "69132")
	@Test (description = "Delete entire series of recurring appointment (every week) using context menu",
			groups = { "smoke", "L1" } )

	public void DeleteSeries_02() throws HarnessException {

		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endTime   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);

        // If you right-click an instance and select delete from the context menu, you
        // Get one dialog:
        // First: do you want to delete all occurrences or this instance and all future instances
        //
        // This is different from the "select an instance and click delete button" usage
        //
        DialogWarning confirmDelete = (DialogWarning)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_SERIES_MENU, Button.O_DELETE_MENU, apptSubject);
        if (confirmDelete == null) {
        	throw new HarnessException("The 'Confirm Delete' dialog never appeared.");
        }
        confirmDelete.zPressButton(Button.B_YES);

        // On the server, verify the appointment is in the trash
        app.zGetActiveAccount().soapSend(
        			"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
        		+		"<query>is:anywhere "+ apptSubject +"</query>"
        		+	"</SearchRequest>");

        String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
        ZAssert.assertEquals(
        		folderID,
        		FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash).getId(),
        		"Verify appointment is in the trash folder");


        // Verify the appointment is not in the GUI view
        ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");
        //boolean deleted = app.zPageCalendar.zWaitForElementDeleted(app.zPageCalendar.zGetApptLocator(apptSubject), "10000");
	    //ZAssert.assertEquals(deleted, true, "Verify instance is deleted from the calendar");

	}


	@DataProvider(name = "DataProviderShortcutKeys")
	public Object[][] DataProviderShortcutKeys() {
		return new Object[][] {
				new Object[] { "VK_DELETE", KeyEvent.VK_DELETE },
				new Object[] { "VK_BACK_SPACE", KeyEvent.VK_BACK_SPACE },
		};
	}

	@Bugs (ids = "69132")
	@Test (description = "Delete entire series appointment (every week) using keyboard shortcuts Del & Backspace",
			groups = { "functional", "L2" },
			dataProvider = "DataProviderShortcutKeys")

	public void DeleteSeries_03(String name, int keyEvent) throws HarnessException {

		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endTime   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);

        DialogWarning deleteRecurringItems = (DialogWarning)app.zPageCalendar.zKeyboardKeyEvent(keyEvent);
        if (deleteRecurringItems == null) {
        	throw new HarnessException("The 'Delete Recurring Items' dialog never appeared.");
        }
        deleteRecurringItems.zPressButton(Button.B_DELETE_THE_SERIES);
        DialogWarning confirmDelete = (DialogWarning)deleteRecurringItems.zPressButton(Button.B_OK);
        if (confirmDelete == null) {
        	throw new HarnessException("The 'Confirm Delete' dialog never appeared.");
        }
        confirmDelete.zPressButton(Button.B_YES);

        // On the server, verify the appointment is in the trash
        app.zGetActiveAccount().soapSend(
        			"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
        		+		"<query>is:anywhere "+ apptSubject +"</query>"
        		+	"</SearchRequest>");

        String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
        ZAssert.assertEquals(
        		folderID,
        		FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash).getId(),
        		"Verify appointment is in the trash folder");


        // Verify the appointment is not in the GUI view
        ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");
		//boolean deleted = app.zPageCalendar.zWaitForElementDeleted(app.zPageCalendar.zGetApptLocator(apptSubject), "10000");
		//ZAssert.assertEquals(deleted, true, "Verify instance is deleted from the calendar");
	}
}