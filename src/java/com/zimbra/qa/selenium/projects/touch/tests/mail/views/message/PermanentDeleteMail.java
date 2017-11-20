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
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;


public class PermanentDeleteMail extends SetGroupMailByMessagePreference {

	public PermanentDeleteMail() {
		logger.info("New "+ DeleteMail.class.getCanonicalName());
	}
	
	@Test (description = "Delete a mail from trash in message view.",
			groups = { "functional" })
			
	public void PermanentDeleteMail_01() throws HarnessException {
		
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
	
		// Click on Inbox 
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Inbox");
		
		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Delete the mail
		 app.zPageMail.zToolbarPressButton(Button.B_DELETE);
		
        // Click on Trash
        app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Trash");
		
		// Select the mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Delete the mail
		app.zPageMail.zToolbarPressButton(Button.B_DELETE);
		
		// Click on Yes
		app.zPageMail.zPressButton(Button.B_YES);
        
		// Verify message is deleted from trash folder 
		MailItem actual = MailItem.importFromSOAP(app.zGetActiveAccount(), "in:trash "+ subject);
        ZAssert.assertNull(actual, "Verify the mail is deleted from trash");
	    ZAssert.assertEquals(app.zPageMail.zVerifyMessageExists(subject),false, "Verify mail is deleted from trash");
        
	}
}
