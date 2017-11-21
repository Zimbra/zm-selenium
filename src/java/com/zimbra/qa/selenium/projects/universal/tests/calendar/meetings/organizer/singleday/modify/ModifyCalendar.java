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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.meetings.organizer.singleday.modify;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew;

public class ModifyCalendar extends CalendarWorkWeekPreference {

	public ModifyCalendar() {
		logger.info("New "+ ModifyCalendar.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}

	@Bugs (ids = "102771")
	@Test (description = "Modify meeting calendar",
			groups = { "functional", "L2" })

	public void ModifyMeetingCalendar_01() throws HarnessException {

		// Create data
		String tz, apptSubject, apptBody, apptAttendee, apptCalendar;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		apptCalendar = ConfigProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		// Create new calendar folder
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ apptCalendar +"' l='"+ root.getId() +"' view='appointment'/>" +
                "</CreateFolderRequest>");
		FolderItem apptCal = FolderItem.importFromSOAP(app.zGetActiveAccount(), apptCalendar);
		
		// Refresh the view
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     "<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     "<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     "<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     "<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     "<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee + "'/>" +
                     "</inv>" +
                     "<e a='"+ apptAttendee +"' t='t'/>" +
                     "<mp content-type='text/plain'>" +
                     "<content>"+ apptBody +"</content>" +
                     "</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // Open appointment and modify calendar folder
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        String locator = "css=td[id$='_folderSelect'] td[id$='_select_container']";
        apptForm.sClickAt(locator, "");

        locator = "//div[@id='z_shell']/div[contains(@id,'_Menu_') and contains(@class, 'DwtMenu')]";
        int count = apptForm.sGetXpathCount(locator);
        for  (int  i = 1; i <= count; i++) {
	    	String calPullDown = locator + "[position()=" + i + "]//tr//*[contains(text(),'" + apptCalendar + "')]";
	    	if (apptForm.zIsVisiblePerPosition(calPullDown, 0, 0)) {
	    	    apptForm.sClickAt(calPullDown, "");
	    	    break;
	    	}
        }
        apptForm.zSubmit();

        // Verify calendar value
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
        ZAssert.assertEquals(actual.getFolder(), apptCal.getId(), "Verify calendar folder value");

	}

}
