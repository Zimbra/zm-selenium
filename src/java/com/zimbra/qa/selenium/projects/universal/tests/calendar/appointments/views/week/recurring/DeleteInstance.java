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
package com.zimbra.qa.selenium.projects.universal.tests.calendar.appointments.views.week.recurring;

import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.*;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekPreference;
import com.zimbra.qa.selenium.projects.universal.pages.*;

public class DeleteInstance extends CalendarWorkWeekPreference {

	public DeleteInstance() {
		logger.info("New "+ DeleteInstance.class.getCanonicalName());
		
		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		// Make sure we are using an account with week view
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "week");
		}};
		
	}
	
	@Bugs (ids = "69132")
	@Test(
			description = "Delete instance of recurring appointment (every month) using toolbar button in week view", 
			groups = { "functional", "L3" } )
	public void DeleteInstance_01() throws HarnessException {
		
		//-- Data Setup
		
		
		
		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
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
        
        
        
        // Verification
        
		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ apptSubject +"</query>"
			+	"</SearchRequest>");

		// http://bugzilla.zimbra.com/show_bug.cgi?id=63412 - "Deleting instance from calendar series does not allow for user restoration from the Trash can"
		// http://bugzilla.zimbra.com/show_bug.cgi?id=13527#c4 - "Moving an instance from one cal to other, moves complete series"
		// For now, nothing should be returned in the SearchResponse
		//		
		//		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		//		ZAssert.assertEquals(
		//				folderID,
		//				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
		//				"Verify appointment is in the trash folder");

		Element[] appts = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist ... See also bug 63412");

		// Verify the appointment is not in the GUI view
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");
		//boolean deleted = app.zPageCalendar.zWaitForElementDeleted(app.zPageCalendar.zGetApptLocator(apptSubject), "10000");
		//ZAssert.assertEquals(deleted, true, "Verify instance is deleted from the calendar");

        
	}
	
	@Bugs (ids = "69132")
	@Test(
			description = "Delete instance of recurring appointment (every year) using context menu in week view", 
			groups = { "functional", "L3" } )
	public void DeleteInstance_02() throws HarnessException {
		
		//-- Data Setup
		
		
		
		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
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
        
        
        
        // Verification
        
		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ apptSubject +"</query>"
			+	"</SearchRequest>");

		// http://bugzilla.zimbra.com/show_bug.cgi?id=63412 - "Deleting instance from calendar series does not allow for user restoration from the Trash can"
		// http://bugzilla.zimbra.com/show_bug.cgi?id=13527#c4 - "Moving an instance from one cal to other, moves complete series"
		// For now, nothing should be returned in the SearchResponse
		//		
		//		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		//		ZAssert.assertEquals(
		//				folderID,
		//				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
		//				"Verify appointment is in the trash folder");

		Element[] appts = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist ... See also bug 63412");

		// Verify the appointment is not in the GUI view
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");
		//boolean deleted = app.zPageCalendar.zWaitForElementDeleted(app.zPageCalendar.zGetApptLocator(apptSubject), "10000");
		//ZAssert.assertEquals(deleted, true, "Verify instance is deleted from the calendar");

        
	}
	
	@DataProvider(name = "DataProviderShortcutKeys")
	public Object[][] DataProviderShortcutKeys() {
		return new Object[][] {
				new Object[] { "VK_DELETE", KeyEvent.VK_DELETE },
				new Object[] { "VK_BACK_SPACE", KeyEvent.VK_BACK_SPACE },
		};
	}

	@Bugs (ids = "69132")
	@Test(
			description = "Delete instance of series appointment (every week) using keyboard shortcuts Del & Backspace in week view",
			groups = { "functional", "L3" },
			dataProvider = "DataProviderShortcutKeys" )
	public void DeleteInstance_03(String name, int keyEvent) throws HarnessException {
		
		//-- Data Setup
		
		
		
		// Appointment data
		String tz, apptSubject, apptBody;
		tz = ZTimeZone.getLocalTimeZone().getID();
		apptSubject = ConfigProperties.getUniqueString();
		apptBody = "body" + ConfigProperties.getUniqueString();
		
		// Absolute dates in UTC zone
		Calendar now = this.calendarWeekDayUTC;
		ZDate startTime = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		ZDate endTime   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 11, 0, 0);
		
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
        
        DialogWarning dialogSeriesOrInstance = (DialogWarning)app.zPageCalendar.zKeyboardKeyEvent(keyEvent);
        dialogSeriesOrInstance.zPressButton(Button.B_DELETE_THIS_INSTANCE);
        DialogWarning confirmDelete = (DialogWarning)dialogSeriesOrInstance.zPressButton(Button.B_OK);
        confirmDelete.zPressButton(Button.B_YES);
        
        
        
        // Verification
        
		// On the server, verify the appointment is in the trash
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startTime.addDays(-7).toMillis() +"' calExpandInstEnd='"+ endTime.addDays(7).toMillis() +"'>"
			+		"<query>is:anywhere "+ apptSubject +"</query>"
			+	"</SearchRequest>");

		// http://bugzilla.zimbra.com/show_bug.cgi?id=63412 - "Deleting instance from calendar series does not allow for user restoration from the Trash can"
		// http://bugzilla.zimbra.com/show_bug.cgi?id=13527#c4 - "Moving an instance from one cal to other, moves complete series"
		// For now, nothing should be returned in the SearchResponse
		//		
		//		String folderID = app.zGetActiveAccount().soapSelectValue("//mail:appt", "l");
		//		ZAssert.assertEquals(
		//				folderID,
		//				FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash).getId(),
		//				"Verify appointment is in the trash folder");

		Element[] appts = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertEquals(appts.length, 0, "Verify the appt element does not exist ... See also bug 63412");

		// Verify the appointment is not in the GUI view
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify instance is deleted from the calendar");
		//boolean deleted = app.zPageCalendar.zWaitForElementDeleted(app.zPageCalendar.zGetApptLocator(apptSubject), "10000");
		//ZAssert.assertEquals(deleted, true, "Verify instance is deleted from the calendar");

        
	}
	
}