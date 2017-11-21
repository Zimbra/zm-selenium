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
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;

public class CreateAppointment extends AjaxCore {

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	
	@Test (description = "Verify Toaster message on Create Appointment",
			groups = { "smoke", "L1" } )
	
	public void Toaster_CreateAppointment_01() throws HarnessException {
		
		// Appointment data
		AppointmentItem appt = new AppointmentItem();
		appt.setSubject(ConfigProperties.getUniqueString());

		// Open the new mail form
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(apptForm, "Verify the new form opened");

		// Fill out the form with the data
		apptForm.zFill(appt);

		// Save appointment (without any method which may wait)
		app.zPageCalendar.sClick("css=div[id^='ztb__APPT-'] td[id$='_SAVE_title']");
		
		// Verify toasted message 'Appointment Created'  
        String expectedMsg ="Appointment Created";
        ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(),expectedMsg, "Verify toast message '" + expectedMsg + "'");
    
	}
}