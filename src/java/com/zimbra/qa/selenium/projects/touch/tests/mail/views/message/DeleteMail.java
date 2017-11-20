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
package com.zimbra.qa.selenium.projects.touch.tests.mail.views.message;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;

public class DeleteMail extends SetGroupMailByMessagePreference {

	public DeleteMail() {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
	}
	
	@Test (description = "Delete a mail item in message view",
			groups = { "sanity" })
			
	public void DeleteMail_01() throws HarnessException {
		
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
		
		// Get message id
		String messageId = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");
		
		// Click on Inbox
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		
		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Delete the mail
		 app.zPageMail.zToolbarPressButton(Button.B_DELETE);
	
		// Verify message is deleted from Inbox folder
		ZAssert.assertNull(messageId, "Verify message is deleted from the Inbox");
		ZAssert.assertEquals(app.zPageMail.zVerifyMessageExists(subject), false, "Verify message is deleted");
		
	    // To check whether deleted message is exist in Trash folder
		MailItem actual= MailItem.importFromSOAP(app.zGetActiveAccount(), "in:trash "+ subject);
        ZAssert.assertNotNull(actual, "Verify the contact is in the trash");
  
        app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Trash");
        ZAssert.assertEquals(app.zPageMail.zVerifyMessageExists(subject), true, "Verify message is deleted");
	
	}	
}