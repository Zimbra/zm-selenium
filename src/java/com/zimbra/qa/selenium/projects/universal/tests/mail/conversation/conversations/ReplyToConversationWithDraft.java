/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.*;
import com.zimbra.qa.selenium.projects.universal.ui.mail.*;

public class ReplyToConversationWithDraft extends PrefGroupMailByConversationTest {

	public ReplyToConversationWithDraft() {
		logger.info("New "+ ReplyToConversationWithDraft.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}
	

	@Bugs (ids = "97098")
	@Test (description = "Reply to a conversation with a draft", groups = { "functional", "L2" })
	
	public void ReplyToConversationWithDraft_01() throws HarnessException {

		//-- DATA
		
		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());
		
		// Create a draft in the conversation
		// First, need to determine the last message received
		int id = 0;
		for (MailItem m : c.getMessageList()) {
			if ( Integer.parseInt(m.getId()) > id ) {
				id = Integer.parseInt(m.getId());
			}
		}
		String body = "draft"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<SaveDraftRequest xmlns='urn:zimbraMail'>" +
					"<m origid='"+ id +"' rt='r'>" +
						"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
						"<su>RE: "+ c.getSubject() +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ body +"</content>" +
						"</mp>" +
					"</m>" +
				"</SaveDraftRequest>");

		// Change the whole conversation to be unread
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='!read' id='"+ c.getId() +"'/>" +
				"</ItemActionRequest>");

		//-- GUI
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(c.getSubject()), "Verify message displayed in current view");
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());
		
		// Click reply
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();


		//-- Verification
		
		
		// From the test account, check the sent folder for the reply
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ c.getSubject() +")");
		ZAssert.assertNotNull(sent, "Verify the sent message in the sent folder");
		
		// Verify the draft body does not appear in the reply
		ZAssert.assertStringDoesNotContain(sent.dBodyText, body, "Verify the draft body does not appear in the reply");
	}
	

	@Bugs (ids = "81920")
	@Test (description = "Reply to a conversation with a trashed message",	groups = { "functional", "L3" })
	
	public void ReplyToConversationWithDraft_02() throws HarnessException {

		//-- DATA
				
		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());
		
		// Create a draft in the conversation
		// First, need to determine the last message received
		int id = 0;
		String body = null;
		for (MailItem m : c.getMessageList()) {
			if ( Integer.parseInt(m.getId()) > id ) {
				id = Integer.parseInt(m.getId());
				body = m.dBodyText;
			}
		}
		
		// Move the last message to the trash
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='trash' id='"+ id +"'/>" +
				"</ItemActionRequest>");

		// Change the whole conversation to be unread
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='!read' id='"+ c.getId() +"'/>" +
				"</ItemActionRequest>");
		
		//-- GUI
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(c.getSubject()), "Verify message displayed in current view");
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());
		
		// Click reply
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();


		//-- Verification
		
		
		// From the test account, check the sent folder for the reply
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ c.getSubject() +")");
		ZAssert.assertNotNull(sent, "Verify the sent message in the sent folder");
		
		// Verify the draft body does not appear in the reply
		ZAssert.assertStringDoesNotContain(sent.dBodyText, body, "Verify the trash body does not appear in the reply");
	}

	
	@Bugs (ids = "81920")
	@Test (description = "Reply to a conversation with a spammed message",	groups = { "functional", "L3" })
	
	public void ReplyToConversationWithDraft_03() throws HarnessException {

		//-- DATA
		
		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());
		
		// Create a draft in the conversation
		// First, need to determine the last message received
		int id = 0;
		String body = null;
		for (MailItem m : c.getMessageList()) {
			if ( Integer.parseInt(m.getId()) > id ) {
				id = Integer.parseInt(m.getId());
				body = m.dBodyText;
			}
		}
		
		// Move the last message to the trash
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='spam' id='"+ id +"'/>" +
				"</ItemActionRequest>");

		// Change the whole conversation to be unread
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='!read' id='"+ c.getId() +"'/>" +
				"</ItemActionRequest>");

		//-- GUI
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(c.getSubject()), "Verify message displayed in current view");
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());
		
		// Click reply
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();


		//-- Verification
		
		
		// From the test account, check the sent folder for the reply
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ c.getSubject() +")");
		ZAssert.assertNotNull(sent, "Verify the sent message in the sent folder");
		
		// Verify the draft body does not appear in the reply
		ZAssert.assertStringDoesNotContain(sent.dBodyText, body, "Verify the spam body does not appear in the reply");
	}

	
	@Bugs (ids = "81920")
	@Test (description = "Reply to a conversation with a sent message", groups = { "functional", "L2" })
	
	public void ReplyToConversationWithDraft_04() throws HarnessException {

		//-- DATA
		
		// Create a conversation
		ConversationItem c = ConversationItem.createConversationItem(app.zGetActiveAccount());
		
		// Create a draft in the conversation
		// First, need to determine the last message received
		int id = 0;
		for (MailItem m : c.getMessageList()) {
			if ( Integer.parseInt(m.getId()) > id ) {
				id = Integer.parseInt(m.getId());
			}
		}
		
		// Reply to the last message
		String body = "firstreply"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m origid='"+ id +"' rt='r'>" +
						"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
						"<su>RE: "+ c.getSubject() +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ body +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Change the whole conversation to be unread
		app.zGetActiveAccount().soapSend(
				"<ItemActionRequest xmlns='urn:zimbraMail'>" +
						"<action op='!read' id='"+ c.getId() +"'/>" +
				"</ItemActionRequest>");

		//-- GUI
		
		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(c.getSubject()), "Verify message displayed in current view");
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, c.getSubject());
		
		// Click reply
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();

		//-- Verification
		
		
		// From the test account, check the sent folder for the reply		
		MailItem sent = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "not content:("+ body + ") in:sent subject:("+ c.getSubject() +") ");

		ZAssert.assertNotNull(sent, "Verify the sent message in the sent folder");
		
		// Verify the draft body does not appear in the reply
		ZAssert.assertStringDoesNotContain(sent.dBodyText, body, "Verify the spam body does not appear in the reply");
	}


}
