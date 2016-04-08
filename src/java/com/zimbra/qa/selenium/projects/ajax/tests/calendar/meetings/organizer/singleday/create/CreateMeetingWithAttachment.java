/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class CreateMeetingWithAttachment extends CalendarWorkWeekTest {

	public CreateMeetingWithAttachment() {
		logger.info("New "+ CreateMeetingWithAttachment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "104231")	
	@Test(description = "Create invite by attaching file",
			groups = { "sanity", "windows" })
			
	public void CreateMeetingWithAttachment_02() throws HarnessException {
	
		try {
		
			// Create appointment data
			AppointmentItem appt = new AppointmentItem();
			// Create file item
			final String fileName = "testtextfile.txt";
			final String filePath = ZimbraSeleniumProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
			
			String apptSubject, apptAttendee1, apptContent;
			Calendar now = this.calendarWeekDayUTC;
			apptSubject = ZimbraSeleniumProperties.getUniqueString();
			apptAttendee1 = ZimbraAccount.Account2().EmailAddress;
			apptContent = ZimbraSeleniumProperties.getUniqueString();
			
			appt.setSubject(apptSubject);
			appt.setAttendees(apptAttendee1);
			appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
			appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
			appt.setContent(apptContent);
		
			// Compose appointment and send it to invitee
			FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
			apptForm.zFill(appt);
			apptForm.zToolbarPressButton(Button.B_ATTACH);
			apptForm.zToolbarPressButton(Button.B_BROWSE);
			zUpload(filePath);
	
			apptForm.zSubmit();
			// Verify appointment exists in current view
	        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");
			
			// Verify appointment exists on the server
			AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
			ZAssert.assertNotNull(actual, "Verify the new appointment is created");
			ZAssert.assertEquals(actual.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
			ZAssert.assertEquals(actual.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
			ZAssert.assertEquals(actual.getContent(), appt.getContent(), "Content: Verify the appointment data");
			Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the sent meeting");
	
			// Verify the attendee receives the meeting
			AppointmentItem received = AppointmentItem.importFromSOAP(ZimbraAccount.Account2(), "subject:("+ appt.getSubject() +")", appt.getStartTime().addDays(-7), appt.getEndTime().addDays(7));
			ZAssert.assertNotNull(received, "Verify the new appointment is created");
			ZAssert.assertEquals(received.getSubject(), appt.getSubject(), "Subject: Verify the appointment data");
			ZAssert.assertEquals(received.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
			ZAssert.assertEquals(received.getContent(), appt.getContent(), "Content: Verify the appointment data");
			
			 nodes = ZimbraAccount.Account2().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
			ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the received meeting");
	
			// Verify the attendee receives the invitation
			MailItem invite = MailItem.importFromSOAP(ZimbraAccount.Account2(), "subject:("+ appt.getSubject() +")");
			ZAssert.assertNotNull(invite, "Verify the invite is received");
			ZAssert.assertEquals(invite.dSubject, appt.getSubject(), "Subject: Verify the appointment data");
		
	        //Login as attendee and accept the invite
	        app.zPageMain.zLogout();
			app.zPageLogin.zLogin(ZimbraAccount.Account2());
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
			SleepUtil.sleepSmall();
			app.zPageCalendar.zNavigateTo();
			// Verify appointment exists in current view
	        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");
				
			// open the appt
	        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_OPEN, apptSubject);
	    	
			// Verify the new appointment has an attachment 
			ZAssert.assertTrue(app.zPageCalendar.zVerifyAttachmentExistsInAppointment(fileName), "Verify attachment exists in the appointment");

		} finally {
			
			Robot robot;
			
			try {
				robot = new Robot();
				robot.delay(250);
				robot.keyPress(KeyEvent.VK_ESCAPE);
				robot.keyRelease(KeyEvent.VK_ESCAPE);
				robot.delay(50);
				
			} catch (AWTException e) {
				e.printStackTrace();
			}
			
		}
	}
}
