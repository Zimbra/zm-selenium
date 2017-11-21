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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.notifications;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning.DialogWarningID;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ZimbraPrefCalendarReminderSMS extends AjaxCore {

	protected String sms = null;
	protected String code = null;

	public ZimbraPrefCalendarReminderSMS() {

		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 8123430160111682678L; {
				put("zimbraFeatureCalendarReminderDeviceEmailEnabled", "TRUE");
			}
		};

		// Determine the SMS number
		sms = ConfigProperties.getStringProperty("sms.default.number", "6505551212");
		code = ConfigProperties.getStringProperty("sms.default.code", "654321");
	}


	@Test (description = "Send SendVerificationCodeRequest to an SMS address",
			groups = { "functional" })

	public void ZimbraPrefCalendarReminderSMS_01() throws HarnessException {

		String locator;
		boolean visible;

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Notifications);

		// Wait for the page to  be drawn
		SleepUtil.sleep(5000);

		// Set the SMS address
		locator = "css=input[id='ZmNotificationsPage_DEVICE_EMAIL_PHONE_input']";

		visible = app.zPagePreferences.zIsVisiblePerPosition(locator, 0, 0);
		ZAssert.assertTrue(visible, "Verify the SMS number field is present");

		app.zPagePreferences.sFocus(locator);
		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.zKeyboardTypeString(sms);

		// Click "Send Code"
		locator = "css=td[id='ZmNotificationsPage_DEVICE_EMAIL_PHONE_SEND_CODE_title']";
		app.zPagePreferences.sClick(locator);

		// Verify the popup is displayed
		DialogWarning dialog = app.zPageMain.zGetWarningDialog(DialogWarningID.SmsVerificationCodeSent);
		dialog.zWaitForActive();

		ZAssert.assertTrue(dialog.zIsActive(), "Verify the confirmation dialog appears");
		dialog.zPressButton(Button.B_OK);
	}


	@Test (description = "Send VerifyCodeRequest to an SMS address",
			groups = { "functional", "L3" })

	public void ZimbraPrefCalendarReminderSMS_02() throws HarnessException {

		String locator;
		boolean visible;

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Notifications);

		// Wait for the page to  be drawn
		SleepUtil.sleep(5000);

		// Set the SMS address
		locator = "css=input[id='ZmNotificationsPage_DEVICE_EMAIL_PHONE_input']";

		visible = app.zPagePreferences.zIsVisiblePerPosition(locator, 0, 0);
		ZAssert.assertTrue(visible, "Verify the SMS number field is present");

		app.zPagePreferences.sFocus(locator);
		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.zKeyboardTypeString(sms);

		// Set the code
		locator = "css=input[id='ZmNotificationsPage_DEVICE_EMAIL_CODE_input']";

		visible = app.zPagePreferences.zIsVisiblePerPosition(locator, 0, 0);
		ZAssert.assertTrue(visible, "Verify the Code field is present");

		app.zPagePreferences.sFocus(locator);
		app.zPagePreferences.sClick(locator);
		app.zPagePreferences.zKeyboardTypeString(code);

		// Click "Validate Code"
		locator = "css=td[id='ZmNotificationsPage_DEVICE_EMAIL_CODE_VALIDATE_title']";
		app.zPagePreferences.sClick(locator);
	}
}