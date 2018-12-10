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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.gui;

import java.util.Calendar;
import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.ZimbraURI;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class NavigateToAppCalendarURL extends AjaxCore {

	public NavigateToAppCalendarURL() {
		logger.info("New " + NavigateToAppCalendarURL.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "96868")
	@Test (description = "Bug 62137 - 'http://<server>?app=calendar' broken with/without login to zcs",
			groups = { "smoke", "L1" })

	public void NavigateToAppCalendarURL_01() throws HarnessException {

		// Creating object for appointment data
		String tz, apptSubject, apptBody, apptAttendee;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.AccountA().EmailAddress;

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 19, 0, 0);

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

        // Reload the application, with app=calendar query parameter
        ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
        uri.addQuery("app", "calendar");
        app.zPageCalendar.sOpen(uri.toString());

        // Verify the page becomes active
        app.zPageMain.zWaitForActive();
        app.zPageCalendar.zWaitForActive();
        ZAssert.assertTrue(app.zPageCalendar.zIsActive(), "Verify the page becomes active");

        // Verify the appointment appears in the view
		boolean found = false;
		List<AppointmentItem> items = app.zPageCalendar.zListGetAppointments();
		for (AppointmentItem item : items ) {
			if ( apptSubject.equals(item.getSubject()) ) {
				found = true;
				break;
			}
		}

		ZAssert.assertTrue(found, "Verify appt gets displayed in work week view");
	}
}