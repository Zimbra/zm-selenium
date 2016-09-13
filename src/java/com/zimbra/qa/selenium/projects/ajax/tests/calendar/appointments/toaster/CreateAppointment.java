/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.toaster;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

public class CreateAppointment extends AjaxCommonTest {

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
		super.startingAccountPreferences = null;
	}
	
	@Test( description = "Verify Toaster message on Create Appointment",
			groups = { "functional" } )
	
	public void Toaster_CreateAppointment_01() throws HarnessException {
		
		// Create the message data to be sent
		AppointmentItem appt = new AppointmentItem();
		appt.setSubject(ConfigProperties.getUniqueString());

		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");

		// Fill out the form with the data
		apptForm.zFill(appt);

		// Send the message
		
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled", ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			// this method won't wait for some sec after submitting data so toast message disappears and testcase fails (JS COVERAGE)
			app.zPageCalendar.zClickAt("css=div[id^='ztb__APPT-'] td[id$='_SAVE_title']", "0,0");
		} else {
			apptForm.zToolbarPressButton(Button.B_SAVE);
		}		
		
		//verify toasted message 'Appointment Created'  
        String expectedMsg ="Appointment Created";
        ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(),expectedMsg, "Verify toast message '" + expectedMsg + "'");
    
	}

}
