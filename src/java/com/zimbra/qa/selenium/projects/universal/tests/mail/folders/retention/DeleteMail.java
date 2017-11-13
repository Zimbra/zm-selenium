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
package com.zimbra.qa.selenium.projects.universal.tests.mail.folders.retention;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning.DialogWarningID;

public class DeleteMail extends PrefGroupMailByMessageTest {

	public DeleteMail() {
		logger.info("New " + DeleteMail.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");

	}

	@Test (description = "Delete a mail that falls within the retention time", groups = { "functional", "L2" })
	public void DeleteMail_01() throws HarnessException {

		// -- Data

		// Create the subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername + "' l='"
						+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox).getId()
						+ "'/>" + "</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Add a retention policy
		app.zGetActiveAccount()
				.soapSend("<FolderActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + folder.getId()
						+ "' op='retentionpolicy'>" + "<retentionPolicy>" + "<keep>"
						+ "<policy lifetime='5d' type='user'/>" + "</keep>" + "</retentionPolicy>" + "</action>"
						+ "</FolderActionRequest>");

		// Add a message to the folder

		String subject = "subject" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='" + folder.getId() + "' f='f'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n" + "Subject: " + subject + "\n"
						+ "MIME-Version: 1.0 \n" + "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n" + "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// -- GUI

		try {

			// Refresh current view
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

			// Click on the subfolder
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Click delete
			app.zPageMail.zToolbarPressButton(Button.B_DELETE);

			// -- Verification

			// A dialog will appear confirming deletion
			DialogWarning warning = (DialogWarning) app.zPageMain
					.zGetWarningDialog(DialogWarningID.DeleteItemWithinRetentionPeriod);
			warning.zWaitForActive();

			warning.zPressButton(Button.B_OK);

		} finally {

			// Select the inbox
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

	@Test (description = "Delete a mail that falls within the retention time - click cancel to the confirmation", groups = {
			"functional", "L2" })
	public void DeleteMail_02() throws HarnessException {

		// -- Data

		// Create the subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername + "' l='"
						+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox).getId()
						+ "'/>" + "</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Add a retention policy
		app.zGetActiveAccount()
				.soapSend("<FolderActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + folder.getId()
						+ "' op='retentionpolicy'>" + "<retentionPolicy>" + "<keep>"
						+ "<policy lifetime='5d' type='user'/>" + "</keep>" + "</retentionPolicy>" + "</action>"
						+ "</FolderActionRequest>");

		// Add a message to the folder

		String subject = "subject" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='" + folder.getId() + "' f='f'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n" + "Subject: " + subject + "\n"
						+ "MIME-Version: 1.0 \n" + "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n" + "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// -- GUI

		try {

			// Refresh current view
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

			// Click on the subfolder
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Click delete
			app.zPageMail.zToolbarPressButton(Button.B_DELETE);

			// -- Verification

			// A dialog will appear confirming deletion
			DialogWarning warning = (DialogWarning) app.zPageMain
					.zGetWarningDialog(DialogWarningID.DeleteItemWithinRetentionPeriod);
			warning.zWaitForActive();

			warning.zPressButton(Button.B_CANCEL);

		} finally {

			// Select the inbox
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,
					FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));

		}

