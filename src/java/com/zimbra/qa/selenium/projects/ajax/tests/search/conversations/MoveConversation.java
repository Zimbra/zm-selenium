/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.search.conversations;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByConversationPreference;

public class MoveConversation extends SetGroupMailByConversationPreference {

	public MoveConversation() {
		logger.info("New "+ MoveConversation.class.getCanonicalName());
	}


	@Test (description = "From search: Move a conversation to a subfolder",
			groups = { "sanity" })

	public void MoveConversation_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);

		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a conversation in inbox to move
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Search for the message
			app.zPageSearch.zAddSearchQuery("subject:("+ c.getSubject() +")");
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			// Select the item
			app.zPageSearch.zListItem(Action.A_LEFTCLICK, c.getSubject());

			// Click move -> subfolder
			app.zPageSearch.zToolbarPressPulldown(Button.B_MOVE, subfolder);

		} finally {
			app.zPageSearch.zClose();
		}

		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "from:("+ ZimbraAccount.AccountA().EmailAddress + ") subject:("+ c.getSubject() +")");
		ZAssert.assertNotNull(message, "Verify the message still exists in the mailbox");
		ZAssert.assertEquals(message.dFolderId, subfolder.getId(), "Verify the message exists in the correct folder");
	}


	@Bugs (ids = "77217")
	@Test (description = "From search: Move a conversation in Trash to a subfolder",
			groups = { "sanity" })

	public void MoveConversation_02() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a message in trash to move
		String subject = "subject" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ trash.getId() +"' >"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Search for the message
			app.zPageSearch.zAddSearchQuery("is:anywhere subject:("+ subject +")");
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			// Select the item
			app.zPageSearch.zListItem(Action.A_LEFTCLICK, subject);

			// Click move -> subfolder
			app.zPageSearch.zToolbarPressPulldown(Button.B_MOVE, subfolder);

		} finally {
			app.zPageSearch.zClose();
		}

		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(message, "Verify the message still exists in the mailbox");
		ZAssert.assertEquals(message.dFolderId, subfolder.getId(), "Verify the message exists in the correct folder");
	}


	@Bugs (ids = "80611")
	@Test (description = "From search: Move a conversation in Sent to a subfolder",
			groups = { "functional" })

	public void MoveConversation_03() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);

		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a message in trash to move
		String subject = "subject" + ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
					"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='text/plain'>" +
						"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
					"</mp>" +
				"</m>" +
			"</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Search for the message
			app.zPageSearch.zAddSearchQuery("subject:("+ subject +")");
			app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);

			// Select the item
			app.zPageSearch.zListItem(Action.A_LEFTCLICK, subject);

			// Click move -> subfolder
			app.zPageSearch.zToolbarPressPulldown(Button.B_MOVE, subfolder);

		} finally {
			app.zPageSearch.zClose();
		}

		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(message, "Verify the message still exists in the mailbox");
		ZAssert.assertEquals(message.dFolderId, subfolder.getId(), "Verify the message exists in the correct folder");
	}
}