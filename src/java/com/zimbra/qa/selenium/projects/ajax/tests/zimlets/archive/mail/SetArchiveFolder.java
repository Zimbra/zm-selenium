/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.archive.mail;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.*;


public class SetArchiveFolder extends PrefGroupMailByMessageTest {

	
	public SetArchiveFolder() {
		logger.info("New "+ SetArchiveFolder.class.getCanonicalName());
		

	}
	
	@Test(	description = "On clicking 'Archive', client should prompt to set the archive folder",
			groups = { "functional" })
	public void SetArchiveFolder_01() throws HarnessException {
		
		
		//-- DATA setup
		
		
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String foldername = "archive" + ZimbraSeleniumProperties.getUniqueString();
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

		
		//-- GUI steps
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Click Archive
		app.zPageMail.zToolbarPressButton(Button.B_ARCHIVE);
		
		// A choose folder dialog will pop up
		DialogMove dialog = new DialogMove(app, ((AppAjaxClient)app).zPageMail);
		dialog.zWaitForActive();
		dialog.zClickTreeFolder(subfolder);
		dialog.zClickButton(Button.B_OK);


		//-- VERIFICATION
		
		/*
		 *	Jan 28, 2013:
		 *		<GetMailboxMetadataResponse xmlns="urn:zimbraMail">
		 *			<meta section="zwc:archiveZimlet">
		 *				<a n="archivedFolder">258</a>
		 *				<a n="hideDeleteButton">false</a>
		 *				<a n="showSendAndArchive">false</a>
		 *			</meta>
		 *		</GetMailboxMetadataResponse>

		 */
		app.zGetActiveAccount().soapSend(
				"<GetMailboxMetadataRequest xmlns='urn:zimbraMail'>" +
					"<meta section='zwc:archiveZimlet'/>" +
				"</GetMailboxMetadataRequest>");

		String id = app.zGetActiveAccount().soapSelectValue("//mail:GetMailboxMetadataResponse//mail:a[@n='archivedFolder']", null);
		
		ZAssert.assertEquals(id, subfolder.getId(), "Verify the archive folder ID was set correctly");
	}



}
