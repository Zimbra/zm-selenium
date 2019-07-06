/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.mobile.tests.mail;

import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.ConversationItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.mobile.core.MobileCore;


public class ReceiveMail extends MobileCore {

	public ReceiveMail() {
		logger.info("New "+ ReceiveMail.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMain;
		super.startingAccount = null;
		
	}
	
	@Test (description = "Verify a new mail shows up in the message list",
			groups = { "smoke" })
	public void ReceiveMail_01() throws HarnessException, InterruptedException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Send a message from user1		
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>"+ body +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");
		
		// Get the newly received message
		app.zPageMail.zRefresh();

		// Create the list of messages in the inbox
		List<ConversationItem> conversations = app.zPageMail.zListGetConversations();
		
		ZAssert.assertGreaterThan(conversations.size(), 0, "Verify that the list contains conversations");

		// Verify that the sent mail is in the list
		boolean found = false;
		for (ConversationItem c : conversations) {
			
			// subject could be truncated and end with "..." ... strip that.
			String s = c.gSubject.trim();
			if ( c.gSubject.trim().endsWith("...") ) {
				s = c.gSubject.trim().replace("...", "");
			}
			
			// subject could be truncated, so check containing rather than equals
			if ( subject.contains(s) ) {
				found = true;		// Found the message!
				break;
			}
		}
		
		ZAssert.assertTrue(found, "Verify that the newly sent message is received in the inbox");
		
	}

}