		// Verify the message is in the trash
		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ") is:anywhere");
		ZAssert.assertNotNull(message, "Verify message remains in the mailbox");
		ZAssert.assertEquals(message.dFolderId, folder.getId(), "Verify message remains in the folder");

	}

	@Test (description = "Hard-delete a mail by selecting and typing 'shift-del' shortcut", groups = { "functional", "L3" })
	public void HardDeleteMail_01() throws HarnessException {

		// -- Data

		// Create the subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername + "' l='"
						+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox).getId()
						+ "'/>" + "</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Add a retention policy
		app.zGetActiveAccount()
				.soapSend("<FolderActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + folder.getId()
						+ "' op='retentionpolicy'>" + "<retentionPolicy>" + "<keep>"
						+ "<policy lifetime='5d' type='user'/>" + "</keep>" + "</retentionPolicy>" + "</action>"
						+ "</FolderActionRequest>");

		// Add a message to the folder

		String subject = "subject" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='" + folder.getId() + "' f='f'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n" + "Subject: " + subject + "\n"
						+ "MIME-Version: 1.0 \n" + "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n" + "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// -- GUI

		try {

			// Refresh current view
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

			// Click on the subfolder
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Click shift-del

			/*
			 * Note: there will be two dialogs on this action: 1. This item is
			 * within the retention period
			 * (DialogWarningID.DeleteItemWithinRetentionPeriod) 2. Are you sure
			 * you want to permanently delete this item?
			 * (DialogWarningID.PermanentlyDeleteTheItem)
			 * 
			 * Luckily, both warning dialogs share the same div (<div
			 * id='OkCancel' .../>). However, if the ID's change in the future,
			 * then the zPageMail.zKeyboardShortcut() method may need to be
			 * reworked. The test cases for hard delete may need to do the
			 * zGetWarningDialog() instead.
			 * 
			 * 
			 */
			DialogWarning dialog = (DialogWarning) app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
			dialog.zPressButton(Button.B_OK);

			DialogWarning warning = (DialogWarning) app.zPageMain
					.zGetWarningDialog(DialogWarningID.PermanentlyDeleteTheItem);
			warning.zWaitForActive();
			warning.zPressButton(Button.B_OK);

		} finally {

			// Select the inbox
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,
					FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));

		}

		// -- Verification

		// Verify the message is hard deleted
		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ") is:anywhere");
		ZAssert.assertNull(message, "Verify message is hard deleted");

	}

	@Test (description = "Hard-delete a mail by selecting and typing 'shift-del' shortcut - click cancel to the confirmation", groups = {
			"functional", "L3" })
	public void HardDeleteMail_02() throws HarnessException {

		// -- Data

		// Create the subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + foldername + "' l='"
						+ FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox).getId()
						+ "'/>" + "</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Add a retention policy
		app.zGetActiveAccount()
				.soapSend("<FolderActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + folder.getId()
						+ "' op='retentionpolicy'>" + "<retentionPolicy>" + "<keep>"
						+ "<policy lifetime='5d' type='user'/>" + "</keep>" + "</retentionPolicy>" + "</action>"
						+ "</FolderActionRequest>");

		// Add a message to the folder

		String subject = "subject" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount()
				.soapSend("<AddMsgRequest xmlns='urn:zimbraMail'>" + "<m l='" + folder.getId() + "' f='f'>"
						+ "<content>From: foo@foo.com\n" + "To: foo@foo.com \n" + "Subject: " + subject + "\n"
						+ "MIME-Version: 1.0 \n" + "Content-Type: text/plain; charset=utf-8 \n"
						+ "Content-Transfer-Encoding: 7bit\n" + "\n" + "simple text string in the body\n" + "</content>"
						+ "</m>" + "</AddMsgRequest>");

		// -- GUI

		try {

			// Refresh current view
			app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

			// Click on the subfolder
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

			// Select the item
			app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

			// Click shift-del

			/*
			 * Note: there will be two dialogs on this action: 1. This item is
			 * within the retention period
			 * (DialogWarningID.DeleteItemWithinRetentionPeriod) 2. Are you sure
			 * you want to permanently delete this item?
			 * (DialogWarningID.PermanentlyDeleteTheItem)
			 * 
			 * Luckily, both warning dialogs share the same div (<div
			 * id='OkCancel' .../>). However, if the ID's change in the future,
			 * then the zPageMail.zKeyboardShortcut() method may need to be
			 * reworked. The test cases for hard delete may need to do the
			 * zGetWarningDialog() instead.
			 * 
			 * 
			 */
			DialogWarning dialog = (DialogWarning) app.zPageMail.zKeyboardShortcut(Shortcut.S_MAIL_HARDELETE);
			dialog.zPressButton(Button.B_CANCEL);

		} finally {

			// Select the inbox
			app.zTreeMail.zTreeItem(Action.A_LEFTCLICK,
					FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox));

		}

		// -- Verification

		// Verify the message is hard deleted
		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ") is:anywhere");
		ZAssert.assertNotNull(message, "Verify message remains in the mailbox");
		ZAssert.assertEquals(message.dFolderId, folder.getId(), "Verify message remains in the folder");

	}

}
