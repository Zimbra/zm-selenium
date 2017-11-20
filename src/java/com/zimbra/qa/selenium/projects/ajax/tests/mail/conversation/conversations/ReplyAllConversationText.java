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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.conversations;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.*;

public class ReplyAllConversationText extends SetGroupMailByConversationPreference {

	public ReplyAllConversationText() {
		logger.info("New "+ ReplyAllConversationText.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}


	@Test (description = "Reply-All to a conversation",
			groups = { "smoke", "L1" })

	public void ReplyAllConversationText_01() throws HarnessException {

		// Create a conversation
		ZimbraAccount account1 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account2 = (new ZimbraAccount()).provision().authenticate();
		ZimbraAccount account3 = (new ZimbraAccount()).provision().authenticate();

		String subject = "subject"+ ConfigProperties.getUniqueString();
		account1.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m >" +
						"<e t='t' a='"+ account2.EmailAddress +"'/>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ account3.EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>body"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");
		String id = account1.soapSelectValue("//mail:m", "id");

		account1.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
					"<m origid='"+ id +"' rt='r'>" +
						"<e t='t' a='"+ account2.EmailAddress +"'/>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ account3.EmailAddress +"'/>" +
						"<su>RE: "+ subject +"</su>" +
						"<mp ct='text/plain'>" +
							"<content>body"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
					"</m>" +
				"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Click reply
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLYALL);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Send the message
		mailform.zSubmit();

		// From the test account, check the sent folder for the reply
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertNotNull(sent, "Verify the sent message in the sent folder");

		// Verify the correct recipients in the Reply-All

		// To: should contain the original sender (account1)
		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify 1 'To'");
		boolean found1 = false;
		for (RecipientItem r : sent.dToRecipients) {
			logger.info("Looking for: "+ account1.EmailAddress +" found "+ r.dEmailAddress);
			if ( r.dEmailAddress.equals(account1.EmailAddress) ) {
				found1 = true;
				break;
			}
		}
		ZAssert.assertTrue(found1, "Verify the correct 'To' address was found");

		// CC: should contain the original CC members and the original To members
		ZAssert.assertEquals(sent.dCcRecipients.size(), 2, "Verify 2 'CC'");
		boolean found2 = false;
		boolean found3 = false;
		for (RecipientItem r : sent.dCcRecipients) {
			logger.info("Looking for: "+ account2.EmailAddress +" found "+ r.dEmailAddress);
			logger.info("Looking for: "+ account3.EmailAddress +" found "+ r.dEmailAddress);
			if ( r.dEmailAddress.equals(account2.EmailAddress) ) {
				found2 = true;
			}
			if ( r.dEmailAddress.equals(account3.EmailAddress) ) {
				found3 = true;
			}
		}
		ZAssert.assertTrue(found2, "Verify the correct 'Cc' address was found");
		ZAssert.assertTrue(found3, "Verify the correct 'Cc' address was found");
	}
}