/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.message;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByConversationTest;

public class ReplyToAllMail extends PrefGroupMailByConversationTest {

	ZimbraAccount account1 = null;
	ZimbraAccount account2 = null;
	ZimbraAccount account3 = null;
	ZimbraAccount account4 = null;

	public ReplyToAllMail() {
		logger.info("New "+ ReplyToAllMail.class.getCanonicalName());
	}

	@Test( description = "Reply to all in message view",
			groups = { "sanity" })

	public void ReplyToAllMail_01() throws HarnessException {

		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String body = "text <strong>bold"+ ZimbraSeleniumProperties.getUniqueString() +"</strong> text";
		String htmlBody = XmlStringUtil.escapeXml(
				"<html>" +
						"<head></head>" +
						"<body>"+ body +"</body>" +
				"</html>");

		// Send a message to the account
		if ( account1 == null ) {
			account1 = (new ZimbraAccount()).provision().authenticate();
			account2 = (new ZimbraAccount()).provision().authenticate();
			account3 = (new ZimbraAccount()).provision().authenticate();
			account4 = (new ZimbraAccount()).provision().authenticate();
		}

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='t' a='"+ account1.EmailAddress +"'/>" +
						"<e t='c' a='"+ account2.EmailAddress +"'/>" +
						"<e t='c' a='"+ account3.EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		// Get Inbox id
		String inboxId = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox).getId();

		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
						+		"<query>inid:"+ inboxId +" subject:("+ subject +")</query>"
						+	"</SearchRequest>");

		// Click on Inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");

		// Select and reply mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK,Button.B_REPLY_TO_ALL, subject);
		app.zFormMailNew.zToolbarPressButton(Button.B_SEND);

		// From the receiving end, verify the message details
		// SOAP
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");

		// UI
		boolean foundAccountA = false;
		boolean foundAccount1 = false;
		boolean foundAccount2 = false;
		boolean foundAccount3 = false;

		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		for (RecipientItem r : sent.dToRecipients) {
			if ( r.dEmailAddress.equals(ZimbraAccount.AccountA().EmailAddress) ) {
				foundAccountA = true;
			}
		}
		ZAssert.assertTrue(foundAccountA, "Verify the original sender is in the To field");

		ZAssert.assertEquals(sent.dCcRecipients.size(), 3, "Verify the message is sent to 3 'cc' recipients");
		for (RecipientItem r : sent.dCcRecipients) {
			if ( r.dEmailAddress.equals(account1.EmailAddress) ) {
				foundAccount1 = true;
			}
			if ( r.dEmailAddress.equals(account2.EmailAddress) ) {
				foundAccount2 = true;
			}
			if ( r.dEmailAddress.equals(account3.EmailAddress) ) {
				foundAccount3 = true;
			}
		}
		ZAssert.assertTrue(foundAccount1, "Verify the To is in the Cc field");
		ZAssert.assertTrue(foundAccount2, "Verify the Cc is in the Cc field");
		ZAssert.assertTrue(foundAccount3, "Verify the Cc is in the Cc field");
	}	
}