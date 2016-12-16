/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.modify;

import java.util.Calendar;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class ModifyByChangingTime extends CalendarWorkWeekTest {	
	
	public ModifyByChangingTime() {
		logger.info("New "+ ModifyByChangingTime.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "102759")
	@Test( description = "Rescheduled appointments do not show the correct time in the summary",
			groups = { "functional", "L2" })
			
	public void ModifyByChangingTime_01() throws HarnessException {
		super.startingAccountPreferences.put("zimbraPrefGroupMailBy", "message");
		ZimbraAccount.Account1().modifyAccountPreferences(startingAccountPreferences);
		
		// Create a meeting			
		AppointmentItem appt = new AppointmentItem();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee1 = ZimbraAccount.Account1().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		
		ZDate changedStartUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate changedEndUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(startUTC);
		appt.setEndTime(endUTC);
	
		// Compose appointment and send it to attendee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmit();
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment displayed in current view");

        // Modify time and re-send the appointment
		app.zPageCalendar.zNavigateTo();
		SleepUtil.sleepMedium(); //"Unable to determine locator for appointment" issue here
		AppointmentItem appt1 = new AppointmentItem();
		appt1.setStartTime(changedStartUTC);
		appt1.setEndTime(changedEndUTC);
        FormApptNew apptForm1 = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm1.zFill(appt1);
        apptForm1.zSubmit();
        
        //Login as attendee and verify that correct time is mentioned in summary
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(ZimbraAccount.Account1());
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		
		String invite1Locator = "css=table[id='zv__TV__TV-main_MSG_hdrTable'] td[class='LabelColValue InvChanged']:contains('" + changedStartUTC.tohh_mm_aa() + "')";
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(invite1Locator), "Mail present");
		
				
	}
	
}
