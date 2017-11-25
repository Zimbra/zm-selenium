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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.folders;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class GetCalendar extends AjaxCore {

	public GetCalendar() {
		logger.info("New "+ GetCalendar.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Get a calendar (under USER_ROOT)",
			groups = { "smoke", "L1" })

	public void GetCalendar_01() throws HarnessException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		ZAssert.assertNotNull(root, "Verify the inbox is available");

		// Create the subfolder
		String name = "calendar" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name +"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(subfolder, "Verify the subfolder is available");


		// Click on Get Mail to refresh the folder list
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);


		// Verify the folder appears in the list
		List<FolderItem> folders = app.zTreeCalendar.zListGetFolders();

		FolderItem found = null;
		for (FolderItem f : folders) {
			if ( name.equals(f.getName()) ) {
				found = f;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the calendar was in the tree");
	}


	@Test (description = "Get a calendar (under subcalendar)",
			groups = { "functional", "L2" })

	public void GetCalendar_02() throws HarnessException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		ZAssert.assertNotNull(root, "Verify the inbox is available");

		// Create a subfolder
		String name1 = "calendar" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name1 +"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is available");

		// Create a subfolder of the subfolder
		String name2 = "calendar" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name2 +"' l='"+ subfolder1.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder2 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name2);
		ZAssert.assertNotNull(subfolder2, "Verify the subfolder is available");


		// Click on Get Mail to refresh the folder list
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);


		// Verify the folder appears in the list
		List<FolderItem> folders = app.zTreeCalendar.zListGetFolders();

		FolderItem found1 = null;
		FolderItem found2 = null;
		for (FolderItem f : folders) {
			if ( name1.equals(f.getName()) ) {
				found1 = f;
			}
			if ( name2.equals(f.getName()) ) {
				found2 = f;
			}
		}
		ZAssert.assertNotNull(found1, "Verify the calendar was in the tree");
		ZAssert.assertNotNull(found2, "Verify the sub-calendar was in the tree");
	}


	@Bugs (ids = "28846")
	@Test (description = "Get a calendar (under subcalendar) - verify appointments appear",
			groups = { "functional", "L2" })

	public void GetCalendar_03() throws HarnessException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		ZAssert.assertNotNull(root, "Verify the inbox is available");

		// Create a subfolder
		String name1 = "calendar" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name1 +"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is available");

		app.zGetActiveAccount().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>" +
                	"<action op='check' id='"+ subfolder1.getId() +"'/>" +
                "</FolderActionRequest>");

		// Create a subfolder of the subfolder
		String name2 = "calendar" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name2 +"' l='"+ subfolder1.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder2 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name2);
		ZAssert.assertNotNull(subfolder2, "Verify the subfolder is available");

		app.zGetActiveAccount().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>" +
                	"<action op='check' id='"+ subfolder2.getId() +"'/>" +
                "</FolderActionRequest>");


		// Create the appointment in the subfolder
		String apptSubject = ConfigProperties.getUniqueString();
		String location = "location" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create an appointment in the subcalendar
		app.zGetActiveAccount().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ subfolder2.getId() +"'>"
				+			"<inv>"
				+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ apptSubject +"' loc='"+ location +"' >"
				+					"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
				+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
				+				"</comp>"
				+			"</inv>"
				+			"<su>"+ apptSubject + "</su>"
				+			"<mp ct='text/plain'>"
				+				"<content>"+ content +"</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

	    // Verify appt displayed in workweek view
		boolean found = false;
		List<AppointmentItem> items = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : items ) {
			if ( apptSubject.equals(item.getSubject()) ) {
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify appt gets displayed in work week view");
	}
}