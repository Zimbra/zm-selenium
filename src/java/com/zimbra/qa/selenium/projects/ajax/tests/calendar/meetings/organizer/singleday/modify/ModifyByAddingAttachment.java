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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.modify;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import org.testng.annotations.*;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class ModifyByAddingAttachment extends CalendarWorkWeekTest {	
	
	public ModifyByAddingAttachment() {
		logger.info("New "+ ModifyByAddingAttachment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "104231")
	@Test(description = "Modify meeting by adding attachment to an existing invite",
			groups = {"windows" })
			
	public void ModifyByAddingAttachment_01() throws HarnessException {
		
		try{
				// Create a meeting			
				String tz = ZTimeZone.TimeZoneEST.getID();
				String apptSubject = ZimbraSeleniumProperties.getUniqueString();
				String apptAttendee1 = ZimbraAccount.Account1().EmailAddress;
				final String fileName = "testtextfile.txt";
				final String filePath = ZimbraSeleniumProperties.getBaseDirectory() + "\\data\\public\\other\\" + fileName;
				
				// Absolute dates in UTC zone
				Calendar now = this.calendarWeekDayUTC;
				ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
				ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
				
				app.zGetActiveAccount().soapSend(
		                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
		                     "<m>"+
		                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
		                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
		                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
		                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
		                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "' d='2'/>" +
		                     	"</inv>" +
		                     	"<e a='"+ ZimbraAccount.Account1().EmailAddress +"' t='t'/>" +
		                     	"<mp content-type='text/plain'>" +
		                     		"<content>"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
		                     	"</mp>" +
		                     "<su>"+ apptSubject +"</su>" +
		                     "</m>" +
		               "</CreateAppointmentRequest>");
		        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		        
		        // Add attendee2 and resend the appointment
		        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
				apptForm.zToolbarPressButton(Button.B_ATTACH);
				apptForm.zToolbarPressButton(Button.B_BROWSE);
				zUpload(filePath);
		
		        apptForm.zToolbarPressButton(Button.B_SEND);
		 
		        // Verify that attendee1 present in the appointment
		        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
				ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
				Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
				ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the sent meeting");
				
				// Verify appointment is present in attendee1's calendar
				AppointmentItem addeddAttendee = AppointmentItem.importFromSOAP(ZimbraAccount.Account1(), "subject:("+ apptSubject +")");
				ZAssert.assertNotNull(addeddAttendee, "Verify meeting invite is present in attendee2's calendar");
				 nodes = ZimbraAccount.Account1().soapSelectNodes("//mail:mp[@filename='" + fileName + "']");
				ZAssert.assertEquals(nodes.length, 1, "Verify attachment exist in the received meeting");
				
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
