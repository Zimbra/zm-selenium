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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.util.Calendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class CreateMeetingByChangingBodyTextFormat extends CalendarWorkWeekTest {

	public CreateMeetingByChangingBodyTextFormat() {
		logger.info("New "+ CreateMeetingByChangingBodyTextFormat.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
    @Bugs(ids = "103714")	
	@Test( description = "Compose a meeting with body text as plain, change to HTML and verify if text is not lost",
			groups = { "smoke", "L1" })
			
	public void PlainText_To_HTML_01() throws HarnessException {
    	
		// Set mail Compose preference to Text format
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		ZimbraAccount.AccountZCS().modifyAccountPreferences(startingAccountPreferences);
		
		// Refresh UI
		app.zPageMain.sRefresh();
		app.zPageCalendar.zNavigateTo();
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		Calendar now = this.calendarWeekDayUTC;
		
		String apptSubject, apptContent;
		apptSubject = ConfigProperties.getUniqueString();
		apptContent = ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setContent(apptContent);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		
		// Compose appointment in enter body text in plain first.
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		
		//Change text format to HTML
		apptForm.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_HTML);
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), apptContent, "Verify content is not lost");

		apptForm.zToolbarPressButton(Button.B_SAVEANDCLOSE);
		
		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
		
	}

    @Bugs(ids = "103714")	
	@Test( description = "Compose a meeting with body text as plain, change to HTML and verify if text is not lost",
			groups = { "smoke", "L1" })
			
	public void HTML_To_PlainText_01() throws HarnessException {
    	
		// Set mail Compose preference to Text format
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		ZimbraAccount.AccountZCS().modifyAccountPreferences(startingAccountPreferences);
		
		// Refresh UI
		app.zPageMain.sRefresh();
		app.zPageCalendar.zNavigateTo();
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		Calendar now = this.calendarWeekDayUTC;
		
		String apptSubject, apptContent;
		apptSubject = ConfigProperties.getUniqueString();
		apptContent = ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setContent(apptContent);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		
		// Compose appointment in enter body text in plain first.
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		
		//Changed text format to plain text.
		DialogWarning dialog = (DialogWarning)apptForm.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_PLAIN_TEXT);
		dialog.zClickButton(Button.B_OK);

		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), apptContent, "Verify content is not lost");
		apptForm.zToolbarPressButton(Button.B_SAVEANDCLOSE);
		
		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
		
	}
    
    @Bugs(ids = "103797")	
	@Test( description = " Description of a previous appointment comes up when changing mode from plain-text to html ",
			groups = { "functional", "L2" })
			
	public void PlainText_To_HTML_02() throws HarnessException {
    	
		// Set mail Compose preference to Text format
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		ZimbraAccount.AccountZCS().modifyAccountPreferences(startingAccountPreferences);
		
		// Refresh UI
		app.zPageMain.sRefresh();
		app.zPageCalendar.zNavigateTo();
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		Calendar now = this.calendarWeekDayUTC;
		
		String apptSubject1, apptContent1, apptSubject2, apptContent2;
		apptSubject1 = ConfigProperties.getUniqueString();
		apptContent1 = ConfigProperties.getUniqueString();
		
		apptSubject2 = ConfigProperties.getUniqueString();
		apptContent2 = ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject1);
		appt.setContent(apptContent1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		
		// Compose appointment in enter body text in plain first.
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		SleepUtil.sleepSmall();
		//Change text format to HTML
		apptForm.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_HTML);
		SleepUtil.sleepSmall();
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), apptContent1, "Verify content is not lost");
		apptForm.zSubmit();

		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
		
		appt.setSubject(apptSubject2);
		appt.setContent(apptContent2);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		
		// Compose appointment in enter body text in plain first.
		apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		//Change text format to HTML
		SleepUtil.sleepLong();
		apptForm.zToolbarPressPulldown(Button.B_OPTIONS, Button.O_FORMAT_AS_HTML);
		ZAssert.assertStringContains(apptForm.zGetApptBodyHtml(), apptContent2, "Verify content is not lost");
		apptForm.zSubmit();

		
		// Verify the new appointment exists on the server
		actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
		
	}

}
