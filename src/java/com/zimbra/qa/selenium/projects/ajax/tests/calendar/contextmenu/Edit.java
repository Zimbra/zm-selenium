/*
 * ***** BEGIN LICENSE BLOCK *****
 *
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.contextmenu;

import java.awt.event.KeyEvent;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar.Locators;

public class Edit extends SetGroupMailByMessagePreference {

	public Edit() {
		logger.info("New " + Edit.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Right click To attendee bubble address and verify edit menus",
			groups = { "smoke", "L1" })

	public void Edit_01() throws HarnessException {

		String apptAttendee1;
		AppointmentItem appt = new AppointmentItem();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		appt.setAttendees(apptAttendee1);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);

		app.zPageCalendar.zRightClickAddressBubble();
		app.zPageMail.zEditAddressContextMenu();

		app.zPageCalendar.sFocus(FormApptNew.Locators.AttendeeField);
		app.zPageCalendar.sClick(FormApptNew.Locators.AttendeeField);
		app.zPageCalendar.zType(FormApptNew.Locators.AttendeeField,"test@test.com");
		app.zPageCalendar.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);

		SleepUtil.sleepMedium();
		ZAssert.assertEquals(app.zPageCalendar.sGetText(Locators.AttendeeBubbleAddr), "test@test.com", "Edited address should present");
	}
}