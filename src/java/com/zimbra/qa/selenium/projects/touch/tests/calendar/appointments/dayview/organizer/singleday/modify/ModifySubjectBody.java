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
package com.zimbra.qa.selenium.projects.touch.tests.calendar.appointments.dayview.organizer.singleday.modify;

import java.util.Calendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.touch.ui.calendar.FormApptNew.Field;

public class ModifySubjectBody extends CalendarWorkWeekTest {

	public ModifySubjectBody() {
		logger.info("New "+ ModifySubjectBody.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test (description = "Modify meeting subject and body",
			groups = { "smoke" })
			
	public void ModifySubjectBody_01() throws HarnessException {
		
		String tz, apptSubject, apptBody, apptAttendee1, modifiedApptSubject, modifiedApptBody;
		
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = ConfigProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		modifiedApptSubject = ConfigProperties.getUniqueString();
        modifiedApptBody = ConfigProperties.getUniqueString();
        
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
					"<at role='REQ' ptst='NE' rsvp='1' a='" + apptAttendee1 + "'/>" + 
					"</inv>" +
					"<e a='"+ apptAttendee1 +"' t='t'/>" +
					"<mp content-type='text/plain'>" +
					"<content>"+ apptBody +"</content>" +
					"</mp>" +
					"<su>"+ apptSubject +"</su>" +
					"</m>" +
			"</CreateAppointmentRequest>");
        
        // Open appointment and modify subject and content
        app.zPageCalendar.zRefresh();
        
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_LEFTCLICK, Button.B_EDIT, apptSubject);
        apptForm.zFillField(Field.Subject, modifiedApptSubject);
        apptForm.zFillField(Field.Body, modifiedApptBody);
        apptForm.zSubmit();
        
        // Use GetAppointmentRequest to verify the changes are saved
        AppointmentItem modifyAppt = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ modifiedApptSubject +")");
        ZAssert.assertNotNull(modifyAppt, "Verify the modified appointment appears on the server");
        
        ZAssert.assertEquals(modifyAppt.getSubject(), modifiedApptSubject, "Subject: Verify modified appointment subject");
        ZAssert.assertStringContains(modifyAppt.getAttendees(), apptAttendee1, "Attendee1: Verify modified attendee");
        ZAssert.assertStringContains(modifyAppt.getContent(), modifiedApptBody, "Body: Verify modified appointment body");
        
        // Verify attendee1 receives meeting invitation message
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ modifiedApptSubject +")" + " " + "content:" + modifiedApptBody + "</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the attendee1's inbox");
		
	}
}
