/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.outofoffice;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Field;

public class OutOfOfficeCalendarAppointment extends AjaxCommonTest {

	public OutOfOfficeCalendarAppointment() {
		logger.info("New " + OutOfOfficeCalendarAppointment.class.getCanonicalName());
		super.startingPage = app.zPagePreferences;
	}

	
	@Bugs(ids = "78890")
	@Test(description = "Set out of office along with calendar appointment and verify all-day appointment creation", priority = 4, groups = {
			"functional", "L2" })
	
	public void OutOfOfficeCalendarAppointment_01() throws HarnessException {

		// Appointment data
		String apptSubject = "Out of Office";
		Calendar now = Calendar.getInstance();
		ZDate startUTC = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
		ZDate endUTC   = new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH) + 4, 14, 0, 0);
		
		// Navigate to preferences -> Out of office
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailOutOfOffice);

		// Select custom work hours for e.g. Tuesday to Friday
		app.zPagePreferences.zSelectRadioButton(Button.R_SEND_AUTOREPLY_MESSAGE);
		app.zPagePreferences.zSelectCheckBox(Button.C_SEND_AUTOREPLY_FOR_TIME_PERIOD);
		app.zPagePreferences.zFillField(Field.StartDate, startUTC);
		app.zPagePreferences.zFillField(Field.EndDate, endUTC);
		app.zPagePreferences.zSelectCheckBox(Button.C_OUT_OF_OFFICE_CALENDAR_APPT);
		
		// Save preferences
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);
		
		// Verify the new appointment exists on the server
		AppointmentItem actual = AppointmentItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ apptSubject +")");
		ZAssert.assertNotNull(actual, "Verify the new appointment is created");
		ZAssert.assertEquals(actual.getSubject(), apptSubject, "Subject: Verify the appointment data");
		ZAssert.assertEquals(app.zGetActiveAccount().soapMatch("//mail:GetAppointmentResponse//mail:comp", "allDay", "1"), true, "");
	}
}