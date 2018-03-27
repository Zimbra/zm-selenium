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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class ReplyMail extends SetGroupMailByMessagePreference {

	public ReplyMail() {
		logger.info("New "+ ReplyMail.class.getCanonicalName());
	}


	@Test (description = "Reply to a message sent to test account (To field) - verify only the sender is used",
			groups = { "functional", "L2" })

	public void ReplyMail_01() throws HarnessException {

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.AccountC().EmailAddress +"'/>" +
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
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		
		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),ZimbraAccount.AccountA().EmailAddress, "Verify the value populated in To field");
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.Cc),"", "Verify the Cc field is empty");
	
		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(sent.dCcRecipients.size(), 0, "Verify the message is sent to 0 'cc' recipients");
	}


	@Test (description = "Reply to a message sent to test account (cc field) - verify only the sender is used",
			groups = { "functional", "L2" })

	public void ReplyMail_02() throws HarnessException {

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<e t='c' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
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
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		
		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),ZimbraAccount.AccountA().EmailAddress," Verify the value populated in To field");
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.Cc),"", "Verify the Cc field is empty");

		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(sent.dCcRecipients.size(), 0, "Verify the message is sent to 0 'cc' recipients");
	}


	@Test (description = "Reply to a message sent from test account (From field) - verify only the sender is used",
			groups = { "functional", "L2" })

	public void ReplyMail_03() throws HarnessException {

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// AccountA needs to delete the message, so that the next message can be searched
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "in:inbox subject:("+ subject +")");
		ZimbraAccount.AccountA().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='move' id='"+ received.getId() +"' l='"+ FolderItem.importFromSOAP(ZimbraAccount.AccountA(), SystemFolder.Trash).getId() +"'/>" +
				"</ItemActionRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Select the sent folder
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Reply the item
			FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
			
			// Verify the values populated in To and Cc fields
			ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),ZimbraAccount.AccountA().EmailAddress," Verify the value populated in To field");
			ZAssert.assertEquals(mailform.zGetFieldValue(Field.Cc),"", "Verify the Cc field is empty");

			// Send the message
			mailform.zSubmit();

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));
		}

		// From the receiving end, verify the message details
		received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "in:inbox subject:("+ subject +")");

		ZAssert.assertEquals(received.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(received.dCcRecipients.size(), 0, "Verify the message is sent to 0 'cc' recipients");
	}


	@Test (description = "Reply to a message - Verify no drafts saved",
			groups = { "functional", "L2" })

	public void ReplyMail_04() throws HarnessException {

		FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);

		// Send a message to the account
		String subject = "subject"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
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
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);

		// Send the message
		mailform.zSubmit();

		// Verify no new drafts
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>inid:"+ drafts.getId() +" subject:("+ subject +")</query>"
			+	"</SearchRequest>");

		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:m");
		ZAssert.assertEquals(nodes.length, 0, "Verify no drafts");
	}
	
	
	@Bugs( ids = "54529" )
	@Test (description = "Reply to a message which has a same Reply To address set -- verify the addresses displayed in To field are not duplicated",
			groups = { "functional", "L3" })

	public void ReplyMail_05() throws HarnessException {

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
		mailform.zFillField(Field.To, account1.EmailAddress + ",");
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
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		
		// Verify the values populated in To and Cc fields
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.To),account2.EmailAddress, "Verify the value populated in To field");
		ZAssert.assertEquals(mailform.zGetFieldValue(Field.Cc),"", "Verify the Cc field is empty");
	
		// Send the message
		mailform.zSubmit();

		// From the receiving end, verify the message details
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		ZAssert.assertEquals(sent.dToRecipients.get(0).dEmailAddress, account2.EmailAddress, "Verify the to field is correct");
		ZAssert.assertEquals(sent.dCcRecipients.size(), 0, "Verify the message is sent to 0 'cc' recipients");
		
		// Verify that the mail is delivered to the set Reply-to address
		MailItem mailReceived = MailItem.importFromSOAP(account2, "in:inbox subject:("+ subject +")");
		ZAssert.assertNotNull(mailReceived, "Verify that the reply mail is deliverd to the ser Reply-To address");
	}
}