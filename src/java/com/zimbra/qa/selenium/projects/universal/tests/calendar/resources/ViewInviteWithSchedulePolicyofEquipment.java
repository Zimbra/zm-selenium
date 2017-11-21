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
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.universal.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.universal.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.universal.pages.mail.DisplayMail.Field;

public class ViewInviteWithSchedulePolicyofEquipment extends CalendarWorkWeekPreference {	
	
	public ViewInviteWithSchedulePolicyofEquipment() {
		logger.info("New "+ ViewInviteWithSchedulePolicyofEquipment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test (description = "Verify that invite appears correctly in resource's account when schedule policy is set to 'Manual accept, auto decline on conflict'",
			groups = { "smoke", "L1" })
	
	public void ManualAcceptAutoDeclineOnConflict_01() throws HarnessException {
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyCalendarResourceRequest xmlns='urn:zimbraAdmin'>" +
					"<name>" + equipment.EmailAddress + "</name>" +
					"<id> " + equipment.ZimbraId + "</id> " +
					"<a n='zimbraCalResAutoAcceptDecline'>FALSE</a>" +
					"<a n='zimbraCalResAutoDeclineIfBusy'>TRUE</a>" +
				"</ModifyCalendarResourceRequest>");

	    ZimbraAdminAccount.GlobalAdmin().soapSend(
	    	      "<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" + 
	    	    	equipment.ZimbraId + "</id>" + 
	    	      "<a n='zimbraPrefGroupMailBy'>message</a>" + 
	    	      "</ModifyAccountRequest>");

		
		String apptSubject, apptAttendee, apptEquipment, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.Account3().EmailAddress;
		apptEquipment = equipment.EmailAddress;
		apptContent = "content" + ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee);
		appt.setEquipment(apptEquipment);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 9, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setContent(apptContent);
	
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmitWithResources();
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(equipment);
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertTrue(display.zHasADTButtons(), "Verify A/D/T buttons");
		ZAssert.assertStringContains(display.zGetMailProperty(Field.Body), apptContent, "Verify the text content");
		
		// Verify the attendee and organizer header		
		String organizer = "css=table[id='zv__TV__TV-main_MSG_hdrTable'] span[class='addrBubble']:contains('" + ZimbraAccount.AccountZCS().EmailAddress + "')";		
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(organizer), "Organizer is present");

	//	String attendee = "css=table[id='zv__TV__TV-main_MSG_hdrTable'] span[class='addrBubble']:contains('" + ZimbraAccount.Account3().EmailAddress + "')";		
//		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(attendee), "Attendee is present");
		
		display.zPressButton(Button.B_ACCEPT);
		SleepUtil.sleepMedium();

	    // Verify Location present in the appointment
	    AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getEquipment(), apptEquipment, "Location: Verify the appointment data");
		
		// Verify location free/busy status
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify Location free/busy status");		

	}

	@Test (description = "Verify that invite appears correctly in resource's account when schedule policy is set to 'Auto accept always'",
			groups = { "smoke", "L1" })
	public void AutoAcceptAlways_02() throws HarnessException {
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyCalendarResourceRequest xmlns='urn:zimbraAdmin'>" +
					"<name>" + equipment.EmailAddress + "</name>" +
					"<id> " + equipment.ZimbraId + "</id> " +
					"<a n='zimbraCalResAutoAcceptDecline'>TRUE</a>" +
					"<a n='zimbraCalResAutoDeclineIfBusy'>FALSE</a>" +
				"</ModifyCalendarResourceRequest>");

	    ZimbraAdminAccount.GlobalAdmin().soapSend(
	    	      "<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" + 
	    	    	equipment.ZimbraId + "</id>" + 
	    	      "<a n='zimbraPrefGroupMailBy'>message</a>" + 
	    	      "</ModifyAccountRequest>");

		
		String apptSubject, apptAttendee, apptEquipment, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.Account3().EmailAddress;
		apptEquipment = equipment.EmailAddress;
		apptContent = "content" + ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee);
		appt.setEquipment(apptEquipment);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0));
		appt.setContent(apptContent);
	
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmitWithResources();

	    // Verify Location present in the appointment
	    AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getEquipment(), apptEquipment, "Location: Verify the appointment data");
		
		// Verify location free/busy status
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify Location free/busy status");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(equipment);
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertTrue(display.zHasADTButtons(), "Verify A/D/T buttons");
		ZAssert.assertStringContains(display.zGetMailProperty(Field.Body), apptContent, "Verify the text content");
		
		// Verify the attendee and organizer header		
		String organizer = "css=table[id='zv__TV__TV-main_MSG_hdrTable'] span[class='addrBubble']:contains('" + ZimbraAccount.AccountZCS().EmailAddress + "')";		
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(organizer), "Organizer is present");

	//	String attendee = "css=table[id='zv__TV__TV-main_MSG_hdrTable'] span[class='addrBubble']:contains('" + ZimbraAccount.Account3().EmailAddress + "')";		
