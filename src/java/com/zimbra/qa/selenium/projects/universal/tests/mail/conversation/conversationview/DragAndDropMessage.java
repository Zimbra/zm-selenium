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
package com.zimbra.qa.selenium.projects.universal.tests.mail.conversation.conversationview;

import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.*;


public class DragAndDropMessage extends UniversalCommonTest {

	@SuppressWarnings("serial")
	public DragAndDropMessage() {
		logger.info("New "+ DragAndDropMessage.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;
		
		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = new HashMap<String , String>() {{
				    put("zimbraPrefGroupMailBy", "conversation");
				}};
	
	}
	
	@Test( description = "Delete a message from a conversation",
			groups = { "smoke", "L1" })
	public void DeleteConversation01() throws HarnessException {
		
		// Create a subfolder
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		String foldername = "folder"+ ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ inbox.getId() +"'/>" +
					"</CreateFolderRequest>");
		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content1 = "contentA" + ConfigProperties.getUniqueString();
		String content2 = "contentB" + ConfigProperties.getUniqueString();
		
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content2 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content1 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the conversation
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Get the list of messages
		List<DisplayConversationMessage> messages = display.zListGetMessages();
				
		// Get the locator to the message
		String id = messages.get(0).getItemId();
		
		// Drag the first message to the subfolder
		app.zPageMail.zDragAndDrop(
				"css=div#"+ id + " div[id$='__header']",
				"css=div[id='zti__main_Mail__"+ subfolder.getId() +"']"); // <div id="zti__main_Mail__67890" .../>
		
		
		//-- Server Verification
		
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + content1 + "</query>" 
			+	"</SearchRequest>");
		String folder1 = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(folder1, subfolder.getId(), "Verify the first message is in the subfolder");
	
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + content2 + "</query>" 
			+	"</SearchRequest>");
		String folder2 = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(folder2, inbox.getId(), "Verify the second message remains in the inbox");

		
	}

}
