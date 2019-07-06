/*
 * ***** BEGIN LICENSE BLOCK *****
 *
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.conversation.messageaction;

import java.awt.AWTException;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByConversationPreference;

	public class MoveMessage extends SetGroupMailByConversationPreference{

		public MoveMessage() {
			logger.info("New "+ MoveMessage.class.getCanonicalName());
		}
			@Test (description = "Move a mail by 'move conversation' button",
					groups = { "bhr" })
			
			
			public void MoveMessage_01() throws HarnessException, AWTException {

				String subject = "subject"+ ConfigProperties.getUniqueString();
				FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
				String folderName = "folder" + ConfigProperties.getUniqueString();
				app.zGetActiveAccount().soapSend(
						"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
		                	"<folder name='"+ folderName +"' l='"+ userRoot.getId() +"'/>" +
		                "</CreateFolderRequest>");
				FolderItem pagemail = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);

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

				// Get the mail item for the new message
				MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
				

		app.zPageMail.zRefresh();	

		// Move message
		app.zPageMail.zConversationListItem(Button.B_CONVERSATION_ACTION_DROPDOWN, subject);
		app.zPageMail.zListItem(Button.B_MOVE_CONVERSATION);
		app.zTreeMail.zSelectFolder(folderName);
		
		// Verification
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" +
					"<m id='" + mail.getId() +"'/>" +
				"</GetMsgRequest>");
		String folderId = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		
		ZAssert.assertEquals(folderId, pagemail.getId(), "Verify the subfolder ID that the message was moved into");
		}	
		

		@Bugs (ids = "83506")
		@Test (description = "Move a mail into subfolder",
					groups = { "sanity" })
		public void MoveMessage_02() throws HarnessException {

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

				// Get the mail item for the new message
				MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
				app.zPageMail.zRefresh();	

				// Move message
				app.zPageMail.zConversationListItem(Button.B_CONVERSATION_ACTION_DROPDOWN, subject);
				SleepUtil.sleepMedium();
				app.zPageMail.zListItem(Button.B_MOVE_CONVERSATION);
				SleepUtil.sleepMedium();
				app.zPageMail.sClickAt("css=div[class='x-unsized x-list-disclosure']", "");
				SleepUtil.sleepMedium();
				app.zTreeMail.zSelectFolder(foldername);

				// Get the message, make sure it is in the correct folder
				app.zGetActiveAccount().soapSend(
						"<GetMsgRequest xmlns='urn:zimbraMail'>" +
							"<m id='" + mail.getId() +"'/>" +
						"</GetMsgRequest>");
				String folderId = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
				
				ZAssert.assertEquals(folderId, subfolder.getId(), "Verify the subfolder ID that the message was moved into");
				
			}

	}