//		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(attendee), "Attendee is present");

	}

	@Test (description = "Verify that invite appears correctly in resource's account when schedule policy is set to 'No auto accept or decline'",
			groups = { "smoke", "L1" })
	public void NoAutoAcceptOrDecline_03() throws HarnessException {
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		ZimbraResource equipment = new ZimbraResource(ZimbraResource.Type.EQUIPMENT);
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyCalendarResourceRequest xmlns='urn:zimbraAdmin'>" +
					"<name>" + equipment.EmailAddress + "</name>" +
					"<id> " + equipment.ZimbraId + "</id> " +
					"<a n='zimbraCalResAutoAcceptDecline'>FALSE</a>" +
					"<a n='zimbraCalResAutoDeclineIfBusy'>FALSE</a>" +
				"</ModifyCalendarResourceRequest>");

	    ZimbraAdminAccount.GlobalAdmin().soapSend(
	    	      "<ModifyAccountRequest xmlns='urn:zimbraAdmin'><id>" + 
	    	    	equipment.ZimbraId + "</id>" + 
	    	      "<a n='zimbraPrefGroupMailBy'>message</a>" + 
	    	      "</ModifyAccountRequest>");

		
		String apptSubject, apptAttendee, apptEquipment, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ConfigProperties.getUniqueString();
		apptAttendee = ZimbraAccount.Account3().EmailAddress;
		apptEquipment = equipment.EmailAddress;
		apptContent = "content" + ConfigProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee);
		appt.setEquipment(apptEquipment);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setContent(apptContent);
	
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zSubmitWithResources();
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(equipment);
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, apptSubject);
		ZAssert.assertTrue(display.zHasADTButtons(), "Verify A/D/T buttons");
		ZAssert.assertStringContains(display.zGetMailProperty(Field.Body), apptContent, "Verify the text content");
		
		// Verify the attendee and organizer header		
		String organizer = "css=table[id='zv__TV__TV-main_MSG_hdrTable'] span[class='addrBubble']:contains('" + ZimbraAccount.AccountZCS().EmailAddress + "')";		
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent(organizer), "Organizer is present");

		//String attendee = "css=table[id='zv__TV__TV-main_MSG_hdrTable'] span[class='addrBubble']:contains('" + ZimbraAccount.Account3().EmailAddress + "')";		
		//ZAssert.assertTrue(app.zPageMail.sIsElementPresent(attendee), "Attendee is present");

		display.zPressButton(Button.B_ACCEPT);
		SleepUtil.sleepMedium();

	    // Verify Location present in the appointment
	    AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertStringContains(actual.getEquipment(), apptEquipment, "Location: Verify the appointment data");
		
		// Verify location free/busy status
		String locationStatus = app.zGetActiveAccount().soapSelectValue("//mail:at[@a='"+ apptEquipment +"']", "ptst");
		ZAssert.assertEquals(locationStatus, "AC", "Verify Location free/busy status");		
		
	}
	
}
