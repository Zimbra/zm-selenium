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
package com.zimbra.qa.selenium.projects.universal.tests.mail.conversation.conversations;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByConversationTest;


public class DragAndDropConversation extends PrefGroupMailByConversationTest {

	public DragAndDropConversation() {
		logger.info("New "+ DragAndDropConversation.class.getCanonicalName());
		
	
	}
	
	@Test (description = "Drag and Drop a conversation",
			groups = { "smoke", "L1" })
	public void DragAndDropConversation01() throws HarnessException {
		
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

		
		// Get the conversation ID
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='conversation'>"
			+		"<query>subject:(" + subject + ")</query>" 
			+	"</SearchRequest>");
		String cid = app.zGetActiveAccount().soapSelectValue("//mail:c", "id");

		
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the conversation (optional step)
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Drag the conversation to the subfolder
		app.zPageMail.zDragAndDrop(
				//"css=span#zlif__CLV-main__"+ cid +"__su",
				"css=span[id*=zlif__CLV-main__" + cid + "]",
				"css=div[id='zti__main_Mail__"+ subfolder.getId() +"']"); // <div id="zti__main_Mail__67890" .../>
		
		
		//-- Server Verification
		
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + content1 + "</query>" 
			+	"</SearchRequest>");
		String m1folderid = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(m1folderid, subfolder.getId(), "Verify the first message is in the subfolder");
	
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + content2 + "</query>" 
			+	"</SearchRequest>");
		String m2folderid = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(m2folderid, subfolder.getId(), "Verify the second message is in the subfolder");

		
	}

	@Test (description = "Drag and Drop a conversation - verify sent messages are not moved",
			groups = { "functional", "L3" })
	public void DragAndDropConversation02() throws HarnessException {
		
		// Create a subfolder
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		FolderItem sent = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent);
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
		
		// Send a message from A -> user
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

		// User needs to get the message ID to reply to it
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + content2 + "</query>" 
			+	"</SearchRequest>");
		String mid = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");

		// User replies to the message
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m origid='"+ mid +"' r='r'>" +
						"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ content1 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");
		String rid = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");

		
		// Get the conversation ID
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='conversation'>"
			+		"<query>subject:(" + subject + ")</query>" 
			+	"</SearchRequest>");
		String cid = app.zGetActiveAccount().soapSelectValue("//mail:c", "id");

		
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the conversation (optional step)
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		
		// Drag the conversation to the subfolder
		app.zPageMail.zDragAndDrop(
				//"css=span#zlif__CLV-main__"+ cid +"__su",
				"css=span[id*=zlif__CLV-main__" + cid + "]",
				"css=div[id='zti__main_Mail__"+ subfolder.getId() +"']"); // <div id="zti__main_Mail__67890" .../>
		
		
		//-- Server Verification
		
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<m id='"+ mid +"'/>" 
			+	"</GetMsgRequest>");
		String m1folderid = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(m1folderid, subfolder.getId(), "Verify the first message is in the subfolder");
	
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<m id='"+ rid +"'/>" 
			+	"</GetMsgRequest>");
		String m2folderid = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(m2folderid, sent.getId(), "Verify the second message remains in the sent");

		
	}
	
	@Test (description = "Drag and Drop a conversation - verify trashed messages are not moved",
			groups = { "functional", "L3" })
	public void DragAndDropConversation03() throws HarnessException {
		
		// Create a subfolder
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
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

		// Delete the first message
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + content2 + "</query>" 
			+	"</SearchRequest>");
		String mid = app.zGetActiveAccount().soapSelectValue("//mail:m", "id");

		app.zGetActiveAccount().soapSend(
				"<MsgActionRequest xmlns='urn:zimbraMail'>"
			+		"<action op='trash' id='"+ mid +"'/>"
			+	"</MsgActionRequest>");

		
		// Get the conversation ID
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='conversation'>"
			+		"<query>subject:(" + subject + ")</query>" 
			+	"</SearchRequest>");
		String cid = app.zGetActiveAccount().soapSelectValue("//mail:c", "id");

		
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		// Select the conversation (optional step)
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		
		// Drag the conversation to the subfolder
		app.zPageMail.zDragAndDrop(
				//"css=span#zlif__CLV-main__"+ cid +"__su",
				"css=span[id*=zlif__CLV-main__" + cid + "]",
				"css=div[id='zti__main_Mail__"+ subfolder.getId() +"']"); // <div id="zti__main_Mail__67890" .../>
		
		
		//-- Server Verification
		
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>" + content1 + "</query>" 
			+	"</SearchRequest>");
		String m1folderid = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(m1folderid, subfolder.getId(), "Verify the first message is in the subfolder");
	
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>is:anywhere " + content2 + "</query>" 
			+	"</SearchRequest>");
		String m2folderid = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		ZAssert.assertEquals(m2folderid, trash.getId(), "Verify the second message remains in the trash");

		
	}


}
