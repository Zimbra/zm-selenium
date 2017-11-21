/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.mail.receiving;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.TreeItem;

public class ForwardACopyTo extends SetGroupMailByMessagePreference {

	public ForwardACopyTo() {
		logger.info("New "+ ForwardACopyTo.class.getCanonicalName());
			
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}
	
	@Test (description = "Set forward a copy to when receiving the message from preferences",	groups = { "functional", "L2" })
	
	public void ForwardACopyTo_01() throws HarnessException {
		
		// Set message arrival email address
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Mail);	
		app.zPagePreferences.sType(("css=input[id='Prefs_Pages_MAIL_MAIL_FORWARDING_ADDRESS']") , ZimbraAccount.AccountB().EmailAddress);	
		app.zPagePreferences.zToolbarPressButton(Button.B_SAVE);

		// Create message from account A
		String subject = "subject"+ ConfigProperties.getUniqueString();
		String bodyText = "text" + ConfigProperties.getUniqueString();
		String bodyHTML = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
		String contentHTML = XmlStringUtil.escapeXml(
			"<html>" +
				"<head></head>" +
				"<body>"+ bodyHTML +"</body>" +
			"</html>");
				
		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
			"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
					"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
					"<su>"+ subject +"</su>" +
					"<mp ct='multipart/alternative'>" +
						"<mp ct='text/plain'>" +
							"<content>"+ bodyText +"</content>" +
						"</mp>" +
						"<mp ct='text/html'>" +
							"<content>"+ contentHTML +"</content>" +
						"</mp>" +
					"</mp>" +
				"</m>" +
			"</SendMsgRequest>");
		
		// SOAP verification 
		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");	
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		
		mail = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+ subject +")");
		ZAssert.assertEquals(mail.dFromRecipient.dEmailAddress, ZimbraAccount.AccountA().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(mail.dToRecipients.get(0).dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(mail.dSubject, mail.dSubject, "Verify the subject field is correct");
	}
}