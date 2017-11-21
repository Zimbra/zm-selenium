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
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.assistant;

import java.util.Calendar;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogAssistant;

public class CreateAppointment extends AjaxCore {

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Create a basic appointment using the Zimbra Assistant",
			groups = { "deprecated" })

	public void CreateAppointment_01() throws HarnessException {

		// Appointment data
		String subject = "subject" + ConfigProperties.getUniqueString();
		String location = "location" + ConfigProperties.getUniqueString();
		String notes = "notes" + ConfigProperties.getUniqueString();
		String command = "appointment \"" + subject + "\" ["+ location +"] ("+ notes +")";

		Calendar start = Calendar.getInstance();
		start.add(Calendar.DATE, -7);
		Calendar finish = Calendar.getInstance();
		finish.add(Calendar.DATE, +7);

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		DialogAssistant assistant = (DialogAssistant)app.zPageCalendar.zKeyboardShortcut(Shortcut.S_ASSISTANT);
		assistant.zEnterCommand(command);
		assistant.zPressButton(Button.B_OK);

		app.zGetActiveAccount().soapSend(
						"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ start.getTimeInMillis() +"' calExpandInstEnd='"+ finish.getTimeInMillis() +"'>"
					+		"<query>subject:("+ subject +")</query>"
					+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the appointment was created");

		String aSubject = app.zGetActiveAccount().soapSelectValue("//mail:appt", "name");
		String aFragment = app.zGetActiveAccount().soapSelectValue("//mail:fr", null);

		ZAssert.assertEquals(aSubject, subject, "Verify the subject matches");
		ZAssert.assertEquals(aFragment, notes, "Verify the notes matches");
	}


	@Bugs (ids = "53005")
	@Test (description = "Verify location is saved when using assistant",
			groups = { "deprecated" })

	public void CreateAppointment_02() throws HarnessException {

		// Appointment data
		String subject = "subject" + ConfigProperties.getUniqueString();
		String location = "location" + ConfigProperties.getUniqueString();
		String notes = "notes" + ConfigProperties.getUniqueString();
		String command = "appointment \"" + subject + "\" ["+ location +"] ("+ notes +")";

		Calendar start = Calendar.getInstance();
		start.add(Calendar.DATE, -7);
		Calendar finish = Calendar.getInstance();
		finish.add(Calendar.DATE, +7);

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		DialogAssistant assistant = (DialogAssistant)app.zPageCalendar.zKeyboardShortcut(Shortcut.S_ASSISTANT);
		assistant.zEnterCommand(command);
		assistant.zPressButton(Button.B_OK);

		app.zGetActiveAccount().soapSend(
						"<SearchRequest xmlns='urn:zimbraMail' types='appointment' calExpandInstStart='"+ start.getTimeInMillis() +"' calExpandInstEnd='"+ finish.getTimeInMillis() +"'>"
					+		"<query>subject:("+ subject +")</query>"
					+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:appt");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the appointment was created");

		String aLocation = app.zGetActiveAccount().soapSelectValue("//mail:appt", "loc");
		ZAssert.assertEquals(aLocation, location, "Verify the location matches");
	}
}