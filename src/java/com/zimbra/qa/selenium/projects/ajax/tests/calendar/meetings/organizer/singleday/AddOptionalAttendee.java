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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ZTimeZone;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees;

public class AddOptionalAttendee extends CalendarWorkWeekTest {	
	
	public AddOptionalAttendee() {
		logger.info("New "+ AddOptionalAttendee.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	
	@Test( description = "Add optional attendee by typing in the field and resend the appointment",
			groups = { "functional", "L2"})
		
	public void AddOptionalAttendee_01() throws HarnessException {
		
		// Create a meeting
		AppointmentItem appt = new AppointmentItem();
			
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptOptionalAttendee = ZimbraAccount.AccountA().EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Verify appointment displayed in current view");

        appt.setOptional(apptOptionalAttendee);
        
        // Add optional attendee by typing in the field and resend the appointment
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm.zFill(appt);
        apptForm.zToolbarPressButton(Button.B_SHOW_OPTIONAL); // Hiding for next test otherwise as per application behaviour optional UI remains enabled.
		apptForm.zSubmit();
   
  	  	// Verify that optional attendee present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getOptional(),apptOptionalAttendee , "optional attendee: Verify the appointment data");
	
		// Verify optional attendee free/busy status
		String attendeeStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptOptionalAttendee +"']", "ptst");
		ZAssert.assertEquals(attendeeStatus, "NE", "Verify optional attendee free/busy status");
		
		// Verify optional attendee receives meeting invitation message
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify optional attendee receives meeting invitation message");
	}
	
	
	@Test( description = "Create appt and add optional attendee to existing appointment from contact picker",
			groups = { "functional", "L2"})
	
	public void CreateMeetingBySelectAttendees_01() throws HarnessException {
		
		// Create a meeting
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		
		String apptSubject, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ConfigProperties.getUniqueString();
		String apptOptionalAttendee = ZimbraAccount.AccountA().EmailAddress;		
		
		apptContent = ConfigProperties.getUniqueString();
		appt.setSubject(apptSubject);
		appt.setOptional(apptOptionalAttendee);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		appt.setContent(apptContent);
	
		// Compose appointment and send it to invitee
		
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
        
        apptForm.zToolbarPressButton(Button.B_OPTIONAL);
        DialogFindAttendees dialogFindAttendees = (DialogFindAttendees) new DialogFindAttendees(app, app.zPageCalendar);

        // Type optional attendee name in search box & perform search
        dialogFindAttendees.zType(Locators.ContactPickerSerachField, apptOptionalAttendee);
        dialogFindAttendees.zClickButton(Button.B_SEARCH);
        dialogFindAttendees.zWaitForBusyOverlay();
        
        // Add optional attendee  and resend the appointment
        dialogFindAttendees.zClick(Locators.ContactPickerFirstContact);
        dialogFindAttendees.zClickButton(Button.B_CHOOSE_CONTACT_FROM_PICKER);
        dialogFindAttendees.zWaitForBusyOverlay();
        dialogFindAttendees.zClickButton(Button.B_OK);
        apptForm.zSubmit();
      
        // Verify optional attendee receives meeting invitation message
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the optional attendee's inbox");
	
		// Verify that optional attendee present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getOptional(), apptOptionalAttendee, "optional attendee: Verify the appointment data");
		
		// Verify appointment is present in optional attendee's calendar
		AppointmentItem addedOptionalAttendee = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(addedOptionalAttendee, "Verify meeting invite is present in optional attendee's calendar");
		
		// Verify optional attendee free/busy status
		String optionalAttendeeStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptOptionalAttendee +"']", "ptst");
		ZAssert.assertEquals(optionalAttendeeStatus, "NE", "Verify optional attendee free/busy status");
		
	}
}
