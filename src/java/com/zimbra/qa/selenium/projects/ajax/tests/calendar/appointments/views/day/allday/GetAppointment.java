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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.day.allday;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class GetAppointment extends AjaxCore {

	public GetAppointment() {
		logger.info("New "+ GetAppointment.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L; {
				put("zimbraPrefCalendarInitialView", "day");
			}
		};
	}

	
	@Bugs (ids = "69132")
	@Test (description = "View a basic all-day appointment in day view",
			groups = { "smoke", "L1" })
	
	public void GetAllDayAppointment_01() throws HarnessException {

		// Create the appointment on the server
		String apptSubject = ConfigProperties.getUniqueString();
		String apptLocation = "location" + ConfigProperties.getUniqueString();
		String apptBody = "content" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 18, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 19, 0, 0);

		// Get local timezone value
		String tz = ZTimeZone.getLocalTimeZone().getID();

		// Create a meeting request from AccountA to the test account
		ZimbraAccount.AccountA().soapSend(
					"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<inv>" +
								"<comp status='CONF' fb='B' class='PUB' transp='O' allDay='1' name='"+ apptSubject +"' loc='"+ apptLocation +"'>" +
									"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
									"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
									"<at role='REQ' ptst='NE' rsvp='1' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
									"<or a='"+ ZimbraAccount.AccountA().EmailAddress + "'/>" +
								"</comp>" +
							"</inv>" +
							"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>" +
							"<su>"+ apptSubject + "</su>" +
							"<mp ct='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
							"</mp>" +
						"</m>" +
					"</CreateAppointmentRequest>");

		AppointmentItem appt = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")", startUTC.addDays(-7), endUTC.addDays(7));
		ZAssert.assertNotNull(appt, "Verify the new appointment is created");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");
	}
}