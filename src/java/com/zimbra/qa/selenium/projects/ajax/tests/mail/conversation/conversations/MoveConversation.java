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

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DialogCreateFolder;

public class MoveConversation extends SetGroupMailByConversationPreference {

	public MoveConversation() {
		logger.info("New "+ MoveConversation.class.getCanonicalName());
	}

	@AfterMethod( groups = { "always" } )
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the Move Dialog ...");

		// Check if the "Move Dialog is still open
		DialogMove dialog = new DialogMove(app, ((AjaxPages)app).zPageMail);
		if ( dialog.zIsActive() ) {
			logger.warn(dialog.myPageName() +" was still active.  Cancelling ...");
			dialog.zPressButton(Button.B_CANCEL);
		}
	}


	@Test (description = "Move a conversation by selecting message, then clicking toolbar 'Move' button",
			groups = { "smoke", "L1" })

	public void MoveConversation_01() throws HarnessException {

		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Create a subfolder to move the message into
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click move -> subfolder
		app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, subfolder);

		// Verify all mesages are in the subfolder
		ConversationItem actual = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:"+ c.getSubject());

		for (MailItem m : actual.getMessageList()) {
			ZAssert.assertEquals(m.dFolderId, subfolder.getId(), "Verify the conversation message is in the sub folder");
		}
	}


	@Test (description = "Move a conversation by selecting message, then click 'm' shortcut",
			groups = { "functional", "L2" })

	public void MoveConversation_02() throws HarnessException {

		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Create a subfolder to move the message into
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click move
		app.zPageMail.zKeyboardShortcut(Shortcut.S_MOVE);

		// A move dialog will pop up
		DialogMove dialog = new DialogMove(app, ((AjaxPages)app).zPageMail);
		dialog.sClickTreeFolder(subfolder);
		dialog.zPressButton(Button.B_OK);

		// Verify all mesages are in the subfolder
		ConversationItem actual = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:"+ c.getSubject());

		for (MailItem m : actual.getMessageList()) {
			ZAssert.assertEquals(m.dFolderId, subfolder.getId(), "Verify the conversation message is in the sub folder");
		}
	}


	@Test (description = "Move a conversation by using 'move to trash' shortcut '.t'",
			groups = { "functional", "L3" })

	public void MoveConversation_03() throws HarnessException {

		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click move
		app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_MOVETOTRASH);

		// Verify all mesages are in the subfolder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		ConversationItem actual = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:"+ c.getSubject());

		for (MailItem m : actual.getMessageList()) {
			ZAssert.assertEquals(m.dFolderId, trash.getId(), "Verify the conversation message is in the trash folder");
		}
	}


	@Test (description = "Move a conversation by using 'move to inbox' shortcut '.i'",
			groups = { "functional", "L3" })

	public void MoveConversation_04() throws HarnessException {

		// Create a subfolder
		String foldername = "folder"+ ConfigProperties.getUniqueString();
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Move the conversation to the subfolder
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
					"<action op='move' l='"+ subfolder.getId() +"' id='"+ c.getId() + "'/>" +
				"</ItemActionRequest>");

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Click the subfolder in the tree
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, subfolder);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click move
		app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_MOVETOINBOX);

		// Verify all mesages are in the subfolder
		ConversationItem actual = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:"+ c.getSubject());

		for (MailItem m : actual.getMessageList()) {
			ZAssert.assertEquals(m.dFolderId, inbox.getId(), "Verify the conversation message is in the inbox folder");
		}
	}


	@Test (description = "Move a conversation by using Move -> New folder",
			groups = { "functional", "L2" })

	public void MoveConversation_05() throws HarnessException {

		// Create a subfolder
		String foldername = "folder"+ ConfigProperties.getUniqueString();

		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click move
		DialogCreateFolder dialog = (DialogCreateFolder) app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, Button.O_NEW_FOLDER);
		dialog.zEnterFolderName(foldername);
		dialog.zPressButton(Button.B_OK);

		// Get the folder
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Verify all mesages are in the subfolder
		ConversationItem actual = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:"+ c.getSubject());

		for (MailItem m : actual.getMessageList()) {
			ZAssert.assertEquals(m.dFolderId, subfolder.getId(), "Verify the conversation message is in the subfolder");
		}
	}


	@Test (description = "Move a conversation - 1 message in inbox, 1 message in sent, 1 message in subfolder",
			groups = { "functional", "L2" })

	public void MoveConversation_10() throws HarnessException {

		// Create a conversation (3 messages)
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());

		// Put one message in inbox, one in trash, one in subfolder

		// Get the system folders
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent);

		// Move the conversation to the trash
		String idTrash = c.getMessageList().get(0).getId();
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
					"<action op='move' l='"+ trash.getId() +"' id='"+ idTrash + "'/>" +
				"</ItemActionRequest>");

		// Create a message in a subfolder
		String foldername = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Move the conversation to the subfolder
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
					"<action op='move' l='"+ subfolder.getId() +"' id='"+ c.getMessageList().get(1).getId() + "'/>" +
				"</ItemActionRequest>");

		// Reply to one message (putting a message in sent)
		app.zGetActiveAccount().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m origid='"+ c.getMessageList().get(2).getId() +"' rt='r'>" +
							"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
							"<su>RE: "+ c.getSubject() +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ "body" + ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		String idSent = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");

		// Create a folder to move the converation to
		String destinationname = "folder"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + destinationname +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem destination = FolderItem.importFromSOAP(app.zGetActiveAccount(), destinationname);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox));

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());

		// Click move -> subfolder
		app.zPageMail.zToolbarPressPulldown(Button.B_MOVE, destination);

		ConversationItem actual = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere subject:"+ c.getSubject());

		for (MailItem m : actual.getMessageList()) {
			if ( idSent.equals(m.getId()) ) {

				// Sent message should remain in sent
				ZAssert.assertEquals(m.dFolderId, sent.getId(), "Verify the conversation message is in the sent folder");

			} else if ( idTrash.equals(m.getId()) ) {

				// Trash message should remain in trash
				ZAssert.assertEquals(m.dFolderId, trash.getId(), "Verify the conversation message is in the trash");

			} else {

				// All other messages should be moved to trash
				ZAssert.assertEquals(m.dFolderId, destination.getId(), "Verify the conversation message is in the subfolder");

			}
		}
	}
}