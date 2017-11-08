/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.attendee.viewappt;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.*;

public class Delete extends CalendarWorkWeekTest {

	public Delete() {
		logger.info("New "+ Delete.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}

	@Test (description = "Delete meeting invite as an attendee", 
			groups = { "smoke" })
	
	public void DeleteMeeting_01() throws HarnessException {

		// ------------------------ Test data ------------------------------------

		String apptSubject = "appointment" + ConfigProperties.getUniqueString();

		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);

		// --------------- Creating invitation (organizer) ----------------------------

		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>"
				+		"<m>"
				+			"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"
				+				"<s d='"+ startUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<e d='"+ endUTC.toTimeZone(ZTimeZone.getLocalTimeZone().getID()).toYYYYMMDDTHHMMSS() +"' tz='"+ ZTimeZone.getLocalTimeZone().getID() +"'/>"
				+				"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>"
				+				"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>"
				+			"</inv>"
				+			"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>"
				+			"<su>"+ apptSubject +"</su>"
				+			"<mp content-type='text/plain'>"
				+				"<content>content</content>"
				+			"</mp>"
				+		"</m>"
				+	"</CreateAppointmentRequest>");
		app.zPageCalendar.zRefresh();
        
        // Select appointment and delete it
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.B_DELETE, apptSubject);
        app.zPageMail.zPressButton(Button.B_YES);
        // Verify appointment is moved to Trash
 		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is deleted");
 		
 		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ "is:anywhere " + apptSubject +"</query>"
			+	"</SearchRequest>");
	
 		String invId = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
	
 		// Verify appointment is in Trash folder
 		app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ invId +"'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:GetAppointmentResponse//mail:comp", "ciFolder"), "3", "Verify appointment is in Trash folder");
	}
}
