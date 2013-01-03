package com.zimbra.qa.selenium.projects.ajax.tests.calendar.meetings.organizer.singleday.create;

import java.awt.event.KeyEvent;
import java.util.Calendar;
import org.testng.annotations.*;

import com.thoughtworks.selenium.DefaultSelenium;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmDeleteOrganizer;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindLocation;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogSendUpdatetoAttendees;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.PageCalendar;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogFindAttendees.Locators;;

@SuppressWarnings("unused")
public class CreateMeetingBySelectAttendees extends CalendarWorkWeekTest {	
	
	public CreateMeetingBySelectAttendees() {
		logger.info("New "+ CreateMeetingBySelectAttendees.class.getCanonicalName());
		
	}
	

	@Test(description = "Create appt and Add attendee to existing appointment from contact picker",
			groups = { "functional" })
	public void CreateMeetingBySelectAttendees_01() throws HarnessException {
		
		// Create a meeting
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		
		String apptSubject, apptAttendee1, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		appt.setSubject(apptSubject);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		appt.setContent(apptContent);
	
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt); 
        apptForm.zToolbarPressButton(Button.B_TO);
        DialogFindAttendees dialogFindAttendees = (DialogFindAttendees) new DialogFindAttendees(app, app.zPageCalendar);
   
        // Type attendee name in search box & perform search
        AppointmentItem apptSearchForm = new AppointmentItem();
        apptSearchForm.setAttendeeName(apptAttendee1);
        // fill the search form	
        dialogFindAttendees.zFill(apptSearchForm);
        // choose the contact and select it
        dialogFindAttendees.zClickButton(Button.B_SEARCH);
        dialogFindAttendees.zClickButton(Button.B_SELECT_FIRST_CONTACT);
        dialogFindAttendees.zClickButton(Button.B_CHOOSE_CONTACT_FROM_PICKER);
        dialogFindAttendees.zClickButton(Button.B_OK);
        
        // send the  appt
        apptForm.zToolbarPressButton(Button.B_SEND);
		apptForm.zSubmit();

        // Verify attendee1 receives meeting invitation message
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ apptSubject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		ZAssert.assertNotNull(id, "Verify new invitation appears in the attendee1's inbox");
 
		
		// Verify that attendee1 present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getAttendees(), apptAttendee1, "Attendees: Verify the appointment data");
		
		// Verify appointment is present in attendee1's calendar
		AppointmentItem addeddAttendee = AppointmentItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(addeddAttendee, "Verify meeting invite is present in attendee1's calendar");
	
	}
}
