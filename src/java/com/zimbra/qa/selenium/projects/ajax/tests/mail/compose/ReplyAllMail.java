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
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class ReplyAllMail extends SetGroupMailByMessagePreference {

	ZimbraAccount account1 = null;
	ZimbraAccount account2 = null;
	ZimbraAccount account3 = null;

	public ReplyAllMail() {
		logger.info("New "+ ReplyAllMail.class.getCanonicalName());
	}


	@Test (description = "Reply to all (test account in To field)",
			groups = { "sanity" })

	public void ReplyAllMail_01() throws HarnessException {

		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
			account3 = (new ZimbraAccount()).provision().authenticate();
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
		
		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),ZimbraAccount.AccountA().EmailAddress," Verify the value populated in To field");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account1.EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account2.EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account3.EmailAddress, "Verify the Cc field is populated with correct address.");

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
			groups = { "sanity" })

	public void ReplyAllMail_02() throws HarnessException {

		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
			account3 = (new ZimbraAccount()).provision().authenticate();
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

		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),ZimbraAccount.AccountA().EmailAddress," Verify the value populated in To field");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account1.EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account2.EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account3.EmailAddress, "Verify the Cc field is populated with correct address.");

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
			groups = { "sanity" })

	public void ReplyAllMail_03() throws HarnessException {

		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
			account3 = (new ZimbraAccount()).provision().authenticate();
		}

		// Send a message from the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ account1.EmailAddress +"'/>" +
						"<e t='c' a='"+ account2.EmailAddress +"'/>" +
						"<e t='c' a='"+ account3.EmailAddress +"'/>" +
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
			
			// Verify the values populated in To and Cc fields
			ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),account1.EmailAddress," Verify the value populated in To field");
			ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account2.EmailAddress, "Verify the Cc field is populated with correct address.");
			ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),account3.EmailAddress, "Verify the Cc field is populated with correct address.");

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
			groups = { "functional" })

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
		
		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),ZimbraAccount.AccountA().EmailAddress," Verify the value populated in To field");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),ZimbraAccount.AccountB().EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertFalse(mailform.zGetFieldValue(Field.Cc).contains(app.zGetActiveAccount().EmailAddress.toUpperCase()), "Verify the Cc field is populated with correct address.");


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
	
	
	@Test (description = "ReplyAll to a message which has a differnt Reply To address set--verify the address displayed in To fields on reply-compose",
			groups = { "sanity" })

	public void ReplyAllMail_05() throws HarnessException {
		
		// Account data
		ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();
		
		// Mail data
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();
		
		// Set the Reply-To address for the account
		app.zGetActiveAccount().soapSend(
				" <ModifyIdentityRequest  xmlns='urn:zimbraAccount'>"
						+   "<identity id='"+ app.zGetActiveAccount().ZimbraId +"'>"
						+     "<a name='zimbraPrefFromAddressType'>sendAs</a>"
						+     "<a name='zimbraPrefReplyToEnabled'>TRUE</a>"
						+     "<a name='zimbraPrefReplyToAddress'>"+ account2.EmailAddress +"</a>"
						+   "</identity>"
						+ "</ModifyIdentityRequest >");
		
		// Refresh the UI to get the changes
		app.zPageMail.zRefreshUI();
		
		// Compose a mail and send it to account1
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, account1.EmailAddress + ","+ ZimbraAccount.Account8().EmailAddress + ",");
		mailform.zFillField(Field.Cc, ZimbraAccount.Account10().EmailAddress + "," + ZimbraAccount.Account9().EmailAddress + ",");
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Send the message
		mailform.zSubmit();
		
		//Login with account1
		app.zPageLogin.zLogin(account1);
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);
		
		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),account2.EmailAddress, "Verify the value populated in To field"); // Set Reply-to address is present
		
		// Set original sender's address is not present
		ZAssert.assertFalse(mailform.zGetFieldValue(Field.To).contains(app.zGetActiveAccount().EmailAddress), "Verify the value in To filed is not the orginal email sender's address");
		
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),ZimbraAccount.Account10().EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),ZimbraAccount.Account9().EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),ZimbraAccount.Account8().EmailAddress, "Verify the Cc field is populated with the other To address.");
		
		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, account2.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(sent.dCcRecipients.size(), 3, "Verify the message is sent to 2 'cc' recipients");
		
		// Verify that the mail is delivered to the set Reply-to address
		MailItem mailReceived = MailItem.importFromSOAP(account2, "in:inbox subject:("+ subject +")");
		ZAssert.assertNotNull(mailReceived, "Verify that the reply mail is deliverd to the ser Reply-To address");
	}
	
	
	@Bugs( ids = "54529")
	@Test (description = "ReplyAll to a message which has a same Reply To address set -- verify the addresses displayed in To field are not duplicated",
			groups = { "functional" })

	public void ReplyAllMail_06() throws HarnessException {

		// Account data
		ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account2 = app.zGetActiveAccount();

		// Mail data
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Set the Reply-To address for the account
		app.zGetActiveAccount().soapSend(
				" <ModifyIdentityRequest  xmlns='urn:zimbraAccount'>"
						+   "<identity id='"+ account2.ZimbraId +"'>"
						+     "<a name='zimbraPrefFromAddressType'>sendAs</a>"
						+     "<a name='zimbraPrefReplyToEnabled'>TRUE</a>"
						+     "<a name='zimbraPrefReplyToAddress'>"+ account2.EmailAddress +"</a>"
						+   "</identity>"
						+ "</ModifyIdentityRequest >");

		// Refresh the UI to get the changes
		app.zPageMail.zRefreshUI();

		// Compose a mail and send it to account1
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);

		// Fill out the form with the data
		mailform.zFillField(Field.To, account1.EmailAddress + "," + ZimbraAccount.Account8().EmailAddress);
		mailform.zFillField(Field.Cc, ZimbraAccount.Account10().EmailAddress + "," + ZimbraAccount.Account9().EmailAddress + ",");
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Send the message
		mailform.zSubmit();

		//Login with account1
		app.zPageLogin.zLogin(account1);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);
		
		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),account2.EmailAddress, "Verify the value populated in To field"); // To filed should not have same address twice
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),ZimbraAccount.Account10().EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),ZimbraAccount.Account9().EmailAddress, "Verify the Cc field is populated with correct address.");
		ZAssert.assertStringContains(mailform.zGetFieldValue(Field.Cc),ZimbraAccount.Account8().EmailAddress, "Verify the Cc field is populated with the other To address.");

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, account2.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(sent.dCcRecipients.size(), 3, "Verify the message is sent to 0 'cc' recipients");
		
		// Verify that the mail is delivered to the set Reply-to address
		MailItem mailReceived = MailItem.importFromSOAP(account2, "in:inbox subject:("+ subject +")");
		ZAssert.assertNotNull(mailReceived, "Verify that the reply mail is deliverd to the ser Reply-To address");
	}
}