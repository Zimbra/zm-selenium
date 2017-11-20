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

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByConversationPreference;
import com.zimbra.qa.selenium.projects.touch.pages.mail.FormMailNew;

public class ForwardMail extends SetGroupMailByConversationPreference {

	public ForwardMail() {
		logger.info("New "+ ForwardMail.class.getCanonicalName());
	}

	@Test (description = "Forward a mail in message view",
			groups = { "sanity" })

	public void ForwardMail_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
		String htmlBody = XmlStringUtil.escapeXml(
				"<html>" +
						"<head></head>" +
						"<body>"+ body +"</body>" +
				"</html>");

		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
						"<content>"+ body +"</content>" +
						"</mp>" +
						"<mp ct='text/html'>" +
						"<content>"+ htmlBody +"</content>" +
						"</mp>" +
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

		// Select and Forward mail		

		MailItem mail = new MailItem();
		mail.dBodyText = "body" + ConfigProperties.getUniqueString();
		mail.dToRecipients.add(new RecipientItem(ZimbraAccount.AccountA(), RecipientType.To));	
		FormMailNew mailform = (FormMailNew) app.zPageMail.zListItem(Action.A_LEFTCLICK,Button.B_FORWARD_MAIL, subject);
		mailform.zFill(mail);
		app.zFormMailNew.zToolbarPressButton(Button.B_SEND);

		// From the receiving end, verify the message details
	
		MailItem sent = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:sent subject:("+ subject +")");
		ZAssert.assertEquals(sent.dToRecipients.size(), 1, "Verify the message is sent to 1 'to' recipient");
		//ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		//ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the to field is correct");
		//ZAssert.assertStringContains(received.dSubject, mail.dSubject, "Verify the subject field is correct");
		//ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'Fwd' prefix");
		
	}	
}