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
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.universal.pages.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.DialogFindEquipment;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew.Locators;

public class AddEquipment extends CalendarWorkWeekPreference {	
	
	public AddEquipment() {
		logger.info("New "+ AddEquipment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	
	@Test (description = "Add Equipment to existing appointment by typing equipment name and verify F/B",
			groups = { "smoke", "L1" })
	
	public void AddEquipment_01() throws HarnessException {
		
		// Create a meeting
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);
		String apptSubject = ConfigProperties.getUniqueString();
		String apptEquipment = equipment.EmailAddress;
		
		// Absolute dates in UTC zone
		String tz = ZTimeZone.getLocalTimeZone().getID();
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
      
        // Set meeting data 
        appt.setEquipment(apptEquipment);
       
        // Modify the meeting , add equipment by typing in the field 
        FormApptNew apptForm = (FormApptNew)app.zPageCalendar.zListItem(Action.A_DOUBLECLICK, apptSubject);
        apptForm.zFill(appt);
		List<AutocompleteEntry> entries = apptForm.zAutocompleteFillField(Field.Equipment, apptEquipment);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(apptEquipment) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");
		apptForm.zAutocompleteSelectItem(found);
        ZAssert.assertTrue(apptForm.zVerifyEquipment(apptEquipment), "Verify appointment equipment");
        apptForm.zToolbarPressButton(Button.B_SHOW_EQUIPMENT); // Hiding for next test otherwise as per application behaviour equipment UI remains enabled.
        apptForm.zSubmitWithResources();

        // Verify equipment in the appointment	
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the meeting shows Subject correctly");
		ZAssert.assertEquals(actual.getEquipment(), apptEquipment, "equipment: Verify the meeting shows equipment correctly");
		
		// Verify equipment free/busy status
		String equipmentStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(equipmentStatus, "AC", "Verify equipment status shows accepted");
	}
	
	
	@Test (description = "Add equipment to exisiting appt by from Serach equipment dialog",
			groups = { "functional", "L2" })
	
	public void AddEquipment_02() throws HarnessException {
		
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);
		String apptSubject = ConfigProperties.getUniqueString();
		String apptEquipment = equipment.EmailAddress;
		
		// Absolute dates in UTC zone
		String tz = ZTimeZone.getLocalTimeZone().getID();
		Calendar now = this.calendarWeekDayUTC;
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 16, 0, 0);
		
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
        apptForm.zToolbarPressButton(Button.B_SHOW_EQUIPMENT);
        apptForm.zToolbarPressButton(Button.B_EQUIPMENT);
        
        DialogFindEquipment dialogFindEquipment = (DialogFindEquipment) new DialogFindEquipment(app, app.zPageCalendar);
        dialogFindEquipment.zType(Locators.EquipmentName, apptEquipment);
        dialogFindEquipment.zPressButton(Button.B_SEARCH_EQUIPMENT);
        dialogFindEquipment.zPressButton(Button.B_SELECT_EQUIPMENT);
        dialogFindEquipment.zPressButton(Button.B_OK);
        apptForm.zSubmitWithResources();
 
        // Verify equipment present in the appointment
        AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getEquipment(), apptEquipment, "Equipment: Verify the appointment data");
		
		// Verify equipment free/busy status
		String equipmentStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(equipmentStatus, "AC", "Verify equipment free/busy status");
	}
	
}
