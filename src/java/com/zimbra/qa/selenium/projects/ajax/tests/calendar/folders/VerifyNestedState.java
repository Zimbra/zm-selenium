/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;

public class VerifyNestedState extends CalendarWorkWeekTest {

	public VerifyNestedState() {
		logger.info("New "+ VerifyNestedState.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test( description = "Grantee opens appointment from grantor's calendar and close it without making any changes",
			groups = { "functional" })
			
	public void VerifyNestedState_01() throws HarnessException {
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();
		String foldername1 = "folder" + ConfigProperties.getUniqueString();
		String foldername2 = "folder" + ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
				
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder name='" + foldername1 + "' l='1' view='appointment'/>"
			+	"</CreateFolderRequest>");
	
	    FolderItem folder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername1);
				
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
			+		"<folder name='" + foldername2 + "' l='" + folder1.getId() + "' view='appointment'/>"
			+	"</CreateFolderRequest>");
	
	    FolderItem folder2 = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername2);
	    
		// Create appointment
		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
			+		"<m l='"+ folder2.getId() +"' >"
			+			"<inv>"
			+				"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='0' name='"+ apptSubject +"' >"
			+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
			+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.TimeZoneEST.getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.TimeZoneEST.getID() +"'/>"
			+					"<or a='"+ app.zGetActiveAccount().EmailAddress + "'/>"
			+				"</comp>"
			+			"</inv>"
			+			"<su>"+ apptSubject + "</su>"
			+			"<mp ct='text/plain'>"
			+				"<content>"+ apptContent +"</content>"
			+			"</mp>"
			+		"</m>"
			+	"</CreateAppointmentRequest>");
	
		// Mark ON to mounted calendar folder and select the appointment
		app.zTreeCalendar.zMarkOnOffCalendarFolder("Calendar");
		app.zTreeCalendar.zMarkOnOffCalendarFolder(foldername2);
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");
        
	    this.app.zPageLogin.zNavigateTo();
	    this.startingPage.zNavigateTo();
	    SleepUtil.sleepLong();
	    app.zTreeCalendar.zVerifyCalendarChecked(true, folder1.getId());
	    SleepUtil.sleepLong();
	    app.zTreeCalendar.zVerifyCalendarChecked(true, folder2.getId());
		       
	}

}
