/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders.dumpster;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.FormRecoverDeletedItems.Field;


public class Dumpster extends SetGroupMailByMessagePreference {

	public Dumpster() {
		logger.info("New "+ Dumpster.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraDumpsterEnabled", "TRUE");
	}


	@Test (description = "Verify the Trash folder's context menu does not contain dumpster",
			groups = { "functional", "L2" })

	public void RecoverItems_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();
		String foldername = "subfolder" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Trash);

		// Create a subfolder (to recover the dumpster item to)
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
					"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
				"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Add an message
		app.zGetActiveAccount().soapSend(
					"<AddMsgRequest xmlns='urn:zimbraMail'>"
				+		"<m l='" + inbox.getId() + "'>"
				+			"<content>"
				+				"From: foo@foo.com\n" + "To: foo@foo.com \n"
				+				"Subject: " + subject + "\n" + "MIME-Version: 1.0 \n"
				+				"Content-Type: text/plain; charset=utf-8 \n"
				+				"Content-Transfer-Encoding: 7bit\n"
				+				"\n"
				+				body +" \n"
				+				"\n"
				+				"\n"
				+			"</content>"
				+		"</m>"
				+	"</AddMsgRequest>");

		String messageID = app.zGetActiveAccount().soapSelectValue("//mail:AddMsgResponse//mail:m", "id");
		ZAssert.assertNotNull(messageID, "Verify the messageID exists");

		// Workaround: search for the message before deleting
		app.zGetActiveAccount().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:("+ subject +")</query>"
				+	"</SearchRequest>");

		// Delete the message, putting it in the dumpster
		app.zGetActiveAccount().soapSend(
				"<MsgActionRequest xmlns='urn:zimbraMail'>"
			+		"<action id='"+ messageID +"' op='delete'/>"
			+	"</MsgActionRequest>");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Right click on Trash, select "Recover Deleted Items"
		FormRecoverDeletedItems form = 	(FormRecoverDeletedItems) app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_RECOVER_DELETED_ITEMS, trash);
		ZAssert.assertNotNull(form, "Verify the 'recover deleted items' dialog pops up");

		// Search for the message
		form.zFillField(Field.Search, body);
		form.zToolbarPressButton(Button.B_SEARCH);

		// Click on the message
		form.zListItem(Action.A_LEFTCLICK, subject);

		// Click "Recover To" subfolder
		DialogMove dialog = (DialogMove) form.zToolbarPressButton(Button.B_RECOVER_TO);
		dialog.sClickTreeFolder(subfolder);
		dialog.zPressButton(Button.B_OK);

		// Dismiss the 'Recover deleted items' dialog
		form.zToolbarPressButton(Button.B_CLOSE);

		// Verify the message is back
		// Work around for https://bugzilla.zimbra.com/show_bug.cgi?id=62101#c1
		MailItem message = MailItem.importFromSOAP(app.zGetActiveAccount(), "inid:"+ subfolder.getId());
		ZAssert.assertNotNull(message, "Verify the message is returned to the mailbox");
	}
}