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
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByConversationPreference;

public class UnFlagMail extends SetGroupMailByConversationPreference {

	public UnFlagMail() {
		logger.info("New "+ UnFlagMail.class.getCanonicalName());
	}

	@Test (description = "Unflag mail",
			groups = { "bhr" })

	public void UnFlagMail_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
		String htmlBody = XmlStringUtil.escapeXml(
				"<html>" +
						"<head></head>" +
						"<body>"+ body +"</body>" +
				"</html>");

		// Create the message data to be sent
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
		app.zPageMail.zRefresh();
		// Create a mail item to represent the message
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		
		// Unflag mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_FLAG_CONVERSATION, subject);
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_UNFLAG_CONVERSATION, subject);

		// SOAP verification
		mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

		// UI VERIFICATION 
		ZAssert.assertStringDoesNotContain(mail.getFlags(), "u", "Verify the message is unflaged");		

	}
}
