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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;

public class MuteMessage extends SetGroupMailByMessagePreference {

	public MuteMessage() {
		logger.info("New "+ MuteMessage.class.getCanonicalName());
	}


	@Bugs (ids = "38449")
	@Test (description = "Mute a message (conversation) using Actions -> Mute",
			groups = { "smoke-skip", "application-bug" })

	public void MuteMessage_01() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
					"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
					"</mp>" +
				"</m>" +
			"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Click "mute"
		app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_MUTE);

		// Verify the redirected message is received
		throw new HarnessException("Need to determine how to verify the conversation is muted (from the server) - see bug 38449 and bug 63312");
	}


	@Bugs (ids = "38449")
	@Test (description = "Mute message, using 'Mute' shortcut key",
			groups = { "functional-skip", "application-bug" })

	public void MuteMessage_02() throws HarnessException {
		throw new HarnessException("See bug https://bugzilla.zimbra.com/show_bug.cgi?id=65844");
	}


	@Test (description = "Mute message, using 'Right Click' -> 'Mute'",
			groups = { "smoke-skip", "application-bug" })

	public void MuteMessage_03() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
					"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
					"</mp>" +
				"</m>" +
			"</SendMsgRequest>");

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Click Mute
		app.zPageMail.zListItem(Action.A_RIGHTCLICK, Button.B_MUTE, mail.dSubject);

		// Verify the redirected message is received
		throw new HarnessException("Need to determine how to verify the conversation is muted (from the server) - see bug 38449 and bug 63312");
	}
}