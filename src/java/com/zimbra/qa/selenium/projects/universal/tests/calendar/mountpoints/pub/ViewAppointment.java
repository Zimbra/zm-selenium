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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.mountpoints.pub;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;

public class ViewAppointment extends CalendarWorkWeekTest {

	public ViewAppointment() {
		logger.info("New "+ ViewAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "47629")
	@Test( description = " HTML view of public shared calendar not showing appointments",
			groups = { "sanity", "L0" })
			
	public void ViewAppointment_01() throws HarnessException {
		
		String apptSubject = ConfigProperties.getUniqueString();
		String apptContent = ConfigProperties.getUniqueString();
		String foldername = "folder" + ConfigProperties.getUniqueString();
		
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 02, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 03, 0, 0);
		
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
				+			"<grant inh='1' gt='pub' perm='r' view='appointment'/>"
				+		"</action>"
				+	"</FolderActionRequest>");
				
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
				+				"<content>" + apptContent + "</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
		
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        if ( ConfigProperties.getStringProperty("server.host").equals(ConfigProperties.getStringProperty("mta.host")) ) {
        	app.zPageCalendar.sOpen("https://" + ConfigProperties.getStringProperty("server.host") + ":8443/home/" + ZimbraAccount.Account1().EmailAddress + "/Calendar/" + foldername + ".html");
		} else {
			app.zPageCalendar.sOpen("https://" + ZimbraAccount.Account1().zGetAccountStoreHost() + "/home/" + ZimbraAccount.Account1().EmailAddress + "/Calendar/" + foldername + ".html");
		}
        
        ZAssert.assertTrue(app.zPageCalendar.sIsElementPresent("css=div[class^='ZhCalMonthAppt']:contains('" + apptSubject + "')"), "Appointment is visible");
        ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
        app.zPageCalendar.sOpen(uri.toString());

	}

}
