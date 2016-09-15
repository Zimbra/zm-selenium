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

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;

public class CreateFlag extends PrefGroupMailByMessageTest {

	public int delaySeconds = 10;

	public CreateFlag() {
		logger.info("New " + CreateFlag.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefMarkMsgRead", "" + delaySeconds);
	}

	@Test( description = "Create flag from new window from action menu -> Create Flag", groups = { "functional" })
	
	public void CreateFlagFromNewWindow_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA()
		.soapSend(
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
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive(); // Make sure the window is there

			ZAssert.assertTrue(window.zIsActive(),"Verify the window is active");

			//Actin Menu --> Flag
			window.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_FLAG_MESSAGE);

			SleepUtil.sleepMedium();

		} finally {

			// Make sure to close the window
			if (window != null) {
				window.zCloseWindow(windowTitle);
				window = null;
			}

		}

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + subject + ")");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem listmail = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for " + mail.dSubject + " found: "
					+ m.gSubject);
			if (mail.dSubject.equals(m.gSubject)) {
				listmail = m;
				break;
			}
		}

		// Make sure the GUI shows "flagged"
		ZAssert.assertNotNull(listmail, "Verify the message is in the list");
		ZAssert.assertTrue(listmail.gIsFlagged,"Verify the message is flagged in the list");

		// Make sure the server shows "flagged"
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject + ")");
		ZAssert.assertStringContains(mail.getFlags(), "f","Verify the message is flagged in the server");

	}

	
	@Test( description = "Create flag from new window using shortcut key 'mf'", groups = { "functional" })
	
	public void CreateFlagFromNewWindow_02() throws HarnessException {

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
			window = (SeparateWindowDisplayMail) app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS,Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive(); // Make sure the window is there

			ZAssert.assertTrue(window.zIsActive(),"Verify the window is active");

			//Actin Menu --> Flag
			//	Use shortcut mf			
			window.zKeyboardShortcut(Shortcut.S_MAIL_MARKFLAG);

			// Flag the item
			//	app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_MARKFLAG);

			SleepUtil.sleepMedium();

		} finally {

			// Make sure to close the window
			if (window != null) {
				window.zCloseWindow(windowTitle);
				window = null;
			}

		}

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(),"subject:(" + subject + ")");
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem listmail = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for " + mail.dSubject + " found: "
					+ m.gSubject);
			if (mail.dSubject.equals(m.gSubject)) {
				listmail = m;
				break;
			}
		}

		// Make sure the GUI shows "flagged"
		ZAssert.assertNotNull(listmail, "Verify the message is in the list");
		ZAssert.assertTrue(listmail.gIsFlagged,"Verify the message is flagged in the list");

		// Make sure the server shows "flagged"
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject + ")");
		ZAssert.assertStringContains(mail.getFlags(), "f","Verify the message is flagged in the server");

	}

}
