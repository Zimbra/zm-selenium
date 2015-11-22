/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.manager.actions.readonlyappt;

import java.util.Calendar;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogInformational;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class CreateACopy extends CalendarWorkWeekTest {

	public CreateACopy() {
		logger.info("New "+ CreateACopy.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "80322")
	@Test(	description = "Assistant right clicks to calendar invite from shared calendar and creates a copy of it",
			groups = { "functional" })
			
	public void CreateACopy_01() throws HarnessException {
		
		String organizer;
		organizerTest = false;
		String apptSubject = ZimbraSeleniumProperties.getUniqueString();
		String apptContent = "body" + ZimbraSeleniumProperties.getUniqueString();
		String newSubject = ZimbraSeleniumProperties.getUniqueString();
		String newContent = ZimbraSeleniumProperties.getUniqueString();
		
		String mountPointName = "mountpoint" + ZimbraSeleniumProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
		// Use system calendar folder
		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Calendar);
		
		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
		
		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountPointName +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"' view='appointment' color='4'/>"
				+	"</CreateMountpointRequest>");
		
		// Create invite
		ZimbraAccount.AccountB().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m l='"+ folder.getId() +"' >"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + ZimbraAccount.AccountA().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>" + apptContent + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");
		
		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffMountedFolder(mountPointName);
		
		// Copy appointment
        DialogInformational dialog = (DialogInformational)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_CREATE_A_COPY_MENU, apptSubject);
		dialog.zClickButton(Button.B_OK);
		
        FormApptNew form = new FormApptNew(app);
        form.zFillField(Field.Subject, newSubject);
        form.zFillField(Field.Body, newContent);
        form.zFillField(Field.Attendees, ZimbraAccount.AccountA().EmailAddress);
        app.zPageCalendar.zToolbarPressButton(Button.B_SEND);
        SleepUtil.sleepLong();
		
		// Verify the new invitation appears in the attendee1's inbox
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + newSubject + ")" + " " + "content:(" + newContent +")" + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		// Verify organizer for the copied appointment
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ newSubject +")" + " " + "content:(" + newContent +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountA().soapSelectValue("//mail:appt", "invId");
		organizer = ZimbraAccount.AccountA().soapSelectValue("//mail:appt/mail:or", "a");
		ZAssert.assertEquals(organizer, app.zGetActiveAccount().EmailAddress, "Verify organizer for the copied appointment");

		// Verify the new invitation appears in the attendee2's inbox
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + newSubject + ")" + " " + "content:(" + newContent +")" + "</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		// Verify organizer for the copied appointment
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>subject:("+ newSubject +")" + " " + "content:(" + newContent +")</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountB().soapSelectValue("//mail:appt", "invId");
		organizer = ZimbraAccount.AccountB().soapSelectValue("//mail:appt/mail:or", "a");
		ZAssert.assertEquals(organizer, app.zGetActiveAccount().EmailAddress, "Verify organizer for the copied appointment");
		
	}
}