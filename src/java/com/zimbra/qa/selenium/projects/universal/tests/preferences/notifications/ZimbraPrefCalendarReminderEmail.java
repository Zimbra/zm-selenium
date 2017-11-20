/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.notifications;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.TreeItem;


public class ZimbraPrefCalendarReminderEmail extends UniversalCore {

	public ZimbraPrefCalendarReminderEmail() {
		
		super.startingPage = app.zPagePreferences;
		
	}


	@Test(
			description = "Set zimbraPrefCalendarReminderEmail to a valid Email address'",
			groups = { "functional", "L3" }
			)
	public void ZimbraPrefCalendarReminderEmail_01() throws HarnessException {
		
		ZimbraAccount destination = new ZimbraAccount();
		destination.provision().authenticate();
		
		
		
		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Notifications);

		
		// Set the address
		String locator = "css=div[id='ZmNotificationsPage'] input[id='ZmNotificationsPage_EMAIL_input']";
		
		// To activate the Search button, need to focus/click
		app.zPagePreferences.sFocus(locator);
		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.sType(locator, destination.EmailAddress);

		// Click save
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);		
		

		// Verify the preference is set to false
		app.zGetActiveAccount().soapSend(
						"<GetPrefsRequest xmlns='urn:zimbraAccount'>"
				+			"<pref name='zimbraPrefCalendarReminderEmail'/>"
				+		"</GetPrefsRequest>");
		
		String value = app.zGetActiveAccount().soapSelectValue("//acct:pref[@name='zimbraPrefCalendarReminderEmail']", null);
		ZAssert.assertEquals(value, destination.EmailAddress, "Verify zimbraPrefCalendarReminderEmail was changed to "+ destination.EmailAddress);
		
	}
}
