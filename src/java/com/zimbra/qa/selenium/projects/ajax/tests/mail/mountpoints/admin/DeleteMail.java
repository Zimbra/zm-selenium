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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mountpoints.admin;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class DeleteMail extends SetGroupMailByMessagePreference {

	public DeleteMail() {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}


	@Bugs (ids = "66525, 26103")
	@Test (description = "Delete a message from a mountpoint folder",
			groups = { "functional", "L2" })

	public void DeleteMail_01() throws HarnessException {

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);

		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidxa'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a message to it
		ZimbraAccount.AccountA().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ folder.getId() +"' f='u'>"
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

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Click on the mountpoint
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, mountpoint);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Click delete
			app.zPageMail.zToolbarPressButton(Button.B_DELETE);

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));
		}

		// Verify the message is now in the local trash
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash);
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +") is:anywhere");
		ZAssert.assertNotNull(mail, "Verify the message exists in the mailbox");
		ZAssert.assertEquals(mail.dFolderId, trash.getId(), "Verify the message exists in the local trash folder");

		// Verify the message is now in the ownser's trash
		trash = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Trash);
		mail = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +") is:anywhere");
		ZAssert.assertNotNull(mail, "Verify the message exists in the mailbox");
		ZAssert.assertEquals(mail.dFolderId, trash.getId(), "Verify the message exists in the owner's trash folder");
	}


	@Bugs (ids = "66525, 26103")
	@Test (description = "Delete multiple messages from a mountpoint folder",
			groups = { "functional", "L2" })

	public void DeleteMail_02() throws HarnessException {

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject1 = "subject" + ConfigProperties.getUniqueString();
		String subject2 = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);

		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidxa'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Add a message to it
		ZimbraAccount.AccountA().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ folder.getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject1 +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ folder.getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject2 +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
				+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		try {

			// Click on the mountpoint
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, mountpoint);

			// Select the item
			app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, subject1);
			app.zPageMail.zListItem(Action.A_MAIL_CHECKBOX, subject2);

			// Click delete
			app.zPageMail.zToolbarPressButton(Button.B_DELETE);

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));
		}

		// Verify the message is now in the local trash
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash);
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject1 +") is:anywhere");
		ZAssert.assertNotNull(mail, "Verify the message exists in the mailbox");
		ZAssert.assertEquals(mail.dFolderId, trash.getId(), "Verify the message exists in the local trash folder");
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject2 +") is:anywhere");
		ZAssert.assertNotNull(mail, "Verify the message exists in the mailbox");
		ZAssert.assertEquals(mail.dFolderId, trash.getId(), "Verify the message exists in the local trash folder");

		// Verify the message is now in the ownser's trash
		trash = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Trash);
		mail = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject1 +") is:anywhere");
		ZAssert.assertNotNull(mail, "Verify the message exists in the mailbox");
		ZAssert.assertEquals(mail.dFolderId, trash.getId(), "Verify the message exists in the owner's trash folder");
		mail = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject2 +") is:anywhere");
		ZAssert.assertNotNull(mail, "Verify the message exists in the mailbox");
		ZAssert.assertEquals(mail.dFolderId, trash.getId(), "Verify the message exists in the owner's trash folder");
	}
}