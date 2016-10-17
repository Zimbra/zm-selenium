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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import java.util.*;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;

public class UnFlagMail extends PrefGroupMailByMessageTest {

	public UnFlagMail() {
		logger.info("New " + UnFlagMail.class.getCanonicalName());
	}

	@Test(description = "Un-Flag a mail by clicking flagged icon", groups = { "smoke" })
	public void UnFlagMail_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount()
				.soapSend("<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='" + inboxFolder.getId() + "' f='f'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n" + "Subject: " + subject + "\n"
						+ "MIME-Version: 1.0 \n" + "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n" + "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// Create a mail item to represent the message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ")");
		ZAssert.assertStringContains(mail.getFlags(), "f", "Verify message is initially flagged");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Flag the item
		app.zPageMail.zListItem(Action.A_MAIL_UNFLAG, mail.dSubject);

		// Get the item from the list
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem listmail = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for " + mail.dSubject + " found: " + m.gSubject);
			if (mail.dSubject.equals(m.gSubject)) {
				listmail = m;
				break;
			}
		}

		// Make sure the GUI shows "flagged"
		ZAssert.assertNotNull(listmail, "Verify the message is in the list");
		ZAssert.assertFalse(listmail.gIsFlagged, "Verify the message is flagged in the list");

		// Make sure the server shows "flagged"
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ")");
		ZAssert.assertStringDoesNotContain(mail.getFlags(), "f", "Verify the message is not flagged in the server");

	}

	@Test(description = "Un-Flag a mail by using shortcut 'mf'", groups = { "functional" })
	public void UnFlagMail_02() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();

		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount()
				.soapSend("<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='" + inboxFolder.getId() + "' f='f'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n" + "Subject: " + subject + "\n"
						+ "MIME-Version: 1.0 \n" + "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n" + "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// Create a mail item to represent the message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ")");
		ZAssert.assertStringContains(mail.getFlags(), "f", "Verify message is initially flagged");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Flag the item
		app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_MARKFLAG);

		// Get the item from the list
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		MailItem listmail = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for " + mail.dSubject + " found: " + m.gSubject);
			if (mail.dSubject.equals(m.gSubject)) {
				listmail = m;
				break;
			}
		}

		// Make sure the GUI shows "flagged"
		ZAssert.assertNotNull(listmail, "Verify the message is in the list");
		ZAssert.assertFalse(listmail.gIsFlagged, "Verify the message is flagged in the list");

		// Make sure the server shows "flagged"
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ")");
		ZAssert.assertStringDoesNotContain(mail.getFlags(), "f", "Verify the message is not flagged in the server");
	}

}
