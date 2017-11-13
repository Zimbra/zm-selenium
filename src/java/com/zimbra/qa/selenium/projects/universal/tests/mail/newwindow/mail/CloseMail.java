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
package com.zimbra.qa.selenium.projects.universal.tests.mail.newwindow.mail;

import org.apache.log4j.*;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.SeparateWindowDisplayMail;

public class CloseMail extends PrefGroupMailByMessageTest {
	protected static Logger logger = LogManager.getLogger(CloseMail.class);

	public CloseMail() throws HarnessException {
		logger.info("New " + CloseMail.class.getCanonicalName());

	}

	@Test (description = "Close a separate window", groups = { "functional", "L2" })
	public void CloseMail_01() throws HarnessException {

		final String subject = "subject13150210210153";

		ZimbraAccount.AccountA()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>body" + ConfigProperties.getUniqueString() + "</content>"
						+ "</mp>" + "</m>" + "</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.zToolbarPressButton(Button.B_CLOSE);

			ZAssert.assertFalse(window.zIsActive(), "Verify the window is closed");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

	}

	@Test (description = "Close a separate window - press Esc", groups = { "functional", "L2" })
	public void CloseMail_02() throws HarnessException {

		final String subject = "subject13150210210153";

		ZimbraAccount.AccountA()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>body" + ConfigProperties.getUniqueString() + "</content>"
						+ "</mp>" + "</m>" + "</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.zKeyboardShortcut(Shortcut.S_ESCAPE);

			ZAssert.assertFalse(window.zIsActive(), "Verify the window is closed");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

	}

}
