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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.week.recurring;

import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.*;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.*;

public class DeleteInstance extends AjaxCore {

	public DeleteInstance() {
		logger.info("New "+ DeleteInstance.class.getCanonicalName());

		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L; {
				put("zimbraPrefCalendarInitialView", "week");
			}
		};
	}


	@Bugs (ids = "69132")
	@Test (description = "Delete instance of recurring appointment (every month) using toolbar button in week view",
			groups = { "functional-duplicate" } )

	public void DeleteInstance_01() throws HarnessException {

		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		ZDate endTime   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='MON'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        DialogWarning dialogSeriesOrInstance = (DialogWarning)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
        dialogSeriesOrInstance.zPressButton(Button.B_DELETE_THIS_INSTANCE);
        DialogWarning confirmDelete = (DialogWarning)dialogSeriesOrInstance.zPressButton(Button.B_OK);
        confirmDelete.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ apptSubject +"</query>"
			+	"</SearchRequest>");

		Element[] appts = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist ... See also bug 63412");

		// Verify the appointment is not in the GUI view
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");
	}


	@Bugs (ids = "69132")
	@Test (description = "Delete instance of recurring appointment (every year) using context menu in week view",
			groups = { "functional-skip" } )

	public void DeleteInstance_02() throws HarnessException {

		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();

		// Absolute dates in UTC zone
		Calendar now = Calendar.getInstance();
		ZDate startTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
		ZDate endTime   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);

		app.zGetActiveAccount().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endTime.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='YEA'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");

		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);

        DialogWarning dialogSeriesOrInstance = (DialogWarning)app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_INSTANCE_MENU, Button.O_DELETE_MENU, apptSubject);;
        dialogSeriesOrInstance.zPressButton(Button.B_YES);

		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ apptSubject +"</query>"
			+	"</SearchRequest>");

		Element[] appts = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist ... See also bug 63412");

		// Verify the appointment is not in the GUI view
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");
	}
}