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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.attendee.recurring.series;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class Forward extends CalendarWorkWeekTest {	
	
	public Forward() {
		logger.info("New "+ Forward.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "95961")
	@Test(description = "Forward entire series by changing content",
			groups = { "smoke" })
			
	public void ForwardMeeting_01() throws HarnessException {
				
		// Creating a meeting
		Calendar now = this.calendarWeekDayUTC;
		String tz = ZTimeZone.TimeZoneEST.getID();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 5, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 7, 0, 0);
		
		String apptSubject = ZimbraSeleniumProperties.getUniqueString();
		String apptBody = "body" + ZimbraSeleniumProperties.getUniqueString();
		String ForwardContent = ZimbraSeleniumProperties.getUniqueString();
		String attendee2 = ZimbraAccount.AccountB().EmailAddress;
		
		ZimbraAccount.AccountA().soapSend(
				"<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
					"<m>"+
						"<inv method='REQUEST' type='event' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
							"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
							"<or a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
							"<at role='REQ' ptst='NE' rsvp='1' a='" + app.zGetActiveAccount().EmailAddress + "'/>" +
							"<recur>" +
								"<add>" +
									"<rule freq='DAI'>" +
										"<interval ival='1'/>" +
									"</rule>" +
								"</add>" +
							"</recur>" +
						"</inv>" +
						"<e a='"+ app.zGetActiveAccount().EmailAddress +"' t='t'/>" +
						"<mp content-type='text/plain'>" +
							"<content>"+ apptBody +"</content>" +
						"</mp>" +
						"<su>"+ apptSubject +"</su>" +
					"</m>" +
				"</CreateAppointmentRequest>");
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");
        
        // Forward appointment to different attendee
        app.zPageCalendar.zListItem(Action.A_RIGHTCLICK, Button.O_SERIES_MENU, Button.O_FORWARD_MENU, apptSubject);
        
        FormApptNew form = new FormApptNew(app);
        form.zFillField(Field.To, attendee2);
        form.zFillField(Field.Body, ForwardContent);
        form.zSubmit();
		
		// Verify the new invitation appears in the inbox
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + (char)34 + "Fwd: " + apptSubject + (char)34 + ")" + " " + "content:(" + ForwardContent +")" + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountB().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify the new invitation appears in the attendee's inbox");
		
		ZimbraAccount.AccountB().soapSend(
				"<GetMsgRequest  xmlns='urn:zimbraMail'>"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		// Verify only one appointment is in the calendar
		AppointmentItem a = AppointmentItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ apptSubject +")"  + " " + "content:(" + ForwardContent +")");
		ZAssert.assertNotNull(a, "Verify only one appointment matches in the calendar");
		
		// Verify meeting notification to organizer
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + "subject:(" + (char)34 + "Meeting Forward Notification: " + apptSubject + (char)34 + ")" + " " + "content:(" + attendee2 +")" + "</query>"
			+	"</SearchRequest>");
		id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify meeting notification to organizer");
		
		ZimbraAccount.AccountB().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
			+		"<query>"+ apptSubject +"</query>"
			+	"</SearchRequest>");
	
		String attendeeInvId = ZimbraAccount.AccountB().soapSelectValue("//mail:appt", "invId");
		ZimbraAccount.AccountB().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ attendeeInvId +"'/>");
		
		String ruleFrequency = ZimbraAccount.AccountB().soapSelectValue("//mail:appt//mail:rule", "freq");
		String interval = ZimbraAccount.AccountB().soapSelectValue("//mail:appt//mail:interval", "ival");
		ZAssert.assertEquals(ruleFrequency, "DAI", "Repeat frequency: Verify the appointment data");
		ZAssert.assertEquals(interval, "1", "Repeat interval: Verify the appointment data");

	}
	
}
