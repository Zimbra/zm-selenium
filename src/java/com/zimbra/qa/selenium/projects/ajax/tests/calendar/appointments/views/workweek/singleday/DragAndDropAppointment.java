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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.workweek.singleday;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.workweek.allday.CreateAppointment;

public class DragAndDropAppointment extends AjaxCore {

	public DragAndDropAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Drag and Drop a appointment from calendar to different calendar",
			groups = { "smoke", "L1" })

	public void DragAndDropAppointment_01() throws HarnessException {

		String foldername = "folder"+ ConfigProperties.getUniqueString();

		// Create a calendar to move the appointment into
		FolderItem rootFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ rootFolder.getId() +"' view='appointment'/>" +
					"</CreateFolderRequest>");
		FolderItem subcalendarFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Refresh view after folder creation
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Creating objects for appointment data
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);

        app.zGetActiveAccount().soapSend(
    			"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
    		+		"<m>"
    		+			"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
    		+				"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
    		+			"</inv>"
    		+			"<mp content-type='text/plain'>"
    		+				"<content>" + apptBody + "</content>"
    		+			"</mp>"
    		+			"<su>" + apptSubject + "</su>"
    		+		"</m>"
    		+	"</CreateAppointmentRequest>");
        String apptId = app.zGetActiveAccount().soapSelectValue("//mail:CreateAppointmentResponse", "apptId");

        // Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // Drag and drop the item
        String locator;
		if (app.zPageCalendar.zIsWeekend()) {
			locator = "css=div[id^='zli__CLW__";
		} else {
			locator = "css=div[id^='zli__CLWW__";
		}

		// Select the item
		app.zPageCalendar.zDragAndDrop(
					locator + apptId + "'] td.appt_name",
					"css=td[id='zti__main_Calendar__" + subcalendarFolder.getId() + "_textCell']");

		// Verification
		AppointmentItem newAppointment = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(newAppointment.getFolder(), subcalendarFolder.getId(), "Verify the appointment moved folders");
	}


	@Test (description = "Drag and Drop a appointment from one time to a different time",
			groups = { "smoke", "L1" })

	public void DragAndDropAppointment_02() throws HarnessException {

		// We need to create two appointments to make a locator to drag and drop to.
		// Steps:
		// 1. Create appointment1
		// 2. Create appointment2 an hour later
		// 3. Drag and drop appointment1 to appointment2
		// 4. Verify appointment1 happens at appointment2 time

		// Creating objects for appointment data
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0);

        app.zGetActiveAccount().soapSend(
    			"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
    		+		"<m>"
    		+			"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
    		+				"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
    		+			"</inv>"
    		+			"<mp content-type='text/plain'>"
    		+				"<content>" + apptBody + "</content>"
    		+			"</mp>"
    		+			"<su>" + apptSubject + "</su>"
    		+		"</m>"
    		+	"</CreateAppointmentRequest>");
        String apptId = app.zGetActiveAccount().soapSelectValue("//mail:CreateAppointmentResponse", "apptId");

        app.zGetActiveAccount().soapSend("<GetAppointmentRequest id='"+ apptId + "' xmlns='urn:zimbraMail'/>");
        String s = app.zGetActiveAccount().soapSelectValue("//mail:s", "d");
        String e = app.zGetActiveAccount().soapSelectValue("//mail:e", "d");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

		String otherSubject = ConfigProperties.getUniqueString();
		ZDate otherStartUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0);
		ZDate otherEndUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);

		app.zGetActiveAccount().soapSend(
    			"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
    		+		"<m>"
    		+			"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ otherSubject +"'>"
    		+				"<s d='"+ otherStartUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<e d='"+ otherEndUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
    		+			"</inv>"
    		+			"<mp content-type='text/plain'>"
    		+				"<content>" + ConfigProperties.getUniqueString() + "</content>"
    		+			"</mp>"
    		+			"<su>" + otherSubject + "</su>"
    		+		"</m>"
    		+	"</CreateAppointmentRequest>");
        String otherApptId = app.zGetActiveAccount().soapSelectValue("//mail:CreateAppointmentResponse", "apptId");

        // Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(otherSubject), "Verify appointment displayed in current view");

        // Drag and drop the item
        String locator;
		if (app.zPageCalendar.zIsWeekend()) {
			locator = "css=div[id^='zli__CLW__";
		} else {
			locator = "css=div[id^='zli__CLWW__";
		}

    	String sourceLocator = locator + apptId + "'] td.appt_name";
    	String destinationLocator = locator + otherApptId + "'] td.appt_name";
    	app.zPageCalendar.zDragAndDropBy(sourceLocator, destinationLocator, 0, 10);

    	// It is difficult to know for certain what time is correct. For now, just make sure it was moved somewhere
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest id='"+ apptId + "' xmlns='urn:zimbraMail'/>");
        String s1 = app.zGetActiveAccount().soapSelectValue("//mail:s", "d");
        String e2 = app.zGetActiveAccount().soapSelectValue("//mail:e", "d");

		ZAssert.assertStringDoesNotContain(s1, s, "Verify the start time changed");
		ZAssert.assertStringDoesNotContain(e2, e, "Verify the end time changed");
	}
}