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
package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.message;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.ui.mail.DisplayMail.Field;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;

public class QuickReply extends PrefGroupMailByMessageTest {

	public QuickReply() {
		logger.info("New "+ QuickReply.class.getCanonicalName());

	}
	
	@Test (description = "Quick Reply to a conversation (1 message, 1 recipient)",
			groups = { "smoke" })
	
	public void QuickReply_01() throws HarnessException {
						
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();
		
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Click on Inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		
		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK,subject);
		SleepUtil.sleepSmall();
		
		// Send a quick reply
		app.zPageMail.zFillField(Field.Body, reply);
		SleepUtil.sleepSmall();
		
		app.zPageMail.zClickButton(Button.B_SEND);
		
		// Verify message in Sent
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		// Verify message is Received by sender
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(received, "Verify the message is received to recepient");
		
	}
	
	@Test (description = "Bug 92120: Verify quick reply text area clears the content after replying to the message.",
			groups = { "functional" })
	
	public void QuickReply_02() throws HarnessException {
				
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String content = "content" + ConfigProperties.getUniqueString();
		String reply = "quickreply" + ConfigProperties.getUniqueString();
		String locator = "css=div[id='ext-textareainput-1'] textarea[id^='ext-element-87']";
		
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Click on Inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		
		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK,subject);
		
		// Send quick Reply
		app.zPageMail.zFillField(Field.Body, reply);
		
		app.zPageMail.zClickButton(Button.B_SEND);
		
		// Verify message in Sent
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(sent, "Verify the message is in the sent folder");

		// Verify message is Received by sender
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(received, "Verify the message is received to recepient");
		
		// Verify quick reply text area clears the content after replying to the message.
		ZAssert.assertEquals(app.zPageMail.sGetValue(locator), "", "Verify quick reply text area clears the content after replying to the message.");
	
	}

}
