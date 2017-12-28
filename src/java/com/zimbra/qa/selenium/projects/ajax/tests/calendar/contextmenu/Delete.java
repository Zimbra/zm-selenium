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

public class Delete extends SetGroupMailByMessagePreference {

	public Delete() {
		logger.info("New " + Delete.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Right click To bubble address and hit Delete",
			groups = { "smoke", "L1" })

	public void Delete_01() throws HarnessException {

		String apptAttendee1;
		AppointmentItem appt = new AppointmentItem();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		appt.setAttendees(apptAttendee1);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);

		app.zPageCalendar.zRightClickAddressBubble();
		app.zPageMail.zDeleteAddressContextMenu();

		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=td[id='zcs1_person'] div div[class='addrBubbleHolder-empty']"),"Attendee should be empty");
	}


	@Test (description = "Right click To bubble address and hit Delete (Keyboard shortcut)",
			groups = { "smoke", "L1", "non-msedge" })

	public void Delete_KeyboardDelete_02() throws HarnessException {

		String apptAttendee1;
		AppointmentItem appt = new AppointmentItem();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		appt.setAttendees(apptAttendee1);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);

		app.zPageCalendar.sMouseOut(Locators.AttendeeBubbleAddr);
		app.zPageCalendar.sClickAt(Locators.AttendeeBubbleAddr,"");

		app.zPageCalendar.zKeyboard.zTypeKeyEvent(KeyEvent.VK_DELETE);
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=td[id='zcs1_person'] div div[class='addrBubbleHolder-empty']"),"Attendee should be empty");
	}


	@Test (description = "Right click To bubble address and hit delete (BackSpace shortcut)",
			groups = { "smoke", "L1" })

	public void Delete_KeyboardBackSpace_03() throws HarnessException {

		String apptAttendee1;
		AppointmentItem appt = new AppointmentItem();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		appt.setAttendees(apptAttendee1);

		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);

		app.zPageCalendar.sMouseOut(Locators.AttendeeBubbleAddr);
		app.zPageCalendar.sClickAt(Locators.AttendeeBubbleAddr,"");

		app.zPageCalendar.zKeyboard.zTypeKeyEvent(KeyEvent.VK_BACK_SPACE);
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=td[id='zcs1_person'] div div[class='addrBubbleHolder-empty']"), "Attendee should be empty");
	}
}