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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mountpoints.manager;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning.DialogWarningID;

public class DeleteMail extends SetGroupMailByMessagePreference {

	public DeleteMail() {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}


	@Bugs (ids = "ZCS-7511")
	@Test (description = "Delete a message from a mountpoint folder",
			groups = { "sanity-application-bug" })

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
						+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx'/>"
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


	@Bugs (ids = "ZCS-7511")
	@Test (description = "Delete multiple messages from a mountpoint folder",
			groups = { "sanity-application-bug" })

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
						+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx'/>"
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


	@Bugs (ids = "ZCS-7511")
	@Test (description = "Delete a message from a mountpoint folder which has retention policy of 1 month and verify the warning dialog",
			groups = { "functional-application-bug" })

	public void DeleteMailFromFolderWithRetention_03() throws HarnessException {

		String warningMessagePart1 = "You are deleting a message that is within its folder’s retention period.";
		String warningMessagePart2 = "Do you wish to delete the message?";
		String subject = "subject" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);

		// Add a retention policy to the inbox folder
		ZimbraAccount.AccountA().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='" + inbox.getId() + "' op='retentionpolicy'>"
						+			"<retentionPolicy>"
						+				"<keep>"
						+					"<policy lifetime='30d' type='user'/>"
						+				"</keep>"
						+			"</retentionPolicy>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Share the inbox folder
		ZimbraAccount.AccountA().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ inbox.getId() +"' op='grant'>"
						+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Add a message to it
		ZimbraAccount.AccountA().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
						+		"<m l='"+ inbox.getId() +"' f='u'>"
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
						+		"<link l='1' name='"+ mountpointname +"'  rid='"+ inbox.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
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

			// A dialog will appear confirming deletion
			DialogWarning warning = (DialogWarning) app.zPageMain.zGetWarningDialog(DialogWarningID.DeleteItemWithinRetentionPeriod);
			warning.zWaitForActive();

			// Verify the warning message displayed in the dialog
			ZAssert.assertStringContains(warning.zGetWarningContent(), warningMessagePart1,
					"Verify the warning message displayed while deleting the mail from retention folder");
			ZAssert.assertStringContains(warning.zGetWarningContent(), warningMessagePart2,
					"Verify the warning message displayed while deleting the mail from retention folder");

			// Confirm the deletion
			warning.zPressButton(Button.B_OK);

		} finally {
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,
					FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));
		}

		// Verify the message is in the trash
		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ") is:anywhere");
		ZAssert.assertNotNull(message, "Verify message remains in the mailbox");
		ZAssert.assertEquals(message.dFolderId,
				FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash).getId(),
				"Verify message is contained in the trash");
	}
}