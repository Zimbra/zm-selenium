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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.conversations;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByConversationPreference;

public class MarkSpamConversation extends SetGroupMailByConversationPreference {

	public MarkSpamConversation() {
		logger.info("New "+ MarkSpamConversation.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}


	@Test (description = "Mark a conversation as spam, using 'Spam' toolbar button",
			groups = { "smoke", "L1" })

	public void MarkSpamConversation_01() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Get the junk folder
		FolderItem junk = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Junk);

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

		// Click spam
		app.zPageMail.zToolbarPressButton(Button.B_RESPORTSPAM);

		// Get the mail item for the new message
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");

		ZAssert.assertNotNull(mail, "Make sure the mail is found");
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");
	}


	@Test (description = "Mark a conversation as spam, using keyboard shortcut (keyboard='ms')",
			groups = { "smoke", "L1" })

	public void MarkSpamConversation_02() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();

		// Get the junk folder
		FolderItem junk = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Junk);

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

		// Spam the item
		app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_MARKSPAM);

		// Get the mail item for the new message
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:("+ subject +")");

		ZAssert.assertNotNull(mail, "Make sure the mail is found");
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");
	}


	@Test (description = "Mark multiple conversations (3) as spam by select and toolbar delete",
			groups = { "functional", "L2" })

	public void MarkSpamConversation_03() throws HarnessException {

		FolderItem junk = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Junk);

		// Create the message data to be sent
		String subject1 = "subject"+ ConfigProperties.getUniqueString();
		String subject2 = "subject"+ ConfigProperties.getUniqueString();
		String subject3 = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject1 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject2 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject3 +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select all three items
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, subject1);
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, subject2);
		app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, subject3);

		// Click toolbar delete button
		app.zPageMail.zToolbarPressButton(Button.B_RESPORTSPAM);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject1 +") is:anywhere");
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");

		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject2 +") is:anywhere");
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");

		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject3 +") is:anywhere");
		ZAssert.assertEquals(mail.dFolderId, junk.getId(), "Verify the message is in the spam folder");
	}
}