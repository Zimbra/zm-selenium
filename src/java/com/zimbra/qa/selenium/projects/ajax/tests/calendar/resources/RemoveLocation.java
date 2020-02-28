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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.resources;

import java.util.Calendar;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;

public class RemoveLocation extends AjaxCore {

	public RemoveLocation() {
		logger.info("New "+ RemoveLocation.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "77588,96593")
	@Test (description = "Remove location from existing appointment and verify F/B",
			groups = { "bhr" })

	public void RemoveLocation_01() throws HarnessException {
		AppointmentItem appt = new AppointmentItem();

		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		String apptLocation = ExecuteHarnessMain.locations.get("location1")[1];

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);

		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"' loc='"+ apptLocation+"'>" +
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee + "' d='2'/>" +
                     		"<at cutype='RES' a='" + apptLocation + "' rsvp='1' role='REQ' url='" + apptLocation + "' ptst='AC'/>" +
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<e a='"+ apptLocation +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        // Remove location and resend the appointment
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm.zRemoveLocation(apptLocation);
        apptForm.zSubmitWithResources();

        // Verify that location doesn't present in the appointment
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee, "Attendees: Verify the appointment data");
		ZAssert.assertNull(appt.getLocation(), "Location: Verify the appointment data");

		// Verify location free/busy status
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst");
		ZAssert.assertNull(locationStatus, "Verify that location status shows null");
	}
}