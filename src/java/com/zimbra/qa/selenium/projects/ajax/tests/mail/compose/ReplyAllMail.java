/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class ReplyAllMail extends SetGroupMailByMessagePreference {

	ZimbraAccount account1 = null;
	ZimbraAccount account2 = null;
	ZimbraAccount account3 = null;
	ZimbraAccount account4 = null;

	public ReplyAllMail() {
		logger.info("New "+ ReplyAllMail.class.getCanonicalName());
	}


	@Test (description = "Reply to all (test account in To field)",
			groups = { "functional", "L2" })

	public void ReplyAllMail_01() throws HarnessException {

		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
			account3 = (new ZimbraAccount()).provision().authenticate();
			account4 = (new ZimbraAccount()).provision().authenticate();
		}

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='t' a='"+ account1.EmailAddress +"'/>" +
							"<e t='c' a='"+ account2.EmailAddress +"'/>" +
							"<e t='c' a='"+ account3.EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		boolean foundAccountA = false;
		boolean foundAccount1 = false;
		boolean foundAccount2 = false;
		boolean foundAccount3 = false;

		// Check the To, which should only contain the original sender
		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		for (RecipientItem r : sent.dToRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				foundAccountA = true;
			}
		}
		ZAssert.assertTrue(foundAccountA, "Verify the original sender is in the To field");

		// Check the CC, which should contain the original To (not the sender), the original CC, and not the zimbra test account
		ZAssert.assertEquals(sent.dCcRecipients.size(), 3, "Verify the message is sent to 3 'cc' recipients");
		for (RecipientItem r : sent.dCcRecipients) {
			if ( r.dEmailAddress.equals(account1.EmailAddress) ) {
				foundAccount1 = true;
			}
			if ( r.dEmailAddress.equals(account2.EmailAddress) ) {
				foundAccount2 = true;
			}
			if ( r.dEmailAddress.equals(account3.EmailAddress) ) {
				foundAccount3 = true;
			}
		}
		ZAssert.assertTrue(foundAccount1, "Verify the To is in the Cc field");
		ZAssert.assertTrue(foundAccount2, "Verify the Cc is in the Cc field");
		ZAssert.assertTrue(foundAccount3, "Verify the Cc is in the Cc field");
	}


	@Test (description = "Reply to all (test account in Cc field)",
			groups = { "functional", "L2" })

	public void ReplyAllMail_02() throws HarnessException {

		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
			account3 = (new ZimbraAccount()).provision().authenticate();
			account4 = (new ZimbraAccount()).provision().authenticate();
		}

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ account1.EmailAddress +"'/>" +
							"<e t='t' a='"+ account2.EmailAddress +"'/>" +
							"<e t='c' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ account3.EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		boolean foundAccountA = false;
		boolean foundAccount1 = false;
		boolean foundAccount2 = false;
		boolean foundAccount3 = false;

		// Check the To, which should only contain the original sender
		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		for (RecipientItem r : sent.dToRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				foundAccountA = true;
			}
		}
		ZAssert.assertTrue(foundAccountA, "Verify the original sender is in the To field");

		// Check the CC, which should contain the original To (not the sender), the original CC, and not the zimbra test account
		ZAssert.assertEquals(sent.dCcRecipients.size(), 3, "Verify the message is sent to 3 'cc' recipients");
		for (RecipientItem r : sent.dCcRecipients) {
			if ( r.dEmailAddress.equals(account1.EmailAddress) ) {
				foundAccount1 = true;
			}
			if ( r.dEmailAddress.equals(account2.EmailAddress) ) {
				foundAccount2 = true;
			}
			if ( r.dEmailAddress.equals(account3.EmailAddress) ) {
				foundAccount3 = true;
			}
		}

		ZAssert.assertTrue(foundAccount1, "Verify the To is in the Cc field");
		ZAssert.assertTrue(foundAccount2, "Verify the Cc is in the Cc field");
		ZAssert.assertTrue(foundAccount3, "Verify the Cc is in the Cc field");
	}


	@Test (description = "Reply to all from the sent folder (test account in From field)",
			groups = { "functional", "L2" })

	public void ReplyAllMail_03() throws HarnessException {

		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
			account3 = (new ZimbraAccount()).provision().authenticate();
			account4 = (new ZimbraAccount()).provision().authenticate();
		}

		// Send a message from the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ account1.EmailAddress +"'/>" +
						"<e t='c' a='"+ account2.EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Click in sent
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Reply the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

			// Send the message
			mailform.zSubmit();

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));
		}

		// All sent messages should not have TO: include the test account
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>in:sent subject:("+ subject +")</query>"
			+	"</SearchRequest>");

		Element[] messages = app.zGetActiveAccount().soapSelectNodes("//mail:m");

		// Make sure there are m nodes
		ZAssert.assertEquals(messages.length, 2, "Verify 2 messages are found in the sent folder");

		// Iterate over the sent messages, make sure the test account is not in the To or CC list
		for (Element message : messages) {

			String id = message.getAttribute("id", null);

			ZAssert.assertNotNull(id, "Verify the sent message ID is not null");

			app.zGetActiveAccount().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail' >"
				+		"<m id='"+ id +"'/>"
				+	"</GetMsgRequest>");

			Element[] elements = app.zGetActiveAccount().soapSelectNodes("//mail:e");

			for ( Element e : elements ) {

				String type = e.getAttribute("t", null);
				String address = e.getAttribute("a", null);

				// Check To (t='t') and Cc (t='c') that they don't contain the sender
				if ( "t".equals(type) || "c".equals(type) ) {
					ZAssert.assertNotEqual(address, app.zGetActiveAccount().EmailAddress, "Verify the sender is not included in To or Cc");
				}
			}
		}
	}


	@Bugs (ids = "79880")
	@Test (description = "Verify user account is not To/Cc when mismatched case)",
			groups = { "functional", "L3" })

	public void ReplyAllMail_04() throws HarnessException {

		// Steps:
		// 1. Receive message to your account, but First.Last@domain.com specified in To
		// 2. Reply-All from the first.last@domain.com account
		// 3. Verify the sent message does not contain To/Cc for first.last@domain.com or First.Last@domain.com

		// Send a message from the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress.toUpperCase() +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

		// Send the message
		mailform.zSubmit();

		// Verify the active account is not in the To/Cc
		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(message, "verify the message in the sent folder");

		for ( RecipientItem r : message.dToRecipients) {
			ZAssert.assertNotEqual(r.dEmailAddress.toLowerCase(), app.zGetActiveAccount().EmailAddress.toLowerCase(), "Verify active account is not in the To field");
		}
		for ( RecipientItem r : message.dCcRecipients) {
			ZAssert.assertNotEqual(r.dEmailAddress.toLowerCase(), app.zGetActiveAccount().EmailAddress.toLowerCase(), "Verify active account is not in the Cc field");
		}
	}


	@Bugs (ids = "79880")
	@Test (description = "Verify user account is not To/Cc when mismatched case)",
			groups = { "functional", "L3" })

	public void ReplyAllMail_05() throws HarnessException {

		// Steps:
		// 1. Receive message to your account, but First.Last@domain.com specified in Cc
		// 2. Reply-All from the first.last@domain.com account
		// 3. Verify the sent message does not contain To/Cc for first.last@domain.com or First.Last@domain.com

		// Send a message from the account
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<e t='c' a='"+ app.zGetActiveAccount().EmailAddress.toUpperCase() +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content" + ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);

		// Send the message
		mailform.zSubmit();

		// Verify the active account is not in the To/Cc
		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(message, "verify the message in the sent folder");

		for ( RecipientItem r : message.dToRecipients) {
			ZAssert.assertNotEqual(r.dEmailAddress.toLowerCase(), app.zGetActiveAccount().EmailAddress.toLowerCase(), "Verify active account is not in the To field");
		}
		for ( RecipientItem r : message.dCcRecipients) {
			ZAssert.assertNotEqual(r.dEmailAddress.toLowerCase(), app.zGetActiveAccount().EmailAddress.toLowerCase(), "Verify active account is not in the Cc field");
		}
	}
}