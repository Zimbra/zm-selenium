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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.mountpoints.viewer.actions;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogInformational;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.FormApptNew.Field;

public class CreateACopy extends CalendarWorkWeekTest {

	public CreateACopy() {
		logger.info("New "+ CreateACopy.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test( description = "Grantee copies appointment from grantor's calendar and creates new invite",
			groups = { "functional", "L2" })
			
	public void CreateACopy_01() throws HarnessException {
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();
		String foldername = "folder" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();
		
		String newSubject = ConfigProperties.getUniqueString();
		String newContent = ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 05, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 06, 0, 0);
		
		FolderItem calendarFolder = FolderItem.importFromSOAP(ZimbraAccount.Account9(), FolderItem.SystemFolder.Calendar);
		
		// Create a folder to share
		ZimbraAccount.Account9().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + calendarFolder.getId() + "' view='appointment'/>"
				+	"</CreateFolderRequest>");
		
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.Account9(), foldername);
		
		// Share it
		ZimbraAccount.Account9().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='r' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.Account9().ZimbraId +"' view='appointment' color='5'/>"
				+	"</CreateMountpointRequest>");
		
		// Create appointment
		ZimbraAccount.Account9().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.Account9().EmailAddress +"'/>"
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
		
		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(mountpointname);
		
		// Copy appointment
        DialogInformational dialog = (DialogInformational)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_A_COPY_MENU, apptSubject);
		dialog.zClickButton(Button.B_OK);
		
        FormApptNew form = new FormApptNew(app);
        form.zFillField(Field.Subject, newSubject);
        form.zFillField(Field.Body, newContent);
        form.zFillField(Field.Attendees, ZimbraAccount.Account2().EmailAddress);
        form.zSubmit();
		
		// Verify the new invitation appears in the attendee1's inbox
		ZimbraAccount.Account9().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + newSubject + ")" + " " + "content:(" + newContent +")" + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.Account9().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		// Verify organizer for the copied appointment
		ZimbraAccount.Account9().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ newSubject +")" + " " + "content:(" + newContent +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.Account9().soapSelectValue("//mail:appt", "invId");
		String organizer = ZimbraAccount.Account9().soapSelectValue("//mail:appt/mail:or", "a");
		ZAssert.assertEquals(organizer, app.zGetActiveAccount().EmailAddress, "Verify organizer for the copied appointment");

		// Verify the new invitation appears in the attendee2's inbox
		ZimbraAccount.Account2().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + newSubject + ")" + " " + "content:(" + newContent +")" + "</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.Account2().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		// Verify organizer for the copied appointment
		ZimbraAccount.Account2().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ newSubject +")" + " " + "content:(" + newContent +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.Account2().soapSelectValue("//mail:appt", "invId");
		organizer = ZimbraAccount.Account2().soapSelectValue("//mail:appt/mail:or", "a");
		ZAssert.assertEquals(organizer, app.zGetActiveAccount().EmailAddress, "Verify organizer for the copied appointment");
		
	}

}
