/*
 * ***** BEGIN LICENSE BLOCK *****
 *
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.universal.tests.preferences.calendar;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.universal.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

import java.awt.event.KeyEvent;
import java.util.Calendar;

import org.testng.annotations.Test;

public class ModifyDefaultAppointmentDuration extends CalendarWorkWeekTest {

	 public ModifyDefaultAppointmentDuration() {
	    logger.info("New " + ModifyDefaultAppointmentDuration.class.getCanonicalName());
	    this.startingPage = this.app.zPagePreferences;
	 }
  
	 @Test (description = "Modify calendar default appointment duration", groups = {"functional", "L2"} )
	
	 public void ModifyDefaultAppointmentDuration_01() throws HarnessException  {
		 
		 Calendar now = this.calendarWeekDayUTC;
		 String apptSubject = ConfigProperties.getUniqueString();
		 ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 10, 0, 0);
		 
		 // Go to calendar preference
		 app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Calendar);
		 SleepUtil.sleepMedium();
		 
		 // Modify appointment duration
		 app.zPagePreferences.sClickAt("css=td[id='Prefs_Select_CAL_DEFAULT_APPT_DURATION_dropdown']", "");
		 SleepUtil.sleepSmall();
		 app.zPagePreferences.sClickAt("css=div[id^='Prefs_Select_CAL_DEFAULT_APPT_DURATION_Menu'] td[id$='_title']:contains('30')", "");
		 SleepUtil.sleepSmall();
		 app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		 SleepUtil.sleepSmall();
		 
		 // Go to calendar
		 startingPage= app.zPageCalendar;
		 startingPage.zNavigateTo();

		 // Create appointment
		 AppointmentItem appt = new AppointmentItem();
		 appt.setSubject(apptSubject);
		 appt.setStartTime(startUTC);
		 
		 // Open the new mail form
		 FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		 apptForm.zFill(appt);
		 // Let end time set automatically
		 app.zPageCalendar.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		 app.zPageCalendar.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		 app.zPageCalendar.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		 apptForm.zSubmit();
		
		 // Verify the new appointment exists on the server
		 app.zGetActiveAccount().soapSend(
				 "<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ startUTC.addDays(-7).toMillis() +"' calExpandInstEnd='"+ startUTC.addDays(7).toMillis() +"'>"
					+	"<query>subject:("+ apptSubject +")</query>"
					+	"</SearchRequest>");
		 String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		 ZAssert.assertNotNull(id, "verify that appointment is synced to the server");
		
		 app.zGetActiveAccount().soapSend("<GetAppointmentRequest  xmlns='urn:zimbraMail' id='"+ id +"'/>");
		 String endtime = app.zGetActiveAccount().soapSelectValue("//mail:e", "d");
		 ZAssert.assertEquals(startUTC.addMinutes(30).toyyyyMMddTHHmmss(), endtime, "Verify that the duartion is 30 minutes");
	
	 } 
}
