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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.viewer.viewappt;

import java.util.Calendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;

public class VerifyDisabledUI extends CalendarWorkWeekTest {

	public VerifyDisabledUI() {
		logger.info("New "+ VerifyDisabledUI.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test( description = "Verify Save, Tag, Accepted and Actions -> Edit, Forward, Delete, Propose New Time & Delete menus are non-functional on mountpoint appointment (read-only share)",
			groups = { "functional" })
			
	public void VerifyDisabledUI_01() throws HarnessException {
		
		organizerTest = false;
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		String foldername = "folder" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
		FolderItem calendarFolder = FolderItem.importFromSOAP(ZimbraAccount.Account1(), FolderItem.SystemFolder.Calendar);
		
		// Create a folder to share
		ZimbraAccount.Account1().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + calendarFolder.getId() + "' view='appointment'/>"
				+	"</CreateFolderRequest>");
		
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account1(), foldername);
		
		// Share it
		ZimbraAccount.Account1().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='r' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account1().ZimbraId +"' view='appointment' color='5'/>"
				+	"</CreateMountpointRequest>");
		
		// Create appointment
		ZimbraAccount.Account1().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account1().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		
		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar"); //need to localize it
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountpointname);
		
		// Verify Save, Tag, Accepted, Edit, Forward, Delete, Propose New Time & Delete menus are disabled
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_OPEN_MENU, apptSubject);
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.B_SAVE_DISABLED_READONLY_APPT), "Verify 'Save' button is disabled");
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.B_TAG_APPOINTMENT_DISABLED_READONLY_APPT), "Verify 'Tag' button is disabled");
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.B_ACCEPTED_DISABLED_READONLY_APPT), "Verify 'Accepted' button is disabled");
		
		app.zPageCalendar.zToolbarPressButton(Button.B_ACTIONS);
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_EDIT_DISABLED_READONLY_APPT), "Verify Actions -> Edit menu is disabled");
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_FORWARD_DISABLED_READONLY_APPT), "Verify Actions -> Forward menu is disabled");
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_DELETE_DISABLED_READONLY_APPT), "Verify Actions -> Delete menu is disabled");
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_PROPOSE_NEW_TIME_DISABLED_READONLY_APPT), "Verify Actions -> Propose New Time menu is disabled");
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_DELETE_DISABLED_READONLY_APPT), "Verify Actions -> Delete menu is disabled");

	}

	@Bugs(ids = "99947")
	@Test( description = "Verify  disabled Create a copy, Move, Forward & Show Original menus for private appointment on mountpoint appointment (read-only share)",
			groups = { "functional" })
			
	public void VerifyDisabledUI_02() throws HarnessException {
		
		organizerTest = false;
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptBody = ConfigProperties.getUniqueString();
		String foldername = "folder" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 17, 0, 0);
		
		FolderItem calendarFolder = FolderItem.importFromSOAP(ZimbraAccount.Account1(), FolderItem.SystemFolder.Calendar);
		
		// Create a folder to share
		ZimbraAccount.Account1().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + calendarFolder.getId() + "' view='appointment'/>"
				+	"</CreateFolderRequest>");
		
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account1(), foldername);
		
		// Share it
		ZimbraAccount.Account1().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='r' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account1().ZimbraId +"' view='appointment' color='5'/>"
				+	"</CreateMountpointRequest>");
		
		// Create appointment
		ZimbraAccount.Account1().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PRI' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account1().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.Account2().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.Account2().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptBody + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
		
		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar"); //need to localize it
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountpointname);
		
		// Verify Save, Tag, Accepted, Edit, Forward, Delete, Propose New Time & Delete menus are disabled
		app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_OPEN_MENU, apptSubject);
		//ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.B_SAVE_DISABLED_READONLY_APPT), "Verify 'Save' button is disabled");
		
		app.zPageCalendar.zToolbarPressButton(Button.B_ACTIONS);
		ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_FORWARD_DISABLED_READONLY_APPT), "Verify Actions -> Forward menu is disabled");
		//ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_SHOW_ORIGINAL_DISABLED), "Verify Actions -> Show original menu is disabled");
		//ZAssert.assertTrue(app.zPageCalendar.zVerifyDisabledControl(Button.O_CREATE_A_COPY_DISABLED), "Verify Actions -> Create a copy menu is disabled");

	}
	
}
