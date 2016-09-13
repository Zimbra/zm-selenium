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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.conversationview;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByConversationTest;


public class ExpandConversation extends PrefGroupMailByConversationTest {

	public ExpandConversation() {
		logger.info("New "+ ExpandConversation.class.getCanonicalName());
		
	
	}
	
	@Test( description = "Expand a conversation",
			groups = { "smoke" })
	public void ExpandConversation01() throws HarnessException {
		
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String fragment1 = "fragment" + ConfigProperties.getUniqueString();
		String fragment2 = "fragment" + ConfigProperties.getUniqueString();
		
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ fragment1 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ fragment2 +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);
		
		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Expand the item
		app.zPageMail.zListItem(Action.A_MAIL_EXPANDCONVERSATION, subject);

		// Verify the list shows: 1 conversation with 2 messages
		
		List<MailItem> items = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(items, "Verify the conversation list exists");

		boolean found = false;
		for (MailItem c : items) {
			logger.info("Subject: looking for "+ subject +" found: "+ c.gSubject);
			if ( subject.equals(c.gSubject) ) {
				found = true;
				break;
			}
		}
		ZAssert.assertTrue(found, "Verify the conversation is in the inbox");
		
		int count = 0;
		for (MailItem m : items) {
			logger.info("Subject: looking for "+ fragment1 +" or "+ fragment2 +" found: "+ m.gFragment);

			if ( m instanceof ConversationItem ) {

				ConversationItem c = (ConversationItem)m;
				
				if ( !c.gIsConvExpanded ) {
					// Not a conversation member
					continue;
				}
				
					
				if ( fragment1.equals(c.gFragment) ) {
					logger.info("Subject: Found "+ fragment1);
					count++;
				}
				if ( fragment2.equals(c.gFragment) ) {
					logger.info("Subject: Found "+ fragment2);
					count++;
				}
				
			}
				
		}
		ZAssert.assertEquals(count, 2, "Verify two messages in the conversation");

	}

}
