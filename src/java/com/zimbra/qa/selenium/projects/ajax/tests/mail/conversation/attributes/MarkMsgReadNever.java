/*
 * ***** BEGIN LICENSE BLOCK *****
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
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.attributes;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class MarkMsgReadNever extends SetGroupMailByConversationPreference {

	public int delaySeconds = 5;

	public MarkMsgReadNever() {
		logger.info("New "+ MarkMsgReadNever.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefMarkMsgRead", "-1");
    }


	@Bugs (ids = "78178")
	@Test (description = "Verify a conversation (1 message) is not marked read if zimbraPrefMarkMsgRead=Never",
			groups = { "sanity" })

	public void MarkMsgReadNever_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

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

		// Create a mail item to represent the message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

		// Wait to read the message
		SleepUtil.sleep(1000L * (delaySeconds));

		// Wait the for the client to send the change to the server
		app.zPageMail.zWaitForBusyOverlay();

		// Verify the message is marked read in the server (flags attribute should not contain (u)nread)
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertStringContains(mail.getFlags(), "u", "Verify the message is still unread in the server");
    }


	@Bugs (ids = "78178")
	@Test (description = "Verify a conversation (multiple message) is not marked read if zimbraPrefMarkMsgRead=Never",
			groups = { "sanity" })

	public void MarkMsgReadNever_02() throws HarnessException {

		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Flag the item
		app.zPageMail.zListItem(Action.A_MAIL_FLAG, c.getSubject());

		// Verify each message is still unread
		c = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ c.getSubject() +")");
		for (MailItem m : c.getMessageList()) {
			ZAssert.assertStringContains(m.getFlags(), "u", "Verify the message is still unread in the server");
		}
    }
}