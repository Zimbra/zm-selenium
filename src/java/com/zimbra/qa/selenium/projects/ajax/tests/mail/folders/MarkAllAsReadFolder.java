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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;


public class MarkAllAsReadFolder extends PrefGroupMailByMessageTest {

	public MarkAllAsReadFolder() {
		logger.info("New "+ MarkAllAsReadFolder.class.getCanonicalName());

		
		
		

	}

	@Test( description = "Mark all messages as read in folder (context menu)",
			groups = { "smoke", "L1" })
			public void MarkAllAsReadFolder_01() throws HarnessException {

		String foldername = "folder" + ConfigProperties.getUniqueString();
		String subject = "subject" + ConfigProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);

		// Create a subfolder in Inbox
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
				"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>" +
		"</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the folder exists on the server");

		// Add an unread message (f=u) to the new subfolder
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>" +
				"<m l='"+ subfolder.getId() +"' f='u'>" +
				"<content>From: foo@foo.com\n" +
				"To: foo@foo.com \n" +
				"Subject: "+ subject +"\n" +
				"MIME-Version: 1.0 \n" +
				"Content-Type: text/plain; charset=utf-8 \n" +
				"Content-Transfer-Encoding: 7bit\n" +
				"\n" +
				"simple text string in the body\n" +
				"</content>" +
				"</m>" +
		"</AddMsgRequest>");

		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		// Right click on folder, select "Mark all as read"
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_FOLDER_MARKASREAD, subfolder);


		// Make sure the folder was created on the server
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		ZAssert.assertNotNull(mail, "Verify the message exists");
		ZAssert.assertStringDoesNotContain(mail.getFlags(), "u", "Verify the mail flags does not contain (u)nread");

	}

}
