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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.singleday.modify;

import java.util.Calendar;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;

public class ModifyCalendarFolder extends CalendarWorkWeekTest {

	public ModifyCalendarFolder() {
		logger.info("New "+ ModifyCalendarFolder.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;
	}
	
	@Test (description = "Edit appointment and modify calendar folder",
			groups = { "smoke" })
	
	public void ModifyCalendarFolder_01() throws HarnessException {
		
		// Creating objects for appointment data
		String tz, apptSubject, apptContent, apptCalendar;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptContent = ConfigProperties.getUniqueString();
		apptCalendar = ConfigProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 2, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 4, 0, 0);
		
		// Create new calendar folder
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ apptCalendar +"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");
		FolderItem apptCal = FolderItem.importFromSOAP(app.zGetActiveAccount(), apptCalendar);
		
        app.zGetActiveAccount().soapSend(
    			"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
    		+		"<m>"
    		+			"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
    		+				"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>"
    		+				"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" 
    		+			"</inv>" 
    		+			"<mp content-type='text/plain'>" 
    		+				"<content>" + apptContent + "</content>" 
    		+			"</mp>"
    		+			"<su>" + apptSubject + "</su>" 
    		+		"</m>" 
    		+	"</CreateAppointmentRequest>");
        app.zPageCalendar.zRefresh();
        
        // Select appointment and modify calendar folder
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.B_EDIT, apptSubject);
        apptForm.zSelectCalendarFolder(apptCalendar);
        apptForm.zSubmit();
        
        // Verify appointment is moved to selected folder
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
        ZAssert.assertEquals(actual.getFolder(), apptCal.getId(), "Verify appointment is moved to selected folder");
	}
	
}
