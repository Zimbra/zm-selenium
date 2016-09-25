/*
 * ***** BEGIN LICENSE BLOCK *****
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
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.DialogEditFilter;

public class CreateFilter extends PrefGroupMailByMessageTest {

	public int delaySeconds = 10;

	public CreateFilter() {
		logger.info("New " + CreateFilter.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefMarkMsgRead", ""+ delaySeconds);
	}

	@Bugs(ids="88916")
	@Test( description = "Verify Add Filter dialog from new window action menu -> Create Filter", groups = { "smoke" })
	public void CreatefilterFromNewWindow() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>"
					+ "<e t='t' a='"
					+ app.zGetActiveAccount().EmailAddress + "'/>"
					+ "<su>" + subject + "</su>"
					+ "<mp ct='text/plain'>" + "<content>content"
					+ ConfigProperties.getUniqueString()
					+ "</content>" + "</mp>" + "</m>"
					+ "</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_NEW_FILTER);

			SleepUtil.sleepMedium();

			DialogEditFilter dialog = new DialogEditFilter(app,((AppAjaxClient) app).zPageMail);
			ZAssert.assertTrue(dialog.zIsActive(), "Add filter dialog should active");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

	}

}
