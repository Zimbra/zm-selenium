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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.quickreply;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByConversationPreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;

public class QuickReply extends SetGroupMailByConversationPreference {

	public QuickReply() {
		logger.info("New "+ QuickReply.class.getCanonicalName());
	}


	@Test (description = "Quick Reply to a conversation (1 message, 1 recipient)",
			groups = { "smoke", "L1" })

	public void QuickReply_01() throws HarnessException {

		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();

		account1.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();

		// Quick Reply
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(0).zFillField(Field.Body, reply);
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_SEND);

		// Verify message in Sent
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		// Verify message is Received by sender
		MailItem received = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(received, "Verify the message is received by the original sender");
	}


	@Test (description = "Quick Reply to a conversation (1 message, 2 recipients)",
			groups = { "functional", "L2" })

	public void QuickReply_02() throws HarnessException {

		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();

		ZimbraAccount account2 = new ZimbraAccount();
		account2.provision();
		account2.authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();

		account1.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ account2.EmailAddress +"'/>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();

		// Quick Reply
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(0).zFillField(Field.Body, reply);
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_SEND);

		// Verify message in Sent
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		// Verify message is NOT Received by active account
		MailItem active = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:inbox subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(active, "Verify the message is NOT received by the active account");

		// Verify message is Received by sender
		MailItem sender = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(sender, "Verify the message is received by the original sender");

		// Verify message is NOT Received by To
		MailItem to = MailItem.importFromSOAP(account2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(to, "Verify the message is NOT received by the original To");
	}


	@Test (description = "Quick Reply to a conversation (1 message, 1 recipient, 1 CC, 1 BCC)",
			groups = { "functional", "L3" })

	public void QuickReply_03() throws HarnessException {

		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();

		ZimbraAccount account2 = new ZimbraAccount();
		account2.provision();
		account2.authenticate();

		ZimbraAccount account3 = new ZimbraAccount();
		account3.provision();
		account3.authenticate();

		ZimbraAccount account4 = new ZimbraAccount();
		account4.provision();
		account4.authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();

		account1.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ account2.EmailAddress +"'/>" +
							"<e t='c' a='"+ account3.EmailAddress +"'/>" +
							"<e t='b' a='"+ account4.EmailAddress +"'/>" +
							"<e t='b' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();

		// Quick Reply
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(0).zFillField(Field.Body, reply);
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_SEND);

		// Verify message in Sent
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +") in:sent");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		// Verify message is Received by sender
		MailItem received = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(received, "Verify the message is received by the original sender");

		// Verify message is NOT Received by To
		MailItem to = MailItem.importFromSOAP(account2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(to, "Verify the message is NOT received by the original To");

		// Verify message is NOT Received by Cc
		MailItem cc = MailItem.importFromSOAP(account3, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(cc, "Verify the message is NOT received by the original Cc");

		// Verify message is NOT Received by Bcc
		MailItem bcc = MailItem.importFromSOAP(account4, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(bcc, "Verify the message is NOT received by the original Bcc");
	}


	@Test (description = "Quick Reply two a 3 message conversation - first message",
			groups = { "functional", "L3" })

	public void QuickReply_10() throws HarnessException {

		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();

		ZimbraAccount account2 = new ZimbraAccount();
		account2.provision();
		account2.authenticate();

		ZimbraAccount account3 = new ZimbraAccount();
		account3.provision();
		account3.authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content1 = "onecontent" + ConfigProperties.getUniqueString();
		String content2 = "twocontent" + ConfigProperties.getUniqueString();
		String content3 = "threecontent" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();

		account1.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content1 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		account2.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content2 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		account3.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content3 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();

		// Quick Reply
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(0).zFillField(Field.Body, reply);
		messages.get(0).zPressButton(Button.B_QUICK_REPLY_SEND);

		MailItem mailitem;

		// Verify message is received by account3
		mailitem = MailItem.importFromSOAP(account3, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(mailitem, "Verify message is received by account3");

		// Verify message is not received by account2 and account1
		mailitem = MailItem.importFromSOAP(account2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailitem, "Verify message is not received by account2 and account1");

		mailitem = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailitem, "Verify message is not received by account2 and account1");
	}


	@Test (description = "Quick Reply two a 3 message conversation - middle message",
			groups = { "functional", "L3" })

	public void QuickReply_11() throws HarnessException {

		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();

		ZimbraAccount account2 = new ZimbraAccount();
		account2.provision();
		account2.authenticate();

		ZimbraAccount account3 = new ZimbraAccount();
		account3.provision();
		account3.authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content1 = "onecontent" + ConfigProperties.getUniqueString();
		String content2 = "twocontent" + ConfigProperties.getUniqueString();
		String content3 = "threecontent" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();

		account1.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content1 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		account2.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content2 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		account3.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content3 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();

		// Quick Reply
		messages.get(1).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(1).zFillField(Field.Body, reply);
		messages.get(1).zPressButton(Button.B_QUICK_REPLY_SEND);

		MailItem mailitem;

		// Verify message is received by account2
		mailitem = MailItem.importFromSOAP(account2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(mailitem, "Verify message is received by account2");

		// Verify message is not received by account3 and account1
		mailitem = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailitem, "Verify message is NOT received by account3 and account1");

		mailitem = MailItem.importFromSOAP(account3, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailitem, "Verify message is NOT received by account3 and account1");
	}


	@Test (description = "Quick Reply two a 3 message conversation - last message",
			groups = { "functional", "L3" })

	public void QuickReply_12() throws HarnessException {

		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();

		ZimbraAccount account2 = new ZimbraAccount();
		account2.provision();
		account2.authenticate();

		ZimbraAccount account3 = new ZimbraAccount();
		account3.provision();
		account3.authenticate();

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content1 = "onecontent" + ConfigProperties.getUniqueString();
		String content2 = "twocontent" + ConfigProperties.getUniqueString();
		String content3 = "threecontent" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();

		account1.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content1 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		account2.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content2 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		account3.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content3 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the first mesage
		List<DisplayConversationMessage> messages = display.zListGetMessages();

		// Quick Reply
		messages.get(2).zPressButton(Button.B_QUICK_REPLY_REPLY);
		messages.get(2).zFillField(Field.Body, reply);
		messages.get(2).zPressButton(Button.B_QUICK_REPLY_SEND);

		MailItem mailitem;

		// Verify message is received by account1
		mailitem = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(mailitem, "Verify message is received by account1");

		// Verify message is not received by account2 and account3
		mailitem = MailItem.importFromSOAP(account2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailitem, "Verify message is NOT received by account2 and account3");

		mailitem = MailItem.importFromSOAP(account3, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailitem, "Verify message is NOT received by account2 and account3");
	}
}