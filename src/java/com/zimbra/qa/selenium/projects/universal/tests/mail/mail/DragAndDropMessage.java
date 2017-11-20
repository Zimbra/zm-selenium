/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.mail;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.SetGroupMailByMessagePreference;


public class DragAndDropMessage extends SetGroupMailByMessagePreference {

	
	public DragAndDropMessage() {
		logger.info("New "+ DragAndDropMessage.class.getCanonicalName());
		
		
		

		
	}
	
	@Test (description = "Drag and Drop a message from Inbox to subfolder",
			groups = { "smoke", "L1" })
	public void DragAndDropMessage_01() throws HarnessException {

		String subject = "subject"+ ConfigProperties.getUniqueString();
		String foldername = "folder"+ ConfigProperties.getUniqueString();

		// Create a subfolder to move the message into
		// i.e. Inbox/subfolder
		//
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

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
		
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inbox);

		// Get the mail item for the new message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
				
		// Expand the Inbox folder to see the subfolder?
		
		// Select the item
		app.zPageMail.zDragAndDrop(
					"css=span[id$='"+ mail.getId() +"__su']", // <td id="zlif__TV__12345__su" .../>
					"css=div[id='zti__main_Mail__"+ subfolder.getId() +"']"); // <div id="zti__main_Mail__67890" .../>
		
		// Get the message, make sure it is in the correct folder
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" +
					"<m id='" + mail.getId() +"'/>" +
				"</GetMsgRequest>");
		String folderId = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		
		ZAssert.assertEquals(folderId, subfolder.getId(), "Verify the subfolder ID that the message was moved into");
		
	}

}
