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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.util.Calendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;


public class CreateMeetingWRTComposePreference extends CalendarWorkWeekTest {

	public CreateMeetingWRTComposePreference() {
		logger.info("New "+ CreateMeetingWRTComposePreference.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		
	}
	
	
	
	@Test(	description = "Verify New appointment creation is based on mail compose preference set to HTML",
			groups = { "functional" })
	public void CreateMeetingWRTComposePreference_01() throws HarnessException {
		
		// Set mail Compose preference to HTML format
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		ZimbraAccount.AccountZWC().modifyAccountPreferences(startingAccountPreferences);
		
		// Logout and login to pick up the preference changes
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();
		
		AppointmentItem appt = new AppointmentItem();
		String apptSubject, apptAttendee1, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		appt.setContent(apptContent);
	
		// Verify Compose appointment format obeys the mail compose format and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertTrue(apptForm.zVerifyComposeFormatHTML(),"Verify that the HTML formatting toolbar is visible on compose appointment page");
	
		apptForm.zFill(appt);	
		apptForm.zSubmit();
	    
		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");

	}
	
	@Test(	description = "Verify New appointment creation is based on mail compose preference set to TEXT",
			groups = { "functional" })
	public void CreateMeetingWRTComposePreference_02() throws HarnessException {
		
		// Set mail Compose preference to Text format
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		ZimbraAccount.AccountZWC().modifyAccountPreferences(startingAccountPreferences);
		
		// Logout and login to pick up the preference changes
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();
		
		AppointmentItem appt = new AppointmentItem();
		String apptSubject, apptAttendee1, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		appt.setContent(apptContent);
	
		// Verify Compose appointment format obeys the mail compose format and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertFalse(apptForm.zVerifyComposeFormatHTML(),"Verify that the HTML formatting toolbar is NOT visible on compose appointment page");

		apptForm.zFill(appt);	
		apptForm.zSubmit();
	  
		// Verify the attendee receives the invitation
		MailItem invite = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ appt.getSubject() +")");
		ZAssert.assertNotNull(invite, "Verify the invite is received");
		ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");

	}

}
