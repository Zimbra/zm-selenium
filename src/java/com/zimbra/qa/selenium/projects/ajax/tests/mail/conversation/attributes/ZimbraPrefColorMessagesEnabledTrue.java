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

import java.util.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class ZimbraPrefColorMessagesEnabledTrue extends PrefGroupMailByConversationTest {

	public ZimbraPrefColorMessagesEnabledTrue() {
		this.startingAccountPreferences.put("zimbraPrefColorMessagesEnabled", "TRUE");
	}


	@Test (description = "ZimbraPrefColorMessagesEnabledTrue=TRUE: Display messages with 1 tag",
			groups = { "functional", "L2" })

	public void ZimbraPrefColorMessagesEnabledTrue_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String tagname = "tag" + ConfigProperties.getUniqueString();

		// Create a tag
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" +
					"<tag name='" + tagname + "' color='1' />" +
				"</CreateTagRequest>");
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagname);

		// Add a message to the mailbox
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
					"<m l='" + inboxFolder.getId() + "' t='" + tag.getId() + "'>" +
						"<content>" +
							"From: foo@foo.com\n" +
							"To: foo@foo.com \n" +
							"Subject: " + subject + "\n" +
							"MIME-Version: 1.0 \n" +
							"Content-Type: text/plain; charset=utf-8 \n" +
							"Content-Transfer-Encoding: 7bit\n" +
							"\n" +
							"simple text string in the body\n" +
						"</content>" +
					"</m>" +
				"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Verify the message is in the list
		List<MailItem> conversations = app.zPageMail.zListGetMessages();

		// Verify the message has the tag color
		MailItem found = null;
		for (MailItem message : conversations) {
			if (subject.equals(message.getSubject())) {
				found = message;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the conversations is found");
	}


	@Test (description = "PrefGroupMailByMessageTest=TRUE: Display messages with 2 tags",
			groups = { "functional", "L3" })

	public void ZimbraPrefColorMessagesEnabledTrue_02() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String tagname1 = "tag" + ConfigProperties.getUniqueString();
		String tagname2 = "tag" + ConfigProperties.getUniqueString();

		// Create a tag
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" +
					"<tag name='" + tagname1 + "' color='1' />" +
				"</CreateTagRequest>");
		TagItem tag1 = TagItem.importFromSOAP(app.zGetActiveAccount(), tagname1);

		// Create a tag
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" +
					"<tag name='" + tagname2 + "' color='2' />" +
				"</CreateTagRequest>");
		TagItem tag2 = TagItem.importFromSOAP(app.zGetActiveAccount(), tagname2);

		// Add a message to the mailbox
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
					"<m l='" + inboxFolder.getId() + "' t='" + tag1.getId() + "," + tag2.getId() +"'>" +
						"<content>" +
							"From: foo@foo.com\n" +
							"To: foo@foo.com \n" +
							"Subject: " + subject + "\n" +
							"MIME-Version: 1.0 \n" +
							"Content-Type: text/plain; charset=utf-8 \n" +
							"Content-Transfer-Encoding: 7bit\n" +
							"\n" +
							"simple text string in the body\n" +
						"</content>" +
					"</m>" +
				"</AddMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Verify the message is in the list
		List<MailItem> conversations = app.zPageMail.zListGetMessages();

		// Verify the message has the tag color
		MailItem found = null;
		for (MailItem message : conversations) {
			if (subject.equals(message.getSubject())) {
				found = message;
				break;
			}
		}

		ZAssert.assertNotNull(found, "Verify the conversations is found");
	}
}