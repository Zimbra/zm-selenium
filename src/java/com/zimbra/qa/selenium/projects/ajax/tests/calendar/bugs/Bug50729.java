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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.bugs;

import java.util.Calendar;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Locators;

@SuppressWarnings("unused")
public class Bug50729 extends CalendarWorkWeekTest {

	public Bug50729() {
		logger.info("New " + Bug50729.class.getCanonicalName());
		
		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;
		
		// Make sure we are using an account with day view
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "workWeek");
		}};
	}

	@Test(
			description = "Bug 50729 - 'No such message exists' exception while replying to appointment via 'Include Original As Attachment'", 
			groups = { "functional" })
	public void Bug50729_01() throws HarnessException {

		// Creating object for appointment data
		String tz, apptSubject, apptBody, apptAttendee;
		tz = ZTimeZone.TimeZoneEST.getID();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
        app.zGetActiveAccount().soapSend(
                          "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                               "<m>"+
                               "<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                               "<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                               "<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                               "<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                               "<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee + "' d='2'/>" + 
                               "</inv>" +
                               "<mp content-type='text/plain'>" +
                               "<content>"+ apptBody +"</content>" +
                               "</mp>" +
                               "<su>"+ apptSubject +"</su>" +
                               "</m>" +
                         "</CreateAppointmentRequest>");
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");

        // Open appointment & close it
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_REPLY_TO_ALL_MENU, apptSubject);
        FormMailNew mailComposeForm = (FormMailNew) new FormMailNew(app).zToolbarPressButton(Button.B_OPTIONS);
        ZAssert.assertEquals(app.zPageMail.sIsElementPresent(Locators.zIncludeOriginalAsAttachmentMenu), false, "Verify 'Include Original As Attachment' menu remains disabled");
		
	}
}