/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.zimlets.archive.conversation;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.ui.*;

public class SetArchiveFolder extends PrefGroupMailByConversationTest {
	
	public SetArchiveFolder() {
		logger.info("New "+ SetArchiveFolder.class.getCanonicalName());
	}
	
	@Test( description = "On clicking 'Archive', client should prompt to set the archive folder",
			groups = { "functional","L3" })
	public void SetArchiveFolder_01() throws HarnessException {
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String foldername = "archive" + ConfigProperties.getUniqueString();
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Inbox);
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.UserRoot);
		
		// Add a message to the inbox
		app.zGetActiveAccount().soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
        		+		"<m l='"+ inbox.getId() +"' >"
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

		// Create the destination archive folder
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername +"' l='"+ root.getId() +"'/>" +
                "</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Click Archive
		app.zPageMail.zToolbarPressButton(Button.B_ARCHIVE);
		
		// A choose folder dialog will pop up
		DialogMove dialog = new DialogMove(app, ((AppUniversalClient)app).zPageMail);
		dialog.zWaitForActive();
		dialog.sClickTreeFolder(subfolder);
		dialog.zPressButton(Button.B_OK);

		app.zGetActiveAccount().soapSend(
				"<GetMailboxMetadataRequest xmlns='urn:zimbraMail'>" +
					"<meta section='zwc:archiveZimlet'/>" +
				"</GetMailboxMetadataRequest>");

		String id = app.zGetActiveAccount().soapSelectValue("//mail:GetMailboxMetadataResponse//mail:a[@n='archivedFolder']", null);
		
		ZAssert.assertEquals(id, subfolder.getId(), "Verify the archive folder ID was set correctly");
	}
	
}
