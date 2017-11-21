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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.resources;

import java.util.Calendar;
import java.util.List;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.universal.pages.*;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.DialogFindLocation;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.PageCalendar.Locators;

public class AddLocation extends CalendarWorkWeekPreference {	
	
	public AddLocation() {
		logger.info("New "+ AddLocation.class.getCanonicalName());
	    super.startingPage =  app.zPageCalendar;
	}
	
	
	@Test (description = "Search Location and add into existing meeting invite", groups = { "smoke", "L1" })
	
	public void AddLocation_01() throws HarnessException {
		
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		String apptLocation1 = location.EmailAddress;
    	
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='AC' rsvp='1' a='" + apptAttendee + "' d='2'/>" +
                     	"</inv>" +
                     	"<e a='"+ ZimbraAccount.AccountA().EmailAddress +"' t='t'/>" +
                     	"<mp content-type='text/plain'>" +
                     		"<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     	"</mp>" +
                     "<su>"+ apptSubject +"</su>" +
                     "</m>" +
               "</CreateAppointmentRequest>");
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        
        // Add location and resend the appointment
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm.zToolbarPressButton(Button.B_LOCATION);
        SleepUtil.sleepMedium();
        
        DialogFindLocation dialogFindLocation = (DialogFindLocation) new DialogFindLocation(app, app.zPageCalendar);
        dialogFindLocation.zType(Locators.LocationName, apptLocation1);
        SleepUtil.sleepSmall(); 
        dialogFindLocation.zPressButton(Button.B_SEARCH_LOCATION);
        SleepUtil.sleepMedium(); 
        dialogFindLocation.zPressButton(Button.B_SELECT_LOCATION);
        SleepUtil.sleepMedium();
        dialogFindLocation.zPressButton(Button.B_OK);
        apptForm.zSubmitWithResources();
        
        // Verify location in the appointment is not null
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getLocation(), apptLocation1, "Verify if the Location has been added to the meeting");
		
		// Verify location free/busy status
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation1 +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify location status shows accepted");
	}
	
	
	@Test (description = "Add location to existing appointment and verify F/B", groups = { "smoke", "L1" })
	
	public void AddLocation_02() throws HarnessException {
		
		// Create a meeting
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		
		String tz = ZTimeZone.getLocalTimeZone().getID();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptAttendee = ZimbraAccount.AccountA().EmailAddress;
		String apptLocation = location.EmailAddress;
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		
		app.zGetActiveAccount().soapSend(
                "<CreateAppointmentRequest xmlns='urn:zimbraMail'>" +
                     "<m>"+
                     	"<inv method='REQUEST' type='event' status='CONF' draft='0' class='PUB' fb='B' transp='O' allDay='0' name='"+ apptSubject +"'>"+
                     		"<s d='"+ startUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<e d='"+ endUTC.toTimeZone(tz).toYYYYMMDDTHHMMSS() +"' tz='"+ tz +"'/>" +
                     		"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     		"<at role='REQ' ptst='AC' rsvp='1' a='" + apptAttendee + "' d='2'/>" +
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
        
        // Add location and resend the appointment
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm.zFillField(Field.Location, apptLocation);
        apptForm.zSubmitWithResources();
        
        // Verify location in the appointment
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertEquals(actual.getAttendees(), apptAttendee, "Attendees: Verify the appointment data");
		ZAssert.assertStringContains(actual.getLocation(), apptLocation, "Location: Verify the appointment data");
		
		// Verify location free/busy status
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify location status shows accepted");
	}

	
	@Bugs (ids = "60789")
	@Test (description = "Name lost when using autocomplete to enter location", groups = { "smoke", "L1" } )
	
	public void AddLocation_03() throws HarnessException {
		
		ZimbraResource location = new ZimbraResource(ZimbraResource.Type.LOCATION);
		String locationName = ConfigProperties.getUniqueString();
		String apptSubject = ConfigProperties.getUniqueString();
		String apptLocation = location.EmailAddress;
		
	    ZimbraAdminAccount.GlobalAdmin().soapSend(
	    	      "<ModifyCalendarResourceRequest xmlns='urn:zimbraAdmin'><id>" + location.ZimbraId + "</id>" + 
		    	      "<a n='displayName'>" + locationName + "</a>" + 
	    	      "</ModifyCalendarResourceRequest>");
	    
		ZimbraDomain domain = new ZimbraDomain(location.EmailAddress.split("@")[1]);
		domain.provision();
		domain.syncGalAccount();
		
		// Absolute dates in UTC zone
		String tz = ZTimeZone.getLocalTimeZone().getID();
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 15, 0, 0);
		
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
	    
	    // Add equipment from 'Search Equipment' dialog and send the meeting
	    FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
		List<AutocompleteEntry> entries = apptForm.zAutocompleteFillField(Field.Location, locationName);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(locationName) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		apptForm.zAutocompleteSelectItem(found);
        ZAssert.assertTrue(apptForm.zVerifyLocation(locationName), "Verify appointment location");
        apptForm.zSubmitWithResources();
		
		// Organizer: Search for the appointment (InvId)
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-10).toMillis() +"' calExpandInstEnd='"+ endUTC.addDays(10).toMillis() +"'>"
				+		"<query>"+ apptSubject +"</query>"
				+	"</SearchRequest>");
		
		ZAssert.assertTrue(app.zGetActiveAccount().soapSelectValue("//mail:appt", "loc").contains(apptLocation), "Location: Verify the appointment data");
		
		// Verify Location present in the appointment
	    AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		
		// Verify location free/busy status
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptLocation +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify Location free/busy status");
	}
}
