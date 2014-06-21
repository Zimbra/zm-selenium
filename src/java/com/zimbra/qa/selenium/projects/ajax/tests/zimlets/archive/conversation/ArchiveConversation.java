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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.archive.conversation;

import java.util.*;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;


public class ArchiveConversation extends ArchiveZimletByConversationTest {

	
	public ArchiveConversation() {
		logger.info("New "+ ArchiveConversation.class.getCanonicalName());
		


	}
	
	@Test(	description = "Archive a conversation",
			groups = { "smoke" })
	public void ArchiveConversation_01() throws HarnessException {
		
		
		
		//-- DATA setup
		
		
		// Create a conversation
		ConversationItem conversation = ConversationItem.createConversationItem(app.zGetActiveAccount());
		

		

		
		//-- GUI steps
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message
		app.zPageMail.zListItem(Action.A_LEFTCLICK, conversation.getSubject());
		
		// Click Archive
		app.zPageMail.zToolbarPressButton(Button.B_ARCHIVE);
		

		//-- VERIFICATION
		
		// Refresh the conversation (otherwise, we will be using stale data)
		conversation = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ conversation.getSubject() +")");
		
		// Verify all messages in the conversation are in the archive folder
		for (MailItem m : conversation.getMessageList()) {
			
			ZAssert.assertEquals(m.dFolderId, this.MyArchiveFolder.getId(), "Verify the archived message is moved to the archive folder");

		}
		

	}

	@Bugs(ids = "89122")
	@Test(	description = "Archive a single message in a conversation",
			groups = { "smoke" })
	public void ArchiveConversation_02() throws HarnessException {
		
		
		
		//-- DATA setup
		
		
		// Create a conversation
		ConversationItem conversation = ConversationItem.createConversationItem(app.zGetActiveAccount());
		
		// Move one message to the archive folder
		List<MailItem> messages = conversation.getMessageList();
		// Move to trash
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='move' id='"+ messages.get(0).getId() +"' l='"+ this.MyArchiveFolder.getId() +"'/>" +
				"</ItemActionRequest>");


		
		//-- GUI steps
		
		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, conversation.getSubject());
		
		// Expand the item
		app.zPageMail.zListItem(Action.A_MAIL_EXPANDCONVERSATION, conversation.getSubject());
		
		// Click Archive
		app.zPageMail.zToolbarPressButton(Button.B_ARCHIVE);
		

		//-- VERIFICATION
		
		// Refresh the conversation (otherwise, we will be using stale data)
		conversation = ConversationItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ conversation.getSubject() +")");
		
		// Verify all messages in the conversation are in the archive folder
		for (MailItem m : conversation.getMessageList()) {
			
			ZAssert.assertEquals(m.dFolderId, this.MyArchiveFolder.getId(), "Verify the archived message is moved to the archive folder");

		}
		

	}



}
